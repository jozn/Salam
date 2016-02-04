package cz.msebera.android.httpclient.params;

@Deprecated
public abstract class AbstractHttpParams implements HttpParams {
    public final long getLongParameter$505cfb67(String name) {
        Object param = getParameter(name);
        if (param == null) {
            return 0;
        }
        return ((Long) param).longValue();
    }

    public final HttpParams setLongParameter(String name, long value) {
        setParameter(name, Long.valueOf(value));
        return this;
    }

    public final int getIntParameter(String name, int defaultValue) {
        Object param = getParameter(name);
        return param == null ? defaultValue : ((Integer) param).intValue();
    }

    public final HttpParams setIntParameter(String name, int value) {
        setParameter(name, Integer.valueOf(value));
        return this;
    }

    public final boolean getBooleanParameter(String name, boolean defaultValue) {
        Object param = getParameter(name);
        return param == null ? defaultValue : ((Boolean) param).booleanValue();
    }

    public final HttpParams setBooleanParameter$59a1668b(String name) {
        setParameter(name, Boolean.TRUE);
        return this;
    }
}
