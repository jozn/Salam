package cz.msebera.android.httpclient.conn;

import java.io.IOException;

public final class UnsupportedSchemeException extends IOException {
    public UnsupportedSchemeException(String message) {
        super(message);
    }
}
