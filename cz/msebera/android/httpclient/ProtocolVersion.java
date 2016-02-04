package cz.msebera.android.httpclient;

import cz.msebera.android.httpclient.util.Args;
import java.io.Serializable;

public class ProtocolVersion implements Serializable, Cloneable {
    protected final int major;
    protected final int minor;
    protected final String protocol;

    public ProtocolVersion(String protocol, int major, int minor) {
        this.protocol = (String) Args.notNull(protocol, "Protocol name");
        this.major = Args.notNegative(major, "Protocol minor version");
        this.minor = Args.notNegative(minor, "Protocol minor version");
    }

    public final String getProtocol() {
        return this.protocol;
    }

    public final int getMajor() {
        return this.major;
    }

    public final int getMinor() {
        return this.minor;
    }

    public ProtocolVersion forVersion(int major, int minor) {
        return (major == this.major && minor == this.minor) ? this : new ProtocolVersion(this.protocol, major, minor);
    }

    public final int hashCode() {
        return (this.protocol.hashCode() ^ (this.major * 100000)) ^ this.minor;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ProtocolVersion)) {
            return false;
        }
        ProtocolVersion that = (ProtocolVersion) obj;
        if (this.protocol.equals(that.protocol) && this.major == that.major && this.minor == that.minor) {
            return true;
        }
        return false;
    }

    public final boolean lessEquals(ProtocolVersion version) {
        boolean z = version != null && this.protocol.equals(version.protocol);
        if (z) {
            Args.notNull(version, "Protocol version");
            String str = "Versions for different protocols cannot be compared: %s %s";
            Object[] objArr = new Object[]{this, version};
            if (this.protocol.equals(version.protocol)) {
                int i = this.major - version.major;
                if (i == 0) {
                    i = this.minor - version.minor;
                }
                if (i <= 0) {
                    return true;
                }
            }
            throw new IllegalArgumentException(String.format(str, objArr));
        }
        return false;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.protocol);
        buffer.append('/');
        buffer.append(Integer.toString(this.major));
        buffer.append('.');
        buffer.append(Integer.toString(this.minor));
        return buffer.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
