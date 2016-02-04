package android.support.v4.media;

import android.os.SystemClock;
import android.support.v7.appcompat.C0170R;
import android.view.KeyEvent;

public abstract class TransportPerformer {
    static final int AUDIOFOCUS_GAIN = 1;
    static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
    static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
    static final int AUDIOFOCUS_LOSS = -1;
    static final int AUDIOFOCUS_LOSS_TRANSIENT = -2;
    static final int AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;

    public abstract long onGetCurrentPosition();

    public abstract long onGetDuration();

    public abstract boolean onIsPlaying();

    public abstract void onPause();

    public abstract void onSeekTo(long j);

    public abstract void onStart();

    public abstract void onStop();

    public int onGetBufferPercentage() {
        return 100;
    }

    public int onGetTransportControlFlags() {
        return 60;
    }

    public boolean onMediaButtonDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case C0170R.styleable.Theme_panelMenuListWidth /*79*/:
            case C0170R.styleable.Theme_colorControlNormal /*85*/:
                if (!onIsPlaying()) {
                    onStart();
                    break;
                }
                onPause();
                break;
            case C0170R.styleable.Theme_colorControlActivated /*86*/:
                onStop();
                break;
            case TransportMediator.KEYCODE_MEDIA_PLAY /*126*/:
                onStart();
                break;
            case TransportMediator.KEYCODE_MEDIA_PAUSE /*127*/:
                onPause();
                break;
        }
        return true;
    }

    public boolean onMediaButtonUp(int keyCode, KeyEvent event) {
        return true;
    }

    public void onAudioFocusChange(int focusChange) {
        int keyCode = 0;
        switch (focusChange) {
            case AUDIOFOCUS_LOSS /*-1*/:
                keyCode = TransportMediator.KEYCODE_MEDIA_PAUSE;
                break;
        }
        if (keyCode != 0) {
            long now = SystemClock.uptimeMillis();
            onMediaButtonDown(TransportMediator.KEYCODE_MEDIA_PAUSE, new KeyEvent(now, now, 0, TransportMediator.KEYCODE_MEDIA_PAUSE, 0));
            onMediaButtonUp(TransportMediator.KEYCODE_MEDIA_PAUSE, new KeyEvent(now, now, AUDIOFOCUS_GAIN, TransportMediator.KEYCODE_MEDIA_PAUSE, 0));
        }
    }
}
