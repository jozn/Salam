package okio;

import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.MotionEventCompat;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

final class RealBufferedSource implements BufferedSource {
    public final Buffer buffer;
    boolean closed;
    public final Source source;

    /* renamed from: okio.RealBufferedSource.1 */
    class C12631 extends InputStream {
        C12631() {
        }

        public final int read() throws IOException {
            if (RealBufferedSource.this.closed) {
                throw new IOException("closed");
            } else if (RealBufferedSource.this.buffer.size == 0 && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
                return -1;
            } else {
                return RealBufferedSource.this.buffer.readByte() & MotionEventCompat.ACTION_MASK;
            }
        }

        public final int read(byte[] data, int offset, int byteCount) throws IOException {
            if (RealBufferedSource.this.closed) {
                throw new IOException("closed");
            }
            Util.checkOffsetAndCount((long) data.length, (long) offset, (long) byteCount);
            if (RealBufferedSource.this.buffer.size == 0 && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
                return -1;
            }
            return RealBufferedSource.this.buffer.read(data, offset, byteCount);
        }

        public final int available() throws IOException {
            if (!RealBufferedSource.this.closed) {
                return (int) Math.min(RealBufferedSource.this.buffer.size, 2147483647L);
            }
            throw new IOException("closed");
        }

        public final void close() throws IOException {
            RealBufferedSource.this.close();
        }

        public final String toString() {
            return RealBufferedSource.this + ".inputStream()";
        }
    }

