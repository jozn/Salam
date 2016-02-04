package com.shamchat.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import java.util.concurrent.atomic.AtomicInteger;

public final class MomentsDownloadJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    private String mobileNumber;
    private String password;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public MomentsDownloadJob(String mobileNumber, String password) {
        Params params = new Params(1);
        params.persistent = true;
        params.requiresNetwork = true;
        params.groupId = "download-moments";
        super(params);
        this.id = jobCounter.incrementAndGet();
        this.mobileNumber = mobileNumber;
        this.password = password;
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
        return false;
    }
}
