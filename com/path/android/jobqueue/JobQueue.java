package com.path.android.jobqueue;

import java.util.Collection;

public interface JobQueue {
    int count();

    int countReadyJobs(boolean z, Collection<String> collection);

    Long getNextJobDelayUntilNs(boolean z);

    long insert(JobHolder jobHolder);

    long insertOrReplace(JobHolder jobHolder);

    JobHolder nextJobAndIncRunCount(boolean z, Collection<String> collection);

    void remove(JobHolder jobHolder);
}
