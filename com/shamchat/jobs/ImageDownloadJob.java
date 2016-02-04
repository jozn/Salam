package com.shamchat.jobs;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.events.ImageDownloadProgressEvent;
import com.shamchat.utils.OKHttpProgress;
import com.shamchat.utils.OKHttpProgress.ProgressListener;
import com.shamchat.utils.Utils;
import de.greenrobot.event.EventBus;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public final class ImageDownloadJob extends Job {
    private static final AtomicInteger jobCounter;
    private String downloadUrl;
    private final int id;
    private String packetId;

    /* renamed from: com.shamchat.jobs.ImageDownloadJob.1 */
    class C10861 implements ProgressListener {
        final /* synthetic */ File val$downloadPath;

        C10861(File file) {
            this.val$downloadPath = file;
        }

        public final void update(long bytesRead, long contentLength, boolean done) {
            int percentDone = (int) ((100 * bytesRead) / contentLength);
            if (percentDone % 5 == 0) {
                EventBus.getDefault().post(new ImageDownloadProgressEvent(ImageDownloadJob.this.packetId, percentDone, this.val$downloadPath.toString(), done));
            }
            if (done) {
                ImageDownloadJob.savePathToDatabase(ImageDownloadJob.this.packetId, this.val$downloadPath.toString());
            }
        }
    }

    static {
        jobCounter = new AtomicInteger(0);
    }

    public ImageDownloadJob(String downloadUrl, String packetId) {
        Params params = new Params(1000);
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.id = jobCounter.incrementAndGet();
        this.downloadUrl = downloadUrl;
        this.packetId = packetId;
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        File folder = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "salam/images");
        folder.mkdir();
        String fileName = "image_" + Utils.packetIdToFileName(this.packetId);
        String ext = this.downloadUrl.substring(this.downloadUrl.lastIndexOf("."));
        if (ext == null) {
            ext = ".jpg";
        }
        File downloadPath = new File(folder.getAbsoluteFile(), fileName + ext);
        OKHttpProgress fileDownloadClient = new OKHttpProgress(this.downloadUrl, downloadPath);
        fileDownloadClient.progressListener = new C10861(downloadPath);
        fileDownloadClient.start();
    }

    public static void savePathToDatabase(String packetId, String localFilePath) {
        Cursor cursor = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(ChatProviderNew.CONTENT_URI_CHAT, null, "packet_id=?", new String[]{packetId}, null);
        cursor.moveToFirst();
        String columnID = Integer.toString(cursor.getInt(cursor.getColumnIndex("_id")));
        cursor.close();
        Uri updateUri = Uri.parse("content://org.zamin.androidclient.provider.Messages/chat_message/" + columnID);
        ContentValues values = new ContentValues();
        values.put("file_url", localFilePath);
        Log.i("ImageDownloadJob", "File path saved to db: " + localFilePath);
        SHAMChatApplication.getMyApplicationContext().getContentResolver().update(updateUri, values, null, null);
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        System.out.println("Image Download Job run again");
        return true;
    }
}
