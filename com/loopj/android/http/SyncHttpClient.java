package com.loopj.android.http;

import android.content.Context;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.HttpContext;

public final class SyncHttpClient extends AsyncHttpClient {
    public SyncHttpClient() {
        super((byte) 0);
    }

    protected final RequestHandle sendRequest$56c1b3a2(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, ResponseHandlerInterface responseHandler, Context context) {
        responseHandler.setUseSynchronousMode(true);
        AsyncHttpClient.newAsyncHttpRequest$bbf1bb6(client, httpContext, uriRequest, responseHandler).run();
        return new RequestHandle(null);
    }
}
