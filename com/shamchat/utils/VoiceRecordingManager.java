package com.shamchat.utils;

import android.content.Context;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import com.shamchat.activity.ShamVoiceRecording;
import com.shamchat.androidclient.SHAMChatApplication;

public final class VoiceRecordingManager {
    private static VoiceRecordingManager instance;
    private Context context;
    public int currentFormat;
    public OnErrorListener errorListener;
    public String[] file_exts;
    public OnInfoListener infoListener;
    public int[] output_formats;
    public String recordedFile;
    public MediaRecorder recorder;
    public ShamVoiceRecording shamVoiceRecording;

    /* renamed from: com.shamchat.utils.VoiceRecordingManager.1 */
    class C11791 implements OnErrorListener {
        C11791() {
        }

        public final void onError(MediaRecorder mr, int what, int extra) {
        }
    }

    /* renamed from: com.shamchat.utils.VoiceRecordingManager.2 */
    class C11802 implements OnInfoListener {
        C11802() {
        }

        public final void onInfo(MediaRecorder mr, int what, int extra) {
        }
    }

    static {
        instance = new VoiceRecordingManager();
    }

    public VoiceRecordingManager() {
        this.recorder = null;
        this.currentFormat = 2;
        this.output_formats = new int[]{2, 1, 0};
        this.file_exts = new String[]{".mp4", ".3gp", ".mp3"};
        this.errorListener = new C11791();
        this.infoListener = new C11802();
        this.context = SHAMChatApplication.getMyApplicationContext();
    }

    public static VoiceRecordingManager getIsntance() {
        return instance;
    }
}
