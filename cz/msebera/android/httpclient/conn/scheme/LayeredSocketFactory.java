package cz.msebera.android.httpclient.conn.scheme;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

@Deprecated
public interface LayeredSocketFactory extends SocketFactory {
    Socket createSocket$1a54fc0c(Socket socket, String str, int i) throws IOException, UnknownHostException;
}
