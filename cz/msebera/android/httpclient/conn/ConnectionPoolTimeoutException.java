package cz.msebera.android.httpclient.conn;

public final class ConnectionPoolTimeoutException extends ConnectTimeoutException {
    public ConnectionPoolTimeoutException(String message) {
        super(message);
    }
}
