package com.loopj.android.http;

import android.os.Build.VERSION;
import android.util.Log;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class LogHandler implements LogInterface {
    boolean mLoggingEnabled;
    int mLoggingLevel;

    public LogHandler() {
        this.mLoggingEnabled = true;
        this.mLoggingLevel = 2;
    }

    private void log(int logLevel, String tag, String msg) {
        logWithThrowable(logLevel, tag, msg, null);
    }

    private void logWithThrowable(int logLevel, String tag, String msg, Throwable t) {
        if (this.mLoggingEnabled) {
            if ((logLevel >= this.mLoggingLevel ? 1 : null) != null) {
                switch (logLevel) {
                    case Logger.WARNING /*2*/:
                        Log.v(tag, msg, t);
                    case Logger.INFO /*3*/:
                        Log.d(tag, msg, t);
                    case Logger.CONFIG /*4*/:
                        Log.i(tag, msg, t);
                    case Logger.FINE /*5*/:
                        Log.w(tag, msg, t);
                    case Logger.FINER /*6*/:
                        Log.e(tag, msg, t);
                    case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                        if (Integer.valueOf(VERSION.SDK).intValue() > 8) {
                            Log.wtf(tag, msg, t);
                        } else {
                            Log.e(tag, msg, t);
                        }
                    default:
                }
            }
        }
    }

    public final void m26v(String tag, String msg) {
        log(2, tag, msg);
    }

    public final void m22d(String tag, String msg) {
        log(2, tag, msg);
    }

    public final void m25i(String tag, String msg) {
        log(4, tag, msg);
    }

    public final void m27w(String tag, String msg) {
        log(5, tag, msg);
    }

    public final void m28w(String tag, String msg, Throwable t) {
        logWithThrowable(5, tag, msg, t);
    }

    public final void m23e(String tag, String msg) {
        log(6, tag, msg);
    }

    public final void m24e(String tag, String msg, Throwable t) {
        logWithThrowable(6, tag, msg, t);
    }
}
