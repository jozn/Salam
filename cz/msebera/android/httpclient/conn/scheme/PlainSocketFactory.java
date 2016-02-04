package cz.msebera.android.httpclient.conn.scheme;

import cz.msebera.android.httpclient.conn.ConnectTimeoutException;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

@Deprecated
public final class PlainSocketFactory implements SchemeSocketFactory, SocketFactory {
    private final HostNameResolver nameResolver;

    public PlainSocketFactory() {
        this.nameResolver = null;
    }

    public final Socket createSocket(HttpParams params) {
        return new Socket();
    }

    public final Socket createSocket() {
        return new Socket();
    }

    public final Socket connectSocket(Socket socket, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException, ConnectTimeoutException {
        Args.notNull(remoteAddress, "Remote address");
        Args.notNull(params, "HTTP parameters");
        Socket sock = socket;
        if (socket == null) {
            sock = new Socket();
        }
        if (localAddress != null) {
            Args.notNull(params, "HTTP parameters");
            sock.setReuseAddress(params.getBooleanParameter("http.socket.reuseaddr", false));
            sock.bind(localAddress);
        }
        int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
        try {
            sock.setSoTimeout(HttpConnectionParams.getSoTimeout(params));
            sock.connect(remoteAddress, connTimeout);
            return sock;
        } catch (SocketTimeoutException e) {
            throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
        }
    }

    public final boolean isSecure(Socket sock) {
        return false;
    }

    @Deprecated
    public final Socket connectSocket(Socket socket, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        InetAddress remoteAddress;
        InetSocketAddress local = null;
        if (localAddress != null || localPort > 0) {
            if (localPort <= 0) {
                localPort = 0;
            }
            local = new InetSocketAddress(localAddress, localPort);
        }
        if (this.nameResolver != null) {
            remoteAddress = this.nameResolver.resolve$585d1bbb();
        } else {
            remoteAddress = InetAddress.getByName(host);
        }
        return connectSocket(socket, new InetSocketAddress(remoteAddress, port), local, params);
    }
}
