package com.loopj.android.http;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class Base64OutputStream extends FilterOutputStream {
    private static final byte[] EMPTY;
    private int bpos;
    private byte[] buffer;
    private final Coder coder;
    private final int flags;

    static {
        EMPTY = new byte[0];
    }

    public Base64OutputStream(OutputStream out) {
        this(out, (byte) 0);
    }

    private Base64OutputStream(OutputStream out, byte b) {
        super(out);
        this.buffer = null;
        this.bpos = 0;
        this.flags = 18;
        this.coder = new Encoder();
    }

    public final void write(int b) throws IOException {
        if (this.buffer == null) {
            this.buffer = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
        }
        if (this.bpos >= this.buffer.length) {
            internalWrite(this.buffer, 0, this.bpos, false);
            this.bpos = 0;
        }
        byte[] bArr = this.buffer;
        int i = this.bpos;
        this.bpos = i + 1;
        bArr[i] = (byte) b;
    }

    private void flushBuffer() throws IOException {
        if (this.bpos > 0) {
            internalWrite(this.buffer, 0, this.bpos, false);
            this.bpos = 0;
        }
    }

    public final void write(byte[] b, int off, int len) throws IOException {
        if (len > 0) {
            flushBuffer();
            internalWrite(b, off, len, false);
        }
    }

    public final void close() throws IOException {
        IOException thrown = null;
        try {
            flushBuffer();
            internalWrite(EMPTY, 0, 0, true);
        } catch (IOException e) {
            thrown = e;
        }
        try {
            if ((this.flags & 16) == 0) {
                this.out.close();
            } else {
                this.out.flush();
            }
        } catch (IOException e2) {
            if (thrown != null) {
                thrown = e2;
            }
        }
        if (thrown != null) {
            throw thrown;
        }
    }

    private void internalWrite(byte[] b, int off, int len, boolean finish) throws IOException {
        Coder coder = this.coder;
        byte[] bArr = this.coder.output;
        int maxOutputSize = this.coder.maxOutputSize(len);
        if (bArr == null || bArr.length < maxOutputSize) {
            bArr = new byte[maxOutputSize];
        }
        coder.output = bArr;
        this.coder.process(b, off, len, finish);
        this.out.write(this.coder.output, 0, this.coder.op);
    }
}
