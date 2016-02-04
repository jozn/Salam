package cz.msebera.android.httpclient.impl.cookie;

import cz.msebera.android.httpclient.cookie.CookieSpec;
import cz.msebera.android.httpclient.cookie.CookieSpecFactory;
import cz.msebera.android.httpclient.cookie.CookieSpecProvider;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.HttpContext;
import java.util.Collection;

@Deprecated
public final class BestMatchSpecFactory implements CookieSpecFactory, CookieSpecProvider {
    private final CookieSpec cookieSpec;

    private BestMatchSpecFactory() {
        this.cookieSpec = new BestMatchSpec(null, false);
    }

    public BestMatchSpecFactory(byte b) {
        this();
    }

    public final CookieSpec newInstance(HttpParams params) {
        if (params == null) {
            return new BestMatchSpec();
        }
        String[] patterns = null;
        Collection<?> param = (Collection) params.getParameter("http.protocol.cookie-datepatterns");
        if (param != null) {
            patterns = (String[]) param.toArray(new String[param.size()]);
        }
        return new BestMatchSpec(patterns, params.getBooleanParameter("http.protocol.single-cookie-header", false));
    }

    public final CookieSpec create(HttpContext context) {
        return this.cookieSpec;
    }
}
