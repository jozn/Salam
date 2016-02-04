package cz.msebera.android.httpclient.client;

import java.io.IOException;

public class ClientProtocolException extends IOException {
    public ClientProtocolException(String s) {
        super(s);
    }

    public ClientProtocolException(Throwable cause) {
        initCause(cause);
    }
}
