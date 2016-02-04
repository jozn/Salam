package cz.msebera.android.httpclient.util;

import java.io.UnsupportedEncodingException;

public final class EncodingUtils {
    public static byte[] getBytes(String data, String charset) {
        Args.notNull(data, "Input");
        String str = "Charset";
        if (charset == null) {
            throw new IllegalArgumentException(str + " may not be null");
        } else if (TextUtils.isEmpty(charset)) {
            throw new IllegalArgumentException(str + " may not be empty");
        } else {
            try {
                return data.getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                return data.getBytes();
            }
        }
    }
}
