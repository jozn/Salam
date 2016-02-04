package cz.msebera.android.httpclient.conn.scheme;

import android.support.v4.internal.view.SupportMenu;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.LangUtils;
import java.util.Locale;

@Deprecated
public final class Scheme {
    public final int defaultPort;
    public final boolean layered;
    final String name;
    public final SchemeSocketFactory socketFactory;
    private String stringRep;

    public Scheme(String name, int port, SchemeSocketFactory factory) {
        boolean z;
        Args.notNull(name, "Scheme name");
        if (port <= 0 || port > SupportMenu.USER_MASK) {
            z = false;
        } else {
            z = true;
        }
        Args.check(z, "Port is invalid");
        Args.notNull(factory, "Socket factory");
        this.name = name.toLowerCase(Locale.ENGLISH);
        this.defaultPort = port;
        if (factory instanceof SchemeLayeredSocketFactory) {
            this.layered = true;
            this.socketFactory = factory;
        } else if (factory instanceof LayeredSchemeSocketFactory) {
            this.layered = true;
            this.socketFactory = new SchemeLayeredSocketFactoryAdaptor2((LayeredSchemeSocketFactory) factory);
        } else {
            this.layered = false;
            this.socketFactory = factory;
        }
    }

    @Deprecated
    public Scheme(String name, SocketFactory factory, int port) {
        boolean z;
        Args.notNull(name, "Scheme name");
        Args.notNull(factory, "Socket factory");
        if (port <= 0 || port > SupportMenu.USER_MASK) {
            z = false;
        } else {
            z = true;
        }
        Args.check(z, "Port is invalid");
        this.name = name.toLowerCase(Locale.ENGLISH);
        if (factory instanceof LayeredSocketFactory) {
            this.socketFactory = new SchemeLayeredSocketFactoryAdaptor((LayeredSocketFactory) factory);
            this.layered = true;
        } else {
            this.socketFactory = new SchemeSocketFactoryAdaptor(factory);
            this.layered = false;
        }
        this.defaultPort = port;
    }

    public final int resolvePort(int port) {
        return port <= 0 ? this.defaultPort : port;
    }

    public final String toString() {
        if (this.stringRep == null) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(this.name);
            buffer.append(':');
            buffer.append(Integer.toString(this.defaultPort));
            this.stringRep = buffer.toString();
        }
        return this.stringRep;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Scheme)) {
            return false;
        }
        Scheme that = (Scheme) obj;
        if (this.name.equals(that.name) && this.defaultPort == that.defaultPort && this.layered == that.layered) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode(this.defaultPort + 629, this.name), this.layered);
    }
}
