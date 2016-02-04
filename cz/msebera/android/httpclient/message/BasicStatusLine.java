package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.StatusLine;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;
import java.io.Serializable;

public final class BasicStatusLine implements StatusLine, Serializable, Cloneable {
    private final ProtocolVersion protoVersion;
    private final String reasonPhrase;
    private final int statusCode;

    public BasicStatusLine(ProtocolVersion version, int statusCode, String reasonPhrase) {
        this.protoVersion = (ProtocolVersion) Args.notNull(version, "Version");
        this.statusCode = Args.notNegative(statusCode, "Status code");
        this.reasonPhrase = reasonPhrase;
    }

    public final int getStatusCode() {
        return this.statusCode;
    }

    public final ProtocolVersion getProtocolVersion() {
        return this.protoVersion;
    }

    public final String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public final String toString() {
        BasicLineFormatter basicLineFormatter = BasicLineFormatter.INSTANCE;
        Args.notNull(this, "Status line");
        CharArrayBuffer initBuffer = BasicLineFormatter.initBuffer(null);
        int estimateProtocolVersionLen = ((BasicLineFormatter.estimateProtocolVersionLen(getProtocolVersion()) + 1) + 3) + 1;
        String reasonPhrase = getReasonPhrase();
        if (reasonPhrase != null) {
            estimateProtocolVersionLen += reasonPhrase.length();
        }
        initBuffer.ensureCapacity(estimateProtocolVersionLen);
        BasicLineFormatter.appendProtocolVersion(initBuffer, getProtocolVersion());
        initBuffer.append(' ');
        initBuffer.append(Integer.toString(getStatusCode()));
        initBuffer.append(' ');
        if (reasonPhrase != null) {
            initBuffer.append(reasonPhrase);
        }
        return initBuffer.toString();
    }

    public final Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
