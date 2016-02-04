package org.jivesoftware.smack.util.stringencoder.android;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import org.jivesoftware.smack.util.stringencoder.StringEncoder;

public final class AndroidBase64UrlSafeEncoder implements StringEncoder {
    private static AndroidBase64UrlSafeEncoder instance;

    static {
        instance = new AndroidBase64UrlSafeEncoder();
    }

    private AndroidBase64UrlSafeEncoder() {
    }

    public static AndroidBase64UrlSafeEncoder getInstance() {
        return instance;
    }

    public final String encode(String string) {
        try {
            return Base64.encodeToString(string.getBytes("UTF-8"), 10);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported", e);
        }
    }
}
