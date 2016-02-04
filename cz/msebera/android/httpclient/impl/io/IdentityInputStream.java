package cz.msebera.android.httpclient.impl.io;

import cz.msebera.android.httpclient.io.BufferInfo;
import cz.msebera.android.httpclient.io.SessionInputBuffer;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;
import java.io.InputStream;

public final class IdentityInputStream extends InputStream {
    private boolean closed;
    private final SessionInputBuffer in;

    public IdentityInputStream(SessionInputBuffer in) {
        this.closed = false;
        this.in = (SessionInputBuffer) Args.notNull(in, "Session input buffer");
    }

    public final int available() throws IOException {
        if (this.in instanceof BufferInfo) {
            return ((BufferInfo) this.in).length();
        }
        return 0;
    }

    public final void close() throws IOException {
        this.closed = true;
    }

    public final int read() throws IOException {
        if (this.closed) {
            return -1;
        }
        return this.in.read();
    }

    public final int read(byte[] b, int off, int len) throws IOException {
        if (this.closed) {
            return -1;
        }
        return this.in.read(b, off, len);
    }
}
