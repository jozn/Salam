package cz.msebera.android.httpclient;

import java.nio.charset.Charset;

public final class Consts {
    public static final Charset ASCII;
    public static final Charset ISO_8859_1;
    public static final Charset UTF_8;

    static {
        UTF_8 = Charset.forName("UTF-8");
        ASCII = Charset.forName("US-ASCII");
        ISO_8859_1 = Charset.forName("ISO-8859-1");
    }
}
