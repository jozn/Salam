package cz.msebera.android.httpclient.client.protocol;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderIterator;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.HttpResponseInterceptor;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.cookie.CookieOrigin;
import cz.msebera.android.httpclient.cookie.CookieSpec;
import cz.msebera.android.httpclient.cookie.MalformedCookieException;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;

public final class ResponseProcessCookies implements HttpResponseInterceptor {
    public HttpClientAndroidLog log;

    public ResponseProcessCookies() {
        this.log = new HttpClientAndroidLog(getClass());
    }

    public final void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        Args.notNull(response, "HTTP request");
        Args.notNull(context, "HTTP context");
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        CookieSpec cookieSpec = (CookieSpec) clientContext.getAttribute("http.cookie-spec", CookieSpec.class);
        if (cookieSpec == null) {
            this.log.debug("Cookie spec not specified in HTTP context");
            return;
        }
        CookieStore cookieStore = clientContext.getCookieStore();
        if (cookieStore == null) {
            this.log.debug("Cookie store not specified in HTTP context");
            return;
        }
        CookieOrigin cookieOrigin = (CookieOrigin) clientContext.getAttribute("http.cookie-origin", CookieOrigin.class);
        if (cookieOrigin == null) {
            this.log.debug("Cookie origin not specified in HTTP context");
            return;
        }
        processCookies(response.headerIterator("Set-Cookie"), cookieSpec, cookieOrigin, cookieStore);
        if (cookieSpec.getVersion() > 0) {
            processCookies(response.headerIterator("Set-Cookie2"), cookieSpec, cookieOrigin, cookieStore);
        }
    }

    private void processCookies(HeaderIterator iterator, CookieSpec cookieSpec, CookieOrigin cookieOrigin, CookieStore cookieStore) {
        while (iterator.hasNext()) {
            Header header = iterator.nextHeader();
            try {
                for (Cookie cookie : cookieSpec.parse(header, cookieOrigin)) {
                    try {
                        cookieSpec.validate(cookie, cookieOrigin);
                        cookieStore.addCookie(cookie);
                        if (this.log.debugEnabled) {
                            this.log.debug("Cookie accepted [" + formatCooke(cookie) + "]");
                        }
                    } catch (MalformedCookieException ex) {
                        if (this.log.warnEnabled) {
                            this.log.warn("Cookie rejected [" + formatCooke(cookie) + "] " + ex.getMessage());
                        }
                    }
                }
            } catch (MalformedCookieException ex2) {
                if (this.log.warnEnabled) {
                    this.log.warn("Invalid cookie header: \"" + header + "\". " + ex2.getMessage());
                }
            }
        }
    }

    private static String formatCooke(Cookie cookie) {
        StringBuilder buf = new StringBuilder();
        buf.append(cookie.getName());
        buf.append("=\"");
        String v = cookie.getValue();
        if (v != null) {
            if (v.length() > 100) {
                v = v.substring(0, 100) + "...";
            }
            buf.append(v);
        }
        buf.append("\"");
        buf.append(", version:");
        buf.append(Integer.toString(cookie.getVersion()));
        buf.append(", domain:");
        buf.append(cookie.getDomain());
        buf.append(", path:");
        buf.append(cookie.getPath());
        buf.append(", expiry:");
        buf.append(cookie.getExpiryDate());
        return buf.toString();
    }
}
