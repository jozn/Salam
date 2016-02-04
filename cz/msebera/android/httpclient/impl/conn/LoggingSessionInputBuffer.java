package cz.msebera.android.httpclient.impl.conn;

import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.io.EofSensor;
import cz.msebera.android.httpclient.io.HttpTransportMetrics;
import cz.msebera.android.httpclient.io.SessionInputBuffer;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Deprecated
public final class LoggingSessionInputBuffer implements EofSensor, SessionInputBuffer {
    private final String charset;
    private final EofSensor eofSensor;
    private final SessionInputBuffer in;
    private final Wire wire;

    public LoggingSessionInputBuffer(SessionInputBuffer in, Wire wire, String charset) {
        this.in = in;
        this.eofSensor = in instanceof EofSensor ? (EofSensor) in : null;
        this.wire = wire;
        if (charset == null) {
            charset = Consts.ASCII.name();
        }
        this.charset = charset;
    }

    public final boolean isDataAvailable(int timeout) throws IOException {
        return this.in.isDataAvailable(timeout);
    }

    public final int read(byte[] b, int off, int len) throws IOException {
        int l = this.in.read(b, off, len);
        if (this.wire.log.debugEnabled && l > 0) {
            Wire wire = this.wire;
            Args.notNull(b, "Input");
            wire.wire("<< ", new ByteArrayInputStream(b, off, l));
        }
        return l;
    }

    public final int read() throws IOException {
        int l = this.in.read();
        if (this.wire.log.debugEnabled && l != -1) {
            this.wire.input(new byte[]{(byte) l});
        }
        return l;
    }

    public final int readLine(CharArrayBuffer buffer) throws IOException {
        int l = this.in.readLine(buffer);
        if (this.wire.log.debugEnabled && l >= 0) {
            this.wire.input((new String(buffer.buffer, buffer.length() - l, l) + "\r\n").getBytes(this.charset));
        }
        return l;
    }

    public final HttpTransportMetrics getMetrics() {
        return this.in.getMetrics();
    }

    public final boolean isEof() {
        if (this.eofSensor != null) {
            return this.eofSensor.isEof();
        }
        return false;
    }
}
