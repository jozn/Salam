package cz.msebera.android.httpclient.impl.cookie;

import cz.msebera.android.httpclient.cookie.CookieSpec;
import cz.msebera.android.httpclient.cookie.CookieSpecFactory;
import cz.msebera.android.httpclient.cookie.CookieSpecProvider;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.HttpContext;
import java.util.Collection;

@Deprecated
public final class BrowserCompatSpecFactory implements CookieSpecFactory, CookieSpecProvider {
    private final CookieSpec cookieSpec;
    private final int securityLevel$505c3d98;

    public enum SecurityLevel {
        ;

        static {
            SECURITYLEVEL_DEFAULT$505c3d98 = 1;
            SECURITYLEVEL_IE_MEDIUM$505c3d98 = 2;
            $VALUES$38aa2a03 = new int[]{SECURITYLEVEL_DEFAULT$505c3d98, SECURITYLEVEL_IE_MEDIUM$505c3d98};
        }
    }

    private BrowserCompatSpecFactory(int securityLevel) {
        this.securityLevel$505c3d98 = securityLevel;
        this.cookieSpec = new BrowserCompatSpec(null, securityLevel);
    }

    public BrowserCompatSpecFactory() {
        this(SecurityLevel.SECURITYLEVEL_DEFAULT$505c3d98);
    }

    public final CookieSpec newInstance(HttpParams params) {
        if (params == null) {
            return new BrowserCompatSpec(null, this.securityLevel$505c3d98);
        }
        String[] patterns = null;
        Collection<?> param = (Collection) params.getParameter("http.protocol.cookie-datepatterns");
        if (param != null) {
            patterns = (String[]) param.toArray(new String[param.size()]);
        }
        return new BrowserCompatSpec(patterns, this.securityLevel$505c3d98);
    }

    public final CookieSpec create(HttpContext context) {
        return this.cookieSpec;
    }
}
