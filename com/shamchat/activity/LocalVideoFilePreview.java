package com.shamchat.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

public class LocalVideoFilePreview extends AppCompatActivity {
    String localFilePath;
    String messageId;
    ProgressBar prgDownload;
    boolean resumedFile;
    TextView tvLoading;
    TextView txtProgress;
    String url;
    VideoView vidPreview;

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(2130903116);
        this.vidPreview = (VideoView) findViewById(2131362157);
        this.prgDownload = (ProgressBar) findViewById(2131362121);
        this.tvLoading = (TextView) findViewById(2131362119);
        this.txtProgress = (TextView) findViewById(2131362122);
        this.tvLoading.setVisibility(8);
        this.prgDownload.setVisibility(8);
        this.txtProgress.setVisibility(8);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(this.vidPreview);
        this.vidPreview.setMediaController(mediaController);
        this.localFilePath = getIntent().getStringExtra("local_file_url");
        this.vidPreview.setVideoPath(this.localFilePath);
        this.vidPreview.start();
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
        if (this.localFilePath != null) {
            Uri uri = Uri.parse(this.localFilePath);
            if (uri != null) {
                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.setType("video/*");
                shareIntent.putExtra("android.intent.extra.STREAM", uri);
                shareIntent.putExtra("android.intent.extra.TEXT", "shared From Salam App");
                startActivity(shareIntent);
            }
        }
        return true;
    }

    protected void onPause() {
        if (this.vidPreview.isPlaying()) {
            this.vidPreview.stopPlayback();
        }
        super.onPause();
    }

    protected void onDestroy() {
        if (this.vidPreview.isPlaying()) {
            this.vidPreview.stopPlayback();
        }
        this.vidPreview = null;
        super.onDestroy();
    }
}
