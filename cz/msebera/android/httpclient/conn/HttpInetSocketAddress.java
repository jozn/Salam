package cz.msebera.android.httpclient.conn;

import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.util.Args;
import java.net.InetAddress;
import java.net.InetSocketAddress;

@Deprecated
public final class HttpInetSocketAddress extends InetSocketAddress {
    public final HttpHost httphost;

    public HttpInetSocketAddress(HttpHost httphost, InetAddress addr, int port) {
        super(addr, port);
        Args.notNull(httphost, "HTTP host");
        this.httphost = httphost;
    }

    public final String toString() {
        return this.httphost.getHostName() + ":" + getPort();
    }
}
