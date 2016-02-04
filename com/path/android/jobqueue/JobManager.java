package com.path.android.jobqueue;

import android.content.Context;
import com.path.android.jobqueue.cachedQueue.CachedJobQueue;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;
import com.path.android.jobqueue.executor.JobConsumerExecutor;
import com.path.android.jobqueue.executor.JobConsumerExecutor.Contract;
import com.path.android.jobqueue.log.JqLog;
import com.path.android.jobqueue.network.NetworkEventProvider;
import com.path.android.jobqueue.network.NetworkEventProvider.Listener;
import com.path.android.jobqueue.network.NetworkUtil;
import com.path.android.jobqueue.nonPersistentQueue.NonPersistentPriorityQueue;
import com.path.android.jobqueue.persistentQueue.sqlite.SqliteJobQueue;
import com.path.android.jobqueue.persistentQueue.sqlite.SqliteJobQueue.JavaSerializer;
import com.path.android.jobqueue.persistentQueue.sqlite.SqliteJobQueue.JobSerializer;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class JobManager implements Listener {
    private final Context appContext;
    private final Contract consumerContract;
    private final DependencyInjector dependencyInjector;
    private final Object getNextJobLock;
    private final JobConsumerExecutor jobConsumerExecutor;
    final NetworkUtil networkUtil;
    final Object newJobListeners;
    final JobQueue nonPersistentJobQueue;
    private final ConcurrentHashMap<Long, CountDownLatch> nonPersistentOnAddedLocks;
    private final Runnable notifyRunnable;
    final JobQueue persistentJobQueue;
    private final ConcurrentHashMap<Long, CountDownLatch> persistentOnAddedLocks;
    volatile boolean running;
    final CopyOnWriteGroupSet runningJobGroups;
    private final long sessionId;
    private ScheduledExecutorService timedExecutor;

    /* renamed from: com.path.android.jobqueue.JobManager.3 */
    class C04973 implements Runnable {
        final /* synthetic */ AsyncAddCallback val$callback;
        final /* synthetic */ Job val$job;

        C04973(Job job) {
            this.val$job = job;
            this.val$callback = null;
        }

        public final void run() {
            try {
                JobManager.this.addJob(this.val$job);
            } catch (Throwable th) {
                JqLog.m40e(th, "addJobInBackground received an exception. job class: %s", this.val$job.getClass().getSimpleName());
            }
        }
    }

    /* renamed from: com.path.android.jobqueue.JobManager.4 */
    class C04984 implements Runnable {
        C04984() {
        }

        public final void run() {
            JobManager.this.notifyJobConsumer();
        }
    }

    /* renamed from: com.path.android.jobqueue.JobManager.5 */
    class C04995 implements Contract {
        C04995() {
        }

        public final boolean isRunning() {
            return JobManager.this.running;
        }

        public final void insertOrReplace(JobHolder jobHolder) {
            JobManager jobManager = JobManager.this;
            JqLog.m38d("re-adding job %s", jobHolder.getId());
            if (jobHolder.cancelled) {
                JqLog.m38d("not re-adding cancelled job " + jobHolder, new Object[0]);
            } else if (jobHolder.job.persistent) {
                synchronized (jobManager.persistentJobQueue) {
                    jobManager.persistentJobQueue.insertOrReplace(jobHolder);
                }
            } else {
                synchronized (jobManager.nonPersistentJobQueue) {
                    jobManager.nonPersistentJobQueue.insertOrReplace(jobHolder);
                }
            }
            if (jobHolder.getGroupId() != null) {
                jobManager.runningJobGroups.remove(jobHolder.getGroupId());
            }
        }

        public final void removeJob(JobHolder jobHolder) {
            JobManager jobManager = JobManager.this;
            if (jobHolder.job.persistent) {
                synchronized (jobManager.persistentJobQueue) {
                    jobManager.persistentJobQueue.remove(jobHolder);
                }
            } else {
                synchronized (jobManager.nonPersistentJobQueue) {
                    jobManager.nonPersistentJobQueue.remove(jobHolder);
                }
            }
            if (jobHolder.getGroupId() != null) {
                jobManager.runningJobGroups.remove(jobHolder.getGroupId());
            }
        }

        public final JobHolder getNextJob(int wait, TimeUnit waitDuration) {
            JobHolder nextJob = JobManager.this.getNextJob();
            if (nextJob != null) {
                return nextJob;
            }
            long waitUntil = waitDuration.toNanos((long) wait) + System.nanoTime();
            long nextJobDelay = JobManager.this.ensureConsumerWhenNeeded(null);
            while (nextJob == null && waitUntil > System.nanoTime() && JobManager.this.running) {
                nextJob = JobManager.this.running ? JobManager.this.getNextJob() : null;
                if (nextJob == null) {
                    long remaining = waitUntil - System.nanoTime();
                    if (remaining > 0) {
                        long maxWait = Math.min(nextJobDelay, TimeUnit.NANOSECONDS.toMillis(remaining));
                        if (maxWait >= 1 && JobManager.this.running) {
                            if (JobManager.this.networkUtil instanceof NetworkEventProvider) {
                                synchronized (JobManager.this.newJobListeners) {
                                    try {
                                        JobManager.this.newJobListeners.wait(maxWait);
                                    } catch (Throwable e) {
                                        JqLog.m40e(e, "exception while waiting for a new job.", new Object[0]);
                                    }
                                }
                            } else {
                                synchronized (JobManager.this.newJobListeners) {
                                    try {
                                        JobManager.this.newJobListeners.wait(Math.min(500, maxWait));
                                    } catch (Throwable e2) {
                                        JqLog.m40e(e2, "exception while waiting for a new job.", new Object[0]);
                                    }
                                }
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
            return nextJob;
        }

        public final int countRemainingReadyJobs() {
            return JobManager.this.countReadyJobs(JobManager.this.networkUtil instanceof NetworkEventProvider ? JobManager.this.hasNetwork() : true);
        }
    }

    public static class DefaultQueueFactory implements QueueFactory {
        JobSerializer jobSerializer;

        public DefaultQueueFactory() {
            this.jobSerializer = new JavaSerializer();
        }

        public final JobQueue createPersistentQueue(Context context, Long sessionId, String id, boolean inTestMode) {
            return new CachedJobQueue(new SqliteJobQueue(context, sessionId.longValue(), id, this.jobSerializer, inTestMode));
        }

        public final JobQueue createNonPersistent$7a7ba21a$78fdc9e4(Long sessionId, String id) {
            return new CachedJobQueue(new NonPersistentPriorityQueue(sessionId.longValue(), id));
        }
    }

    public JobManager(Context context, Configuration config) {
        this.newJobListeners = new Object();
        this.getNextJobLock = new Object();
        this.notifyRunnable = new C04984();
        this.consumerContract = new C04995();
        if (config.customLogger != null) {
            JqLog.setCustomLogger(config.customLogger);
        }
        this.appContext = context.getApplicationContext();
        this.running = true;
        this.runningJobGroups = new CopyOnWriteGroupSet();
        this.sessionId = System.nanoTime();
        this.persistentJobQueue = config.queueFactory.createPersistentQueue(context, Long.valueOf(this.sessionId), config.id, config.inTestMode);
        this.nonPersistentJobQueue = config.queueFactory.createNonPersistent$7a7ba21a$78fdc9e4(Long.valueOf(this.sessionId), config.id);
        this.persistentOnAddedLocks = new ConcurrentHashMap();
        this.nonPersistentOnAddedLocks = new ConcurrentHashMap();
        this.networkUtil = config.networkUtil;
        this.dependencyInjector = config.dependencyInjector;
        if (this.networkUtil instanceof NetworkEventProvider) {
            ((NetworkEventProvider) this.networkUtil).setListener(this);
        }
        this.jobConsumerExecutor = new JobConsumerExecutor(config, this.consumerContract);
        this.timedExecutor = Executors.newSingleThreadScheduledExecutor();
        if (!this.running) {
            this.running = true;
            notifyJobConsumer();
        }
    }

    final int countReadyJobs(boolean hasNetwork) {
        int total;
        synchronized (this.nonPersistentJobQueue) {
            total = this.nonPersistentJobQueue.countReadyJobs(hasNetwork, this.runningJobGroups.getSafe()) + 0;
        }
        synchronized (this.persistentJobQueue) {
            total += this.persistentJobQueue.countReadyJobs(hasNetwork, this.runningJobGroups.getSafe());
        }
        return total;
    }

    public final long addJob(Job job) {
        long id;
        JobHolder jobHolder = new JobHolder(job.priority, job, job.delayInMs > 0 ? System.nanoTime() + (job.delayInMs * 1000000) : Long.MIN_VALUE);
        if (job.persistent) {
            synchronized (this.persistentJobQueue) {
                id = this.persistentJobQueue.insert(jobHolder);
                addOnAddedLock(this.persistentOnAddedLocks, id);
            }
        } else {
            synchronized (this.nonPersistentJobQueue) {
                id = this.nonPersistentJobQueue.insert(jobHolder);
                addOnAddedLock(this.nonPersistentOnAddedLocks, id);
            }
        }
        if (JqLog.isDebugEnabled()) {
            JqLog.m38d("added job id: %d class: %s priority: %d delay: %d group : %s persistent: %s requires network: %s", Long.valueOf(id), job.getClass().getSimpleName(), Integer.valueOf(job.priority), Long.valueOf(job.delayInMs), job.groupId, Boolean.valueOf(job.persistent), Boolean.valueOf(job.requiresNetwork));
        }
        jobHolder.job.onAdded();
        if (job.persistent) {
            synchronized (this.persistentJobQueue) {
                clearOnAddedLock(this.persistentOnAddedLocks, id);
            }
        } else {
            synchronized (this.nonPersistentJobQueue) {
                clearOnAddedLock(this.nonPersistentOnAddedLocks, id);
            }
        }
        notifyJobConsumer();
        return id;
    }

    public final void addJobInBackground(Job job) {
        this.timedExecutor.execute(new C04973(job));
    }

    private static void addOnAddedLock(ConcurrentHashMap<Long, CountDownLatch> lockMap, long id) {
        lockMap.put(Long.valueOf(id), new CountDownLatch(1));
    }

    private static void waitForOnAddedLock(ConcurrentHashMap<Long, CountDownLatch> lockMap, long id) {
        CountDownLatch latch = (CountDownLatch) lockMap.get(Long.valueOf(id));
        if (latch != null) {
            try {
                latch.await();
            } catch (Throwable e) {
                JqLog.m40e(e, "could not wait for onAdded lock", new Object[0]);
            }
        }
    }

    private static void clearOnAddedLock(ConcurrentHashMap<Long, CountDownLatch> lockMap, long id) {
        CountDownLatch latch = (CountDownLatch) lockMap.get(Long.valueOf(id));
        if (latch != null) {
            latch.countDown();
        }
        lockMap.remove(Long.valueOf(id));
    }

    final long ensureConsumerWhenNeeded(Boolean hasNetwork) {
        if (hasNetwork == null) {
            hasNetwork = Boolean.valueOf(this.networkUtil instanceof NetworkEventProvider ? hasNetwork() : true);
        }
        synchronized (this.nonPersistentJobQueue) {
            Long nextRunNs = this.nonPersistentJobQueue.getNextJobDelayUntilNs(hasNetwork.booleanValue());
        }
        if (nextRunNs == null || nextRunNs.longValue() > System.nanoTime()) {
            Long persistedJobRunNs;
            synchronized (this.persistentJobQueue) {
                persistedJobRunNs = this.persistentJobQueue.getNextJobDelayUntilNs(hasNetwork.booleanValue());
            }
            if (persistedJobRunNs != null) {
                if (nextRunNs == null) {
                    nextRunNs = persistedJobRunNs;
                } else if (persistedJobRunNs.longValue() < nextRunNs.longValue()) {
                    nextRunNs = persistedJobRunNs;
                }
            }
            if (nextRunNs == null) {
                return Long.MAX_VALUE;
            }
            if (nextRunNs.longValue() < System.nanoTime()) {
                notifyJobConsumer();
                return 0;
            }
            long diff = (long) Math.ceil(((double) (nextRunNs.longValue() - System.nanoTime())) / 1000000.0d);
            this.timedExecutor.schedule(this.notifyRunnable, diff, TimeUnit.MILLISECONDS);
            return diff;
        }
        notifyJobConsumer();
        return 0;
    }

    final void notifyJobConsumer() {
        synchronized (this.newJobListeners) {
            this.newJobListeners.notifyAll();
        }
        this.jobConsumerExecutor.doINeedANewThread(false, true);
    }

    final boolean hasNetwork() {
        return this.networkUtil == null || this.networkUtil.isConnected(this.appContext);
    }

    final JobHolder getNextJob() {
        JobHolder jobHolder;
        boolean haveNetwork = hasNetwork();
        boolean persistent = false;
        synchronized (this.getNextJobLock) {
            Collection<String> runningJobGroups = this.runningJobGroups.getSafe();
            synchronized (this.nonPersistentJobQueue) {
                jobHolder = this.nonPersistentJobQueue.nextJobAndIncRunCount(haveNetwork, runningJobGroups);
            }
            if (jobHolder == null) {
                synchronized (this.persistentJobQueue) {
                    jobHolder = this.persistentJobQueue.nextJobAndIncRunCount(haveNetwork, runningJobGroups);
                    persistent = true;
                }
            }
            if (jobHolder == null) {
                jobHolder = null;
            } else {
                if (jobHolder.getGroupId() != null) {
                    this.runningJobGroups.add(jobHolder.getGroupId());
                }
                if (persistent) {
                    waitForOnAddedLock(this.persistentOnAddedLocks, jobHolder.getId().longValue());
                } else {
                    waitForOnAddedLock(this.nonPersistentOnAddedLocks, jobHolder.getId().longValue());
                }
            }
        }
        return jobHolder;
    }

    public final void onNetworkChange(boolean isConnected) {
        ensureConsumerWhenNeeded(Boolean.valueOf(isConnected));
    }
}
