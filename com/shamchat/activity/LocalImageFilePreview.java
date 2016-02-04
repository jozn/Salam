package com.shamchat.activity;

import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.shamchat.gallery.ZoomableImageView;
import java.io.File;

public class LocalImageFilePreview extends AppCompatActivity {
    File downloadedFile;
    private ImageLoader imageLoader;
    ZoomableImageView imgPreview;
    String messageId;
    ProgressBar prgDownload;
    boolean resumedFile;
    TextView tvLoading;
    TextView txtProgress;
    String url;
    private DisplayImageOptions userImageOptions;

    public LocalImageFilePreview() {
        this.imageLoader = ImageLoader.getInstance();
    }

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(2130903113);
        initImageOptions();
        this.imgPreview = (ZoomableImageView) findViewById(2131362120);
        this.prgDownload = (ProgressBar) findViewById(2131362121);
        this.tvLoading = (TextView) findViewById(2131362119);
        this.txtProgress = (TextView) findViewById(2131362122);
        this.tvLoading.setVisibility(8);
        this.prgDownload.setVisibility(8);
        this.txtProgress.setVisibility(8);
        String url = getIntent().getStringExtra("local_file_url");
        byte[] mapUrl = getIntent().getByteArrayExtra("map_file_url");
        if (url != null) {
            this.imageLoader.displayImage("file://" + url, this.imgPreview, this.userImageOptions);
        }
        if (mapUrl != null) {
            this.imgPreview.setImageBitmap(BitmapFactory.decodeByteArray(mapUrl, 0, mapUrl.length));
        }
    }

    private void initImageOptions() {
        Builder builder = new Builder();
        builder.imageResOnLoading = 2130837621;
        builder.imageResForEmptyUri = 2130837621;
        builder.imageResOnFail = 2130837992;
        builder.imageScaleType$641b8ab2 = ImageScaleType.EXACTLY$641b8ab2;
        this.userImageOptions = builder.bitmapConfig(Config.ALPHA_8).build();
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
