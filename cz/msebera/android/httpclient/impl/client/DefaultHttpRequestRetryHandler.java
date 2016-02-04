package cz.msebera.android.httpclient.impl.client;

import cz.msebera.android.httpclient.HttpEntityEnclosingRequest;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.client.HttpRequestRetryHandler;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.client.protocol.HttpClientContext;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.net.ssl.SSLException;

public final class DefaultHttpRequestRetryHandler implements HttpRequestRetryHandler {
    public static final DefaultHttpRequestRetryHandler INSTANCE;
    private final Set<Class<? extends IOException>> nonRetriableClasses;
    private final boolean requestSentRetryEnabled;
    private final int retryCount;

    static {
        INSTANCE = new DefaultHttpRequestRetryHandler((byte) 0);
    }

    private DefaultHttpRequestRetryHandler(Collection<Class<? extends IOException>> clazzes) {
        this.retryCount = 3;
        this.requestSentRetryEnabled = false;
        this.nonRetriableClasses = new HashSet();
        for (Class<? extends IOException> clazz : clazzes) {
            this.nonRetriableClasses.add(clazz);
        }
    }

    private DefaultHttpRequestRetryHandler() {
        this(Arrays.asList(new Class[]{InterruptedIOException.class, UnknownHostException.class, ConnectException.class, SSLException.class}));
    }

    public DefaultHttpRequestRetryHandler(byte b) {
        this();
    }

    public final boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        Args.notNull(exception, "Exception parameter");
        Args.notNull(context, "HTTP context");
        if (executionCount > this.retryCount) {
            return false;
        }
        if (this.nonRetriableClasses.contains(exception.getClass())) {
            return false;
        }
        HttpRequest httpRequest;
        Object obj;
        for (Class isInstance : this.nonRetriableClasses) {
            if (isInstance.isInstance(exception)) {
                return false;
            }
        }
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = (HttpRequest) clientContext.getAttribute("http.request", HttpRequest.class);
        if (request instanceof RequestWrapper) {
            httpRequest = ((RequestWrapper) request).original;
        } else {
            httpRequest = request;
        }
        if ((httpRequest instanceof HttpUriRequest) && ((HttpUriRequest) httpRequest).isAborted()) {
            obj = 1;
        } else {
            obj = null;
        }
        if (obj != null) {
            return false;
        }
        if (request instanceof HttpEntityEnclosingRequest) {
            obj = null;
        } else {
            obj = 1;
        }
        if (obj != null) {
            return true;
        }
        Boolean bool = (Boolean) clientContext.getAttribute("http.request_sent", Boolean.class);
        if (bool == null || !bool.booleanValue()) {
            obj = null;
        } else {
            obj = 1;
        }
        return obj == null || this.requestSentRetryEnabled;
    }
}
