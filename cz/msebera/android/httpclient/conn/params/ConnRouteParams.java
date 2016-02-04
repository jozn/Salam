package cz.msebera.android.httpclient.conn.params;

import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;
import java.net.InetAddress;

@Deprecated
public final class ConnRouteParams {
    public static final HttpHost NO_HOST;
    public static final HttpRoute NO_ROUTE;

    static {
        NO_HOST = new HttpHost("127.0.0.255", 0, "no-host");
        NO_ROUTE = new HttpRoute(NO_HOST);
    }

    public static HttpHost getDefaultProxy(HttpParams params) {
        Args.notNull(params, "Parameters");
        HttpHost proxy = (HttpHost) params.getParameter("http.route.default-proxy");
        if (proxy == null || !NO_HOST.equals(proxy)) {
            return proxy;
        }
        return null;
    }

    public static HttpRoute getForcedRoute(HttpParams params) {
        Args.notNull(params, "Parameters");
        HttpRoute route = (HttpRoute) params.getParameter("http.route.forced-route");
        if (route == null || !NO_ROUTE.equals(route)) {
            return route;
        }
        return null;
    }

    public static InetAddress getLocalAddress(HttpParams params) {
        Args.notNull(params, "Parameters");
        return (InetAddress) params.getParameter("http.route.local-address");
    }
}
