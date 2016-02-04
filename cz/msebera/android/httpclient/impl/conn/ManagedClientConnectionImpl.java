package cz.msebera.android.httpclient.impl.conn;

import cz.msebera.android.httpclient.HttpEntityEnclosingRequest;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.ClientConnectionOperator;
import cz.msebera.android.httpclient.conn.ManagedClientConnection;
import cz.msebera.android.httpclient.conn.OperatedClientConnection;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.conn.routing.RouteTracker;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.Asserts;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

@Deprecated
final class ManagedClientConnectionImpl implements ManagedClientConnection {
    private volatile long duration;
    final ClientConnectionManager manager;
    private final ClientConnectionOperator operator;
    volatile HttpPoolEntry poolEntry;
    volatile boolean reusable;

    ManagedClientConnectionImpl(ClientConnectionManager manager, ClientConnectionOperator operator, HttpPoolEntry entry) {
        Args.notNull(manager, "Connection manager");
        Args.notNull(operator, "Connection operator");
        Args.notNull(entry, "HTTP pool entry");
        this.manager = manager;
        this.operator = operator;
        this.poolEntry = entry;
        this.reusable = false;
        this.duration = Long.MAX_VALUE;
    }

    final HttpPoolEntry detach() {
        HttpPoolEntry local = this.poolEntry;
        this.poolEntry = null;
        return local;
    }

    private OperatedClientConnection getConnection() {
        HttpPoolEntry local = this.poolEntry;
        if (local == null) {
            return null;
        }
        return (OperatedClientConnection) local.conn;
    }

    private OperatedClientConnection ensureConnection() {
        HttpPoolEntry local = this.poolEntry;
        if (local != null) {
            return (OperatedClientConnection) local.conn;
        }
        throw new ConnectionShutdownException();
    }

    private HttpPoolEntry ensurePoolEntry() {
        HttpPoolEntry local = this.poolEntry;
        if (local != null) {
            return local;
        }
        throw new ConnectionShutdownException();
    }

    public final void close() throws IOException {
        HttpPoolEntry local = this.poolEntry;
        if (local != null) {
            OperatedClientConnection conn = local.conn;
            local.tracker.reset();
            conn.close();
        }
    }

    public final void shutdown() throws IOException {
        HttpPoolEntry local = this.poolEntry;
        if (local != null) {
            OperatedClientConnection conn = local.conn;
            local.tracker.reset();
            conn.shutdown();
        }
    }

    public final boolean isOpen() {
        OperatedClientConnection conn = getConnection();
        if (conn != null) {
            return conn.isOpen();
        }
        return false;
    }

    public final boolean isStale() {
        OperatedClientConnection conn = getConnection();
        if (conn != null) {
            return conn.isStale();
        }
        return true;
    }

    public final void setSocketTimeout(int timeout) {
        ensureConnection().setSocketTimeout(timeout);
    }

    public final void flush() throws IOException {
        ensureConnection().flush();
    }

    public final boolean isResponseAvailable(int timeout) throws IOException {
        return ensureConnection().isResponseAvailable(timeout);
    }

