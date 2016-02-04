package com.shamchat.jobs;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.appcompat.BuildConfig;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.StickerProvider;
import com.shamchat.events.StickerPackDBCompletedEvent;
import de.greenrobot.event.EventBus;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class StickerPackDownloadJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;
    public String stickerPackId;

    /* renamed from: com.shamchat.jobs.StickerPackDownloadJob.1 */
    class C10891 extends FileAsyncHttpResponseHandler {
        final /* synthetic */ List val$localFileUrls;

        C10891(File x0, List list) {
            this.val$localFileUrls = list;
            super(x0);
        }

        public final void onFailure$5442d429(File file) {
            file.delete();
        }

        public final void onSuccess$2d604dda(File file) {
            this.val$localFileUrls.add(file.getAbsolutePath());
        }

        public final void onProgress(long bytesWritten, long totalSize) {
            super.onProgress(bytesWritten, totalSize);
        }
    }

    static {
        jobCounter = new AtomicInteger(0);
    }

    public StickerPackDownloadJob() {
        super(new Params(100));
        this.id = jobCounter.incrementAndGet();
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id == jobCounter.get()) {
            ContentResolver cr = SHAMChatApplication.getMyApplicationContext().getContentResolver();
            String[] strArr = new String[1];
            strArr[0] = this.stickerPackId;
            Cursor cursor = cr.query(StickerProvider.CONTENT_URI_STICKER, null, "pack_id=?", strArr, null);
            while (cursor.moveToNext()) {
                File folder = new File(Environment.getExternalStorageDirectory() + "/salam/stickers/" + this.stickerPackId);
                if (!folder.exists()) {
                    folder.mkdirs();
                    try {
                        new File(folder.getAbsolutePath() + "/.nomedia").createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ContentValues updateVals = new ContentValues();
                updateVals.put("is_sticker_downloaded", Integer.valueOf(2));
                cr.update(StickerProvider.CONTENT_URI_STICKER, updateVals, "pack_id=?", new String[]{this.stickerPackId});
                List<String> list = Arrays.asList(cursor.getString(cursor.getColumnIndex("download_url")).split("\\s*,\\s*"));
                list.size();
                List<String> localFileUrls = new ArrayList();
                for (int x = 0; x < list.size(); x++) {
                    String url = (String) list.get(x);
                    String str = url;
                    File downloadedFile = new File(folder.getAbsolutePath() + MqttTopic.TOPIC_LEVEL_SEPARATOR + str.substring(url.lastIndexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1));
                    if (downloadedFile.exists()) {
                        try {
                            localFileUrls.add(downloadedFile.getAbsolutePath());
                        } catch (Exception e2) {
                            System.out.println("DisableStickerPackDBLoadJob " + e2);
                            return;
                        }
                    }
                    downloadedFile.createNewFile();
                    SyncHttpClient client = new SyncHttpClient();
                    client.setMaxRetriesAndTimeout(3, 300000);
                    client.setTimeout(300000);
                    client.setResponseTimeout(300000);
                    client.get(url, new C10891(downloadedFile, localFileUrls));
                }
                ContentValues values = new ContentValues();
                if (localFileUrls.size() != list.size()) {
                    values.put("is_sticker_downloaded", Integer.valueOf(0));
                } else {
                    values.put("is_sticker_downloaded", Integer.valueOf(1));
                    values.put("local_file_url", localFileUrls.toString().replace("[", BuildConfig.VERSION_NAME).replace("]", BuildConfig.VERSION_NAME));
                }
                ContentValues contentValues = values;
                cr.update(StickerProvider.CONTENT_URI_STICKER, contentValues, "pack_id=?", new String[]{this.stickerPackId});
            }
            cursor.close();
            EventBus.getDefault().post(new StickerPackDBCompletedEvent());
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
