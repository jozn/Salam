package com.path.android.jobqueue.nonPersistentQueue;

import com.path.android.jobqueue.JobHolder;
import java.util.Collection;
import java.util.Comparator;

public final class NetworkAwarePriorityQueue extends MergedQueue {
    public NetworkAwarePriorityQueue(Comparator<JobHolder> comparator) {
        super(5, comparator, new TimeAwareComparator(comparator));
    }

    public final JobHolder peek(boolean canUseNetwork, Collection<String> excludeGroupIds) {
        if (canUseNetwork) {
            return super.peek(excludeGroupIds);
        }
        return SetId.S1$4402fb74 == SetId.S0$4402fb74 ? this.queue0.peek(excludeGroupIds) : this.queue1.peek(excludeGroupIds);
    }

    protected final int decideQueue$6b176c58(JobHolder jobHolder) {
        return jobHolder.requiresNetwork() ? SetId.S0$4402fb74 : SetId.S1$4402fb74;
    }

    protected final JobSet createQueue$522eec24(int ignoredQueueId, int initialCapacity, Comparator<JobHolder> comparator) {
        return new TimeAwarePriorityQueue(initialCapacity, comparator);
    }

    public final CountWithGroupIdsResult countReadyJobs(boolean hasNetwork, Collection<String> excludeGroups) {
        long now = System.nanoTime();
        if (hasNetwork) {
            return super.countReadyJobs$45a02daf(SetId.S0$4402fb74, now, excludeGroups).mergeWith(super.countReadyJobs$45a02daf(SetId.S1$4402fb74, now, excludeGroups));
        }
        return super.countReadyJobs$45a02daf(SetId.S1$4402fb74, now, excludeGroups);
    }

    public final CountWithGroupIdsResult countReadyJobs(long now, Collection<String> collection) {
        throw new UnsupportedOperationException("cannot call network aware priority queue count w/o providing network status");
    }

    public final CountWithGroupIdsResult countReadyJobs(Collection<String> collection) {
        throw new UnsupportedOperationException("cannot call network aware priority queue count w/o providing network status");
    }
}
