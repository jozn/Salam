package com.path.android.jobqueue;

import java.util.Collections;
import java.util.Set;

public final class JobHolder {
    boolean cancelled;
    protected long createdNs;
    protected long delayUntilNs;
    protected String groupId;
    protected Long id;
    public transient Job job;
    protected int priority;
    protected boolean requiresNetwork;
    protected int runCount;
    protected long runningSessionId;
    private boolean successful;
    protected final Set<String> tags;

    public JobHolder(Long id, int priority, String groupId, int runCount, Job job, long createdNs, long delayUntilNs, long runningSessionId) {
        this.id = id;
        this.priority = priority;
        this.groupId = groupId;
        this.runCount = runCount;
        this.createdNs = createdNs;
        this.delayUntilNs = delayUntilNs;
        this.job = job;
        this.runningSessionId = runningSessionId;
        this.requiresNetwork = job.requiresNetwork;
        this.tags = job.readonlyTags == null ? null : Collections.unmodifiableSet(job.readonlyTags);
    }

    public JobHolder(int priority, Job job, long delayUntilNs) {
        this(null, priority, job.groupId, 0, job, System.nanoTime(), delayUntilNs, Long.MIN_VALUE);
    }

    public final Long getId() {
        return this.id;
    }

    public final void setId(Long id) {
        this.id = id;
    }

    public final boolean requiresNetwork() {
        return this.requiresNetwork;
    }

    public final int getPriority() {
        return this.priority;
    }

    public final int getRunCount() {
        return this.runCount;
    }

    public final void setRunCount(int runCount) {
        this.runCount = runCount;
    }

    public final long getCreatedNs() {
        return this.createdNs;
    }

    public final long getRunningSessionId() {
        return this.runningSessionId;
    }

    public final void setRunningSessionId(long runningSessionId) {
        this.runningSessionId = runningSessionId;
    }

    public final long getDelayUntilNs() {
        return this.delayUntilNs;
    }

    public final String getGroupId() {
        return this.groupId;
    }

    public final Set<String> getTags() {
        return this.tags;
    }

    public final int hashCode() {
        if (this.id == null) {
            return super.hashCode();
        }
        return this.id.intValue();
    }

    public final boolean equals(Object o) {
        if (!(o instanceof JobHolder)) {
            return false;
        }
        JobHolder other = (JobHolder) o;
        if (this.id == null || other.id == null) {
            return false;
        }
        return this.id.equals(other.id);
    }

    public final boolean hasTags() {
        return this.tags != null && this.tags.size() > 0;
    }

    public final synchronized void markAsSuccessful() {
        this.successful = true;
    }
}
