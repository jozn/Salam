package cz.msebera.android.httpclient.impl.conn.tsccm;

import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.ClientConnectionOperator;
import cz.msebera.android.httpclient.conn.ClientConnectionRequest;
import cz.msebera.android.httpclient.conn.ConnectionPoolTimeoutException;
import cz.msebera.android.httpclient.conn.ManagedClientConnection;
import cz.msebera.android.httpclient.conn.params.ConnPerRouteBean;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.impl.conn.DefaultClientConnectionOperator;
import cz.msebera.android.httpclient.impl.conn.SchemeRegistryFactory;
import cz.msebera.android.httpclient.impl.conn.tsccm.ConnPoolByRoute.C12491;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.Asserts;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Deprecated
public final class ThreadSafeClientConnManager implements ClientConnectionManager {
    protected final ClientConnectionOperator connOperator;
    protected final ConnPerRouteBean connPerRoute;
    protected final AbstractConnPool connectionPool;
    public HttpClientAndroidLog log;
    protected final ConnPoolByRoute pool;
    protected final SchemeRegistry schemeRegistry;

    /* renamed from: cz.msebera.android.httpclient.impl.conn.tsccm.ThreadSafeClientConnManager.1 */
    class C12501 implements ClientConnectionRequest {
        final /* synthetic */ PoolEntryRequest val$poolRequest;
        final /* synthetic */ HttpRoute val$route;

        C12501(PoolEntryRequest poolEntryRequest, HttpRoute httpRoute) {
            this.val$poolRequest = poolEntryRequest;
            this.val$route = httpRoute;
        }

        public final ManagedClientConnection getConnection(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
            Args.notNull(this.val$route, "Route");
            if (ThreadSafeClientConnManager.this.log.debugEnabled) {
                ThreadSafeClientConnManager.this.log.debug("Get connection: " + this.val$route + ", timeout = " + timeout);
            }
            return new BasicPooledConnAdapter(ThreadSafeClientConnManager.this, this.val$poolRequest.getPoolEntry(timeout, tunit));
        }
    }

    private ThreadSafeClientConnManager(SchemeRegistry schreg) {
        this(schreg, TimeUnit.MILLISECONDS);
    }

    public ThreadSafeClientConnManager() {
        this(SchemeRegistryFactory.createDefault());
    }

    private ThreadSafeClientConnManager(SchemeRegistry schreg, TimeUnit connTTLTimeUnit) {
        this(schreg, connTTLTimeUnit, new ConnPerRouteBean());
    }

    private ThreadSafeClientConnManager(SchemeRegistry schreg, TimeUnit connTTLTimeUnit, ConnPerRouteBean connPerRoute) {
        Args.notNull(schreg, "Scheme registry");
        this.log = new HttpClientAndroidLog(getClass());
        this.schemeRegistry = schreg;
        this.connPerRoute = connPerRoute;
        this.connOperator = createConnectionOperator(schreg);
        this.pool = new ConnPoolByRoute(this.connOperator, this.connPerRoute, 20, connTTLTimeUnit);
        this.connectionPool = this.pool;
    }

    @Deprecated
    public ThreadSafeClientConnManager(HttpParams params, SchemeRegistry schreg) {
        Args.notNull(schreg, "Scheme registry");
        this.log = new HttpClientAndroidLog(getClass());
        this.schemeRegistry = schreg;
        this.connPerRoute = new ConnPerRouteBean();
        this.connOperator = createConnectionOperator(schreg);
        this.pool = new ConnPoolByRoute(this.connOperator, params);
        this.connectionPool = this.pool;
    }

    protected final void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    private static ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
        return new DefaultClientConnectionOperator(schreg);
    }

    public final SchemeRegistry getSchemeRegistry() {
        return this.schemeRegistry;
    }

    public final ClientConnectionRequest requestConnection(HttpRoute route, Object state) {
        return new C12501(new C12491(new WaitingThreadAborter(), route, state), route);
    }

    public final void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
        Args.check(conn instanceof BasicPooledConnAdapter, "Connection class mismatch, connection not obtained from this manager");
        BasicPooledConnAdapter hca = (BasicPooledConnAdapter) conn;
        if (hca.getPoolEntry() != null) {
            Asserts.check(hca.getManager() == this, "Connection not obtained from this manager");
        }
        synchronized (hca) {
            BasicPoolEntry entry = (BasicPoolEntry) hca.getPoolEntry();
            if (entry == null) {
                return;
            }
            boolean reusable;
            try {
                if (hca.isOpen() && !hca.markedReusable) {
                    hca.shutdown();
                }
                reusable = hca.markedReusable;
                if (this.log.debugEnabled) {
                    if (reusable) {
                        this.log.debug("Released connection is reusable.");
                    } else {
                        this.log.debug("Released connection is not reusable.");
                    }
                }
                hca.detach();
                this.pool.freeEntry(entry, reusable, validDuration, timeUnit);
            } catch (IOException iox) {
                if (this.log.debugEnabled) {
                    this.log.debug("Exception shutting down released connection.", iox);
                }
                reusable = hca.markedReusable;
                if (this.log.debugEnabled) {
                    if (reusable) {
                        this.log.debug("Released connection is reusable.");
                    } else {
                        this.log.debug("Released connection is not reusable.");
                    }
                }
                hca.detach();
                this.pool.freeEntry(entry, reusable, validDuration, timeUnit);
            } catch (Throwable th) {
                Throwable th2 = th;
                reusable = hca.markedReusable;
                if (this.log.debugEnabled) {
                    if (reusable) {
                        this.log.debug("Released connection is reusable.");
                    } else {
                        this.log.debug("Released connection is not reusable.");
                    }
                }
                hca.detach();
                this.pool.freeEntry(entry, reusable, validDuration, timeUnit);
            }
        }
    }

    public final void shutdown() {
        this.log.debug("Shutting down");
        this.pool.shutdown();
    }
}
