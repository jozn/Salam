package cz.msebera.android.httpclient.conn.params;

import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;

@Deprecated
public final class ConnManagerParams {
    private static final ConnPerRoute DEFAULT_CONN_PER_ROUTE;

    /* renamed from: cz.msebera.android.httpclient.conn.params.ConnManagerParams.1 */
    static class C12451 implements ConnPerRoute {
        C12451() {
        }

        public final int getMaxForRoute(HttpRoute route) {
            return 2;
        }
    }

    @Deprecated
    public static void setTimeout(HttpParams params, long timeout) {
        Args.notNull(params, "HTTP parameters");
        params.setLongParameter("http.conn-manager.timeout", timeout);
    }

    static {
        DEFAULT_CONN_PER_ROUTE = new C12451();
    }

    public static void setMaxConnectionsPerRoute(HttpParams params, ConnPerRoute connPerRoute) {
        Args.notNull(params, "HTTP parameters");
        params.setParameter("http.conn-manager.max-per-route", connPerRoute);
    }

    public static ConnPerRoute getMaxConnectionsPerRoute(HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        ConnPerRoute connPerRoute = (ConnPerRoute) params.getParameter("http.conn-manager.max-per-route");
        if (connPerRoute == null) {
            return DEFAULT_CONN_PER_ROUTE;
        }
        return connPerRoute;
    }

    public static void setMaxTotalConnections$465ac0a8(HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        params.setIntParameter("http.conn-manager.max-total", 10);
    }

    public static int getMaxTotalConnections(HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getIntParameter("http.conn-manager.max-total", 20);
    }
}
