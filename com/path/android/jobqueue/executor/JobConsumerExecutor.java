package com.path.android.jobqueue.executor;

import com.path.android.jobqueue.JobHolder;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.JqLog;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class JobConsumerExecutor {
    private final AtomicInteger activeConsumerCount;
    private final Contract contract;
    final int keepAliveSeconds;
    private int loadFactor;
    private int maxConsumerSize;
    private int minConsumerSize;
    final ConcurrentHashMap<String, JobHolder> runningJobHolders;
    private final ThreadGroup threadGroup;

    public interface Contract {
        int countRemainingReadyJobs();

        JobHolder getNextJob(int i, TimeUnit timeUnit);

        void insertOrReplace(JobHolder jobHolder);

        boolean isRunning();

        void removeJob(JobHolder jobHolder);
    }

    private static class JobConsumer implements Runnable {
        private final Contract contract;
        private boolean didRunOnce;
        private final JobConsumerExecutor executor;

        public JobConsumer(Contract contract, JobConsumerExecutor executor) {
            this.didRunOnce = false;
            this.executor = executor;
            this.contract = contract;
        }

        public final void run() {
            boolean canDie;
            do {
                try {
                    if (JqLog.isDebugEnabled()) {
                        if (this.didRunOnce) {
                            JqLog.m38d("re-running consumer %s", Thread.currentThread().getName());
                        } else {
                            JqLog.m38d("starting consumer %s", Thread.currentThread().getName());
                            this.didRunOnce = true;
                        }
                    }
                    JobHolder nextJob;
                    do {
                        nextJob = this.contract.isRunning() ? this.contract.getNextJob(this.executor.keepAliveSeconds, TimeUnit.SECONDS) : null;
                        if (nextJob != null) {
                            JobConsumerExecutor jobConsumerExecutor = this.executor;
                            synchronized (jobConsumerExecutor.runningJobHolders) {
                                jobConsumerExecutor.runningJobHolders.put(JobConsumerExecutor.createRunningJobHolderKey(nextJob), nextJob);
                            }
                            switch (nextJob.job.safeRun(nextJob, nextJob.getRunCount())) {
                                case Logger.SEVERE /*1*/:
                                    nextJob.markAsSuccessful();
                                    this.contract.removeJob(nextJob);
                                    break;
                                case Logger.WARNING /*2*/:
                                    this.contract.removeJob(nextJob);
                                    break;
                                case Logger.INFO /*3*/:
                                    JqLog.m38d("running job failed and cancelled, doing nothing. Will be removed after it's onCancel is called by the JobManager", new Object[0]);
                                    break;
                                case Logger.CONFIG /*4*/:
                                    this.contract.insertOrReplace(nextJob);
                                    break;
                            }
                            jobConsumerExecutor = this.executor;
                            synchronized (jobConsumerExecutor.runningJobHolders) {
                                jobConsumerExecutor.runningJobHolders.remove(JobConsumerExecutor.createRunningJobHolderKey(nextJob));
                                jobConsumerExecutor.runningJobHolders.notifyAll();
                            }
                            continue;
                        }
                    } while (nextJob != null);
                    canDie = JobConsumerExecutor.access$300(this.executor);
                    if (JqLog.isDebugEnabled()) {
                        if (canDie) {
                            JqLog.m38d("finishing consumer %s", Thread.currentThread().getName());
                            continue;
                        } else {
                            JqLog.m38d("didn't allow me to die, re-running %s", Thread.currentThread().getName());
                            continue;
                        }
                    }
                } catch (Throwable th) {
                    canDie = JobConsumerExecutor.access$300(this.executor);
                    if (JqLog.isDebugEnabled()) {
                        if (canDie) {
                            JqLog.m38d("finishing consumer %s", Thread.currentThread().getName());
                        } else {
                            JqLog.m38d("didn't allow me to die, re-running %s", Thread.currentThread().getName());
                        }
                    }
                }
            } while (!canDie);
        }
    }

    static /* synthetic */ boolean access$300(JobConsumerExecutor x0) {
        return !x0.doINeedANewThread(true, false);
    }

    public JobConsumerExecutor(Configuration config, Contract contract) {
        this.activeConsumerCount = new AtomicInteger(0);
        this.loadFactor = config.loadFactor;
        this.maxConsumerSize = config.maxConsumerCount;
        this.minConsumerSize = config.minConsumerCount;
        this.keepAliveSeconds = config.consumerKeepAlive;
        this.contract = contract;
        this.threadGroup = new ThreadGroup("JobConsumers");
        this.runningJobHolders = new ConcurrentHashMap();
    }

    public final boolean doINeedANewThread(boolean inConsumerThread, boolean addIfNeeded) {
        boolean z = false;
        if (this.contract.isRunning()) {
            synchronized (this.threadGroup) {
                if (isAboveLoadFactor(inConsumerThread) && canAddMoreConsumers()) {
                    if (addIfNeeded) {
                        JqLog.m38d("adding another consumer", new Object[0]);
                        synchronized (this.threadGroup) {
                            Thread thread = new Thread(this.threadGroup, new JobConsumer(this.contract, this));
                            this.activeConsumerCount.incrementAndGet();
                            thread.start();
                        }
                    }
                    z = true;
                } else {
                    if (inConsumerThread) {
                        this.activeConsumerCount.decrementAndGet();
                    }
                }
            }
        } else if (inConsumerThread) {
            this.activeConsumerCount.decrementAndGet();
        }
        return z;
    }

    private boolean canAddMoreConsumers() {
        boolean z;
        synchronized (this.threadGroup) {
            z = this.activeConsumerCount.intValue() < this.maxConsumerSize;
        }
        return z;
    }

    private boolean isAboveLoadFactor(boolean inConsumerThread) {
        boolean res = false;
        synchronized (this.threadGroup) {
            int i;
            int intValue = this.activeConsumerCount.intValue();
            if (inConsumerThread) {
                i = 1;
            } else {
                i = 0;
            }
            int consumerCnt = intValue - i;
            if (consumerCnt < this.minConsumerSize || this.loadFactor * consumerCnt < this.contract.countRemainingReadyJobs() + this.runningJobHolders.size()) {
                res = true;
            }
            if (JqLog.isDebugEnabled()) {
                JqLog.m38d("%s: load factor check. %s = (%d < %d)|| (%d * %d < %d + %d). consumer thread: %s", Thread.currentThread().getName(), Boolean.valueOf(res), Integer.valueOf(consumerCnt), Integer.valueOf(this.minConsumerSize), Integer.valueOf(consumerCnt), Integer.valueOf(this.loadFactor), Integer.valueOf(this.contract.countRemainingReadyJobs()), Integer.valueOf(this.runningJobHolders.size()), Boolean.valueOf(inConsumerThread));
            }
        }
        return res;
    }

    static String createRunningJobHolderKey(JobHolder jobHolder) {
        return jobHolder.getId().longValue() + "_" + (jobHolder.job.persistent ? "t" : "f");
    }
}
