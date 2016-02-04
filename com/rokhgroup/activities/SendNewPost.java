package com.rokhgroup.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenVideo;
import com.kbeanie.imagechooser.api.FileUtils;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.kbeanie.imagechooser.api.MediaChooserListener;
import com.kbeanie.imagechooser.api.MediaChooserManager;
import com.kbeanie.imagechooser.api.VideoChooserListener;
import com.kbeanie.imagechooser.api.VideoChooserManager;
import com.rokhgroup.utils.RokhPref;
import com.shamchat.activity.AddFavoriteTextActivity;
import com.shamchat.activity.ProgressBarLoadingDialog;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.utils.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;

public class SendNewPost extends AppCompatActivity implements ImageChooserListener, MediaChooserListener, VideoChooserListener {
    private String Path;
    private int RESULT_OK;
    RokhPref Session;
    private MediaChooserManager chooserManager;
    private int chooserType;
    private String filePath;
    private ChosenImage imageChoosed;
    private ImageChooserManager imageChooserManager;
    private boolean isVideo;
    private Button mBtnCamera;
    private Button mBtnGallery;
    private Button mBtnSend;
    private Button mBtnVideoCamera;
    private EditText mContent;
    Context mContext;
    private ImageView mImage;
    private LinearLayout mLoading;
    private ProgressBarLoadingDialog progressBarPopup;
    private ChosenVideo videoChoosed;
    private VideoChooserManager videoChooserManager;

    /* renamed from: com.rokhgroup.activities.SendNewPost.11 */
    class AnonymousClass11 implements Runnable {
        final /* synthetic */ ChosenVideo val$video;

        AnonymousClass11(ChosenVideo chosenVideo) {
            this.val$video = chosenVideo;
        }

