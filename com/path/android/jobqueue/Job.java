package com.path.android.jobqueue;

import com.path.android.jobqueue.log.JqLog;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

public abstract class Job implements Serializable {
    transient boolean cancelled;
    private transient int currentRunCount;
    transient long delayInMs;
    String groupId;
    public boolean persistent;
    transient int priority;
    Set<String> readonlyTags;
    boolean requiresNetwork;

    public abstract void onAdded();

    public abstract void onCancel();

    public abstract void onRun() throws Throwable;

    public abstract boolean shouldReRunOnThrowable(Throwable th);

    public Job(Params params) {
        this.requiresNetwork = params.requiresNetwork;
        this.persistent = params.persistent;
        this.groupId = params.groupId;
        this.priority = params.priority;
        this.delayInMs = params.delayMs;
        Set<String> tags = params.tags;
        this.readonlyTags = tags == null ? null : Collections.unmodifiableSet(tags);
    }

    public final int safeRun(JobHolder holder, int currentRunCount) {
        boolean z = false;
        this.currentRunCount = currentRunCount;
        if (JqLog.isDebugEnabled()) {
            JqLog.m38d("running job %s", getClass().getSimpleName());
        }
        boolean reRun = false;
        boolean failed = false;
        try {
            onRun();
            if (JqLog.isDebugEnabled()) {
                JqLog.m38d("finished job %s", this);
            }
        } catch (Throwable th) {
            JqLog.m40e(th, "shouldReRunOnThrowable did throw an exception", new Object[0]);
        }
        String str = "safeRunResult for %s : %s. re run:%s. cancelled: %s";
        Object[] objArr = new Object[4];
        objArr[0] = this;
        if (!failed) {
            z = true;
        }
        objArr[1] = Boolean.valueOf(z);
        objArr[2] = Boolean.valueOf(reRun);
        objArr[3] = Boolean.valueOf(this.cancelled);
        JqLog.m38d(str, objArr);
        if (!failed) {
            return 1;
        }
        if (holder.cancelled) {
            return 3;
        }
        if (reRun) {
            return 4;
        }
        try {
            onCancel();
        } catch (Throwable th2) {
        }
        return 2;
    }

    public int getRetryLimit() {
        return 20;
    }
}
