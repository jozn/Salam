package cz.msebera.android.httpclient.conn.routing;

public interface HttpRouteDirector {
    int nextStep(RouteInfo routeInfo, RouteInfo routeInfo2);
}
