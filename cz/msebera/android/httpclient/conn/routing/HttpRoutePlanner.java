package cz.msebera.android.httpclient.conn.routing;

import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.HttpRequest;

public interface HttpRoutePlanner {
    HttpRoute determineRoute$1e70857f(HttpHost httpHost, HttpRequest httpRequest) throws HttpException;
}
