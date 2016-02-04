package com.shamchat.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.events.ChatThreadsDBLoadCompletedEvent;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class ChatThreadsDBLoadJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    private JobManager jobManager;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public ChatThreadsDBLoadJob() {
        super(new Params(10000));
        this.id = jobCounter.incrementAndGet();
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        System.out.println("id " + this.id + " jobcounter" + jobCounter.get());
        if (this.id != jobCounter.get()) {
            System.out.println("new message job already running cancel additional one");
            return;
        }
        System.out.println("ChatThreadsDBLoad: All ChatThreads  are refreshing from database - slow and in efficient?");
        String str = SHAMChatApplication.getConfig().userId;
        ArrayList arrayList = new ArrayList();
        SHAMChatApplication.getMyApplicationContext().getContentResolver();
        ChatProviderNew chatProviderNew = new ChatProviderNew();
        EventBus.getDefault().post(new ChatThreadsDBLoadCompletedEvent(ChatProviderNew.getChatThreadsSorted()));
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