        public final void run() {
            SendNewPost.this.mLoading.setVisibility(8);
            if (this.val$video != null) {
                SendNewPost.this.mImage.setImageURI(Uri.parse(new File(this.val$video.videoPreviewImage).toString()));
                SendNewPost.this.Path = this.val$video.videoFilePath.toString();
                SendNewPost.this.videoChoosed = this.val$video;
                SendNewPost.this.isVideo = true;
                SendNewPost.this.mBtnSend.setEnabled(true);
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.SendNewPost.1 */
    class C05951 implements OnClickListener {
        C05951() {
        }

        public final void onClick(View arg0) {
            SendNewPost.this.captureVideo();
        }
    }

    /* renamed from: com.rokhgroup.activities.SendNewPost.2 */
    class C05962 implements OnClickListener {
        C05962() {
        }

        public final void onClick(View arg0) {
            SendNewPost.this.takePicture();
        }
    }

    /* renamed from: com.rokhgroup.activities.SendNewPost.3 */
    class C05973 implements OnClickListener {
        C05973() {
        }

        public final void onClick(View arg0) {
            SendNewPost.this.pickMedia();
        }
    }

    /* renamed from: com.rokhgroup.activities.SendNewPost.4 */
    class C05984 implements OnClickListener {
        C05984() {
        }

        public final void onClick(View v) {
            if (SendNewPost.this.Path == BuildConfig.VERSION_NAME) {
                Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493343), 1).show();
            } else {
                SendNewPost.this.SendPost();
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.SendNewPost.5 */
    class C05995 implements Runnable {
        final /* synthetic */ ChosenImage val$image;

        C05995(ChosenImage chosenImage) {
            this.val$image = chosenImage;
        }

        public final void run() {
            SendNewPost.this.mLoading.setVisibility(8);
            if (this.val$image != null) {
                SendNewPost.this.mImage.setImageURI(Uri.parse(new File(this.val$image.fileThumbnailSmall).toString()));
                SendNewPost.this.mImage.setBackgroundColor(SendNewPost.this.getResources().getColor(17170444));
                SendNewPost.this.Path = this.val$image.filePathOriginal.toString();
                SendNewPost.this.imageChoosed = this.val$image;
                SendNewPost.this.mBtnSend.setEnabled(true);
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.SendNewPost.6 */
    class C06006 implements Runnable {
        C06006() {
        }

        public final void run() {
            SendNewPost.this.mLoading.setVisibility(8);
            Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493313), 1).show();
        }
    }

    /* renamed from: com.rokhgroup.activities.SendNewPost.7 */
    static class C06017 extends RequestBody {
        final /* synthetic */ MediaType val$contentType;
        final /* synthetic */ File val$file;

        C06017(MediaType mediaType, File file) {
            this.val$contentType = mediaType;
            this.val$file = file;
        }

        public final MediaType contentType() {
            return this.val$contentType;
        }

        public final long contentLength() {
            return this.val$file.length();
        }

        public final void writeTo(BufferedSink sink) throws IOException {
            try {
                Source source = Okio.source(this.val$file);
                Buffer buf = new Buffer();
                Long remaining = Long.valueOf(contentLength());
                while (true) {
                    long readCount = source.read(buf, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
                    if (readCount != -1) {
                        sink.write(buf, readCount);
                        long percentDone = (100 * (contentLength() - remaining.longValue())) / contentLength();
                        if (percentDone % 10 == 0) {
                            try {
                                ProgressBarLoadingDialog p = ProgressBarLoadingDialog.getInstance();
                                if (p != null) {
                                    p.setMessage(Long.toString(percentDone) + "% uploaded...");
                                }
                            } catch (Exception e) {
                            }
                        }
                        remaining = Long.valueOf(remaining.longValue() - readCount);
                        Log.w("uploading", "percent done: " + percentDone + " percent readCount: " + readCount + " total: " + contentLength());
                    } else {
                        return;
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.SendNewPost.8 */
    class C06058 implements Callback {
        private IOException f11e;
        private Request request;
        final /* synthetic */ String val$postCon;

        /* renamed from: com.rokhgroup.activities.SendNewPost.8.1 */
        class C06021 implements Runnable {
            final /* synthetic */ IOException val$e;

            C06021(IOException iOException) {
                this.val$e = iOException;
            }

            public final void run() {
                this.val$e.printStackTrace();
                try {
                    ProgressBarLoadingDialog.getInstance().dismiss();
                } catch (Exception e) {
                }
                SendNewPost.this.mBtnSend.setEnabled(true);
                Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493314), 1).show();
                SendNewPost.this.hideSoftKeyboard(SendNewPost.this);
                SendNewPost.this.finish();
            }
        }

        /* renamed from: com.rokhgroup.activities.SendNewPost.8.2 */
        class C06032 implements Runnable {
            final /* synthetic */ String val$stringResponse;

            C06032(String str) {
                this.val$stringResponse = str;
            }

            public final void run() {
                try {
                    System.out.println(this.val$stringResponse);
                    try {
                        ProgressBarLoadingDialog.getInstance().dismiss();
                    } catch (Exception e) {
                    }
                    SendNewPost.this.mBtnSend.setEnabled(true);
                    Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493336), 1).show();
                    JSONObject Result = new JSONObject(this.val$stringResponse);
                    if (Result.getBoolean(NotificationCompatApi21.CATEGORY_STATUS)) {
                        SendNewPost.this.updateVideoFile(Result.getString("video"), C06058.this.val$postCon);
                        return;
                    }
                    Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493314), 1).show();
                } catch (Exception e2) {
                    try {
                        ProgressBarLoadingDialog.getInstance().dismiss();
                    } catch (Exception e3) {
                    }
                    SendNewPost.this.mBtnSend.setEnabled(true);
                    Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493314), 1).show();
                    SendNewPost.this.hideSoftKeyboard(SendNewPost.this);
                    SendNewPost.this.finish();
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.SendNewPost.8.3 */
        class C06043 implements Runnable {
            C06043() {
            }

            public final void run() {
                try {
                    ProgressBarLoadingDialog.getInstance().dismiss();
                } catch (Exception e) {
                }
                SendNewPost.this.mBtnSend.setEnabled(true);
                Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493314), 1).show();
                SendNewPost.this.hideSoftKeyboard(SendNewPost.this);
                SendNewPost.this.finish();
            }
        }

        C06058(String str) {
            this.val$postCon = str;
        }

        public final void onFailure(Request request, IOException e) {
            Debug.waitForDebugger();
            this.request = request;
            this.f11e = e;
            SendNewPost.this.runOnUiThread(new C06021(e));
            if (e != null) {
                e.printStackTrace();
            }
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    SendNewPost.this.runOnUiThread(new C06032(stringResponse));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                SendNewPost.this.runOnUiThread(new C06043());
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.rokhgroup.activities.SendNewPost.9 */
    class C06099 implements Callback {

        /* renamed from: com.rokhgroup.activities.SendNewPost.9.1 */
        class C06061 implements Runnable {
            C06061() {
            }

            public final void run() {
                try {
                    ProgressBarLoadingDialog.getInstance().dismiss();
                } catch (Exception e) {
                }
                SendNewPost.this.mBtnSend.setEnabled(true);
                Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493314), 1).show();
                SendNewPost.this.hideSoftKeyboard(SendNewPost.this);
                SendNewPost.this.finish();
            }
        }

        /* renamed from: com.rokhgroup.activities.SendNewPost.9.2 */
        class C06072 implements Runnable {
            final /* synthetic */ String val$stringResponse;

            C06072(String str) {
                this.val$stringResponse = str;
            }

            public final void run() {
                try {
                    System.out.println(this.val$stringResponse);
                    try {
                        ProgressBarLoadingDialog.getInstance().dismiss();
                    } catch (Exception e) {
                    }
                    SendNewPost.this.mBtnSend.setEnabled(true);
                    Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493336), 1).show();
                    SendNewPost.this.hideSoftKeyboard(SendNewPost.this);
                    SendNewPost.this.finish();
                } catch (Exception e2) {
                    try {
                        ProgressBarLoadingDialog.getInstance().dismiss();
                    } catch (Exception e3) {
                    }
                    SendNewPost.this.mBtnSend.setEnabled(true);
                    Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493314), 1).show();
                    SendNewPost.this.hideSoftKeyboard(SendNewPost.this);
                    SendNewPost.this.finish();
                }
            }
        }

