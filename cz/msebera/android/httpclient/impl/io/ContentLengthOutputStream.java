package cz.msebera.android.httpclient.impl.io;

import cz.msebera.android.httpclient.io.SessionOutputBuffer;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;
import java.io.OutputStream;

public final class ContentLengthOutputStream extends OutputStream {
    private boolean closed;
    private final long contentLength;
    private final SessionOutputBuffer out;
    private long total;

    public ContentLengthOutputStream(SessionOutputBuffer out, long contentLength) {
        this.total = 0;
        this.closed = false;
        this.out = (SessionOutputBuffer) Args.notNull(out, "Session output buffer");
        this.contentLength = Args.notNegative(contentLength, "Content length");
    }

    public final void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.out.flush();
        }
    }

    public final void flush() throws IOException {
        this.out.flush();
    }

    public final void write(byte[] b, int off, int len) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        } else if (this.total < this.contentLength) {
            long max = this.contentLength - this.total;
            int chunk = len;
            if (((long) len) > max) {
                chunk = (int) max;
            }
            this.out.write(b, off, chunk);
            this.total += (long) chunk;
        }
    }

    public final void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public final void write(int b) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        } else if (this.total < this.contentLength) {
            this.out.write(b);
            this.total++;
        }
    }
}
