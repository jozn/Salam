package com.loopj.android.http;

import cz.msebera.android.httpclient.client.methods.HttpEntityEnclosingRequestBase;
import java.net.URI;

public final class HttpGet extends HttpEntityEnclosingRequestBase {
    public HttpGet(String uri) {
        this.uri = URI.create(uri);
    }

    public final String getMethod() {
        return "GET";
    }
}
