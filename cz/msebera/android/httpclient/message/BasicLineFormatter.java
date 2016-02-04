package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.FormattedHeader;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.ProtocolVersion;
import cz.msebera.android.httpclient.RequestLine;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;

public final class BasicLineFormatter implements LineFormatter {
    @Deprecated
    public static final BasicLineFormatter DEFAULT;
    public static final BasicLineFormatter INSTANCE;

    static {
        DEFAULT = new BasicLineFormatter();
        INSTANCE = new BasicLineFormatter();
    }

    static CharArrayBuffer initBuffer(CharArrayBuffer charBuffer) {
        CharArrayBuffer buffer = charBuffer;
        if (charBuffer == null) {
            return new CharArrayBuffer(64);
        }
        buffer.len = 0;
        return buffer;
    }

    static CharArrayBuffer appendProtocolVersion(CharArrayBuffer buffer, ProtocolVersion version) {
        Args.notNull(version, "Protocol version");
        CharArrayBuffer result = buffer;
        int len = estimateProtocolVersionLen(version);
        if (result == null) {
            result = new CharArrayBuffer(len);
        } else {
            result.ensureCapacity(len);
        }
        result.append(version.getProtocol());
        result.append('/');
        result.append(Integer.toString(version.getMajor()));
        result.append('.');
        result.append(Integer.toString(version.getMinor()));
        return result;
    }

    static int estimateProtocolVersionLen(ProtocolVersion version) {
        return version.getProtocol().length() + 4;
    }

    public final CharArrayBuffer formatRequestLine(CharArrayBuffer buffer, RequestLine reqline) {
        Args.notNull(reqline, "Request line");
        CharArrayBuffer result = initBuffer(buffer);
        String method = reqline.getMethod();
        String uri = reqline.getUri();
        result.ensureCapacity((((method.length() + 1) + uri.length()) + 1) + estimateProtocolVersionLen(reqline.getProtocolVersion()));
        result.append(method);
        result.append(' ');
        result.append(uri);
        result.append(' ');
        appendProtocolVersion(result, reqline.getProtocolVersion());
        return result;
    }

    public final CharArrayBuffer formatHeader(CharArrayBuffer buffer, Header header) {
        Args.notNull(header, "Header");
        if (header instanceof FormattedHeader) {
            return ((FormattedHeader) header).getBuffer();
        }
        CharArrayBuffer result = initBuffer(buffer);
        String name = header.getName();
        String value = header.getValue();
        int length = name.length() + 2;
        if (value != null) {
            length += value.length();
        }
        result.ensureCapacity(length);
        result.append(name);
        result.append(": ");
        if (value == null) {
            return result;
        }
        result.append(value);
        return result;
    }
}
