package cz.msebera.android.httpclient.impl.cookie;

import cz.msebera.android.httpclient.FormattedHeader;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.cookie.CommonCookieAttributeHandler;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.cookie.CookieAttributeHandler;
import cz.msebera.android.httpclient.cookie.CookieOrigin;
import cz.msebera.android.httpclient.cookie.MalformedCookieException;
import cz.msebera.android.httpclient.impl.cookie.BrowserCompatSpecFactory.SecurityLevel;
import cz.msebera.android.httpclient.message.BasicHeaderElement;
import cz.msebera.android.httpclient.message.BasicHeaderValueFormatter;
import cz.msebera.android.httpclient.message.BufferedHeader;
import cz.msebera.android.httpclient.message.ParserCursor;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public final class BrowserCompatSpec extends CookieSpecBase {
    private static final String[] DEFAULT_DATE_PATTERNS;

    /* renamed from: cz.msebera.android.httpclient.impl.cookie.BrowserCompatSpec.1 */
    class C12511 extends BasicPathHandler {
        C12511() {
        }

        public final void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        }
    }

    static {
        DEFAULT_DATE_PATTERNS = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z"};
    }

    public BrowserCompatSpec(String[] datepatterns, int securityLevel) {
        String[] strArr;
        CommonCookieAttributeHandler[] commonCookieAttributeHandlerArr = new CommonCookieAttributeHandler[7];
        commonCookieAttributeHandlerArr[0] = new BrowserCompatVersionAttributeHandler();
        commonCookieAttributeHandlerArr[1] = new BasicDomainHandler();
        commonCookieAttributeHandlerArr[2] = securityLevel == SecurityLevel.SECURITYLEVEL_IE_MEDIUM$505c3d98 ? new C12511() : new BasicPathHandler();
        commonCookieAttributeHandlerArr[3] = new BasicMaxAgeHandler();
        commonCookieAttributeHandlerArr[4] = new BasicSecureHandler();
        commonCookieAttributeHandlerArr[5] = new BasicCommentHandler();
        if (datepatterns != null) {
            strArr = (String[]) datepatterns.clone();
        } else {
            strArr = DEFAULT_DATE_PATTERNS;
        }
        commonCookieAttributeHandlerArr[6] = new BasicExpiresHandler(strArr);
        super(commonCookieAttributeHandlerArr);
    }

    public BrowserCompatSpec() {
        this(null, SecurityLevel.SECURITYLEVEL_DEFAULT$505c3d98);
    }

    public final List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
        Args.notNull(header, "Header");
        Args.notNull(origin, "Cookie origin");
        if (header.getName().equalsIgnoreCase("Set-Cookie")) {
            HeaderElement[] helems = header.getElements();
            boolean versioned = false;
            boolean netscape = false;
            for (HeaderElement helem : helems) {
                if (helem.getParameterByName("version") != null) {
                    versioned = true;
                }
                if (helem.getParameterByName("expires") != null) {
                    netscape = true;
                }
            }
            if (!netscape && versioned) {
                return parse(helems, origin);
            }
            CharArrayBuffer buffer;
            ParserCursor cursor;
            String s;
            NetscapeDraftHeaderParser netscapeDraftHeaderParser = NetscapeDraftHeaderParser.DEFAULT;
            if (header instanceof FormattedHeader) {
                buffer = ((FormattedHeader) header).getBuffer();
                cursor = new ParserCursor(((FormattedHeader) header).getValuePos(), buffer.length());
            } else {
                s = header.getValue();
                if (s == null) {
                    throw new MalformedCookieException("Header value is null");
                }
                buffer = new CharArrayBuffer(s.length());
                buffer.append(s);
                cursor = new ParserCursor(0, buffer.length());
            }
            HeaderElement elem = NetscapeDraftHeaderParser.parseHeader(buffer, cursor);
            String name = elem.getName();
            String value = elem.getValue();
            if (name == null || name.isEmpty()) {
                throw new MalformedCookieException("Cookie name may not be empty");
            }
            BasicClientCookie cookie = new BasicClientCookie(name, value);
            cookie.cookiePath = CookieSpecBase.getDefaultPath(origin);
            cookie.setDomain(origin.host);
            NameValuePair[] attribs = elem.getParameters();
            for (int j = attribs.length - 1; j >= 0; j--) {
                NameValuePair attrib = attribs[j];
                s = attrib.getName().toLowerCase(Locale.ROOT);
                cookie.setAttribute(s, attrib.getValue());
                CookieAttributeHandler handler = findAttribHandler(s);
                if (handler != null) {
                    handler.parse(cookie, attrib.getValue());
                }
            }
            if (netscape) {
                cookie.cookieVersion = 0;
            }
            return Collections.singletonList(cookie);
        }
        throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
    }

    public final List<Header> formatCookies(List<Cookie> cookies) {
        Args.notEmpty(cookies, "List of cookies");
        CharArrayBuffer buffer = new CharArrayBuffer(cookies.size() * 20);
        buffer.append("Cookie");
        buffer.append(": ");
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = (Cookie) cookies.get(i);
            if (i > 0) {
                buffer.append("; ");
            }
            String cookieName = cookie.getName();
            String cookieValue = cookie.getValue();
            if (cookie.getVersion() > 0) {
                int i2;
                if (cookieValue != null && cookieValue.startsWith("\"") && cookieValue.endsWith("\"")) {
                    i2 = 1;
                } else {
                    boolean z = false;
                }
                if (i2 == 0) {
                    int i3;
                    BasicHeaderValueFormatter basicHeaderValueFormatter = BasicHeaderValueFormatter.INSTANCE;
                    HeaderElement basicHeaderElement = new BasicHeaderElement(cookieName, cookieValue);
                    Args.notNull(basicHeaderElement, "Header element");
                    i2 = basicHeaderElement.getName().length();
                    String value = basicHeaderElement.getValue();
                    if (value != null) {
                        i2 += value.length() + 3;
                    }
                    int parameterCount = basicHeaderElement.getParameterCount();
                    if (parameterCount > 0) {
                        i3 = 0;
                        while (i3 < parameterCount) {
                            int estimateNameValuePairLen = (BasicHeaderValueFormatter.estimateNameValuePairLen(basicHeaderElement.getParameter(i3)) + 2) + i2;
                            i3++;
                            i2 = estimateNameValuePairLen;
                        }
                    }
                    buffer.ensureCapacity(i2);
                    buffer.append(basicHeaderElement.getName());
                    String value2 = basicHeaderElement.getValue();
                    if (value2 != null) {
                        buffer.append('=');
                        BasicHeaderValueFormatter.doFormatValue(buffer, value2, false);
                    }
                    i3 = basicHeaderElement.getParameterCount();
                    if (i3 > 0) {
                        for (i2 = 0; i2 < i3; i2++) {
                            buffer.append("; ");
                            BasicHeaderValueFormatter.formatNameValuePair(buffer, basicHeaderElement.getParameter(i2), false);
                        }
                    }
                }
            }
            buffer.append(cookieName);
            buffer.append("=");
            if (cookieValue != null) {
                buffer.append(cookieValue);
            }
        }
        List<Header> headers = new ArrayList(1);
        headers.add(new BufferedHeader(buffer));
        return headers;
    }

    public final int getVersion() {
        return 0;
    }

    public final Header getVersionHeader() {
        return null;
    }

    public final String toString() {
        return "compatibility";
    }
}
