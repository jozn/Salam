package com.shamchat.activity;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.path.android.jobqueue.JobManager;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.jobs.FileUploadJob;
import com.shamchat.utils.VoiceRecordingManager;
import java.io.File;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class ShamVoiceRecording extends Fragment implements OnTouchListener {
    private static final int recordTimeLimit = 1;
    private Button btnAddVoice;
    private long endedMillisec;
    private Handler handler;
    private boolean isGroupChat;
    private JobManager jobManager;
    private String recipientId;
    private Runnable runnable;
    private String senderId;
    private long startedMillisec;
    private TextView txtTime;

    /* renamed from: com.shamchat.activity.ShamVoiceRecording.1 */
    class C08921 implements Runnable {
        C08921() {
        }

        public final void run() {
            ShamVoiceRecording.this.txtTime.setText(" : " + ((System.currentTimeMillis() - ShamVoiceRecording.this.startedMillisec) / 1000));
            ShamVoiceRecording.this.handler.postDelayed(this, 100);
        }
    }

    public ShamVoiceRecording() {
        this.handler = new Handler();
        this.runnable = new C08921();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(2130903146, container, false);
        this.btnAddVoice = (Button) inflate.findViewById(2131361942);
        this.txtTime = (TextView) inflate.findViewById(2131362249);
        this.btnAddVoice.setOnTouchListener(this);
        this.senderId = getArguments().getString("message_sender");
        this.recipientId = getArguments().getString("message_recipient");
        this.isGroupChat = getArguments().getBoolean("is_group_chat");
        this.jobManager = SHAMChatApplication.getInstance().jobManager;
        return inflate;
    }

    public void uploadRecordingCompletedFiles(String recordedFile) {
        if (getRecordedDuration() > 1) {
            this.jobManager.addJobInBackground(new FileUploadJob(recordedFile, this.senderId, this.recipientId, this.isGroupChat, MessageContentType.VOICE_RECORD.ordinal(), "VOICE_RECORD", false, null, 0.0d, 0.0d));
            return;
        }
        Toast.makeText(getActivity(), 2131493284, 0).show();
    }

    private long getRecordedDuration() {
        return (this.endedMillisec - this.startedMillisec) / 1000;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public boolean onTouch(View v, MotionEvent event) {
        System.out.println("Action " + event.getAction());
        switch (event.getAction()) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                System.out.println("ACTION DOWN");
                this.btnAddVoice.setBackgroundResource(2130838113);
                startRecording();
                break;
            case recordTimeLimit /*1*/:
                System.out.println("ACTION UP");
                this.btnAddVoice.setBackgroundResource(2130838112);
                stopRecording();
                break;
        }
        return false;
    }

    private void startRecording() {
        this.txtTime.setVisibility(0);
        this.startedMillisec = System.currentTimeMillis();
        this.handler.postDelayed(this.runnable, 1000);
        VoiceRecordingManager isntance = VoiceRecordingManager.getIsntance();
        isntance.recorder = new MediaRecorder();
        isntance.recorder.setAudioSource(0);
        isntance.recorder.setOutputFormat(isntance.output_formats[isntance.currentFormat]);
        isntance.recorder.setAudioEncoder(recordTimeLimit);
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "salam/audio");
        if (!file.exists()) {
            file.mkdirs();
        }
        isntance.recordedFile = file.getAbsolutePath() + MqttTopic.TOPIC_LEVEL_SEPARATOR + System.currentTimeMillis() + isntance.file_exts[isntance.currentFormat];
        isntance.recorder.setOutputFile(isntance.recordedFile);
        isntance.recorder.setOnErrorListener(isntance.errorListener);
        isntance.recorder.setOnInfoListener(isntance.infoListener);
        try {
            isntance.recorder.prepare();
            isntance.recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        isntance.shamVoiceRecording = this;
    }

    private void stopRecording() {
        this.handler.removeCallbacks(this.runnable);
        this.endedMillisec = System.currentTimeMillis();
        System.out.println("Holded Duration : " + ((this.endedMillisec - this.startedMillisec) / 1000) + " sec");
        VoiceRecordingManager isntance = VoiceRecordingManager.getIsntance();
        if (isntance.recorder != null) {
            try {
                isntance.recorder.stop();
                isntance.recorder.reset();
                isntance.recorder.release();
                isntance.recorder = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            isntance.shamVoiceRecording.uploadRecordingCompletedFiles(isntance.recordedFile);
        }
        this.txtTime.setVisibility(8);
    }
}
