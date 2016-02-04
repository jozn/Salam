package cz.msebera.android.httpclient.protocol;

import cz.msebera.android.httpclient.util.Args;

@Deprecated
public final class DefaultedHttpContext implements HttpContext {
    private final HttpContext defaults;
    private final HttpContext local;

    public DefaultedHttpContext(HttpContext local, HttpContext defaults) {
        this.local = (HttpContext) Args.notNull(local, "HTTP context");
        this.defaults = defaults;
    }

    public final Object getAttribute(String id) {
        Object obj = this.local.getAttribute(id);
        if (obj == null) {
            return this.defaults.getAttribute(id);
        }
        return obj;
    }

    public final void setAttribute(String id, Object obj) {
        this.local.setAttribute(id, obj);
    }

    public final String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[local: ").append(this.local);
        buf.append("defaults: ").append(this.defaults);
        buf.append("]");
        return buf.toString();
    }
}
