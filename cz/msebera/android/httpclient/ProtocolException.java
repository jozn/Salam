package cz.msebera.android.httpclient;

public class ProtocolException extends HttpException {
    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }
}
