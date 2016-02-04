package cz.msebera.android.httpclient.client.methods;

import java.net.URI;

public final class HttpHead extends HttpRequestBase {
    public HttpHead(URI uri) {
        this.uri = uri;
    }

    public final String getMethod() {
        return "HEAD";
    }
}
