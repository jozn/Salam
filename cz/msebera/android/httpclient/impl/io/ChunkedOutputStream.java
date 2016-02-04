package cz.msebera.android.httpclient.impl.io;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.BuildConfig;
import cz.msebera.android.httpclient.io.SessionOutputBuffer;
import java.io.IOException;
import java.io.OutputStream;

public final class ChunkedOutputStream extends OutputStream {
    private final byte[] cache;
    private int cachePosition;
    private boolean closed;
    private final SessionOutputBuffer out;
    private boolean wroteLastChunk;

    @Deprecated
    public ChunkedOutputStream(SessionOutputBuffer out) throws IOException {
        this(out, (byte) 0);
    }

    private ChunkedOutputStream(SessionOutputBuffer out, byte b) {
        this.cachePosition = 0;
        this.wroteLastChunk = false;
        this.closed = false;
        this.cache = new byte[AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT];
        this.out = out;
    }

    private void flushCache() throws IOException {
        if (this.cachePosition > 0) {
            this.out.writeLine(Integer.toHexString(this.cachePosition));
            this.out.write(this.cache, 0, this.cachePosition);
            this.out.writeLine(BuildConfig.VERSION_NAME);
            this.cachePosition = 0;
        }
    }

    public final void write(int b) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        }
        this.cache[this.cachePosition] = (byte) b;
        this.cachePosition++;
        if (this.cachePosition == this.cache.length) {
            flushCache();
        }
    }

    public final void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public final void write(byte[] src, int off, int len) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted write to closed stream.");
        } else if (len >= this.cache.length - this.cachePosition) {
            this.out.writeLine(Integer.toHexString(this.cachePosition + len));
            this.out.write(this.cache, 0, this.cachePosition);
            this.out.write(src, off, len);
            this.out.writeLine(BuildConfig.VERSION_NAME);
            this.cachePosition = 0;
        } else {
            System.arraycopy(src, off, this.cache, this.cachePosition, len);
            this.cachePosition += len;
        }
    }

    public final void flush() throws IOException {
        flushCache();
        this.out.flush();
    }

    public final void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            if (!this.wroteLastChunk) {
                flushCache();
                this.out.writeLine("0");
                this.out.writeLine(BuildConfig.VERSION_NAME);
                this.wroteLastChunk = true;
            }
            this.out.flush();
        }
    }
}
