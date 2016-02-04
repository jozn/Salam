package org.jivesoftware.smack.util.stringencoder;

import java.io.UnsupportedEncodingException;

public final class Base64 {
    public static Encoder base64encoder;

    public interface Encoder {
        byte[] decode(String str);

        byte[] encode$7dcc7401(byte[] bArr, int i);
    }

    public static final String encode(String string) {
        try {
            return encodeToString(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported", e);
        }
    }

    public static final String encodeToString(byte[] input) {
        try {
            return new String(base64encoder.encode$7dcc7401(input, input.length), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static final byte[] decode(String string) {
        return base64encoder.decode(string);
    }
}
