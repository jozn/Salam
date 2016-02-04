package cz.msebera.android.httpclient.protocol;

import cz.msebera.android.httpclient.Consts;
import java.nio.charset.Charset;

public final class HTTP {
    public static final Charset DEF_CONTENT_CHARSET;
    public static final Charset DEF_PROTOCOL_CHARSET;

    static {
        DEF_CONTENT_CHARSET = Consts.ISO_8859_1;
        DEF_PROTOCOL_CHARSET = Consts.ASCII;
    }

    public static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }
}
