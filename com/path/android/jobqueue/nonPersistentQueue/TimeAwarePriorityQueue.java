package com.path.android.jobqueue.nonPersistentQueue;

import com.path.android.jobqueue.JobHolder;
import java.util.Collection;
import java.util.Comparator;

public final class TimeAwarePriorityQueue extends MergedQueue {
    public TimeAwarePriorityQueue(int initialCapacity, Comparator<JobHolder> comparator) {
        super(initialCapacity, comparator, new TimeAwareComparator(comparator));
    }

    protected final int decideQueue$6b176c58(JobHolder jobHolder) {
        return jobHolder.getDelayUntilNs() <= System.nanoTime() ? SetId.S0$4402fb74 : SetId.S1$4402fb74;
    }

    protected final JobSet createQueue$522eec24(int setId, int initialCapacity, Comparator<JobHolder> comparator) {
        if (setId == SetId.S0$4402fb74) {
            return new NonPersistentJobSet(comparator);
        }
        return new NonPersistentJobSet(new ConsistentTimedComparator(comparator));
    }

    public final CountWithGroupIdsResult countReadyJobs(long now, Collection<String> excludeGroups) {
        return (SetId.S0$4402fb74 == SetId.S0$4402fb74 ? this.queue0.countReadyJobs(excludeGroups) : this.queue1.countReadyJobs(excludeGroups)).mergeWith(super.countReadyJobs$45a02daf(SetId.S1$4402fb74, now, excludeGroups));
    }

    public final CountWithGroupIdsResult countReadyJobs(Collection<String> collection) {
        throw new UnsupportedOperationException("cannot call time aware priority queue's count ready jobs w/o providing a time");
    }
}
