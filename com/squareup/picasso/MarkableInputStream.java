package com.squareup.picasso;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

final class MarkableInputStream extends InputStream {
    private long defaultMark;
    private final InputStream in;
    private long limit;
    private long offset;
    private long reset;

    public MarkableInputStream(InputStream in) {
        this(in, (byte) 0);
    }

    private MarkableInputStream(InputStream in, byte b) {
        this.defaultMark = -1;
        if (!in.markSupported()) {
            in = new BufferedInputStream(in, AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD);
        }
        this.in = in;
    }

    public final void mark(int readLimit) {
        this.defaultMark = savePosition(readLimit);
    }

    public final long savePosition(int readLimit) {
        long offsetLimit = this.offset + ((long) readLimit);
        if (this.limit < offsetLimit) {
            try {
                if (this.reset >= this.offset || this.offset > this.limit) {
                    this.reset = this.offset;
                    this.in.mark((int) (offsetLimit - this.offset));
                } else {
                    this.in.reset();
                    this.in.mark((int) (offsetLimit - this.reset));
                    skip(this.reset, this.offset);
                }
                this.limit = offsetLimit;
            } catch (IOException e) {
                throw new IllegalStateException("Unable to mark: " + e);
            }
        }
        return this.offset;
    }

    public final void reset() throws IOException {
        reset(this.defaultMark);
    }

    public final void reset(long token) throws IOException {
        if (this.offset > this.limit || token < this.reset) {
            throw new IOException("Cannot reset");
        }
        this.in.reset();
        skip(this.reset, token);
        this.offset = token;
    }

    private void skip(long current, long target) throws IOException {
        while (current < target) {
            long skipped = this.in.skip(target - current);
            if (skipped == 0) {
                if (read() != -1) {
                    skipped = 1;
                } else {
                    return;
                }
            }
            current += skipped;
        }
    }

    public final int read() throws IOException {
        int result = this.in.read();
        if (result != -1) {
            this.offset++;
        }
        return result;
    }

    public final int read(byte[] buffer) throws IOException {
        int count = this.in.read(buffer);
        if (count != -1) {
            this.offset += (long) count;
        }
        return count;
    }

    public final int read(byte[] buffer, int offset, int length) throws IOException {
        int count = this.in.read(buffer, offset, length);
        if (count != -1) {
            this.offset += (long) count;
        }
        return count;
    }

    public final long skip(long byteCount) throws IOException {
        long skipped = this.in.skip(byteCount);
        this.offset += skipped;
        return skipped;
    }

    public final int available() throws IOException {
        return this.in.available();
    }

    public final void close() throws IOException {
        this.in.close();
    }

    public final boolean markSupported() {
        return this.in.markSupported();
    }
}
