package cz.msebera.android.httpclient.impl.cookie;

import cz.msebera.android.httpclient.cookie.CommonCookieAttributeHandler;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.cookie.CookieOrigin;
import cz.msebera.android.httpclient.cookie.MalformedCookieException;
import cz.msebera.android.httpclient.cookie.SetCookie;
import cz.msebera.android.httpclient.cookie.SetCookie2;

public final class RFC2965DiscardAttributeHandler implements CommonCookieAttributeHandler {
    public final void parse(SetCookie cookie, String commenturl) throws MalformedCookieException {
        if (cookie instanceof SetCookie2) {
            ((SetCookie2) cookie).setDiscard$1385ff();
        }
    }

    public final void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
    }

    public final boolean match(Cookie cookie, CookieOrigin origin) {
        return true;
    }

    public final String getAttributeName() {
        return "discard";
    }
}
