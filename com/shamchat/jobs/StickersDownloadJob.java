package com.shamchat.jobs;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.appcompat.BuildConfig;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.StickerProvider;
import com.shamchat.events.StickersDownloadedEvent;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.conn.scheme.PlainSocketFactory;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.conn.tsccm.ThreadSafeClientConnManager;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpParams;
import de.greenrobot.event.EventBus;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONObject;

public final class StickersDownloadJob extends Job {
    private static final AtomicInteger jobCounter;
    private final int id;

    static {
        jobCounter = new AtomicInteger(0);
    }

    public StickersDownloadJob() {
        Params params = new Params(1);
        params.persistent = true;
        params.requiresNetwork = true;
        super(params);
        this.id = jobCounter.incrementAndGet();
    }

    public final void onAdded() {
    }

    protected final void onCancel() {
    }

    public final void onRun() throws Throwable {
        if (this.id == jobCounter.get()) {
            try {
                File file = new File(Environment.getExternalStorageDirectory() + "/salam/stickers");
                if (!file.exists()) {
                    file.mkdirs();
                    new File(file.getAbsolutePath() + "/.nomedia").createNewFile();
                }
                BasicHttpParams params = new BasicHttpParams();
                SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme("http", new PlainSocketFactory(), 80));
                schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                InputStream inputStream = new DefaultHttpClient(new ThreadSafeClientConnManager((HttpParams) params, schemeRegistry), params).execute((HttpUriRequest) new HttpGet("http://stickers.rabtcdn.com/sticker.php")).getEntity().getContent();
                BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseStr = new StringBuilder();
                while (true) {
                    String responseLineStr = bufferreader.readLine();
                    if (responseLineStr == null) {
                        break;
                    }
                    responseStr.append(responseLineStr);
                }
                bufferreader.close();
                inputStream.close();
                String result = responseStr.toString();
                if (result != null) {
                    JSONObject jSONObject = new JSONObject(result);
                    if (jSONObject.getString(NotificationCompatApi21.CATEGORY_STATUS).equalsIgnoreCase("success")) {
                        JSONArray stickerArray = jSONObject.getJSONArray("data");
                        for (int i = 0; i < stickerArray.length(); i++) {
                            JSONObject stickerObject = stickerArray.getJSONObject(i);
                            String stickerPackId = stickerObject.getString("pack_name");
                            String stickerName = stickerObject.getString("sticker_show_name");
                            String stickerDesc = stickerObject.getString("pack_desc");
                            String stickerThumbUrl = stickerObject.getString("thumbnail");
                            System.out.println("stickerDownloadUrls thumbnail " + stickerThumbUrl);
                            String stickerPackIconURL = stickerObject.getString("pack_tab_icon");
                            String stickerUrlString = stickerObject.getJSONArray("sticker_urls").toString().replace("[", BuildConfig.VERSION_NAME).replace("]", BuildConfig.VERSION_NAME).replaceAll("\"", BuildConfig.VERSION_NAME).replace("\\", BuildConfig.VERSION_NAME);
                            ContentValues cv = new ContentValues();
                            cv.put("pack_id", stickerPackId);
                            cv.put("pack_name", stickerName);
                            cv.put("pack_desc", stickerDesc);
                            cv.put("thumnail_url", stickerThumbUrl);
                            cv.put("download_url", stickerUrlString);
                            cv.put("sticker_pack_icon", stickerPackIconURL);
                            ContentResolver resolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
                            Cursor cursor = resolver.query(StickerProvider.CONTENT_URI_STICKER, new String[]{"download_url"}, "pack_id=?", new String[]{stickerPackId}, null);
                            if (cursor.getCount() > 0) {
                                cursor.moveToFirst();
                                resolver.update(StickerProvider.CONTENT_URI_STICKER, cv, "pack_id=?", new String[]{stickerPackId});
                            } else {
                                resolver.insert(StickerProvider.CONTENT_URI_STICKER, cv);
                            }
                            cursor.close();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("StickersDownloadJob e " + e);
            }
            EventBus.getDefault().post(new StickersDownloadedEvent());
        }
    }

    protected final boolean shouldReRunOnThrowable(Throwable throwable) {
        return true;
    }
}
