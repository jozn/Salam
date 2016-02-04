package com.shamchat.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.path.android.jobqueue.JobManager;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.events.ImageDownloadProgressEvent;
import com.shamchat.gallery.ZoomableImageView;
import com.shamchat.jobs.ImageDownloadJob;
import com.shamchat.utils.Utils;
import com.squareup.picasso.Target;
import de.greenrobot.event.EventBus;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class DownloadedImageFilePreview extends AppCompatActivity {
    private Bitmap imageBitmap;
    private ZoomableImageView imgPreview;
    private JobManager jobManager;
    private String messageId;
    private String packetId;
    private ProgressBar prgDownload;
    final Set<Target> protectedFromGarbageCollectorTargets;
    private TextView txtProgress;
    private String url;

    /* renamed from: com.shamchat.activity.DownloadedImageFilePreview.1 */
    class C07491 implements Runnable {
        final /* synthetic */ String val$filePath;

        C07491(String str) {
            this.val$filePath = str;
        }

        public final void run() {
            DownloadedImageFilePreview.this.displayImage(this.val$filePath);
        }
    }

    public DownloadedImageFilePreview() {
        this.imageBitmap = null;
        this.protectedFromGarbageCollectorTargets = new HashSet();
    }

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(2130903113);
        this.imgPreview = (ZoomableImageView) findViewById(2131362120);
        this.prgDownload = (ProgressBar) findViewById(2131362121);
        this.txtProgress = (TextView) findViewById(2131362122);
        this.txtProgress.setVisibility(8);
        this.url = getIntent().getStringExtra("url");
        this.packetId = getIntent().getStringExtra("packet_id");
        this.messageId = getIntent().getStringExtra(MqttServiceConstants.MESSAGE_ID);
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        try {
            EventBus.getDefault().register(this, true, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.url != null && this.url.length() > 0) {
            handleImage(this.messageId, this.imgPreview);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623948, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("item ID : ", "onOptionsItemSelected Item ID" + id);
        if (id != 2131362569) {
            return super.onOptionsItemSelected(item);
        }
        String localFilePath = Utils.getImageLocalFilePath(this.packetId);
        boolean fileExists = false;
        if (localFilePath != null) {
            fileExists = Utils.fileExists(localFilePath);
        }
        if (localFilePath != null && localFilePath.length() > 0 && fileExists) {
            Uri uri = Uri.parse(localFilePath);
            if (uri != null) {
                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setType("image/*");
                shareIntent.putExtra("android.intent.extra.STREAM", uri);
                shareIntent.putExtra("android.intent.extra.TEXT", "shared From Salam App");
                startActivity(shareIntent);
            }
        }
        return true;
    }

    private void handleImage(String messageId, ImageView imageView) {
        String localFilePath = Utils.getImageLocalFilePath(this.packetId);
        if (localFilePath == null || localFilePath.length() <= 0) {
            this.jobManager.addJobInBackground(new ImageDownloadJob(this.url, this.packetId));
            return;
        }
        this.prgDownload.setVisibility(8);
        this.txtProgress = (TextView) findViewById(2131362122);
        this.txtProgress.setVisibility(8);
        displayImage(localFilePath);
    }

    public boolean displayImage(String localFilePath) {
        this.imgPreview = (ZoomableImageView) findViewById(2131362120);
        if (localFilePath == null || localFilePath.length() <= 0) {
            Log.e("DownloadedImageFilePreview", "localFilePath is empty - displayImage");
            return false;
        }
        Bitmap bitmap;
        File path = new File(localFilePath);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) SHAMChatApplication.getMyApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path.getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        if (screenWidth > imageWidth) {
            bitmap = Utils.decodeSampledBitmapFromFile(path.getAbsolutePath(), imageWidth, imageHeight);
        } else {
            bitmap = Utils.decodeSampledBitmapFromFile(path.getAbsolutePath(), screenWidth, screenHeight);
        }
        if (bitmap != null) {
            this.imgPreview.setImageBitmap(bitmap);
        }
        this.protectedFromGarbageCollectorTargets.remove(this);
        return true;
    }

    public void onEventMainThread(ImageDownloadProgressEvent event) {
        if (event.packetId.equals(this.packetId)) {
            this.txtProgress.setVisibility(0);
            if (event.isDone) {
                System.out.println("DownloadedImageFilePreview oncomplete");
                this.prgDownload.setVisibility(8);
                this.txtProgress = (TextView) findViewById(2131362122);
                this.txtProgress.setVisibility(8);
                new Handler().postDelayed(new C07491(event.downloadedFilePath), 3000);
                return;
            }
            Log.i("ImageDownloadProgressEvent", "downloaded: " + event.percentCompleted);
            this.txtProgress = (TextView) findViewById(2131362122);
            this.txtProgress.setText(String.valueOf(event.percentCompleted));
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        View v = findViewById(2131362120);
        if (v != null) {
            unbindDrawables(v);
        }
        this.imgPreview = null;
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if ((view instanceof ViewGroup) && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
