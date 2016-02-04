package com.shamchat.jobs;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.models.ChatMessage.MessageStatusType;
import java.util.concurrent.atomic.AtomicInteger;

public final class MarkFailedStatusToFailedFilesDBLoadJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public MarkFailedStatusToFailedFilesDBLoadJob() {
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
                ContentResolver mContentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
                Cursor cursor = mContentResolver.query(ChatProviderNew.CONTENT_URI_CHAT, new String[]{"packet_id"}, "message_sender=? AND uploaded_percentage<100 AND message_content_type!=0 AND message_content_type!=1", new String[]{SHAMChatApplication.getConfig().userId}, null);
                ContentValues values = new ContentValues();
                values.put("uploaded_percentage", Integer.valueOf(9999));
                values.put("message_status", Integer.valueOf(MessageStatusType.SENDING.ordinal()));
                while (cursor.moveToNext()) {
                    mContentResolver.update(ChatProviderNew.CONTENT_URI_CHAT, values, "packet_id=?", new String[]{cursor.getString(cursor.getColumnIndex("packet_id"))});
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
