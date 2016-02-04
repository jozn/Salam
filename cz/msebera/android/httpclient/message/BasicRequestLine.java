package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.RequestLine;
import cz.msebera.android.httpclient.util.Args;
import java.io.Serializable;

public final class BasicRequestLine implements RequestLine, Serializable, Cloneable {
    private final String method;
    private final ProtocolVersion protoversion;
    private final String uri;

    public BasicRequestLine(String method, String uri, ProtocolVersion version) {
        this.method = (String) Args.notNull(method, "Method");
        this.uri = (String) Args.notNull(uri, "URI");
        this.protoversion = (ProtocolVersion) Args.notNull(version, "Version");
    }

    public final String getMethod() {
        return this.method;
    }

    public final ProtocolVersion getProtocolVersion() {
        return this.protoversion;
    }

    public final String getUri() {
        return this.uri;
    }

    public final String toString() {
        return BasicLineFormatter.INSTANCE.formatRequestLine(null, this).toString();
    }

    public final Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
