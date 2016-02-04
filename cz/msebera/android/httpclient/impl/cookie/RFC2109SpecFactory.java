package cz.msebera.android.httpclient.impl.cookie;

import cz.msebera.android.httpclient.cookie.CookieSpec;
import cz.msebera.android.httpclient.cookie.CookieSpecFactory;
import cz.msebera.android.httpclient.cookie.CookieSpecProvider;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.HttpContext;
import java.util.Collection;

@Deprecated
public final class RFC2109SpecFactory implements CookieSpecFactory, CookieSpecProvider {
    private final CookieSpec cookieSpec;

    private RFC2109SpecFactory() {
        this.cookieSpec = new RFC2109Spec(null, false);
    }

    public RFC2109SpecFactory(byte b) {
        this();
    }

    public final CookieSpec newInstance(HttpParams params) {
        if (params == null) {
            return new RFC2109Spec();
        }
        String[] patterns = null;
        Collection<?> param = (Collection) params.getParameter("http.protocol.cookie-datepatterns");
        if (param != null) {
            patterns = (String[]) param.toArray(new String[param.size()]);
        }
        return new RFC2109Spec(patterns, params.getBooleanParameter("http.protocol.single-cookie-header", false));
    }

    public final CookieSpec create(HttpContext context) {
        return this.cookieSpec;
    }
}
