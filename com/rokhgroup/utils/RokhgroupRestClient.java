package com.rokhgroup.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public final class RokhgroupRestClient {
    public static int DEFAULT_TIMEOUT;
    public static AsyncHttpClient client;
    public static SyncHttpClient sclient;

    static {
        client = new AsyncHttpClient();
        sclient = new SyncHttpClient();
        DEFAULT_TIMEOUT = 1800000;
    }

    public static void get$6a529083(String url, AsyncHttpResponseHandler responseHandler) {
        client.get$3a5bfd5a(url, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(null, url, params, responseHandler);
        client.setTimeout(DEFAULT_TIMEOUT);
    }
}
