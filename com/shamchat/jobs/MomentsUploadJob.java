package com.shamchat.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import java.util.concurrent.atomic.AtomicInteger;

public final class MomentsUploadJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public MomentsUploadJob() {
        Params params = new Params(1);
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.id = jobCounter.incrementAndGet();
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id == jobCounter.get()) {
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("AAA download should re run on throwable");
        return true;
    }
}
