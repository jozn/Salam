package com.loopj.android.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.client.HttpResponseException;
import cz.msebera.android.httpclient.util.ByteArrayBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URI;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public abstract class AsyncHttpResponseHandler implements ResponseHandlerInterface {
    private WeakReference<Object> TAG;
    private Handler handler;
    private Looper looper;
    private Header[] requestHeaders;
    URI requestURI;
    String responseCharset;
    boolean usePoolThread;
    boolean useSynchronousMode;

    private static class ResponderHandler extends Handler {
        private final AsyncHttpResponseHandler mResponder;

        ResponderHandler(AsyncHttpResponseHandler mResponder, Looper looper) {
            super(looper);
            this.mResponder = mResponder;
        }

        public final void handleMessage(Message msg) {
            this.mResponder.handleMessage(msg);
        }
    }

    public abstract void onFailure(int i, Header[] headerArr, byte[] bArr, Throwable th);

    public abstract void onSuccess(int i, Header[] headerArr, byte[] bArr);

    public AsyncHttpResponseHandler() {
        this((byte) 0);
    }

    private AsyncHttpResponseHandler(byte b) {
        this.responseCharset = "UTF-8";
        this.requestURI = null;
        this.requestHeaders = null;
        this.looper = null;
        this.TAG = new WeakReference(null);
        this.looper = Looper.myLooper();
        setUseSynchronousMode(false);
        this.usePoolThread = false;
    }

    public AsyncHttpResponseHandler(char c) {
        this.responseCharset = "UTF-8";
        this.requestURI = null;
        this.requestHeaders = null;
        this.looper = null;
        this.TAG = new WeakReference(null);
        this.usePoolThread = false;
        if (!this.usePoolThread) {
            this.looper = Looper.myLooper();
            setUseSynchronousMode(false);
        }
    }

    public final void setRequestURI(URI requestURI) {
        this.requestURI = requestURI;
    }

    public final void setRequestHeaders(Header[] requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public final boolean getUseSynchronousMode() {
        return this.useSynchronousMode;
    }

    public final void setUseSynchronousMode(boolean sync) {
        if (!sync && this.looper == null) {
            sync = true;
            AsyncHttpClient.log.m20w("AsyncHttpRH", "Current thread has not called Looper.prepare(). Forcing synchronous mode.");
        }
        if (!sync && this.handler == null) {
            this.handler = new ResponderHandler(this, this.looper);
        } else if (sync && this.handler != null) {
            this.handler = null;
        }
        this.useSynchronousMode = sync;
    }

    public final boolean getUsePoolThread() {
        return this.usePoolThread;
    }

    public final String getCharset() {
        return this.responseCharset == null ? "UTF-8" : this.responseCharset;
    }

    public void onProgress(long bytesWritten, long totalSize) {
        LogInterface logInterface = AsyncHttpClient.log;
        String str = "AsyncHttpRH";
        String str2 = "Progress %d from %d (%2.0f%%)";
        Object[] objArr = new Object[3];
        objArr[0] = Long.valueOf(bytesWritten);
        objArr[1] = Long.valueOf(totalSize);
        objArr[2] = Double.valueOf(totalSize > 0 ? ((((double) bytesWritten) * 1.0d) / ((double) totalSize)) * 100.0d : -1.0d);
        logInterface.m19v(str, String.format(str2, objArr));
    }

    public void onRetry(int retryNo) {
        AsyncHttpClient.log.m15d("AsyncHttpRH", String.format("Request retry no. %d", new Object[]{Integer.valueOf(retryNo)}));
    }

    public final void sendProgressMessage(long bytesWritten, long bytesTotal) {
        sendMessage(obtainMessage(4, new Object[]{Long.valueOf(bytesWritten), Long.valueOf(bytesTotal)}));
    }

    public final void sendFailureMessage(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
        sendMessage(obtainMessage(1, new Object[]{Integer.valueOf(statusCode), headers, responseBody, throwable}));
    }

    public final void sendStartMessage() {
        sendMessage(obtainMessage(2, null));
    }

    public final void sendFinishMessage() {
        sendMessage(obtainMessage(3, null));
    }

    public final void sendRetryMessage(int retryNo) {
        sendMessage(obtainMessage(5, new Object[]{Integer.valueOf(retryNo)}));
    }

    public final void sendCancelMessage() {
        sendMessage(obtainMessage(6, null));
    }

    protected final void handleMessage(Message message) {
        RuntimeException runtimeException;
        try {
            Object[] response;
            switch (message.what) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    response = message.obj;
                    if (response == null || response.length < 3) {
                        AsyncHttpClient.log.m16e("AsyncHttpRH", "SUCCESS_MESSAGE didn't got enough params");
                        return;
                    } else {
                        onSuccess(((Integer) response[0]).intValue(), (Header[]) response[1], (byte[]) response[2]);
                        return;
                    }
                case Logger.SEVERE /*1*/:
                    response = (Object[]) message.obj;
                    if (response == null || response.length < 4) {
                        AsyncHttpClient.log.m16e("AsyncHttpRH", "FAILURE_MESSAGE didn't got enough params");
                        return;
                    } else {
                        onFailure(((Integer) response[0]).intValue(), (Header[]) response[1], (byte[]) response[2], (Throwable) response[3]);
                        return;
                    }
                case Logger.CONFIG /*4*/:
                    response = (Object[]) message.obj;
                    if (response == null || response.length < 2) {
                        AsyncHttpClient.log.m16e("AsyncHttpRH", "PROGRESS_MESSAGE didn't got enough params");
                        return;
                    } else {
                        onProgress(((Long) response[0]).longValue(), ((Long) response[1]).longValue());
                        return;
                    }
                case Logger.FINE /*5*/:
                    response = (Object[]) message.obj;
                    if (response == null || response.length != 1) {
                        AsyncHttpClient.log.m16e("AsyncHttpRH", "RETRY_MESSAGE didn't get enough params");
                        return;
                    } else {
                        onRetry(((Integer) response[0]).intValue());
                        return;
                    }
                case Logger.FINER /*6*/:
                    AsyncHttpClient.log.m15d("AsyncHttpRH", "Request got cancelled");
                    return;
                default:
                    return;
            }
        } catch (Throwable th) {
            AsyncHttpClient.log.m17e("AsyncHttpRH", "User-space exception detected!", th);
            runtimeException = new RuntimeException(th);
        }
        AsyncHttpClient.log.m17e("AsyncHttpRH", "User-space exception detected!", th);
        runtimeException = new RuntimeException(th);
    }

    private void sendMessage(Message msg) {
        if (this.useSynchronousMode || this.handler == null) {
            handleMessage(msg);
        } else if (!Thread.currentThread().isInterrupted()) {
            Utils.asserts(this.handler != null, "handler should not be null!");
            this.handler.sendMessage(msg);
        }
    }

    protected final void postRunnable(Runnable runnable) {
        if (this.useSynchronousMode || this.handler == null) {
            runnable.run();
        } else {
            this.handler.post(runnable);
        }
    }

    private Message obtainMessage(int responseMessageId, Object responseMessageData) {
        return Message.obtain(this.handler, responseMessageId, responseMessageData);
    }

    public final void sendResponseMessage(HttpResponse response) throws IOException {
        if (!Thread.currentThread().isInterrupted()) {
            StatusLine status = response.getStatusLine();
            byte[] responseBody = getResponseData(response.getEntity());
            if (!Thread.currentThread().isInterrupted()) {
                if (status.getStatusCode() >= 300) {
                    sendFailureMessage(status.getStatusCode(), response.getAllHeaders(), responseBody, new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()));
                    return;
                }
                int statusCode = status.getStatusCode();
                Header[] allHeaders = response.getAllHeaders();
                sendMessage(obtainMessage(0, new Object[]{Integer.valueOf(statusCode), allHeaders, responseBody}));
            }
        }
    }

    byte[] getResponseData(HttpEntity entity) throws IOException {
        int buffersize = AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD;
        byte[] responseBody = null;
        if (entity != null) {
            InputStream instream = entity.getContent();
            if (instream != null) {
                long contentLength = entity.getContentLength();
                if (contentLength > 2147483647L) {
                    throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
                }
                if (contentLength > 0) {
                    buffersize = (int) contentLength;
                }
                try {
                    ByteArrayBuffer buffer = new ByteArrayBuffer(buffersize);
                    byte[] tmp = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD];
                    long count = 0;
                    while (true) {
                        int l = instream.read(tmp);
                        if (l == -1 || Thread.currentThread().isInterrupted()) {
                            break;
                        }
                        long j;
                        count += (long) l;
                        buffer.append(tmp, 0, l);
                        if (contentLength <= 0) {
                            j = 1;
                        } else {
                            j = contentLength;
                        }
                        sendProgressMessage(count, j);
                    }
                    AsyncHttpClient.silentCloseInputStream(instream);
                    AsyncHttpClient.endEntityViaReflection(entity);
                    responseBody = buffer.toByteArray();
                } catch (OutOfMemoryError e) {
                    System.gc();
                    throw new IOException("File too large to fit into available memory");
                } catch (Throwable th) {
                    AsyncHttpClient.silentCloseInputStream(instream);
                    AsyncHttpClient.endEntityViaReflection(entity);
                }
            }
        }
        return responseBody;
    }
}
