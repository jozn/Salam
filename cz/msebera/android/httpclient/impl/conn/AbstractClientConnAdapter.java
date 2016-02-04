package cz.msebera.android.httpclient.impl.conn;

import cz.msebera.android.httpclient.HttpEntityEnclosingRequest;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.ManagedClientConnection;
import cz.msebera.android.httpclient.conn.OperatedClientConnection;
import cz.msebera.android.httpclient.protocol.HttpContext;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

@Deprecated
public abstract class AbstractClientConnAdapter implements ManagedClientConnection, HttpContext {
    private final ClientConnectionManager connManager;
    private volatile long duration;
    public volatile boolean markedReusable;
    volatile boolean released;
    volatile OperatedClientConnection wrappedConnection;

    protected AbstractClientConnAdapter(ClientConnectionManager mgr, OperatedClientConnection conn) {
        this.connManager = mgr;
        this.wrappedConnection = conn;
        this.markedReusable = false;
        this.released = false;
        this.duration = Long.MAX_VALUE;
    }

    public synchronized void detach() {
        this.wrappedConnection = null;
        this.duration = Long.MAX_VALUE;
    }

    public ClientConnectionManager getManager() {
        return this.connManager;
    }

    private void assertValid(OperatedClientConnection wrappedConn) throws ConnectionShutdownException {
        if (this.released || wrappedConn == null) {
            throw new ConnectionShutdownException();
        }
    }

    public final boolean isOpen() {
        OperatedClientConnection conn = this.wrappedConnection;
        if (conn == null) {
            return false;
        }
        return conn.isOpen();
    }

    public final boolean isStale() {
        if (this.released) {
            return true;
        }
        OperatedClientConnection conn = this.wrappedConnection;
        if (conn != null) {
            return conn.isStale();
        }
        return true;
    }

    public final void setSocketTimeout(int timeout) {
        OperatedClientConnection conn = this.wrappedConnection;
        assertValid(conn);
        conn.setSocketTimeout(timeout);
    }

    public final void flush() throws IOException {
        OperatedClientConnection conn = this.wrappedConnection;
        assertValid(conn);
        conn.flush();
    }

    public final boolean isResponseAvailable(int timeout) throws IOException {
        OperatedClientConnection conn = this.wrappedConnection;
        assertValid(conn);
        return conn.isResponseAvailable(timeout);
    }

    public final void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
        OperatedClientConnection conn = this.wrappedConnection;
        assertValid(conn);
        this.markedReusable = false;
        conn.receiveResponseEntity(response);
    }

    public final HttpResponse receiveResponseHeader() throws HttpException, IOException {
        OperatedClientConnection conn = this.wrappedConnection;
        assertValid(conn);
        this.markedReusable = false;
        return conn.receiveResponseHeader();
    }

    public final void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
        OperatedClientConnection conn = this.wrappedConnection;
        assertValid(conn);
        this.markedReusable = false;
        conn.sendRequestEntity(request);
    }

    public final void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
        OperatedClientConnection conn = this.wrappedConnection;
        assertValid(conn);
        this.markedReusable = false;
        conn.sendRequestHeader(request);
    }

    public final InetAddress getRemoteAddress() {
        OperatedClientConnection conn = this.wrappedConnection;
        assertValid(conn);
        return conn.getRemoteAddress();
    }

    public final int getRemotePort() {
        OperatedClientConnection conn = this.wrappedConnection;
        assertValid(conn);
        return conn.getRemotePort();
    }

    public final SSLSession getSSLSession() {
        OperatedClientConnection conn = this.wrappedConnection;
        assertValid(conn);
        if (!isOpen()) {
            return null;
        }
        Socket sock = conn.getSocket();
        if (sock instanceof SSLSocket) {
            return ((SSLSocket) sock).getSession();
        }
        return null;
    }

    public final void markReusable() {
        this.markedReusable = true;
    }

    public final void unmarkReusable() {
        this.markedReusable = false;
    }

    public final void setIdleDuration(long duration, TimeUnit unit) {
        if (duration > 0) {
            this.duration = unit.toMillis(duration);
        } else {
            this.duration = -1;
        }
    }

    public final synchronized void releaseConnection() {
        if (!this.released) {
            this.released = true;
            this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
        }
    }

    public final synchronized void abortConnection() {
        if (!this.released) {
            this.released = true;
            this.markedReusable = false;
            try {
                shutdown();
            } catch (IOException e) {
            }
            this.connManager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
        }
    }

    public final Object getAttribute(String id) {
        OperatedClientConnection conn = this.wrappedConnection;
        assertValid(conn);
        if (conn instanceof HttpContext) {
            return ((HttpContext) conn).getAttribute(id);
        }
        return null;
    }

    public final void setAttribute(String id, Object obj) {
        OperatedClientConnection conn = this.wrappedConnection;
        assertValid(conn);
        if (conn instanceof HttpContext) {
            ((HttpContext) conn).setAttribute(id, obj);
        }
    }
}
