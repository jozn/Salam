package cz.msebera.android.httpclient.impl.io;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import cz.msebera.android.httpclient.ConnectionClosedException;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.MalformedChunkCodingException;
import cz.msebera.android.httpclient.TruncatedChunkException;
import cz.msebera.android.httpclient.config.MessageConstraints;
import cz.msebera.android.httpclient.io.BufferInfo;
import cz.msebera.android.httpclient.io.SessionInputBuffer;
import cz.msebera.android.httpclient.message.BasicLineParser;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class ChunkedInputStream extends InputStream {
    private final CharArrayBuffer buffer;
    private int chunkSize;
    private boolean closed;
    private final MessageConstraints constraints;
    private boolean eof;
    private Header[] footers;
    private final SessionInputBuffer in;
    private int pos;
    private int state;

    private ChunkedInputStream(SessionInputBuffer in) {
        this.eof = false;
        this.closed = false;
        this.footers = new Header[0];
        this.in = (SessionInputBuffer) Args.notNull(in, "Session input buffer");
        this.pos = 0;
        this.buffer = new CharArrayBuffer(16);
        this.constraints = MessageConstraints.DEFAULT;
        this.state = 1;
    }

    public ChunkedInputStream(SessionInputBuffer in, byte b) {
        this(in);
    }

    public final int available() throws IOException {
        if (this.in instanceof BufferInfo) {
            return Math.min(((BufferInfo) this.in).length(), this.chunkSize - this.pos);
        }
        return 0;
    }

    public final int read() throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        } else if (this.eof) {
            return -1;
        } else {
            if (this.state != 2) {
                nextChunk();
                if (this.eof) {
                    return -1;
                }
            }
            int b = this.in.read();
            if (b == -1) {
                return b;
            }
            this.pos++;
            if (this.pos < this.chunkSize) {
                return b;
            }
            this.state = 3;
            return b;
        }
    }

    public final int read(byte[] b, int off, int len) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        } else if (this.eof) {
            return -1;
        } else {
            if (this.state != 2) {
                nextChunk();
                if (this.eof) {
                    return -1;
                }
            }
            int bytesRead = this.in.read(b, off, Math.min(len, this.chunkSize - this.pos));
            if (bytesRead != -1) {
                this.pos += bytesRead;
                if (this.pos < this.chunkSize) {
                    return bytesRead;
                }
                this.state = 3;
                return bytesRead;
            }
            this.eof = true;
            throw new TruncatedChunkException("Truncated chunk ( expected size: " + this.chunkSize + "; actual size: " + this.pos + ")");
        }
    }

    public final int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    private void nextChunk() throws IOException {
        if (this.state == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) {
            throw new MalformedChunkCodingException("Corrupt data stream");
        }
        try {
            this.chunkSize = getChunkSize();
            if (this.chunkSize < 0) {
                throw new MalformedChunkCodingException("Negative chunk size");
            }
            this.state = 2;
            this.pos = 0;
            if (this.chunkSize == 0) {
                this.eof = true;
                this.footers = AbstractMessageParser.parseHeaders(this.in, this.constraints.maxHeaderCount, this.constraints.maxLineLength, BasicLineParser.INSTANCE, new ArrayList());
            }
        } catch (Throwable e) {
            IOException malformedChunkCodingException = new MalformedChunkCodingException("Invalid footer: " + e.getMessage());
            malformedChunkCodingException.initCause(e);
            throw malformedChunkCodingException;
        } catch (MalformedChunkCodingException ex) {
            this.state = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            throw ex;
        }
    }

    private int getChunkSize() throws IOException {
        switch (this.state) {
            case Logger.SEVERE /*1*/:
                break;
            case Logger.INFO /*3*/:
                this.buffer.len = 0;
                if (this.in.readLine(this.buffer) != -1) {
                    int i;
                    if (this.buffer.len == 0) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    if (i != 0) {
                        this.state = 1;
                        break;
                    }
                    throw new MalformedChunkCodingException("Unexpected content at the end of chunk");
                }
                throw new MalformedChunkCodingException("CRLF expected at end of chunk");
            default:
                throw new IllegalStateException("Inconsistent codec state");
        }
        this.buffer.len = 0;
        if (this.in.readLine(this.buffer) == -1) {
            throw new ConnectionClosedException("Premature end of chunk coded message body: closing chunk expected");
        }
        int separator = this.buffer.indexOf(59);
        if (separator < 0) {
            separator = this.buffer.length();
        }
        try {
            return Integer.parseInt(this.buffer.substringTrimmed(0, separator), 16);
        } catch (NumberFormatException e) {
            throw new MalformedChunkCodingException("Bad chunk header");
        }
    }

    public final void close() throws IOException {
        if (!this.closed) {
            try {
                if (this.eof || this.state == ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) {
                    this.eof = true;
                    this.closed = true;
                }
                do {
                } while (read(new byte[AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT]) >= 0);
                this.eof = true;
                this.closed = true;
            } catch (Throwable th) {
                this.eof = true;
                this.closed = true;
            }
        }
    }
}
