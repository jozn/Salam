package com.shamchat.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.path.android.jobqueue.JobManager;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.events.FeedbackSendCompleteEvent;
import com.shamchat.jobs.SendFeedbackJob;
import de.greenrobot.event.EventBus;

public class FeedbackActivity extends AppCompatActivity {
    JobManager jobManager;
    private EditText msgFeedBack;
    SharedPreferences preferences;
    private ProgressBar spinner;
    private Button submit;

    /* renamed from: com.shamchat.activity.FeedbackActivity.1 */
    class C07551 implements OnClickListener {
        C07551() {
        }

        public final void onClick(View v) {
            String msg = FeedbackActivity.this.msgFeedBack.getText().toString();
            if (msg.toString().equalsIgnoreCase(BuildConfig.VERSION_NAME)) {
                Toast.makeText(FeedbackActivity.this.getApplicationContext(), 2131493131, 0).show();
                return;
            }
            FeedbackActivity.this.jobManager = SHAMChatApplication.getInstance().jobManager;
            FeedbackActivity.this.submit.setVisibility(8);
            FeedbackActivity.this.spinner.setVisibility(0);
            FeedbackActivity.this.jobManager.addJobInBackground(new SendFeedbackJob(msg.toString()));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        getWindow().addFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        super.onCreate(savedInstanceState);
        setContentView(2130903086);
        this.msgFeedBack = (EditText) findViewById(2131362015);
        this.submit = (Button) findViewById(2131362016);
        this.spinner = (ProgressBar) findViewById(2131362017);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.submit.setOnClickListener(new C07551());
    }

    public void onResume() {
        super.onResume();
        System.out.println("AAAA MF onResume");
        try {
            EventBus.getDefault().register(this, false, 0);
        } catch (Throwable th) {
        }
    }

    public void onPause() {
        super.onPause();
        System.out.println("AAA MF onPause");
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(FeedbackSendCompleteEvent event) {
        System.out.println("FeedBack send complete event ");
        this.submit.setVisibility(0);
        this.spinner.setVisibility(8);
        Toast.makeText(getApplicationContext(), 2131493133, 0).show();
        finish();
    }
}
