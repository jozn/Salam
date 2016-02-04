package cz.msebera.android.httpclient.extras;

import android.util.Log;

public final class HttpClientAndroidLog {
    public boolean debugEnabled;
    public boolean errorEnabled;
    public boolean infoEnabled;
    public String logTag;
    private boolean traceEnabled;
    public boolean warnEnabled;

    public HttpClientAndroidLog(Object tag) {
        this.logTag = tag.toString();
        this.debugEnabled = false;
        this.errorEnabled = false;
        this.traceEnabled = false;
        this.warnEnabled = false;
        this.infoEnabled = false;
    }

    public final void debug(Object message) {
        if (this.debugEnabled) {
            Log.d(this.logTag, message.toString());
        }
    }

    public final void debug(Object message, Throwable t) {
        if (this.debugEnabled) {
            Log.d(this.logTag, message.toString(), t);
        }
    }

    public final void warn(Object message) {
        if (this.warnEnabled) {
            Log.w(this.logTag, message.toString());
        }
    }

    public final void warn(Object message, Throwable t) {
        if (this.warnEnabled) {
            Log.w(this.logTag, message.toString(), t);
        }
    }

    public final void info(Object message) {
        if (this.infoEnabled) {
            Log.i(this.logTag, message.toString());
        }
    }
}
