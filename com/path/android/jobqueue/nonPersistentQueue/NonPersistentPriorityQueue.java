package com.path.android.jobqueue.nonPersistentQueue;

import com.path.android.jobqueue.JobHolder;
import com.path.android.jobqueue.JobQueue;
import java.util.Collection;
import java.util.Comparator;

public final class NonPersistentPriorityQueue implements JobQueue {
    private final String id;
    public final Comparator<JobHolder> jobComparator;
    private NetworkAwarePriorityQueue jobs;
    private long nonPersistentJobIdGenerator;
    private final long sessionId;

    /* renamed from: com.path.android.jobqueue.nonPersistentQueue.NonPersistentPriorityQueue.1 */
    class C05021 implements Comparator<JobHolder> {
        C05021() {
        }

        public final /* bridge */ /* synthetic */ int compare(Object x0, Object x1) {
            JobHolder jobHolder = (JobHolder) x0;
            JobHolder jobHolder2 = (JobHolder) x1;
            int priority = jobHolder.getPriority();
            int priority2 = jobHolder2.getPriority();
            priority = priority > priority2 ? -1 : priority2 > priority ? 1 : 0;
            if (priority != 0) {
                return priority;
            }
            priority = -NonPersistentPriorityQueue.access$100(jobHolder.getCreatedNs(), jobHolder2.getCreatedNs());
            return priority == 0 ? -NonPersistentPriorityQueue.access$100(jobHolder.getId().longValue(), jobHolder2.getId().longValue()) : priority;
        }
    }

    static /* synthetic */ int access$100(long x0, long x1) {
        if (x0 > x1) {
            return -1;
        }
        return x1 > x0 ? 1 : 0;
    }

    public NonPersistentPriorityQueue(long sessionId, String id) {
        this.nonPersistentJobIdGenerator = -2147483648L;
        this.jobComparator = new C05021();
        this.id = id;
        this.sessionId = sessionId;
        this.jobs = new NetworkAwarePriorityQueue(this.jobComparator);
    }

    public final synchronized long insert(JobHolder jobHolder) {
        this.nonPersistentJobIdGenerator++;
        jobHolder.setId(Long.valueOf(this.nonPersistentJobIdGenerator));
        this.jobs.offer(jobHolder);
        return jobHolder.getId().longValue();
    }

    public final long insertOrReplace(JobHolder jobHolder) {
        remove(jobHolder);
        jobHolder.setRunningSessionId(Long.MIN_VALUE);
        this.jobs.offer(jobHolder);
        return jobHolder.getId().longValue();
    }

    public final void remove(JobHolder jobHolder) {
        this.jobs.remove(jobHolder);
    }

    public final int count() {
        return this.jobs.size();
    }

    public final int countReadyJobs(boolean hasNetwork, Collection<String> excludeGroups) {
        return this.jobs.countReadyJobs(hasNetwork, (Collection) excludeGroups).count;
    }

    public final JobHolder nextJobAndIncRunCount(boolean hasNetwork, Collection<String> excludeGroups) {
        JobHolder jobHolder = this.jobs.peek(hasNetwork, excludeGroups);
        if (jobHolder == null) {
            return jobHolder;
        }
        if (jobHolder.getDelayUntilNs() > System.nanoTime()) {
            return null;
        }
        jobHolder.setRunningSessionId(this.sessionId);
        jobHolder.setRunCount(jobHolder.getRunCount() + 1);
        this.jobs.remove(jobHolder);
        return jobHolder;
    }

    public final Long getNextJobDelayUntilNs(boolean hasNetwork) {
        JobHolder next = this.jobs.peek(hasNetwork, null);
        if (next == null) {
            return null;
        }
        return Long.valueOf(next.getDelayUntilNs());
    }
}
