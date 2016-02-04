package cz.msebera.android.httpclient.impl.conn;

import cz.msebera.android.httpclient.conn.OperatedClientConnection;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.conn.routing.RouteTracker;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.pool.PoolEntry;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Deprecated
final class HttpPoolEntry extends PoolEntry<HttpRoute, OperatedClientConnection> {
    public HttpClientAndroidLog log;
    final RouteTracker tracker;

    public HttpPoolEntry(HttpClientAndroidLog log, String id, HttpRoute route, OperatedClientConnection conn, TimeUnit tunit) {
        super(id, route, conn, tunit);
        this.log = log;
        this.tracker = new RouteTracker(route);
    }

    public final boolean isExpired(long now) {
        boolean expired = super.isExpired(now);
        if (expired && this.log.debugEnabled) {
            this.log.debug("Connection " + this + " expired @ " + new Date(getExpiry()));
        }
        return expired;
    }

    public final boolean isClosed() {
        return !((OperatedClientConnection) this.conn).isOpen();
    }

    public final void close() {
        try {
            this.conn.close();
        } catch (IOException ex) {
            this.log.debug("I/O error closing connection", ex);
        }
    }
}
