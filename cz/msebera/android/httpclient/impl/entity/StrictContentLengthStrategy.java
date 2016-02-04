package cz.msebera.android.httpclient.impl.entity;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpMessage;
import cz.msebera.android.httpclient.HttpVersion;
import cz.msebera.android.httpclient.ProtocolException;
import cz.msebera.android.httpclient.entity.ContentLengthStrategy;
import cz.msebera.android.httpclient.util.Args;

public final class StrictContentLengthStrategy implements ContentLengthStrategy {
    public static final StrictContentLengthStrategy INSTANCE;
    private final int implicitLen;

    static {
        INSTANCE = new StrictContentLengthStrategy((byte) 0);
    }

    private StrictContentLengthStrategy() {
        this.implicitLen = -1;
    }

    public StrictContentLengthStrategy(byte b) {
        this();
    }

    public final long determineLength(HttpMessage message) throws HttpException {
        String s;
        Args.notNull(message, "HTTP message");
        Header transferEncodingHeader = message.getFirstHeader("Transfer-Encoding");
        if (transferEncodingHeader != null) {
            s = transferEncodingHeader.getValue();
            if ("chunked".equalsIgnoreCase(s)) {
                if (!message.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
                    return -2;
                }
                throw new ProtocolException("Chunked transfer encoding not allowed for " + message.getProtocolVersion());
            } else if ("identity".equalsIgnoreCase(s)) {
                return -1;
            } else {
                throw new ProtocolException("Unsupported transfer encoding: " + s);
            }
        }
        Header contentLengthHeader = message.getFirstHeader("Content-Length");
        if (contentLengthHeader == null) {
            return (long) this.implicitLen;
        }
        s = contentLengthHeader.getValue();
        try {
            long len = Long.parseLong(s);
            if (len >= 0) {
                return len;
            }
            throw new ProtocolException("Negative content length: " + s);
        } catch (NumberFormatException e) {
            throw new ProtocolException("Invalid content length: " + s);
        }
    }
}
