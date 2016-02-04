package cz.msebera.android.httpclient.util;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public final class CharsetUtils {
    public static Charset lookup(String name) {
        try {
            return Charset.forName(name);
        } catch (UnsupportedCharsetException e) {
            return null;
        }
    }
}