    private RealBufferedSource(Source source, Buffer buffer) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        this.buffer = buffer;
        this.source = source;
    }

    public RealBufferedSource(Source source) {
        this(source, new Buffer());
    }

    public final Buffer buffer() {
        return this.buffer;
    }

    public final long read(Buffer sink, long byteCount) throws IOException {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        } else if (byteCount < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
        } else if (this.closed) {
            throw new IllegalStateException("closed");
        } else if (this.buffer.size == 0 && this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
            return -1;
        } else {
            return this.buffer.read(sink, Math.min(byteCount, this.buffer.size));
        }
    }

    public final boolean exhausted() throws IOException {
        if (!this.closed) {
            return this.buffer.exhausted() && this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1;
        } else {
            throw new IllegalStateException("closed");
        }
    }

    public final void require(long byteCount) throws IOException {
        if (!request(byteCount)) {
            throw new EOFException();
        }
    }

    private boolean request(long byteCount) throws IOException {
        if (byteCount < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + byteCount);
        } else if (this.closed) {
            throw new IllegalStateException("closed");
        } else {
            while (this.buffer.size < byteCount) {
                if (this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
                    return false;
                }
            }
            return true;
        }
    }

    public final byte readByte() throws IOException {
        require(1);
        return this.buffer.readByte();
    }

    public final ByteString readByteString(long byteCount) throws IOException {
        require(byteCount);
        return this.buffer.readByteString(byteCount);
    }

    public final byte[] readByteArray() throws IOException {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteArray();
    }

    public final byte[] readByteArray(long byteCount) throws IOException {
        require(byteCount);
        return this.buffer.readByteArray(byteCount);
    }

    public final String readUtf8LineStrict() throws IOException {
        long newline = indexOf((byte) 10);
        if (newline != -1) {
            return this.buffer.readUtf8Line(newline);
        }
        Buffer data = new Buffer();
        this.buffer.copyTo(data, 0, Math.min(32, this.buffer.size));
        throw new EOFException("\\n not found: size=" + this.buffer.size + " content=" + data.readByteString().hex() + "...");
    }

    public final short readShort() throws IOException {
        require(2);
        return this.buffer.readShort();
    }

    public final short readShortLe() throws IOException {
        require(2);
        return Util.reverseBytesShort(this.buffer.readShort());
    }

    public final int readInt() throws IOException {
        require(4);
        return this.buffer.readInt();
    }

    public final int readIntLe() throws IOException {
        require(4);
        return Util.reverseBytesInt(this.buffer.readInt());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final long readDecimalLong() throws java.io.IOException {
        /*
        r8 = this;
        r1 = 0;
    L_0x0001:
        r2 = r1 + 1;
        r2 = (long) r2;
        r2 = r8.request(r2);
        if (r2 == 0) goto L_0x0022;
    L_0x000a:
        r2 = r8.buffer;
        r4 = (long) r1;
        r0 = r2.getByte(r4);
        r2 = 48;
        if (r0 < r2) goto L_0x0019;
    L_0x0015:
        r2 = 57;
        if (r0 <= r2) goto L_0x001f;
    L_0x0019:
        if (r1 != 0) goto L_0x0022;
    L_0x001b:
        r2 = 45;
        if (r0 != r2) goto L_0x0022;
    L_0x001f:
        r1 = r1 + 1;
        goto L_0x0001;
    L_0x0022:
        if (r1 != 0) goto L_0x0045;
    L_0x0024:
        r2 = new java.lang.NumberFormatException;
        r3 = new java.lang.StringBuilder;
        r4 = "Expected leading [0-9] or '-' character but was 0x";
        r3.<init>(r4);
        r4 = r8.buffer;
        r6 = 0;
        r4 = r4.getByte(r6);
        r4 = java.lang.Integer.toHexString(r4);
        r3 = r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
    L_0x0045:
        r2 = r8.buffer;
        r2 = r2.readDecimalLong();
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.RealBufferedSource.readDecimalLong():long");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final long readHexadecimalUnsignedLong() throws java.io.IOException {
        /*
        r8 = this;
        r1 = 0;
    L_0x0001:
        r2 = r1 + 1;
        r2 = (long) r2;
        r2 = r8.request(r2);
        if (r2 == 0) goto L_0x002c;
    L_0x000a:
        r2 = r8.buffer;
        r4 = (long) r1;
        r0 = r2.getByte(r4);
        r2 = 48;
        if (r0 < r2) goto L_0x0019;
    L_0x0015:
        r2 = 57;
        if (r0 <= r2) goto L_0x0029;
    L_0x0019:
        r2 = 97;
        if (r0 < r2) goto L_0x0021;
    L_0x001d:
        r2 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        if (r0 <= r2) goto L_0x0029;
    L_0x0021:
        r2 = 65;
        if (r0 < r2) goto L_0x002c;
    L_0x0025:
        r2 = 70;
        if (r0 > r2) goto L_0x002c;
    L_0x0029:
        r1 = r1 + 1;
        goto L_0x0001;
    L_0x002c:
        if (r1 != 0) goto L_0x004f;
    L_0x002e:
        r2 = new java.lang.NumberFormatException;
        r3 = new java.lang.StringBuilder;
        r4 = "Expected leading [0-9a-fA-F] character but was 0x";
        r3.<init>(r4);
        r4 = r8.buffer;
        r6 = 0;
        r4 = r4.getByte(r6);
        r4 = java.lang.Integer.toHexString(r4);
        r3 = r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
    L_0x004f:
        r2 = r8.buffer;
        r2 = r2.readHexadecimalUnsignedLong();
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.RealBufferedSource.readHexadecimalUnsignedLong():long");
    }

    public final void skip(long byteCount) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (byteCount > 0) {
            if (this.buffer.size == 0 && this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
                throw new EOFException();
            }
            long toSkip = Math.min(byteCount, this.buffer.size);
            this.buffer.skip(toSkip);
            byteCount -= toSkip;
        }
    }

    public final long indexOf(byte b) throws IOException {
        long j = 0;
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (0 >= this.buffer.size) {
            if (this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) == -1) {
                return -1;
            }
        }
        do {
            j = this.buffer.indexOf(b, j);
            if (j != -1) {
                return j;
            }
            j = this.buffer.size;
        } while (this.source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH) != -1);
        return -1;
    }

    public final InputStream inputStream() {
        return new C12631();
    }

    public final void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.source.close();
            this.buffer.clear();
        }
    }

    public final Timeout timeout() {
        return this.source.timeout();
    }

    public final String toString() {
        return "buffer(" + this.source + ")";
    }
}
