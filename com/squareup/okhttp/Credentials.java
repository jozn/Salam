package com.squareup.okhttp;

import java.io.UnsupportedEncodingException;
import okio.Base64;
import okio.ByteString;

public final class Credentials {
    private Credentials() {
    }

    public static String basic(String userName, String password) {
        try {
            return "Basic " + Base64.encode(ByteString.of((userName + ":" + password).getBytes("ISO-8859-1")).data);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError();
        }
    }
}
