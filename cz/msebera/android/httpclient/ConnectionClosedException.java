package cz.msebera.android.httpclient;

import java.io.IOException;

public final class ConnectionClosedException extends IOException {
    public ConnectionClosedException(String message) {
        super(message);
    }
}
