package cz.msebera.android.httpclient.cookie;

public interface ClientCookie extends Cookie {
    boolean containsAttribute(String str);

    String getAttribute(String str);
}
