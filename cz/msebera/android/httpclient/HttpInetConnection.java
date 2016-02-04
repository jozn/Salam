package cz.msebera.android.httpclient;

import java.net.InetAddress;

public interface HttpInetConnection extends HttpConnection {
    InetAddress getRemoteAddress();

    int getRemotePort();
}
