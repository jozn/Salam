package com.path.android.jobqueue.nonPersistentQueue;

import com.path.android.jobqueue.JobHolder;
import java.util.Comparator;

public final class ConsistentTimedComparator implements Comparator<JobHolder> {
    final Comparator<JobHolder> baseComparator;

    public final /* bridge */ /* synthetic */ int compare(Object x0, Object x1) {
        JobHolder jobHolder = (JobHolder) x0;
        JobHolder jobHolder2 = (JobHolder) x1;
        if (jobHolder.getDelayUntilNs() < jobHolder2.getDelayUntilNs()) {
            return -1;
        }
        return jobHolder.getDelayUntilNs() > jobHolder2.getDelayUntilNs() ? 1 : this.baseComparator.compare(jobHolder, jobHolder2);
    }

    public ConsistentTimedComparator(Comparator<JobHolder> baseComparator) {
        this.baseComparator = baseComparator;
    }
}
