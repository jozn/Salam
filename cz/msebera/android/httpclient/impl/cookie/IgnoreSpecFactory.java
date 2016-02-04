package cz.msebera.android.httpclient.impl.cookie;

import cz.msebera.android.httpclient.cookie.CookieSpec;
import cz.msebera.android.httpclient.cookie.CookieSpecFactory;
import cz.msebera.android.httpclient.cookie.CookieSpecProvider;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.HttpContext;

@Deprecated
public final class IgnoreSpecFactory implements CookieSpecFactory, CookieSpecProvider {
    public final CookieSpec newInstance(HttpParams params) {
        return new IgnoreSpec();
    }

    public final CookieSpec create(HttpContext context) {
        return new IgnoreSpec();
    }
}