    public final void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
        ensureConnection().receiveResponseEntity(response);
    }

    public final HttpResponse receiveResponseHeader() throws HttpException, IOException {
        return ensureConnection().receiveResponseHeader();
    }

    public final void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
        ensureConnection().sendRequestEntity(request);
    }

    public final void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
        ensureConnection().sendRequestHeader(request);
    }

    public final InetAddress getRemoteAddress() {
        return ensureConnection().getRemoteAddress();
    }

    public final int getRemotePort() {
        return ensureConnection().getRemotePort();
    }

    public final SSLSession getSSLSession() {
        Socket sock = ensureConnection().getSocket();
        if (sock instanceof SSLSocket) {
            return ((SSLSocket) sock).getSession();
        }
        return null;
    }

    public final HttpRoute getRoute() {
        return ensurePoolEntry().tracker.toRoute();
    }

    public final void open(HttpRoute route, HttpContext context, HttpParams params) throws IOException {
        OperatedClientConnection conn;
        HttpHost httpHost;
        Args.notNull(route, "Route");
        Args.notNull(params, "HTTP parameters");
        synchronized (this) {
            if (this.poolEntry == null) {
                throw new ConnectionShutdownException();
            }
            RouteTracker tracker = this.poolEntry.tracker;
            Asserts.notNull(tracker, "Route tracker");
            Asserts.check(!tracker.connected, "Connection already open");
            conn = this.poolEntry.conn;
        }
        HttpHost proxy = route.getProxyHost();
        ClientConnectionOperator clientConnectionOperator = this.operator;
        if (proxy != null) {
            httpHost = proxy;
        } else {
            httpHost = route.targetHost;
        }
        clientConnectionOperator.openConnection(conn, httpHost, route.localAddress, context, params);
        synchronized (this) {
            if (this.poolEntry == null) {
                throw new InterruptedIOException();
            }
            tracker = this.poolEntry.tracker;
            if (proxy == null) {
                tracker.connectTarget(conn.isSecure());
            } else {
                tracker.connectProxy(proxy, conn.isSecure());
            }
        }
    }

    public final void tunnelTarget$83338f(HttpParams params) throws IOException {
        HttpHost target;
        OperatedClientConnection conn;
        Args.notNull(params, "HTTP parameters");
        synchronized (this) {
            if (this.poolEntry == null) {
                throw new ConnectionShutdownException();
            }
            boolean z;
            RouteTracker tracker = this.poolEntry.tracker;
            Asserts.notNull(tracker, "Route tracker");
            Asserts.check(tracker.connected, "Connection not open");
            if (tracker.isTunnelled()) {
                z = false;
            } else {
                z = true;
            }
            Asserts.check(z, "Connection is already tunnelled");
            target = tracker.targetHost;
            conn = this.poolEntry.conn;
        }
        conn.update(null, target, false, params);
        synchronized (this) {
            if (this.poolEntry == null) {
                throw new InterruptedIOException();
            }
            this.poolEntry.tracker.tunnelTarget$1385ff();
        }
    }

    public final void layerProtocol(HttpContext context, HttpParams params) throws IOException {
        HttpHost target;
        OperatedClientConnection conn;
        Args.notNull(params, "HTTP parameters");
        synchronized (this) {
            if (this.poolEntry == null) {
                throw new ConnectionShutdownException();
            }
            RouteTracker tracker = this.poolEntry.tracker;
            Asserts.notNull(tracker, "Route tracker");
            Asserts.check(tracker.connected, "Connection not open");
            Asserts.check(tracker.isTunnelled(), "Protocol layering without a tunnel not supported");
            Asserts.check(!tracker.isLayered(), "Multiple protocol layering not supported");
            target = tracker.targetHost;
            conn = this.poolEntry.conn;
        }
        this.operator.updateSecureConnection(conn, target, context, params);
        synchronized (this) {
            if (this.poolEntry == null) {
                throw new InterruptedIOException();
            }
            this.poolEntry.tracker.layerProtocol(conn.isSecure());
        }
    }

    public final void setState(Object state) {
        ensurePoolEntry().state = state;
    }

    public final void markReusable() {
        this.reusable = true;
    }

    public final void unmarkReusable() {
        this.reusable = false;
    }

    public final void setIdleDuration(long duration, TimeUnit unit) {
        if (duration > 0) {
            this.duration = unit.toMillis(duration);
        } else {
            this.duration = -1;
        }
    }

    public final void releaseConnection() {
        synchronized (this) {
            if (this.poolEntry == null) {
                return;
            }
            this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
            this.poolEntry = null;
        }
    }

    public final void abortConnection() {
        synchronized (this) {
            if (this.poolEntry == null) {
                return;
            }
            this.reusable = false;
            try {
                this.poolEntry.conn.shutdown();
            } catch (IOException e) {
            }
            this.manager.releaseConnection(this, this.duration, TimeUnit.MILLISECONDS);
            this.poolEntry = null;
        }
    }
}
