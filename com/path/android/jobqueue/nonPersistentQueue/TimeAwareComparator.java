package com.path.android.jobqueue.nonPersistentQueue;

import com.path.android.jobqueue.JobHolder;
import java.util.Comparator;

public final class TimeAwareComparator implements Comparator<JobHolder> {
    final Comparator<JobHolder> baseComparator;

    public final /* bridge */ /* synthetic */ int compare(Object x0, Object x1) {
        int i = 0;
        JobHolder jobHolder = (JobHolder) x0;
        JobHolder jobHolder2 = (JobHolder) x1;
        long nanoTime = System.nanoTime();
        int i2 = jobHolder.getDelayUntilNs() <= nanoTime ? 1 : 0;
        if (jobHolder2.getDelayUntilNs() <= nanoTime) {
            i = 1;
        }
        if (i2 != 0) {
            if (i == 0) {
                return -1;
            }
        } else if (i != 0) {
            return 1;
        } else {
            if (jobHolder.getDelayUntilNs() < jobHolder2.getDelayUntilNs()) {
                return -1;
            }
            if (jobHolder.getDelayUntilNs() > jobHolder2.getDelayUntilNs()) {
                return 1;
            }
        }
        return this.baseComparator.compare(jobHolder, jobHolder2);
    }

    public TimeAwareComparator(Comparator<JobHolder> baseComparator) {
        this.baseComparator = baseComparator;
    }
}
