package com.path.android.jobqueue.nonPersistentQueue;

import com.path.android.jobqueue.JobHolder;
import java.util.Collection;
import java.util.Comparator;

public abstract class MergedQueue implements JobSet {
    final Comparator<JobHolder> comparator;
    JobSet queue0;
    JobSet queue1;
    final Comparator<JobHolder> retrieveComparator;

    protected enum SetId {
        ;

        static {
            S0$4402fb74 = 1;
            S1$4402fb74 = 2;
            $VALUES$18ed5c31 = new int[]{S0$4402fb74, S1$4402fb74};
        }
    }

    protected abstract JobSet createQueue$522eec24(int i, int i2, Comparator<JobHolder> comparator);

    protected abstract int decideQueue$6b176c58(JobHolder jobHolder);

    public MergedQueue(int initialCapacity, Comparator<JobHolder> comparator, Comparator<JobHolder> retrieveComparator) {
        this.comparator = comparator;
        this.retrieveComparator = retrieveComparator;
        this.queue0 = createQueue$522eec24(SetId.S0$4402fb74, initialCapacity, comparator);
        this.queue1 = createQueue$522eec24(SetId.S1$4402fb74, initialCapacity, comparator);
    }

    public final boolean offer(JobHolder jobHolder) {
        if (decideQueue$6b176c58(jobHolder) == SetId.S0$4402fb74) {
            return this.queue0.offer(jobHolder);
        }
        return this.queue1.offer(jobHolder);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.path.android.jobqueue.JobHolder peek(java.util.Collection<java.lang.String> r5) {
        /*
        r4 = this;
    L_0x0000:
        r2 = r4.queue0;
        r0 = r2.peek(r5);
        if (r0 == 0) goto L_0x001b;
    L_0x0008:
        r2 = r4.decideQueue$6b176c58(r0);
        r3 = com.path.android.jobqueue.nonPersistentQueue.MergedQueue.SetId.S0$4402fb74;
        if (r2 == r3) goto L_0x001b;
    L_0x0010:
        r2 = r4.queue1;
        r2.offer(r0);
        r2 = r4.queue0;
        r2.remove(r0);
        goto L_0x0000;
    L_0x001b:
        r2 = r4.queue1;
        r1 = r2.peek(r5);
        if (r1 == 0) goto L_0x0036;
    L_0x0023:
        r2 = r4.decideQueue$6b176c58(r1);
        r3 = com.path.android.jobqueue.nonPersistentQueue.MergedQueue.SetId.S1$4402fb74;
        if (r2 == r3) goto L_0x0036;
    L_0x002b:
        r2 = r4.queue0;
        r2.offer(r1);
        r2 = r4.queue1;
        r2.remove(r1);
        goto L_0x0000;
    L_0x0036:
        if (r0 != 0) goto L_0x0039;
    L_0x0038:
        return r1;
    L_0x0039:
        if (r1 != 0) goto L_0x003d;
    L_0x003b:
        r1 = r0;
        goto L_0x0038;
    L_0x003d:
        r2 = r4.retrieveComparator;
        r2 = r2.compare(r0, r1);
        r3 = -1;
        if (r2 != r3) goto L_0x0038;
    L_0x0046:
        r1 = r0;
        goto L_0x0038;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.path.android.jobqueue.nonPersistentQueue.MergedQueue.peek(java.util.Collection):com.path.android.jobqueue.JobHolder");
    }

    public final boolean remove(JobHolder holder) {
        return this.queue1.remove(holder) || this.queue0.remove(holder);
    }

    public final int size() {
        return this.queue0.size() + this.queue1.size();
    }

    public final CountWithGroupIdsResult countReadyJobs$45a02daf(int setId, long now, Collection<String> excludeGroups) {
        if (setId == SetId.S0$4402fb74) {
            return this.queue0.countReadyJobs(now, excludeGroups);
        }
        return this.queue1.countReadyJobs(now, excludeGroups);
    }
}
