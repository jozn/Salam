package cz.msebera.android.httpclient.impl.conn;

import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.conn.ClientConnectionOperator;
import cz.msebera.android.httpclient.conn.OperatedClientConnection;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.conn.routing.RouteTracker;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.Asserts;
import java.io.IOException;
import java.io.InterruptedIOException;

@Deprecated
public abstract class AbstractPoolEntry {
    protected final ClientConnectionOperator connOperator;
    public final OperatedClientConnection connection;
    public volatile HttpRoute route;
    protected volatile Object state;
    protected volatile RouteTracker tracker;

    public AbstractPoolEntry(ClientConnectionOperator connOperator, HttpRoute route) {
        Args.notNull(connOperator, "Connection operator");
        this.connOperator = connOperator;
        this.connection = connOperator.createConnection();
        this.route = route;
        this.tracker = null;
    }

    public final Object getState() {
        return this.state;
    }

    public final void setState(Object state) {
        this.state = state;
    }

    public final void open(HttpRoute route, HttpContext context, HttpParams params) throws IOException {
        HttpHost httpHost;
        Args.notNull(route, "Route");
        Args.notNull(params, "HTTP parameters");
        if (this.tracker != null) {
            Asserts.check(!this.tracker.connected, "Connection already open");
        }
        this.tracker = new RouteTracker(route);
        HttpHost proxy = route.getProxyHost();
        ClientConnectionOperator clientConnectionOperator = this.connOperator;
        OperatedClientConnection operatedClientConnection = this.connection;
        if (proxy != null) {
            httpHost = proxy;
        } else {
            httpHost = route.targetHost;
        }
        clientConnectionOperator.openConnection(operatedClientConnection, httpHost, route.localAddress, context, params);
        RouteTracker localTracker = this.tracker;
        if (localTracker == null) {
            throw new InterruptedIOException("Request aborted");
        } else if (proxy == null) {
            localTracker.connectTarget(this.connection.isSecure());
        } else {
            localTracker.connectProxy(proxy, this.connection.isSecure());
        }
    }

    public final void tunnelTarget$83338f(HttpParams params) throws IOException {
        Args.notNull(params, "HTTP parameters");
        Asserts.notNull(this.tracker, "Route tracker");
        Asserts.check(this.tracker.connected, "Connection not open");
        Asserts.check(!this.tracker.isTunnelled(), "Connection is already tunnelled");
        this.connection.update(null, this.tracker.targetHost, false, params);
        this.tracker.tunnelTarget$1385ff();
    }

    public final void layerProtocol(HttpContext context, HttpParams params) throws IOException {
        Args.notNull(params, "HTTP parameters");
        Asserts.notNull(this.tracker, "Route tracker");
        Asserts.check(this.tracker.connected, "Connection not open");
        Asserts.check(this.tracker.isTunnelled(), "Protocol layering without a tunnel not supported");
        Asserts.check(!this.tracker.isLayered(), "Multiple protocol layering not supported");
        this.connOperator.updateSecureConnection(this.connection, this.tracker.targetHost, context, params);
        this.tracker.layerProtocol(this.connection.isSecure());
    }

    public void shutdownEntry() {
        this.tracker = null;
        this.state = null;
    }
}
