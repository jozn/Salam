package cz.msebera.android.httpclient.impl.conn;

import cz.msebera.android.httpclient.HttpClientConnection;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.ClientConnectionOperator;
import cz.msebera.android.httpclient.conn.ClientConnectionRequest;
import cz.msebera.android.httpclient.conn.ManagedClientConnection;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.Asserts;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Deprecated
public final class BasicClientConnectionManager implements ClientConnectionManager {
    private static final AtomicLong COUNTER;
    private ManagedClientConnectionImpl conn;
    private final ClientConnectionOperator connOperator;
    public HttpClientAndroidLog log;
    private HttpPoolEntry poolEntry;
    private final SchemeRegistry schemeRegistry;
    private volatile boolean shutdown;

    /* renamed from: cz.msebera.android.httpclient.impl.conn.BasicClientConnectionManager.1 */
    class C12481 implements ClientConnectionRequest {
        final /* synthetic */ HttpRoute val$route;
        final /* synthetic */ Object val$state;

        C12481(HttpRoute httpRoute, Object obj) {
            this.val$route = httpRoute;
            this.val$state = obj;
        }

        public final ManagedClientConnection getConnection(long timeout, TimeUnit tunit) {
            return BasicClientConnectionManager.this.getConnection$6f5a3e0b(this.val$route);
        }
    }

    static {
        COUNTER = new AtomicLong();
    }

    public BasicClientConnectionManager(SchemeRegistry schreg) {
        this.log = new HttpClientAndroidLog(getClass());
        Args.notNull(schreg, "Scheme registry");
        this.schemeRegistry = schreg;
        this.connOperator = new DefaultClientConnectionOperator(schreg);
    }

    public BasicClientConnectionManager() {
        this(SchemeRegistryFactory.createDefault());
    }

    protected final void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    public final SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    public final ClientConnectionRequest requestConnection(HttpRoute route, Object state) {
        return new C12481(route, state);
    }

    final ManagedClientConnection getConnection$6f5a3e0b(HttpRoute route) {
        ManagedClientConnection managedClientConnection;
        boolean z = true;
        Args.notNull(route, "Route");
        synchronized (this) {
            boolean z2;
            if (this.shutdown) {
                z2 = false;
            } else {
                z2 = true;
            }
            Asserts.check(z2, "Connection manager has been shut down");
            if (this.log.debugEnabled) {
                this.log.debug("Get connection for route " + route);
            }
            if (this.conn != null) {
                z = false;
            }
            Asserts.check(z, "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
            if (!(this.poolEntry == null || ((HttpRoute) this.poolEntry.route).equals(route))) {
                this.poolEntry.close();
                this.poolEntry = null;
            }
            if (this.poolEntry == null) {
                HttpRoute httpRoute = route;
                this.poolEntry = new HttpPoolEntry(this.log, Long.toString(COUNTER.getAndIncrement()), httpRoute, this.connOperator.createConnection(), TimeUnit.MILLISECONDS);
            }
            if (this.poolEntry.isExpired(System.currentTimeMillis())) {
                this.poolEntry.close();
                this.poolEntry.tracker.reset();
            }
            this.conn = new ManagedClientConnectionImpl(this, this.connOperator, this.poolEntry);
            managedClientConnection = this.conn;
        }
        return managedClientConnection;
    }

    private void shutdownConnection(HttpClientConnection conn) {
        try {
            conn.shutdown();
        } catch (IOException iox) {
            if (this.log.debugEnabled) {
                this.log.debug("I/O exception shutting down connection", iox);
            }
        }
    }

    public final void releaseConnection(ManagedClientConnection conn, long keepalive, TimeUnit tunit) {
        Args.check(conn instanceof ManagedClientConnectionImpl, "Connection class mismatch, connection not obtained from this manager");
        ManagedClientConnectionImpl managedConn = (ManagedClientConnectionImpl) conn;
        synchronized (managedConn) {
            if (this.log.debugEnabled) {
                this.log.debug("Releasing connection " + conn);
            }
            if (managedConn.poolEntry == null) {
                return;
            }
            Asserts.check(managedConn.manager == this, "Connection not obtained from this manager");
            synchronized (this) {
                if (this.shutdown) {
                    shutdownConnection(managedConn);
                    return;
                }
                try {
                    if (managedConn.isOpen() && !managedConn.reusable) {
                        shutdownConnection(managedConn);
                    }
                    if (managedConn.reusable) {
                        this.poolEntry.updateExpiry(keepalive, tunit != null ? tunit : TimeUnit.MILLISECONDS);
                        if (this.log.debugEnabled) {
                            String s;
                            if (keepalive > 0) {
                                s = "for " + keepalive + " " + tunit;
                            } else {
                                s = "indefinitely";
                            }
                            this.log.debug("Connection can be kept alive " + s);
                        }
                    }
                    managedConn.detach();
                    this.conn = null;
                    if (this.poolEntry.isClosed()) {
                        this.poolEntry = null;
                    }
                    return;
                } catch (Throwable th) {
                    managedConn.detach();
                    this.conn = null;
                    if (this.poolEntry.isClosed()) {
                        this.poolEntry = null;
                    }
                }
            }
        }
    }

    public final void shutdown() {
        synchronized (this) {
            this.shutdown = true;
            try {
                if (this.poolEntry != null) {
                    this.poolEntry.close();
                }
                this.poolEntry = null;
                this.conn = null;
            } catch (Throwable th) {
                this.poolEntry = null;
                this.conn = null;
            }
        }
    }
}
