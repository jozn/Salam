package cz.msebera.android.httpclient.params;

@Deprecated
public final class SyncBasicHttpParams extends BasicHttpParams {
    public final synchronized HttpParams setParameter(String name, Object value) {
        return super.setParameter(name, value);
    }

    public final synchronized Object getParameter(String name) {
        return super.getParameter(name);
    }

    public final synchronized Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
