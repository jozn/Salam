package cz.msebera.android.httpclient.protocol;

public interface HttpContext {
    Object getAttribute(String str);

    void setAttribute(String str, Object obj);
}
