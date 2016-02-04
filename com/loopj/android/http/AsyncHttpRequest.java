package com.loopj.android.http;

import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.AbstractHttpClient;
import cz.msebera.android.httpclient.protocol.HttpContext;
import java.util.concurrent.atomic.AtomicBoolean;

public final class AsyncHttpRequest implements Runnable {
    private boolean cancelIsNotified;
    private final AbstractHttpClient client;
    private final HttpContext context;
    private int executionCount;
    private final AtomicBoolean isCancelled;
    volatile boolean isFinished;
    private boolean isRequestPreProcessed;
    private final HttpUriRequest request;
    private final ResponseHandlerInterface responseHandler;

    public AsyncHttpRequest(AbstractHttpClient client, HttpContext context, HttpUriRequest request, ResponseHandlerInterface responseHandler) {
        this.isCancelled = new AtomicBoolean();
        this.client = (AbstractHttpClient) Utils.notNull(client, "client");
        this.context = (HttpContext) Utils.notNull(context, "context");
        this.request = (HttpUriRequest) Utils.notNull(request, "request");
        this.responseHandler = (ResponseHandlerInterface) Utils.notNull(responseHandler, "responseHandler");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
        r10 = this;
        r3 = 0;
        r5 = 0;
        r2 = 1;
        r1 = r10.isCancelled();
        if (r1 == 0) goto L_0x000a;
    L_0x0009:
        return;
    L_0x000a:
        r1 = r10.isRequestPreProcessed;
        if (r1 != 0) goto L_0x0010;
    L_0x000e:
        r10.isRequestPreProcessed = r2;
    L_0x0010:
        r1 = r10.isCancelled();
        if (r1 != 0) goto L_0x0009;
    L_0x0016:
        r1 = r10.responseHandler;
        r1.sendStartMessage();
        r1 = r10.isCancelled();
        if (r1 != 0) goto L_0x0009;
    L_0x0021:
        r1 = r10.client;	 Catch:{ IOException -> 0x00a2 }
        r6 = r1.getHttpRequestRetryHandler();	 Catch:{ IOException -> 0x00a2 }
        r1 = r5;
        r4 = r2;
    L_0x0029:
        if (r4 == 0) goto L_0x00a1;
    L_0x002b:
        r1 = r10.isCancelled();	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        if (r1 != 0) goto L_0x00ae;
    L_0x0031:
        r1 = r10.request;	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r1 = r1.getURI();	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r1 = r1.getScheme();	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        if (r1 != 0) goto L_0x00c3;
    L_0x003d:
        r1 = new java.net.MalformedURLException;	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r4 = "No valid URI scheme was provided";
        r1.<init>(r4);	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        throw r1;	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
    L_0x0045:
        r1 = move-exception;
        r4 = new java.io.IOException;	 Catch:{ Exception -> 0x007e }
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x007e }
        r8 = "UnknownHostException exception: ";
        r7.<init>(r8);	 Catch:{ Exception -> 0x007e }
        r8 = r1.getMessage();	 Catch:{ Exception -> 0x007e }
        r7 = r7.append(r8);	 Catch:{ Exception -> 0x007e }
        r7 = r7.toString();	 Catch:{ Exception -> 0x007e }
        r4.<init>(r7);	 Catch:{ Exception -> 0x007e }
        r7 = r10.executionCount;	 Catch:{ Exception -> 0x007e }
        if (r7 <= 0) goto L_0x011b;
    L_0x0062:
        r7 = r10.executionCount;	 Catch:{ Exception -> 0x007e }
        r7 = r7 + 1;
        r10.executionCount = r7;	 Catch:{ Exception -> 0x007e }
        r8 = r10.context;	 Catch:{ Exception -> 0x007e }
        r1 = r6.retryRequest(r1, r7, r8);	 Catch:{ Exception -> 0x007e }
        if (r1 == 0) goto L_0x011b;
    L_0x0070:
        r1 = r2;
    L_0x0071:
        r9 = r4;
        r4 = r1;
        r1 = r9;
    L_0x0074:
        if (r4 == 0) goto L_0x0029;
    L_0x0076:
        r7 = r10.responseHandler;	 Catch:{ Exception -> 0x007e }
        r8 = r10.executionCount;	 Catch:{ Exception -> 0x007e }
        r7.sendRetryMessage(r8);	 Catch:{ Exception -> 0x007e }
        goto L_0x0029;
    L_0x007e:
        r1 = move-exception;
        r4 = r1;
        r1 = com.loopj.android.http.AsyncHttpClient.log;	 Catch:{ IOException -> 0x00a2 }
        r6 = "AsyncHttpRequest";
        r7 = "Unhandled exception origin cause";
        r1.m17e(r6, r7, r4);	 Catch:{ IOException -> 0x00a2 }
        r1 = new java.io.IOException;	 Catch:{ IOException -> 0x00a2 }
        r6 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x00a2 }
        r7 = "Unhandled exception: ";
        r6.<init>(r7);	 Catch:{ IOException -> 0x00a2 }
        r4 = r4.getMessage();	 Catch:{ IOException -> 0x00a2 }
        r4 = r6.append(r4);	 Catch:{ IOException -> 0x00a2 }
        r4 = r4.toString();	 Catch:{ IOException -> 0x00a2 }
        r1.<init>(r4);	 Catch:{ IOException -> 0x00a2 }
    L_0x00a1:
        throw r1;	 Catch:{ IOException -> 0x00a2 }
    L_0x00a2:
        r0 = move-exception;
        r1 = r10.isCancelled();
        if (r1 != 0) goto L_0x0133;
    L_0x00a9:
        r1 = r10.responseHandler;
        r1.sendFailureMessage(r3, r5, r5, r0);
    L_0x00ae:
        r1 = r10.isCancelled();
        if (r1 != 0) goto L_0x0009;
    L_0x00b4:
        r1 = r10.responseHandler;
        r1.sendFinishMessage();
        r1 = r10.isCancelled();
        if (r1 != 0) goto L_0x0009;
    L_0x00bf:
        r10.isFinished = r2;
        goto L_0x0009;
    L_0x00c3:
        r1 = r10.responseHandler;	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r1 = r1 instanceof com.loopj.android.http.RangeFileAsyncHttpResponseHandler;	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        if (r1 == 0) goto L_0x00d2;
    L_0x00c9:
        r1 = r10.responseHandler;	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r1 = (com.loopj.android.http.RangeFileAsyncHttpResponseHandler) r1;	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r4 = r10.request;	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r1.updateRequestHeaders(r4);	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
    L_0x00d2:
        r1 = r10.client;	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r4 = r10.request;	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r7 = r10.context;	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r1 = r1.execute(r4, r7);	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r4 = r10.isCancelled();	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        if (r4 != 0) goto L_0x00ae;
    L_0x00e2:
        r4 = r10.isCancelled();	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        if (r4 != 0) goto L_0x00ae;
    L_0x00e8:
        r4 = r10.responseHandler;	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r4.sendResponseMessage(r1);	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        r10.isCancelled();	 Catch:{ UnknownHostException -> 0x0045, NullPointerException -> 0x00f1, IOException -> 0x011e, Exception -> 0x007e }
        goto L_0x00ae;
    L_0x00f1:
        r1 = move-exception;
        r4 = new java.io.IOException;	 Catch:{ Exception -> 0x007e }
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x007e }
        r8 = "NPE in HttpClient: ";
        r7.<init>(r8);	 Catch:{ Exception -> 0x007e }
        r1 = r1.getMessage();	 Catch:{ Exception -> 0x007e }
        r1 = r7.append(r1);	 Catch:{ Exception -> 0x007e }
        r1 = r1.toString();	 Catch:{ Exception -> 0x007e }
        r4.<init>(r1);	 Catch:{ Exception -> 0x007e }
        r1 = r10.executionCount;	 Catch:{ Exception -> 0x007e }
        r1 = r1 + 1;
        r10.executionCount = r1;	 Catch:{ Exception -> 0x007e }
        r7 = r10.context;	 Catch:{ Exception -> 0x007e }
        r1 = r6.retryRequest(r4, r1, r7);	 Catch:{ Exception -> 0x007e }
        r9 = r4;
        r4 = r1;
        r1 = r9;
        goto L_0x0074;
    L_0x011b:
        r1 = r3;
        goto L_0x0071;
    L_0x011e:
        r1 = move-exception;
        r4 = r10.isCancelled();	 Catch:{ Exception -> 0x007e }
        if (r4 != 0) goto L_0x00ae;
    L_0x0125:
        r4 = r10.executionCount;	 Catch:{ Exception -> 0x007e }
        r4 = r4 + 1;
        r10.executionCount = r4;	 Catch:{ Exception -> 0x007e }
        r7 = r10.context;	 Catch:{ Exception -> 0x007e }
        r4 = r6.retryRequest(r1, r4, r7);	 Catch:{ Exception -> 0x007e }
        goto L_0x0074;
    L_0x0133:
        r1 = com.loopj.android.http.AsyncHttpClient.log;
        r3 = "AsyncHttpRequest";
        r4 = "makeRequestWithRetries returned error";
        r1.m17e(r3, r4, r0);
        goto L_0x00ae;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loopj.android.http.AsyncHttpRequest.run():void");
    }

    public final boolean isCancelled() {
        boolean cancelled = this.isCancelled.get();
        if (cancelled) {
            sendCancelNotification();
        }
        return cancelled;
    }

    private synchronized void sendCancelNotification() {
        if (!(this.isFinished || !this.isCancelled.get() || this.cancelIsNotified)) {
            this.cancelIsNotified = true;
            this.responseHandler.sendCancelMessage();
        }
    }
}
