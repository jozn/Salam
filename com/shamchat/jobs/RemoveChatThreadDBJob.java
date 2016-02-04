package com.shamchat.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.events.ChatThreadDBRemoveCompletedEvent;
import de.greenrobot.event.EventBus;
import java.util.concurrent.atomic.AtomicInteger;

public final class RemoveChatThreadDBJob extends Job {
    private static final AtomicInteger jobCounter;
    public String chatThreadId;
    private final int id;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public RemoveChatThreadDBJob() {
        super(new Params(100));
        this.id = jobCounter.incrementAndGet();
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id == jobCounter.get()) {
            System.out.println("Chat thread " + this.chatThreadId);
            SHAMChatApplication.getMyApplicationContext().getContentResolver().delete(ChatProviderNew.CONTENT_URI_THREAD, "thread_id=?", new String[]{this.chatThreadId});
            SHAMChatApplication.getMyApplicationContext().getContentResolver().delete(ChatProviderNew.CONTENT_URI_CHAT, "thread_id=?", new String[]{this.chatThreadId});
            System.out.println("Remove job ended " + this.chatThreadId);
            EventBus.getDefault().post(new ChatThreadDBRemoveCompletedEvent());
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
