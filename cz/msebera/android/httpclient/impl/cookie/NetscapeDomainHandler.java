package cz.msebera.android.httpclient.impl.cookie;

import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.cookie.CookieOrigin;
import cz.msebera.android.httpclient.cookie.CookieRestrictionViolationException;
import cz.msebera.android.httpclient.cookie.MalformedCookieException;
import cz.msebera.android.httpclient.cookie.SetCookie;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.TextUtils;
import java.util.Locale;
import java.util.StringTokenizer;

public final class NetscapeDomainHandler extends BasicDomainHandler {
    public final void parse(SetCookie cookie, String value) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (TextUtils.isBlank(value)) {
            throw new MalformedCookieException("Blank or null value for domain attribute");
        }
        cookie.setDomain(value);
    }

    public final void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        String host = origin.host;
        String domain = cookie.getDomain();
        if (!host.equals(domain) && !BasicDomainHandler.domainMatch(domain, host)) {
            throw new CookieRestrictionViolationException("Illegal domain attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
        } else if (host.contains(".")) {
            int domainParts = new StringTokenizer(domain, ".").countTokens();
            String toUpperCase = domain.toUpperCase(Locale.ROOT);
            Object obj = (toUpperCase.endsWith(".COM") || toUpperCase.endsWith(".EDU") || toUpperCase.endsWith(".NET") || toUpperCase.endsWith(".GOV") || toUpperCase.endsWith(".MIL") || toUpperCase.endsWith(".ORG") || toUpperCase.endsWith(".INT")) ? 1 : null;
            if (obj != null) {
                if (domainParts < 2) {
                    throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates the Netscape cookie specification for special domains");
                }
            } else if (domainParts < 3) {
                throw new CookieRestrictionViolationException("Domain attribute \"" + domain + "\" violates the Netscape cookie specification");
            }
        }
    }

    public final boolean match(Cookie cookie, CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        String host = origin.host;
        String domain = cookie.getDomain();
        if (domain == null) {
            return false;
        }
        return host.endsWith(domain);
    }

    public final String getAttributeName() {
        return "domain";
    }
}
