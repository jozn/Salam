package cz.msebera.android.httpclient.impl.io;

import cz.msebera.android.httpclient.io.SessionOutputBuffer;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;
import java.io.OutputStream;

public final class IdentityOutputStream extends OutputStream {
    private boolean closed;
    private final SessionOutputBuffer out;

    public IdentityOutputStream(SessionOutputBuffer out) {
        this.closed = false;
        this.out = (SessionOutputBuffer) Args.notNull(out, "Session output buffer");
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
        }
        this.out.write(b, off, len);
    }

    public final void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public final void write(int b) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        }
        this.out.write(b);
    }
}
