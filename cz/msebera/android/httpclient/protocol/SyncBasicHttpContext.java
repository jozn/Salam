package cz.msebera.android.httpclient.protocol;

@Deprecated
public final class SyncBasicHttpContext extends BasicHttpContext {
    public SyncBasicHttpContext(HttpContext parentContext) {
        super(parentContext);
    }

    public final synchronized Object getAttribute(String id) {
        return super.getAttribute(id);
    }

    public final synchronized void setAttribute(String id, Object obj) {
        super.setAttribute(id, obj);
    }
}
