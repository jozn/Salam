package com.path.android.jobqueue.nonPersistentQueue;

import com.path.android.jobqueue.JobHolder;
import java.util.Collection;

public interface JobSet {
    CountWithGroupIdsResult countReadyJobs(long j, Collection<String> collection);

    CountWithGroupIdsResult countReadyJobs(Collection<String> collection);

    boolean offer(JobHolder jobHolder);

    JobHolder peek(Collection<String> collection);

    boolean remove(JobHolder jobHolder);

    int size();
}
