package cz.msebera.android.httpclient.entity;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import java.io.IOException;

public abstract class AbstractHttpEntity implements HttpEntity {
    protected boolean chunked;
    protected Header contentEncoding;
    protected Header contentType;

    protected AbstractHttpEntity() {
    }

    public final Header getContentType() {
        return this.contentType;
    }

    public final Header getContentEncoding() {
        return this.contentEncoding;
    }

    public final boolean isChunked() {
        return this.chunked;
    }

    public final void setContentType(Header contentType) {
        this.contentType = contentType;
    }

    public final void setContentType(String ctString) {
        Header h = null;
        if (ctString != null) {
            h = new BasicHeader("Content-Type", ctString);
        }
        this.contentType = h;
    }

    public final void setContentEncoding(Header contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public final void setChunked(boolean b) {
        this.chunked = b;
    }

    @Deprecated
    public final void consumeContent() throws IOException {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (this.contentType != null) {
            sb.append("Content-Type: ");
            sb.append(this.contentType.getValue());
            sb.append(',');
        }
        if (this.contentEncoding != null) {
            sb.append("Content-Encoding: ");
            sb.append(this.contentEncoding.getValue());
            sb.append(',');
        }
        long len = getContentLength();
        if (len >= 0) {
            sb.append("Content-Length: ");
            sb.append(len);
            sb.append(',');
        }
        sb.append("Chunked: ");
        sb.append(this.chunked);
        sb.append(']');
        return sb.toString();
    }
}
