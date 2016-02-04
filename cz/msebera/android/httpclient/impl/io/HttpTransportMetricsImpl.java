package cz.msebera.android.httpclient.impl.io;

import cz.msebera.android.httpclient.io.HttpTransportMetrics;

public final class HttpTransportMetricsImpl implements HttpTransportMetrics {
    private long bytesTransferred;

    public HttpTransportMetricsImpl() {
        this.bytesTransferred = 0;
    }

    public final void incrementBytesTransferred(long count) {
        this.bytesTransferred += count;
    }
}
