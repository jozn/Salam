package cz.msebera.android.httpclient.params;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import cz.msebera.android.httpclient.util.Args;

@Deprecated
public final class HttpConnectionParams {
    public static int getSoTimeout(HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getIntParameter("http.socket.timeout", 0);
    }

    public static void setSoTimeout(HttpParams params, int timeout) {
        Args.notNull(params, "HTTP parameters");
        params.setIntParameter("http.socket.timeout", timeout);
    }

    public static void setTcpNoDelay$465b0079(HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        params.setBooleanParameter$59a1668b("http.tcp.nodelay");
    }

    public static void setSocketBufferSize$465ac0a8(HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        params.setIntParameter("http.socket.buffer-size", AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD);
    }

    public static int getConnectionTimeout(HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getIntParameter("http.connection.timeout", 0);
    }

    public static void setConnectionTimeout(HttpParams params, int timeout) {
        Args.notNull(params, "HTTP parameters");
        params.setIntParameter("http.connection.timeout", timeout);
    }
}
