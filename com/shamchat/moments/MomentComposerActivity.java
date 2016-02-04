package com.shamchat.moments;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.path.android.jobqueue.JobManager;
import com.shamchat.activity.ChatActivity;
import com.shamchat.activity.ProgressBarDialogLogin;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.data.MomentProvider;
import com.shamchat.jobs.MomentsUploadJob;
import com.shamchat.utils.Utils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MomentComposerActivity extends AppCompatActivity implements OnClickListener {
    public ImageView btCamera;
    public ImageView btSmiley;
    private ImageView btVideo;
    private FragmentManager fragmentManager;
    private JobManager jobManager;
    private List<String> photoFileUrls;
    private Fragment pictureFragment;
    private LinearLayout sendItemsLayout;
    private Fragment stickerFragment;
    private List<String> stickerLocalFileUrls;
    private EditText textMessage;
    private Fragment videoFragment;
    private String videoUrl;

    /* renamed from: com.shamchat.moments.MomentComposerActivity.1 */
    class C11031 implements OnTouchListener {
        C11031() {
        }

        public final boolean onTouch(View v, MotionEvent event) {
            MomentComposerActivity.this.sendItemsLayout.setVisibility(8);
            Utils.showKeyboard(MomentComposerActivity.this.textMessage, MomentComposerActivity.this.getApplicationContext());
            return false;
        }
    }

    /* renamed from: com.shamchat.moments.MomentComposerActivity.2 */
    class C11042 implements OnTouchListener {
        C11042() {
        }

        public final boolean onTouch(View v, MotionEvent event) {
            MomentComposerActivity.this.sendItemsLayout.setVisibility(8);
            Utils.showKeyboard(MomentComposerActivity.this.textMessage, MomentComposerActivity.this.getApplicationContext());
            return false;
        }
    }

    /* renamed from: com.shamchat.moments.MomentComposerActivity.3 */
    class C11063 implements Runnable {
        final /* synthetic */ String val$text;

        /* renamed from: com.shamchat.moments.MomentComposerActivity.3.1 */
        class C11051 implements Runnable {
            C11051() {
            }

            public final void run() {
                try {
                    ProgressBarDialogLogin.getInstance().dismiss();
                    MomentComposerActivity.this.finish();
                } catch (Exception e) {
                    MomentComposerActivity.this.finish();
                }
            }
        }

        C11063(String str) {
            this.val$text = str;
        }

        public final void run() {
            Utils.hideKeyboard(MomentComposerActivity.this.textMessage, MomentComposerActivity.this.getApplicationContext());
            ProgressBarDialogLogin.getInstance().show(MomentComposerActivity.this.getSupportFragmentManager(), BuildConfig.VERSION_NAME);
            String userId = SHAMChatApplication.getConfig().userId;
            ContentValues values = new ContentValues();
            values.put("post_id", userId + "_" + System.currentTimeMillis());
            values.put("post_text", this.val$text);
            values.put("posted_video_url", MomentComposerActivity.this.videoUrl);
            values.put(ChatActivity.INTENT_EXTRA_USER_ID, userId);
            if (MomentComposerActivity.this.photoFileUrls.size() > 0) {
                values.put("posted_image_url", MomentComposerActivity.this.photoFileUrls.toString().replace("[", BuildConfig.VERSION_NAME).replace("]", BuildConfig.VERSION_NAME));
            }
            if (MomentComposerActivity.this.stickerLocalFileUrls.size() > 0) {
                values.put("posted_sticker_url", MomentComposerActivity.this.stickerLocalFileUrls.toString().replace("[", BuildConfig.VERSION_NAME).replace("]", BuildConfig.VERSION_NAME));
            }
            values.put("posted_datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.valueOf(System.currentTimeMillis()).longValue())));
            MomentComposerActivity.this.getContentResolver().insert(MomentProvider.CONTENT_URI_MOMENT, values);
            MomentComposerActivity.this.jobManager.addJobInBackground(new MomentsUploadJob());
            System.out.println("AAAA Compose moments end");
            MomentComposerActivity.this.setResult(-1);
            SHAMChatApplication instance = SHAMChatApplication.getInstance();
            instance.handler.postDelayed(new C11051(), 500);
        }
    }

    public MomentComposerActivity() {
        this.videoUrl = BuildConfig.VERSION_NAME;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(2130903182);
        this.textMessage = (EditText) findViewById(2131361925);
        this.sendItemsLayout = (LinearLayout) findViewById(2131361947);
        this.fragmentManager = getSupportFragmentManager();
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        this.photoFileUrls = new ArrayList();
        this.stickerLocalFileUrls = new ArrayList();
        this.stickerFragment = new MomentStickerFragment();
        this.pictureFragment = new MomentPhotoFragment();
        this.videoFragment = new MomentVideoFragment();
        ImageView btDone = (ImageView) findViewById(2131362367);
        this.btSmiley = (ImageView) findViewById(2131362368);
        this.btCamera = (ImageView) findViewById(2131362369);
        this.btVideo = (ImageView) findViewById(2131362370);
        this.textMessage.setOnTouchListener(new C11031());
        findViewById(2131362365).setOnTouchListener(new C11042());
        btDone.setOnClickListener(this);
        this.btSmiley.setOnClickListener(this);
        this.btCamera.setOnClickListener(this);
        this.btVideo.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case 2131362367:
                String text = this.textMessage.getText().toString();
                if (this.videoUrl.length() > 1 || text.length() > 1 || this.photoFileUrls.size() > 0 || this.stickerLocalFileUrls.size() > 0) {
                    new Thread(new C11063(text)).start();
                }
            case 2131362368:
                this.sendItemsLayout.setVisibility(0);
                Utils.hideKeyboard(this.textMessage, getApplicationContext());
                this.fragmentManager.beginTransaction().setCustomAnimations(2130968589, 2130968590).addToBackStack(null).replace(2131361947, this.stickerFragment).commit();
            case 2131362369:
                this.sendItemsLayout.setVisibility(0);
                Utils.hideKeyboard(this.textMessage, getApplicationContext());
                this.fragmentManager.beginTransaction().setCustomAnimations(2130968589, 2130968590).addToBackStack(null).replace(2131361947, this.pictureFragment).commit();
            case 2131362370:
                this.sendItemsLayout.setVisibility(0);
                Utils.hideKeyboard(this.textMessage, getApplicationContext());
                this.fragmentManager.beginTransaction().setCustomAnimations(2130968589, 2130968590).addToBackStack(null).replace(2131361947, this.videoFragment).commit();
            default:
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (MomentStickerFragment.stickersWindow != null && MomentStickerFragment.stickersWindow.isShowing()) {
            MomentStickerFragment.stickersWindow.dismiss();
        } else if (this.sendItemsLayout.getVisibility() == 0) {
            this.sendItemsLayout.setVisibility(8);
        } else {
            finish();
        }
        return true;
    }

    public List<String> getPhotoFileUrls() {
        return this.photoFileUrls;
    }

    public List<String> getStickerFileUrls() {
        return this.stickerLocalFileUrls;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        this.btVideo.setImageResource(2130838110);
    }
}
