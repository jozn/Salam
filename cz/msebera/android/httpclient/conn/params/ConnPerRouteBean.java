package cz.msebera.android.httpclient.conn.params;

import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.util.Args;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public final class ConnPerRouteBean implements ConnPerRoute {
    private volatile int defaultMax;
    private final ConcurrentHashMap<HttpRoute, Integer> maxPerHostMap;

    public ConnPerRouteBean(int defaultMax) {
        this.maxPerHostMap = new ConcurrentHashMap();
        Args.positive(defaultMax, "Default max per route");
        this.defaultMax = defaultMax;
    }

    public ConnPerRouteBean() {
        this(2);
    }

    public final int getMaxForRoute(HttpRoute route) {
        Args.notNull(route, "HTTP route");
        Integer max = (Integer) this.maxPerHostMap.get(route);
        if (max != null) {
            return max.intValue();
        }
        return this.defaultMax;
    }

    public final String toString() {
        return this.maxPerHostMap.toString();
    }
}
