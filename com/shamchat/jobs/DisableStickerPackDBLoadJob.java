package com.shamchat.jobs;

import android.content.ContentResolver;
import android.content.ContentValues;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.StickerProvider;
import com.shamchat.events.StickerPackDBCompletedEvent;
import de.greenrobot.event.EventBus;
import java.util.concurrent.atomic.AtomicInteger;

public final class DisableStickerPackDBLoadJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    public String stickerPackId;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public DisableStickerPackDBLoadJob() {
        super(new Params(100));
        this.id = jobCounter.incrementAndGet();
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id == jobCounter.get()) {
            try {
                ContentResolver cr = SHAMChatApplication.getMyApplicationContext().getContentResolver();
                ContentValues values = new ContentValues();
                values.put("is_sticker_downloaded", Integer.valueOf(0));
                cr.update(StickerProvider.CONTENT_URI_STICKER, values, "pack_id=?", new String[]{this.stickerPackId});
                EventBus.getDefault().post(new StickerPackDBCompletedEvent());
            } catch (Exception e) {
                System.out.println("DisableStickerPackDBLoadJob " + e);
            }
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
