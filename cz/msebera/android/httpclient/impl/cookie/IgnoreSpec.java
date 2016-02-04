package cz.msebera.android.httpclient.impl.cookie;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.cookie.CookieOrigin;
import cz.msebera.android.httpclient.cookie.MalformedCookieException;
import java.util.Collections;
import java.util.List;

public final class IgnoreSpec extends CookieSpecBase {
    public final int getVersion() {
        return 0;
    }

    public final List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
        return Collections.emptyList();
    }

    public final List<Header> formatCookies(List<Cookie> list) {
        return Collections.emptyList();
    }

    public final Header getVersionHeader() {
        return null;
    }
}
