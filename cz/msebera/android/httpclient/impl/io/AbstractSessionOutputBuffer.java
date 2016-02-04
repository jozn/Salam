package cz.msebera.android.httpclient.impl.io;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import cz.msebera.android.httpclient.io.BufferInfo;
import cz.msebera.android.httpclient.io.HttpTransportMetrics;
import cz.msebera.android.httpclient.io.SessionOutputBuffer;
import cz.msebera.android.httpclient.util.ByteArrayBuffer;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;

@Deprecated
public abstract class AbstractSessionOutputBuffer implements BufferInfo, SessionOutputBuffer {
    private static final byte[] CRLF;
    boolean ascii;
    private ByteBuffer bbuf;
    ByteArrayBuffer buffer;
    Charset charset;
    CharsetEncoder encoder;
    HttpTransportMetricsImpl metrics;
    int minChunkLimit;
    CodingErrorAction onMalformedCharAction;
    CodingErrorAction onUnmappableCharAction;
    OutputStream outstream;

    static {
        CRLF = new byte[]{MqttWireMessage.MESSAGE_TYPE_PINGRESP, (byte) 10};
    }

    public final int length() {
        return this.buffer.len;
    }

    private void flushBuffer() throws IOException {
        int len = this.buffer.len;
        if (len > 0) {
            this.outstream.write(this.buffer.buffer, 0, len);
            this.buffer.len = 0;
            this.metrics.incrementBytesTransferred((long) len);
        }
    }

    public final void flush() throws IOException {
        flushBuffer();
        this.outstream.flush();
    }

    public final void write(byte[] b, int off, int len) throws IOException {
        if (b != null) {
            if (len > this.minChunkLimit || len > this.buffer.buffer.length) {
                flushBuffer();
                this.outstream.write(b, off, len);
                this.metrics.incrementBytesTransferred((long) len);
                return;
            }
            if (len > this.buffer.buffer.length - this.buffer.len) {
                flushBuffer();
            }
            this.buffer.append(b, off, len);
        }
    }

    private void write(byte[] b) throws IOException {
        if (b != null) {
            write(b, 0, b.length);
        }
    }

    public final void write(int b) throws IOException {
        if (this.buffer.isFull()) {
            flushBuffer();
        }
        ByteArrayBuffer byteArrayBuffer = this.buffer;
        int i = byteArrayBuffer.len + 1;
        if (i > byteArrayBuffer.buffer.length) {
            byteArrayBuffer.expand(i);
        }
        byteArrayBuffer.buffer[byteArrayBuffer.len] = (byte) b;
        byteArrayBuffer.len = i;
    }

    public final void writeLine(String s) throws IOException {
        if (s != null) {
            if (s.length() > 0) {
                if (this.ascii) {
                    for (int i = 0; i < s.length(); i++) {
                        write(s.charAt(i));
                    }
                } else {
                    writeEncoded(CharBuffer.wrap(s));
                }
            }
            write(CRLF);
        }
    }

    public final void writeLine(CharArrayBuffer charbuffer) throws IOException {
        if (charbuffer != null) {
            if (this.ascii) {
                int off = 0;
                int remaining = charbuffer.length();
                while (remaining > 0) {
                    int chunk = Math.min(this.buffer.buffer.length - this.buffer.len, remaining);
                    if (chunk > 0) {
                        ByteArrayBuffer byteArrayBuffer = this.buffer;
                        if (charbuffer != null) {
                            char[] cArr = charbuffer.buffer;
                            if (cArr != null) {
                                if (off < 0 || off > cArr.length || chunk < 0 || off + chunk < 0 || off + chunk > cArr.length) {
                                    throw new IndexOutOfBoundsException("off: " + off + " len: " + chunk + " b.length: " + cArr.length);
                                } else if (chunk != 0) {
                                    int i = byteArrayBuffer.len;
                                    int i2 = i + chunk;
                                    if (i2 > byteArrayBuffer.buffer.length) {
                                        byteArrayBuffer.expand(i2);
                                    }
                                    int i3 = off;
                                    while (i < i2) {
                                        byteArrayBuffer.buffer[i] = (byte) cArr[i3];
                                        i3++;
                                        i++;
                                    }
                                    byteArrayBuffer.len = i2;
                                }
                            }
                        }
                    }
                    if (this.buffer.isFull()) {
                        flushBuffer();
                    }
                    off += chunk;
                    remaining -= chunk;
                }
            } else {
                writeEncoded(CharBuffer.wrap(charbuffer.buffer, 0, charbuffer.length()));
            }
            write(CRLF);
        }
    }

    private void writeEncoded(CharBuffer cbuf) throws IOException {
        if (cbuf.hasRemaining()) {
            if (this.encoder == null) {
                this.encoder = this.charset.newEncoder();
                this.encoder.onMalformedInput(this.onMalformedCharAction);
                this.encoder.onUnmappableCharacter(this.onUnmappableCharAction);
            }
            if (this.bbuf == null) {
                this.bbuf = ByteBuffer.allocate(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            }
            this.encoder.reset();
            while (cbuf.hasRemaining()) {
                handleEncodingResult(this.encoder.encode(cbuf, this.bbuf, true));
            }
            handleEncodingResult(this.encoder.flush(this.bbuf));
            this.bbuf.clear();
        }
    }

    private void handleEncodingResult(CoderResult result) throws IOException {
        if (result.isError()) {
            result.throwException();
        }
        this.bbuf.flip();
        while (this.bbuf.hasRemaining()) {
            write(this.bbuf.get());
        }
        this.bbuf.compact();
    }

    public final HttpTransportMetrics getMetrics() {
        return this.metrics;
    }
}
