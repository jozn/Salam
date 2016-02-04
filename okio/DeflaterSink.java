package okio;

import java.io.IOException;
import java.util.zip.Deflater;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public final class DeflaterSink implements Sink {
    private boolean closed;
    private final Deflater deflater;
    private final BufferedSink sink;

    public DeflaterSink(Sink sink, Deflater deflater) {
        this(Okio.buffer(sink), deflater);
    }

    private DeflaterSink(BufferedSink sink, Deflater deflater) {
        if (sink == null) {
            throw new IllegalArgumentException("source == null");
        } else if (deflater == null) {
            throw new IllegalArgumentException("inflater == null");
        } else {
            this.sink = sink;
            this.deflater = deflater;
        }
    }

    public final void write(Buffer source, long byteCount) throws IOException {
        Util.checkOffsetAndCount(source.size, 0, byteCount);
        while (byteCount > 0) {
            Segment head = source.head;
            int toDeflate = (int) Math.min(byteCount, (long) (head.limit - head.pos));
            this.deflater.setInput(head.data, head.pos, toDeflate);
            deflate(false);
            source.size -= (long) toDeflate;
            head.pos += toDeflate;
            if (head.pos == head.limit) {
                source.head = head.pop();
                SegmentPool.recycle(head);
            }
            byteCount -= (long) toDeflate;
        }
    }

    @IgnoreJRERequirement
    private void deflate(boolean syncFlush) throws IOException {
        Buffer buffer = this.sink.buffer();
        while (true) {
            Segment s = buffer.writableSegment(1);
            int deflated = syncFlush ? this.deflater.deflate(s.data, s.limit, 2048 - s.limit, 2) : this.deflater.deflate(s.data, s.limit, 2048 - s.limit);
            if (deflated > 0) {
                s.limit += deflated;
                buffer.size += (long) deflated;
                this.sink.emitCompleteSegments();
            } else if (this.deflater.needsInput()) {
                break;
            }
        }
        if (s.pos == s.limit) {
            buffer.head = s.pop();
            SegmentPool.recycle(s);
        }
    }

    public final void flush() throws IOException {
        deflate(true);
        this.sink.flush();
    }

    public final void close() throws IOException {
        if (!this.closed) {
            Throwable thrown = null;
            try {
                this.deflater.finish();
                deflate(false);
            } catch (Throwable th) {
                thrown = th;
            }
            try {
                this.deflater.end();
            } catch (Throwable e) {
                if (thrown == null) {
                    thrown = e;
                }
            }
            try {
                this.sink.close();
            } catch (Throwable e2) {
                if (thrown == null) {
                    thrown = e2;
                }
            }
            this.closed = true;
            if (thrown != null) {
                Util.sneakyRethrow(thrown);
            }
        }
    }

    public final Timeout timeout() {
        return this.sink.timeout();
    }

    public final String toString() {
        return "DeflaterSink(" + this.sink + ")";
    }
}
