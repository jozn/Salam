package cz.msebera.android.httpclient.auth;

import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.LangUtils;
import java.util.Locale;

public final class AuthScope {
    public static final AuthScope ANY;
    public static final String ANY_HOST;
    public static final String ANY_REALM;
    public static final String ANY_SCHEME;
    public final String host;
    private final HttpHost origin;
    public final int port;
    public final String realm;
    public final String scheme;

    static {
        ANY_HOST = null;
        ANY_REALM = null;
        ANY_SCHEME = null;
        ANY = new AuthScope(ANY_HOST, -1, ANY_REALM, ANY_SCHEME);
    }

    public AuthScope(String host, int port, String realm, String schemeName) {
        this.host = host == null ? ANY_HOST : host.toLowerCase(Locale.ROOT);
        if (port < 0) {
            port = -1;
        }
        this.port = port;
        if (realm == null) {
            realm = ANY_REALM;
        }
        this.realm = realm;
        this.scheme = schemeName == null ? ANY_SCHEME : schemeName.toUpperCase(Locale.ROOT);
        this.origin = null;
    }

    public AuthScope(HttpHost origin, String realm, String schemeName) {
        Args.notNull(origin, "Host");
        this.host = origin.getHostName().toLowerCase(Locale.ROOT);
        this.port = origin.getPort() < 0 ? -1 : origin.getPort();
        if (realm == null) {
            realm = ANY_REALM;
        }
        this.realm = realm;
        this.scheme = schemeName == null ? ANY_SCHEME : schemeName.toUpperCase(Locale.ROOT);
        this.origin = origin;
    }

    public AuthScope(String host, int port) {
        this(host, port, ANY_REALM, ANY_SCHEME);
    }

    public final boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof AuthScope)) {
            return super.equals(o);
        }
        AuthScope that = (AuthScope) o;
        if (LangUtils.equals(this.host, that.host) && this.port == that.port && LangUtils.equals(this.realm, that.realm) && LangUtils.equals(this.scheme, that.scheme)) {
            return true;
        }
        return false;
    }

    public final String toString() {
        StringBuilder buffer = new StringBuilder();
        if (this.scheme != null) {
            buffer.append(this.scheme.toUpperCase(Locale.ROOT));
            buffer.append(' ');
        }
        if (this.realm != null) {
            buffer.append('\'');
            buffer.append(this.realm);
            buffer.append('\'');
        } else {
            buffer.append("<any realm>");
        }
        if (this.host != null) {
            buffer.append('@');
            buffer.append(this.host);
            if (this.port >= 0) {
                buffer.append(':');
                buffer.append(this.port);
            }
        }
        return buffer.toString();
    }

    public final int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode((LangUtils.hashCode(17, this.host) * 37) + this.port, this.realm), this.scheme);
    }
}
