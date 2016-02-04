package com.shamchat.jobs;

import android.content.ContentResolver;
import android.database.Cursor;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.MomentProvider;
import java.util.concurrent.atomic.AtomicInteger;

public final class RemoveOldMomentsDBJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public RemoveOldMomentsDBJob() {
        super(new Params(100));
        this.id = jobCounter.incrementAndGet();
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id == jobCounter.get()) {
            ContentResolver mContentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
            Cursor cursor = mContentResolver.query(MomentProvider.CONTENT_URI_MOMENT, new String[]{"post_id"}, "user_id!=?", new String[]{SHAMChatApplication.getConfig().userId}, "posted_datetime ASC");
            if (cursor.getCount() > 100) {
                int size = cursor.getCount() - 100;
                for (int x = 0; x < size; x++) {
                    cursor.moveToPosition(x);
                    String momentId = cursor.getString(cursor.getColumnIndex("post_id"));
                    mContentResolver.delete(MomentProvider.CONTENT_URI_MOMENT, "post_id=?", new String[]{momentId});
                    mContentResolver.delete(MomentProvider.CONTENT_URI_LIKE, "post_id=?", new String[]{momentId});
                    mContentResolver.delete(MomentProvider.CONTENT_URI_COMMENT, "post_id=?", new String[]{momentId});
                }
            }
            cursor.close();
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