        /* renamed from: com.rokhgroup.activities.SendNewPost.9.3 */
        class C06083 implements Runnable {
            C06083() {
            }

            public final void run() {
                try {
                    ProgressBarLoadingDialog.getInstance().dismiss();
                } catch (Exception e) {
                }
                SendNewPost.this.mBtnSend.setEnabled(true);
                Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493314), 1).show();
                SendNewPost.this.hideSoftKeyboard(SendNewPost.this);
                SendNewPost.this.finish();
            }
        }

        C06099() {
        }

        public final void onFailure(Request request, IOException e) {
            SendNewPost.this.runOnUiThread(new C06061());
            e.printStackTrace();
        }

        public final void onResponse(Response response) throws IOException {
            try {
                if (response.isSuccessful()) {
                    String stringResponse = response.body().string();
                    response.body().close();
                    System.out.println(stringResponse);
                    SendNewPost.this.runOnUiThread(new C06072(stringResponse));
                    return;
                }
                throw new IOException("Unexpected code " + response);
            } catch (Exception e) {
                SendNewPost.this.runOnUiThread(new C06083());
                e.printStackTrace();
            }
        }
    }

    public SendNewPost() {
        this.Path = BuildConfig.VERSION_NAME;
        this.RESULT_OK = -1;
        this.isVideo = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(2130903219);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(12);
        actionBar.setCustomView(2130903219);
        this.mContext = this;
        this.Session = new RokhPref(this.mContext);
        this.mLoading = new LinearLayout(this.mContext);
        this.mContent = (EditText) findViewById(2131362493);
        this.mLoading = (LinearLayout) findViewById(2131362489);
        this.mLoading.setVisibility(8);
        this.mBtnVideoCamera = (Button) findViewById(2131362490);
        this.mBtnVideoCamera.setOnClickListener(new C05951());
        this.mBtnCamera = (Button) findViewById(2131362491);
        this.mBtnCamera.setOnClickListener(new C05962());
        this.mBtnGallery = (Button) findViewById(2131362492);
        this.mBtnGallery.setOnClickListener(new C05973());
        this.mImage = (ImageView) findViewById(2131362488);
        this.mBtnSend = (Button) findViewById(2131362494);
        this.mBtnSend.setOnClickListener(new C05984());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                hideSoftKeyboard(this);
                finish();
                break;
        }
        return false;
    }

    private void takePicture() {
        this.chooserType = 294;
        this.imageChooserManager = new ImageChooserManager(this);
        this.imageChooserManager.listener = this;
        try {
            this.mLoading.setVisibility(0);
            this.filePath = this.imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void captureVideo() {
        this.chooserType = 292;
        this.videoChooserManager = new VideoChooserManager(this);
        this.videoChooserManager.listener = this;
        try {
            this.mLoading.setVisibility(0);
            this.filePath = this.videoChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void pickMedia() {
        this.chooserManager = new MediaChooserManager(this);
        this.chooserManager.listener = this;
        try {
            this.mLoading.setVisibility(0);
            this.filePath = this.chooserManager.choose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == this.RESULT_OK && requestCode == 300) {
            this.chooserManager.submit(requestCode, data);
        } else if (resultCode == this.RESULT_OK && (requestCode == 292 || requestCode == 295)) {
            if (this.videoChooserManager == null) {
                reinitializeVideoChooser();
            }
            this.videoChooserManager.submit$10b55c15(data);
        } else if (resultCode == this.RESULT_OK && (requestCode == 291 || requestCode == 294)) {
            if (this.imageChooserManager == null) {
                reinitializeImageChooser();
            }
            this.imageChooserManager.submit(requestCode, data);
        } else {
            this.mLoading.setVisibility(8);
        }
    }

    public void onImageChosen(ChosenImage image) {
        runOnUiThread(new C05995(image));
    }

    private void reinitializeImageChooser() {
        this.imageChooserManager = new ImageChooserManager(this, this.chooserType, "Rokhgroup/Image");
        this.imageChooserManager.listener = this;
        this.imageChooserManager.reinitialize(this.filePath);
    }

    private void reinitializeVideoChooser() {
        this.videoChooserManager = new VideoChooserManager(this, this.chooserType);
        this.videoChooserManager.listener = this;
        this.videoChooserManager.reinitialize(this.filePath);
    }

    public void onError(String reason) {
        runOnUiThread(new C06006());
    }

    public static RequestBody createCustomRequestBody(MediaType contentType, File file) {
        return new C06017(contentType, file);
    }

    public void SendPost() {
        this.mBtnSend.setEnabled(false);
        try {
            ProgressBarLoadingDialog.getInstance().show(getSupportFragmentManager(), BuildConfig.VERSION_NAME);
        } catch (Exception e) {
        }
        String Token = this.Session.getUSERTOKEN();
        String postCon = this.mContent.getText().toString();
        String URL;
        OkHttpClient client;
        String fileExt;
        String fileName;
        String mimeType;
        RequestBody requestBody;
        if (this.isVideo) {
            URL = "http://social.rabtcdn.com/pin/d/video/upload/?token=" + Token;
            try {
                client = new OkHttpClient();
                client.setConnectTimeout(60, TimeUnit.SECONDS);
                client.setReadTimeout(60, TimeUnit.SECONDS);
                client.setWriteTimeout(60, TimeUnit.SECONDS);
                fileExt = FileUtils.getFileExtension(this.Path);
                fileName = this.Path.substring(this.Path.lastIndexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1);
                mimeType = Utils.getMimeType(this.Path);
                if (fileExt.equals(fileName)) {
                    fileName = fileName + ".mp4";
                }
                if (mimeType == null) {
                    mimeType = "video/mp4";
                }
                requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addFormDataPart("file", fileName, createCustomRequestBody(MediaType.parse(mimeType), new File(this.Path))).build();
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(this.Path);
                long duration = Long.parseLong(retriever.extractMetadata(9)) / 1000;
                long hours = duration / 3600;
                if (duration - ((3600 * hours) + (60 * ((duration - (3600 * hours)) / 60))) > 15) {
                    try {
                        ProgressBarLoadingDialog.getInstance().dismiss();
                    } catch (Exception e2) {
                    }
                    this.mBtnSend.setEnabled(true);
                    Toast.makeText(this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493441), 1).show();
                    return;
                }
                client.newCall(new Builder().url(URL).post(requestBody).build()).enqueue(new C06058(postCon));
                return;
            } catch (Exception e3) {
                return;
            }
        }
        URL = "http://social.rabtcdn.com/pin/d_send/?token=" + Token;
        if (this.imageChoosed == null || this.Path == null) {
            Utils.getMimeType(this.Path);
            client = new OkHttpClient();
            requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addFormDataPart("description", postCon).addFormDataPart("category", "1").build();
        } else {
            fileExt = FileUtils.getFileExtension(this.Path);
            fileName = this.Path.substring(this.Path.lastIndexOf(MqttTopic.TOPIC_LEVEL_SEPARATOR) + 1);
            mimeType = Utils.getMimeType(this.Path);
            if (fileExt.equals(fileName)) {
                fileName = fileName + ".jpg";
            }
            if (mimeType == null) {
                mimeType = "image/jpeg";
            }
            client = new OkHttpClient();
            requestBody = new MultipartBuilder().type(MultipartBuilder.FORM).addFormDataPart("description", postCon).addFormDataPart("category", "1").addFormDataPart("image", fileName, RequestBody.create(MediaType.parse(mimeType), new File(this.Path))).build();
        }
        client.newCall(new Builder().url(URL).post(requestBody).build()).enqueue(new C06099());
    }

    public void updateVideoFile(String videoId, String description) {
        String URL = "http://social.rabtcdn.com/pin/d/post/update/" + videoId + "/?token=" + this.Session.getUSERTOKEN();
        new OkHttpClient().newCall(new Builder().url(URL).post(new FormEncodingBuilder().add(AddFavoriteTextActivity.EXTRA_RESULT_TEXT, description).add("category", "1").build()).build()).enqueue(new Callback() {

            /* renamed from: com.rokhgroup.activities.SendNewPost.10.1 */
            class C05921 implements Runnable {
                C05921() {
                }

                public final void run() {
                    try {
                        ProgressBarLoadingDialog.getInstance().dismiss();
                    } catch (Exception e) {
                    }
                    SendNewPost.this.mBtnSend.setEnabled(true);
                    Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493314), 1).show();
                    SendNewPost.this.hideSoftKeyboard(SendNewPost.this);
                    SendNewPost.this.finish();
                }
            }

            /* renamed from: com.rokhgroup.activities.SendNewPost.10.2 */
            class C05932 implements Runnable {
                final /* synthetic */ String val$stringResponse;

                C05932(String str) {
                    this.val$stringResponse = str;
                }

                public final void run() {
                    try {
                        ProgressBarLoadingDialog.getInstance().dismiss();
                    } catch (Exception e) {
                    }
                    try {
                        if (this.val$stringResponse.equals("success")) {
                            SendNewPost.this.mBtnSend.setEnabled(true);
                            Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493336), 1).show();
                            SendNewPost.this.hideSoftKeyboard(SendNewPost.this);
                            SendNewPost.this.finish();
                        }
                    } catch (Exception e2) {
                        try {
                            ProgressBarLoadingDialog.getInstance().dismiss();
                        } catch (Exception e3) {
                        }
                        SendNewPost.this.mBtnSend.setEnabled(true);
                        Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493314), 1).show();
                        SendNewPost.this.hideSoftKeyboard(SendNewPost.this);
                        SendNewPost.this.finish();
                    }
                }
            }

            /* renamed from: com.rokhgroup.activities.SendNewPost.10.3 */
            class C05943 implements Runnable {
                C05943() {
                }

                public final void run() {
                    try {
                        ProgressBarLoadingDialog.getInstance().dismiss();
                    } catch (Exception e) {
                    }
                    SendNewPost.this.mBtnSend.setEnabled(true);
                    Toast.makeText(SendNewPost.this.mContext, SHAMChatApplication.getInstance().getApplicationContext().getResources().getString(2131493314), 1).show();
                    SendNewPost.this.hideSoftKeyboard(SendNewPost.this);
                    SendNewPost.this.finish();
                }
            }

            public final void onFailure(Request request, IOException e) {
                SendNewPost.this.runOnUiThread(new C05921());
                e.printStackTrace();
            }

            public final void onResponse(Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        SendNewPost.this.runOnUiThread(new C05932(response.body().string()));
                        return;
                    }
                    throw new IOException("Unexpected code " + response);
                } catch (Exception e) {
                    SendNewPost.this.runOnUiThread(new C05943());
                    e.printStackTrace();
                }
            }
        });
    }

    public void hideSoftKeyboard(Activity activity) {
        ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void onVideoChosen(ChosenVideo video) {
        runOnUiThread(new AnonymousClass11(video));
    }
}
