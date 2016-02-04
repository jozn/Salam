package cz.msebera.android.httpclient.impl;

import cz.msebera.android.httpclient.io.HttpTransportMetrics;

public final class HttpConnectionMetricsImpl {
    private final HttpTransportMetrics inTransportMetric;
    private final HttpTransportMetrics outTransportMetric;
    long requestCount;
    long responseCount;

    public HttpConnectionMetricsImpl(HttpTransportMetrics inTransportMetric, HttpTransportMetrics outTransportMetric) {
        this.requestCount = 0;
        this.responseCount = 0;
        this.inTransportMetric = inTransportMetric;
        this.outTransportMetric = outTransportMetric;
    }
}
