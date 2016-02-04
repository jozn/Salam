package cz.msebera.android.httpclient.impl;

import cz.msebera.android.httpclient.HttpInetConnection;
import cz.msebera.android.httpclient.impl.io.HttpRequestWriter;
import cz.msebera.android.httpclient.impl.io.SocketInputBuffer;
import cz.msebera.android.httpclient.impl.io.SocketOutputBuffer;
import cz.msebera.android.httpclient.io.EofSensor;
import cz.msebera.android.httpclient.io.SessionInputBuffer;
import cz.msebera.android.httpclient.io.SessionOutputBuffer;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.Asserts;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

@Deprecated
public class SocketHttpClientConnection extends AbstractHttpClientConnection implements HttpInetConnection {
    private volatile boolean open;
    private volatile Socket socket;

    public SocketHttpClientConnection() {
        this.socket = null;
    }

    public final void assertNotOpen() {
        Asserts.check(!this.open, "Connection is already open");
    }

    public final void assertOpen() {
        Asserts.check(this.open, "Connection is not open");
    }

    public SessionInputBuffer createSessionInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        return new SocketInputBuffer(socket, buffersize, params);
    }

    public SessionOutputBuffer createSessionOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        return new SocketOutputBuffer(socket, buffersize, params);
    }

    public final void bind(Socket socket, HttpParams params) throws IOException {
        Args.notNull(socket, "Socket");
        Args.notNull(params, "HTTP parameters");
        this.socket = socket;
        int buffersize = params.getIntParameter("http.socket.buffer-size", -1);
        SessionInputBuffer createSessionInputBuffer = createSessionInputBuffer(socket, buffersize, params);
        SessionOutputBuffer createSessionOutputBuffer = createSessionOutputBuffer(socket, buffersize, params);
        this.inbuffer = (SessionInputBuffer) Args.notNull(createSessionInputBuffer, "Input session buffer");
        this.outbuffer = (SessionOutputBuffer) Args.notNull(createSessionOutputBuffer, "Output session buffer");
        if (createSessionInputBuffer instanceof EofSensor) {
            this.eofSensor = (EofSensor) createSessionInputBuffer;
        }
        this.responseParser = createResponseParser(createSessionInputBuffer, DefaultHttpResponseFactory.INSTANCE, params);
        this.requestWriter = new HttpRequestWriter(createSessionOutputBuffer);
        this.metrics = new HttpConnectionMetricsImpl(createSessionInputBuffer.getMetrics(), createSessionOutputBuffer.getMetrics());
        this.open = true;
    }

    public final boolean isOpen() {
        return this.open;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public final InetAddress getRemoteAddress() {
        if (this.socket != null) {
            return this.socket.getInetAddress();
        }
        return null;
    }

    public final int getRemotePort() {
        if (this.socket != null) {
            return this.socket.getPort();
        }
        return -1;
    }

    public final void setSocketTimeout(int timeout) {
        assertOpen();
        if (this.socket != null) {
            try {
                this.socket.setSoTimeout(timeout);
            } catch (SocketException e) {
            }
        }
    }

    public void shutdown() throws IOException {
        this.open = false;
        Socket tmpsocket = this.socket;
        if (tmpsocket != null) {
            tmpsocket.close();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws java.io.IOException {
        /*
        r2 = this;
        r1 = r2.open;
        if (r1 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r1 = 0;
        r2.open = r1;
        r0 = r2.socket;
        r2.doFlush();	 Catch:{ all -> 0x0017 }
        r0.shutdownOutput();	 Catch:{ IOException -> 0x001c, UnsupportedOperationException -> 0x0020 }
    L_0x0010:
        r0.shutdownInput();	 Catch:{ IOException -> 0x001e, UnsupportedOperationException -> 0x0020 }
    L_0x0013:
        r0.close();
        goto L_0x0004;
    L_0x0017:
        r1 = move-exception;
        r0.close();
        throw r1;
    L_0x001c:
        r1 = move-exception;
        goto L_0x0010;
    L_0x001e:
        r1 = move-exception;
        goto L_0x0013;
    L_0x0020:
        r1 = move-exception;
        goto L_0x0013;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.impl.SocketHttpClientConnection.close():void");
    }

    private static void formatAddress(StringBuilder buffer, SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress) {
            Object hostAddress;
            InetSocketAddress addr = (InetSocketAddress) socketAddress;
            if (addr.getAddress() != null) {
                hostAddress = addr.getAddress().getHostAddress();
            } else {
                hostAddress = addr.getAddress();
            }
            buffer.append(hostAddress).append(':').append(addr.getPort());
            return;
        }
        buffer.append(socketAddress);
    }

    public String toString() {
        if (this.socket == null) {
            return super.toString();
        }
        StringBuilder buffer = new StringBuilder();
        SocketAddress remoteAddress = this.socket.getRemoteSocketAddress();
        SocketAddress localAddress = this.socket.getLocalSocketAddress();
        if (!(remoteAddress == null || localAddress == null)) {
            formatAddress(buffer, localAddress);
            buffer.append("<->");
            formatAddress(buffer, remoteAddress);
        }
        return buffer.toString();
    }
}
