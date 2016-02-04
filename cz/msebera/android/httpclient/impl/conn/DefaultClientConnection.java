package cz.msebera.android.httpclient.impl.conn;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpResponseFactory;
import cz.msebera.android.httpclient.conn.ManagedHttpClientConnection;
import cz.msebera.android.httpclient.conn.OperatedClientConnection;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.impl.SocketHttpClientConnection;
import cz.msebera.android.httpclient.io.HttpMessageParser;
import cz.msebera.android.httpclient.io.SessionInputBuffer;
import cz.msebera.android.httpclient.io.SessionOutputBuffer;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.params.HttpProtocolParams;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

@Deprecated
public final class DefaultClientConnection extends SocketHttpClientConnection implements ManagedHttpClientConnection, OperatedClientConnection, HttpContext {
    private final Map<String, Object> attributes;
    private boolean connSecure;
    public HttpClientAndroidLog headerLog;
    public HttpClientAndroidLog log;
    private volatile boolean shutdown;
    private volatile Socket socket;
    private HttpHost targetHost;
    public HttpClientAndroidLog wireLog;

    public DefaultClientConnection() {
        this.log = new HttpClientAndroidLog(getClass());
        this.headerLog = new HttpClientAndroidLog("cz.msebera.android.httpclient.headers");
        this.wireLog = new HttpClientAndroidLog("cz.msebera.android.httpclient.wire");
        this.attributes = new HashMap();
    }

    public final boolean isSecure() {
        return this.connSecure;
    }

    public final Socket getSocket() {
        return this.socket;
    }

    public final SSLSession getSSLSession() {
        if (this.socket instanceof SSLSocket) {
            return ((SSLSocket) this.socket).getSession();
        }
        return null;
    }

    public final void opening(Socket sock, HttpHost target) throws IOException {
        assertNotOpen();
        this.socket = sock;
        this.targetHost = target;
        if (this.shutdown) {
            sock.close();
            throw new InterruptedIOException("Connection already shutdown");
        }
    }

    public final void openCompleted(boolean secure, HttpParams params) throws IOException {
        Args.notNull(params, "Parameters");
        assertNotOpen();
        this.connSecure = secure;
        bind(this.socket, params);
    }

    public final void shutdown() throws IOException {
        this.shutdown = true;
        try {
            super.shutdown();
            if (this.log.debugEnabled) {
                this.log.debug("Connection " + this + " shut down");
            }
            Socket sock = this.socket;
            if (sock != null) {
                sock.close();
            }
        } catch (IOException ex) {
            this.log.debug("I/O error shutting down connection", ex);
        }
    }

    public final void close() throws IOException {
        try {
            super.close();
            if (this.log.debugEnabled) {
                this.log.debug("Connection " + this + " closed");
            }
        } catch (IOException ex) {
            this.log.debug("I/O error closing connection", ex);
        }
    }

    protected final SessionInputBuffer createSessionInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        if (buffersize <= 0) {
            buffersize = AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD;
        }
        SessionInputBuffer inbuffer = super.createSessionInputBuffer(socket, buffersize, params);
        if (this.wireLog.debugEnabled) {
            return new LoggingSessionInputBuffer(inbuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(params));
        }
        return inbuffer;
    }

    protected final SessionOutputBuffer createSessionOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        if (buffersize <= 0) {
            buffersize = AccessibilityNodeInfoCompat.ACTION_SCROLL_BACKWARD;
        }
        SessionOutputBuffer outbuffer = super.createSessionOutputBuffer(socket, buffersize, params);
        if (this.wireLog.debugEnabled) {
            return new LoggingSessionOutputBuffer(outbuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(params));
        }
        return outbuffer;
    }

    protected final HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
        return new DefaultHttpResponseParser(buffer, responseFactory, params);
    }

    public final void update(Socket sock, HttpHost target, boolean secure, HttpParams params) throws IOException {
        assertOpen();
        Args.notNull(target, "Target host");
        Args.notNull(params, "Parameters");
        if (sock != null) {
            this.socket = sock;
            bind(sock, params);
        }
        this.targetHost = target;
        this.connSecure = secure;
    }

    public final HttpResponse receiveResponseHeader() throws HttpException, IOException {
        HttpResponse response = super.receiveResponseHeader();
        if (this.log.debugEnabled) {
            this.log.debug("Receiving response: " + response.getStatusLine());
        }
        if (this.headerLog.debugEnabled) {
            this.headerLog.debug("<< " + response.getStatusLine().toString());
            for (Header header : response.getAllHeaders()) {
                this.headerLog.debug("<< " + header.toString());
            }
        }
        return response;
    }

    public final void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
        if (this.log.debugEnabled) {
            this.log.debug("Sending request: " + request.getRequestLine());
        }
        super.sendRequestHeader(request);
        if (this.headerLog.debugEnabled) {
            this.headerLog.debug(">> " + request.getRequestLine().toString());
            for (Header header : request.getAllHeaders()) {
                this.headerLog.debug(">> " + header.toString());
            }
        }
    }

    public final Object getAttribute(String id) {
        return this.attributes.get(id);
    }

    public final void setAttribute(String id, Object obj) {
        this.attributes.put(id, obj);
    }
}
