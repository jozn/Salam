package cz.msebera.android.httpclient.client.methods;

import java.net.URI;

public final class HttpGet extends HttpRequestBase {
    public HttpGet(URI uri) {
        this.uri = uri;
    }

    public HttpGet(String uri) {
        this.uri = URI.create(uri);
    }

    public final String getMethod() {
        return "GET";
    }
}
