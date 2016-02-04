package com.shamchat.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPreviewAndComment extends AppCompatActivity {
    EditText editDesc;
    private String fullFilePath;
    private Dialog popUp;
    Uri uri;

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(2130903105);
        this.editDesc = (EditText) findViewById(2131362021);
        this.uri = getIntent().getData();
        if (this.uri != null) {
            VideoView videoView = (VideoView) findViewById(2131362094);
            videoView.setVideoURI(this.uri);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(2131623945, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                setResult(0);
                finish();
                return true;
            case 2131362566:
                Intent data = new Intent();
                data.putExtra("fullFilePath", getIntent().getStringExtra("fullFilePath"));
                data.putExtra("description", this.editDesc.getText().toString());
                data.putExtra("type", 9);
                setResult(-1, data);
                finish();
                return true;
            default:
                return false;
        }
    }
}
