package cz.msebera.android.httpclient.impl.conn;

import cz.msebera.android.httpclient.HttpConnection;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public final class IdleConnectionHandler {
    public final Map<HttpConnection, Object> connectionToTimes;
    public HttpClientAndroidLog log;

    public IdleConnectionHandler() {
        this.log = new HttpClientAndroidLog(getClass());
        this.connectionToTimes = new HashMap();
    }
}
