package cz.msebera.android.httpclient.impl.client;

import cz.msebera.android.httpclient.conn.routing.HttpRoute;

@Deprecated
public final class RoutedRequest {
    protected final RequestWrapper request;
    protected final HttpRoute route;

    public RoutedRequest(RequestWrapper req, HttpRoute route) {
        this.request = req;
        this.route = route;
    }

    public final RequestWrapper getRequest() {
        return this.request;
    }

    public final HttpRoute getRoute() {
        return this.route;
    }
}
