package com.shamchat.activity;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
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
import com.path.android.jobqueue.JobManager;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.ChatProviderNew;
import com.shamchat.events.VideoDownloadProgressEvent;
import com.shamchat.jobs.VideoDownloadJob;
import com.shamchat.utils.Utils;
import de.greenrobot.event.EventBus;

public class DownloadedVideoFilePreview extends AppCompatActivity {
    private JobManager jobManager;
    private String packetId;
    private ProgressBar prgDownload;
    private TextView txtProgress;
    private String url;
    private VideoView vidPreview;

    /* renamed from: com.shamchat.activity.DownloadedVideoFilePreview.1 */
    class C07501 implements OnPreparedListener {
        C07501() {
        }

        public final void onPrepared(MediaPlayer mp) {
            DownloadedVideoFilePreview.this.prgDownload.setVisibility(8);
            DownloadedVideoFilePreview.this.txtProgress.setVisibility(8);
        }
    }

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(2130903116);
        this.vidPreview = (VideoView) findViewById(2131362157);
        this.prgDownload = (ProgressBar) findViewById(2131362121);
        this.txtProgress = (TextView) findViewById(2131362122);
        this.txtProgress.setVisibility(8);
        this.url = getIntent().getStringExtra("url");
        this.packetId = getIntent().getStringExtra("packet_id");
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        try {
            EventBus.getDefault().register(this, true, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.jobManager.addJobInBackground(new VideoDownloadJob(this.url, this.packetId));
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
        String localFilePath = getVideoLocalFilePath();
        boolean fileExists = false;
        if (localFilePath != null) {
            fileExists = Utils.fileExists(localFilePath);
        }
        if (localFilePath != null && localFilePath.length() > 0 && fileExists) {
            Uri uri = Uri.parse(localFilePath);
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

    public void onEventMainThread(VideoDownloadProgressEvent event) {
        if (event.packetId.equals(this.packetId)) {
            this.txtProgress.setVisibility(0);
            if (event.isDone) {
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(this.vidPreview);
                this.vidPreview.setMediaController(mediaController);
                this.vidPreview.setOnPreparedListener(new C07501());
                this.vidPreview.setVideoPath(event.downloadedFilePath);
                this.vidPreview.start();
                return;
            }
            Log.i("VideoDownloadProgressEvent", "downloaded: " + event.percentCompleted);
            this.txtProgress = (TextView) findViewById(2131362122);
            this.txtProgress.setText(String.valueOf(event.percentCompleted));
        }
    }

    public String getVideoLocalFilePath() {
        Cursor cursor = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(ChatProviderNew.CONTENT_URI_CHAT, null, "packet_id=?", new String[]{this.packetId}, null);
        cursor.moveToFirst();
        String fileUrl = cursor.getString(cursor.getColumnIndex("file_url"));
        cursor.close();
        return fileUrl;
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
