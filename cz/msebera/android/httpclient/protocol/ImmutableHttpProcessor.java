package cz.msebera.android.httpclient.protocol;

import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpRequestInterceptor;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpResponseInterceptor;
import java.io.IOException;

public final class ImmutableHttpProcessor implements HttpProcessor {
    private final HttpRequestInterceptor[] requestInterceptors;
    private final HttpResponseInterceptor[] responseInterceptors;

    public ImmutableHttpProcessor(HttpRequestInterceptor[] requestInterceptors, HttpResponseInterceptor[] responseInterceptors) {
        int l = requestInterceptors.length;
        this.requestInterceptors = new HttpRequestInterceptor[l];
        System.arraycopy(requestInterceptors, 0, this.requestInterceptors, 0, l);
        l = responseInterceptors.length;
        this.responseInterceptors = new HttpResponseInterceptor[l];
        System.arraycopy(responseInterceptors, 0, this.responseInterceptors, 0, l);
    }

    public final void process(HttpRequest request, HttpContext context) throws IOException, HttpException {
        for (HttpRequestInterceptor process : this.requestInterceptors) {
            process.process(request, context);
        }
    }

    public final void process(HttpResponse response, HttpContext context) throws IOException, HttpException {
        for (HttpResponseInterceptor process : this.responseInterceptors) {
            process.process(response, context);
        }
    }
}
