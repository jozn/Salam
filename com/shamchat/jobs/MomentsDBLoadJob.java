package com.shamchat.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import java.util.concurrent.atomic.AtomicInteger;

public final class MomentsDBLoadJob extends Job {
    private static final AtomicInteger jobCounter;
    private String friendId;
    private final int id;
    private int lastLoadedIndex;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public MomentsDBLoadJob(String friendId, int currentLoadedIndex) {
        Params params = new Params(100);
        params.persistent = true;
        params.groupId = "download-db-moments";
        super(params);
        this.id = jobCounter.incrementAndGet();
        this.friendId = friendId;
        this.lastLoadedIndex = currentLoadedIndex;
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id != jobCounter.get()) {
            System.out.println("moment job return");
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
