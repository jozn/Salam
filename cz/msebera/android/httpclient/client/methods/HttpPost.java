package cz.msebera.android.httpclient.client.methods;

import java.net.URI;

public final class HttpPost extends HttpEntityEnclosingRequestBase {
    public HttpPost(URI uri) {
        this.uri = uri;
    }

    public HttpPost(String uri) {
        this.uri = URI.create(uri);
    }

    public final String getMethod() {
        return "POST";
    }
}
