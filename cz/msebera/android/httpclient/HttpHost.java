package cz.msebera.android.httpclient;

import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.LangUtils;
import cz.msebera.android.httpclient.util.TextUtils;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Locale;

public final class HttpHost implements Serializable, Cloneable {
    protected final InetAddress address;
    protected final String hostname;
    protected final String lcHostname;
    protected final int port;
    protected final String schemeName;

    public HttpHost(String hostname, int port, String scheme) {
        String str = "Host name";
        if (hostname == null) {
            throw new IllegalArgumentException(str + " may not be null");
        } else if (TextUtils.containsBlanks(hostname)) {
            throw new IllegalArgumentException(str + " may not contain blanks");
        } else {
            this.hostname = hostname;
            this.lcHostname = hostname.toLowerCase(Locale.ROOT);
            if (scheme != null) {
                this.schemeName = scheme.toLowerCase(Locale.ROOT);
            } else {
                this.schemeName = "http";
            }
            this.port = port;
            this.address = null;
        }
    }

    public HttpHost(String hostname, int port) {
        this(hostname, port, null);
    }

    public HttpHost(InetAddress address, int port, String scheme) {
        this((InetAddress) Args.notNull(address, "Inet address"), address.getHostName(), port, scheme);
    }

    private HttpHost(InetAddress address, String hostname, int port, String scheme) {
        this.address = (InetAddress) Args.notNull(address, "Inet address");
        this.hostname = (String) Args.notNull(hostname, "Hostname");
        this.lcHostname = this.hostname.toLowerCase(Locale.ROOT);
        if (scheme != null) {
            this.schemeName = scheme.toLowerCase(Locale.ROOT);
        } else {
            this.schemeName = "http";
        }
        this.port = port;
    }

    public final String getHostName() {
        return this.hostname;
    }

    public final int getPort() {
        return this.port;
    }

    public final String getSchemeName() {
        return this.schemeName;
    }

    public final InetAddress getAddress() {
        return this.address;
    }

    public final String toHostString() {
        if (this.port == -1) {
            return this.hostname;
        }
        StringBuilder buffer = new StringBuilder(this.hostname.length() + 6);
        buffer.append(this.hostname);
        buffer.append(":");
        buffer.append(Integer.toString(this.port));
        return buffer.toString();
    }

    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.schemeName);
        stringBuilder.append("://");
        stringBuilder.append(this.hostname);
        if (this.port != -1) {
            stringBuilder.append(':');
            stringBuilder.append(Integer.toString(this.port));
        }
        return stringBuilder.toString();
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HttpHost)) {
            return false;
        }
        HttpHost that = (HttpHost) obj;
        if (this.lcHostname.equals(that.lcHostname) && this.port == that.port && this.schemeName.equals(that.schemeName)) {
            if (this.address == null) {
                if (that.address == null) {
                    return true;
                }
            } else if (this.address.equals(that.address)) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        int hash = LangUtils.hashCode((LangUtils.hashCode(17, this.lcHostname) * 37) + this.port, this.schemeName);
        if (this.address != null) {
            return LangUtils.hashCode(hash, this.address);
        }
        return hash;
    }

    public final Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
