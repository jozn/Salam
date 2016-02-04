package cz.msebera.android.httpclient.impl.io;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import cz.msebera.android.httpclient.io.BufferInfo;
import cz.msebera.android.httpclient.io.HttpTransportMetrics;
import cz.msebera.android.httpclient.io.SessionInputBuffer;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.ByteArrayBuffer;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;

@Deprecated
public abstract class AbstractSessionInputBuffer implements BufferInfo, SessionInputBuffer {
    boolean ascii;
    byte[] buffer;
    int bufferlen;
    int bufferpos;
    private CharBuffer cbuf;
    Charset charset;
    CharsetDecoder decoder;
    InputStream instream;
    ByteArrayBuffer linebuffer;
    int maxLineLen;
    HttpTransportMetricsImpl metrics;
    int minChunkLimit;
    CodingErrorAction onMalformedCharAction;
    CodingErrorAction onUnmappableCharAction;

    public final int length() {
        return this.bufferlen - this.bufferpos;
    }

    protected int fillBuffer() throws IOException {
        if (this.bufferpos > 0) {
            int len = this.bufferlen - this.bufferpos;
            if (len > 0) {
                System.arraycopy(this.buffer, this.bufferpos, this.buffer, 0, len);
            }
            this.bufferpos = 0;
            this.bufferlen = len;
        }
        int off = this.bufferlen;
        int l = this.instream.read(this.buffer, off, this.buffer.length - off);
        if (l == -1) {
            return -1;
        }
        this.bufferlen = off + l;
        this.metrics.incrementBytesTransferred((long) l);
        return l;
    }

    protected final boolean hasBufferedData() {
        return this.bufferpos < this.bufferlen;
    }

    public final int read() throws IOException {
        while (!hasBufferedData()) {
            if (fillBuffer() == -1) {
                return -1;
            }
        }
        byte[] bArr = this.buffer;
        int i = this.bufferpos;
        this.bufferpos = i + 1;
        return bArr[i] & MotionEventCompat.ACTION_MASK;
    }

    public final int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            return 0;
        }
        int chunk;
        if (hasBufferedData()) {
            chunk = Math.min(len, this.bufferlen - this.bufferpos);
            System.arraycopy(this.buffer, this.bufferpos, b, off, chunk);
            this.bufferpos += chunk;
            return chunk;
        } else if (len > this.minChunkLimit) {
            int read = this.instream.read(b, off, len);
            if (read > 0) {
                this.metrics.incrementBytesTransferred((long) read);
            }
            return read;
        } else {
            while (!hasBufferedData()) {
                if (fillBuffer() == -1) {
                    return -1;
                }
            }
            chunk = Math.min(len, this.bufferlen - this.bufferpos);
            System.arraycopy(this.buffer, this.bufferpos, b, off, chunk);
            this.bufferpos += chunk;
            return chunk;
        }
    }

    public final int readLine(CharArrayBuffer charbuffer) throws IOException {
        int i;
        Args.notNull(charbuffer, "Char array buffer");
        int noRead = 0;
        boolean retry = true;
        while (retry) {
            int i2;
            for (i = this.bufferpos; i < this.bufferlen; i++) {
                if (this.buffer[i] == (byte) 10) {
                    i2 = i;
                    break;
                }
            }
            i2 = -1;
            if (i2 == -1) {
                if (hasBufferedData()) {
                    this.linebuffer.append(this.buffer, this.bufferpos, this.bufferlen - this.bufferpos);
                    this.bufferpos = this.bufferlen;
                }
                noRead = fillBuffer();
                if (noRead == -1) {
                    retry = false;
                }
            } else if (this.linebuffer.isEmpty()) {
                int i3 = this.bufferpos;
                this.bufferpos = i2 + 1;
                if (i2 > i3 && this.buffer[i2 - 1] == MqttWireMessage.MESSAGE_TYPE_PINGRESP) {
                    i2--;
                }
                i = i2 - i3;
                if (!this.ascii) {
                    return appendDecoded(charbuffer, ByteBuffer.wrap(this.buffer, i3, i));
                }
                charbuffer.append(this.buffer, i3, i);
                return i;
            } else {
                retry = false;
                this.linebuffer.append(this.buffer, this.bufferpos, (i2 + 1) - this.bufferpos);
                this.bufferpos = i2 + 1;
            }
            if (this.maxLineLen > 0 && this.linebuffer.len >= this.maxLineLen) {
                throw new IOException("Maximum line length limit exceeded");
            }
        }
        if (noRead == -1 && this.linebuffer.isEmpty()) {
            return -1;
        }
        i = this.linebuffer.len;
        if (i > 0) {
            if (this.linebuffer.buffer[i - 1] == (byte) 10) {
                i--;
            }
            if (i > 0) {
                if (this.linebuffer.buffer[i - 1] == MqttWireMessage.MESSAGE_TYPE_PINGRESP) {
                    i--;
                }
            }
        }
        if (this.ascii) {
            ByteArrayBuffer byteArrayBuffer = this.linebuffer;
            if (byteArrayBuffer != null) {
                charbuffer.append(byteArrayBuffer.buffer, 0, i);
            }
        } else {
            i = appendDecoded(charbuffer, ByteBuffer.wrap(this.linebuffer.buffer, 0, i));
        }
        this.linebuffer.len = 0;
        return i;
    }

    private int appendDecoded(CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException {
        if (!bbuf.hasRemaining()) {
            return 0;
        }
        if (this.decoder == null) {
            this.decoder = this.charset.newDecoder();
            this.decoder.onMalformedInput(this.onMalformedCharAction);
            this.decoder.onUnmappableCharacter(this.onUnmappableCharAction);
        }
        if (this.cbuf == null) {
            this.cbuf = CharBuffer.allocate(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        }
        this.decoder.reset();
        int len = 0;
        while (bbuf.hasRemaining()) {
            len += handleDecodingResult$9566064(this.decoder.decode(bbuf, this.cbuf, true), charbuffer);
        }
        len += handleDecodingResult$9566064(this.decoder.flush(this.cbuf), charbuffer);
        this.cbuf.clear();
        return len;
    }

    private int handleDecodingResult$9566064(CoderResult result, CharArrayBuffer charbuffer) throws IOException {
        if (result.isError()) {
            result.throwException();
        }
        this.cbuf.flip();
        int len = this.cbuf.remaining();
        while (this.cbuf.hasRemaining()) {
            charbuffer.append(this.cbuf.get());
        }
        this.cbuf.compact();
        return len;
    }

    public final HttpTransportMetrics getMetrics() {
        return this.metrics;
    }
}
