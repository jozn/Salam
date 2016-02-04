package com.shamchat.jobs;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.events.MessageDeletedEvent;
import de.greenrobot.event.EventBus;
import java.util.concurrent.atomic.AtomicInteger;

public final class DeleteChatMessageDBLoadJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    private String packetId;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public DeleteChatMessageDBLoadJob(String packetId) {
        super(new Params(100));
        this.id = jobCounter.incrementAndGet();
        this.packetId = packetId;
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id == jobCounter.get()) {
            try {
                SHAMChatApplication.getMyApplicationContext().getContentResolver().delete(ChatProviderNew.CONTENT_URI_CHAT, "packet_id=?", new String[]{this.packetId});
                EventBus.getDefault().post(new MessageDeletedEvent());
            } catch (Exception e) {
                System.out.println("DisableStickerPackDBLoadJob " + e);
            }
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
