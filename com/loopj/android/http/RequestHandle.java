package com.loopj.android.http;

import java.lang.ref.WeakReference;

public final class RequestHandle {
    final WeakReference<AsyncHttpRequest> request;

    public RequestHandle(AsyncHttpRequest request) {
        this.request = new WeakReference(request);
    }
}
