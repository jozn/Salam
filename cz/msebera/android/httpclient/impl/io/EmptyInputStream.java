package cz.msebera.android.httpclient.impl.io;

import java.io.InputStream;

public final class EmptyInputStream extends InputStream {
    public static final EmptyInputStream INSTANCE;

    static {
        INSTANCE = new EmptyInputStream();
    }

    private EmptyInputStream() {
    }

    public final int available() {
        return 0;
    }

    public final void close() {
    }

    public final void mark(int readLimit) {
    }

    public final boolean markSupported() {
        return true;
    }

    public final int read() {
        return -1;
    }

    public final int read(byte[] buf) {
        return -1;
    }

    public final int read(byte[] buf, int off, int len) {
        return -1;
    }

    public final void reset() {
    }

    public final long skip(long n) {
        return 0;
    }
}
