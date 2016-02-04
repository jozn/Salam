package cz.msebera.android.httpclient.impl.conn;

import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.io.HttpTransportMetrics;
import cz.msebera.android.httpclient.io.SessionOutputBuffer;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Deprecated
public final class LoggingSessionOutputBuffer implements SessionOutputBuffer {
    private final String charset;
    private final SessionOutputBuffer out;
    private final Wire wire;

    public LoggingSessionOutputBuffer(SessionOutputBuffer out, Wire wire, String charset) {
        this.out = out;
        this.wire = wire;
        if (charset == null) {
            charset = Consts.ASCII.name();
        }
        this.charset = charset;
    }

    public final void write(byte[] b, int off, int len) throws IOException {
        this.out.write(b, off, len);
        if (this.wire.log.debugEnabled) {
            Wire wire = this.wire;
            Args.notNull(b, "Output");
            wire.wire(">> ", new ByteArrayInputStream(b, off, len));
        }
    }

    public final void write(int b) throws IOException {
        this.out.write(b);
        if (this.wire.log.debugEnabled) {
            this.wire.output(new byte[]{(byte) b});
        }
    }

    public final void flush() throws IOException {
        this.out.flush();
    }

    public final void writeLine(CharArrayBuffer buffer) throws IOException {
        this.out.writeLine(buffer);
        if (this.wire.log.debugEnabled) {
            this.wire.output((new String(buffer.buffer, 0, buffer.length()) + "\r\n").getBytes(this.charset));
        }
    }

    public final void writeLine(String s) throws IOException {
        this.out.writeLine(s);
        if (this.wire.log.debugEnabled) {
            this.wire.output((s + "\r\n").getBytes(this.charset));
        }
    }

    public final HttpTransportMetrics getMetrics() {
        return this.out.getMetrics();
    }
}
