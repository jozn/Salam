package cz.msebera.android.httpclient.protocol;

import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpRequestInterceptor;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpResponseInterceptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public final class BasicHttpProcessor implements HttpProcessor, Cloneable {
    protected final List<HttpRequestInterceptor> requestInterceptors;
    protected final List<HttpResponseInterceptor> responseInterceptors;

    public BasicHttpProcessor() {
        this.requestInterceptors = new ArrayList();
        this.responseInterceptors = new ArrayList();
    }

    public final void addRequestInterceptor$62d44063(HttpRequestInterceptor itcp) {
        if (itcp != null) {
            this.requestInterceptors.add(0, itcp);
        }
    }

    public final void addInterceptor(HttpRequestInterceptor interceptor) {
        if (interceptor != null) {
            this.requestInterceptors.add(interceptor);
        }
    }

    public final int getRequestInterceptorCount() {
        return this.requestInterceptors.size();
    }

    public final HttpRequestInterceptor getRequestInterceptor(int index) {
        if (index < 0 || index >= this.requestInterceptors.size()) {
            return null;
        }
        return (HttpRequestInterceptor) this.requestInterceptors.get(index);
    }

    public final void addInterceptor(HttpResponseInterceptor interceptor) {
        if (interceptor != null) {
            this.responseInterceptors.add(interceptor);
        }
    }

    public final int getResponseInterceptorCount() {
        return this.responseInterceptors.size();
    }

    public final HttpResponseInterceptor getResponseInterceptor(int index) {
        if (index < 0 || index >= this.responseInterceptors.size()) {
            return null;
        }
        return (HttpResponseInterceptor) this.responseInterceptors.get(index);
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

    public final Object clone() throws CloneNotSupportedException {
        BasicHttpProcessor clone = (BasicHttpProcessor) super.clone();
        clone.requestInterceptors.clear();
        clone.requestInterceptors.addAll(this.requestInterceptors);
        clone.responseInterceptors.clear();
        clone.responseInterceptors.addAll(this.responseInterceptors);
        return clone;
    }
}
