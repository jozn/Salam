package org.jivesoftware.smack.util.stringencoder.android;

import android.util.Base64;
import org.jivesoftware.smack.util.stringencoder.Base64.Encoder;

public final class AndroidBase64Encoder implements Encoder {
    private static AndroidBase64Encoder instance;

    static {
        instance = new AndroidBase64Encoder();
    }

    private AndroidBase64Encoder() {
    }

    public static AndroidBase64Encoder getInstance() {
        return instance;
    }

    public final byte[] decode(String string) {
        return Base64.decode(string, 0);
    }

    public final byte[] encode$7dcc7401(byte[] input, int len) {
        return Base64.encode(input, 0, len, 2);
    }
}
