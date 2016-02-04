package cz.msebera.android.httpclient.client;

import cz.msebera.android.httpclient.ProtocolException;

public final class NonRepeatableRequestException extends ProtocolException {
    public NonRepeatableRequestException(String message) {
        super(message);
    }

    public NonRepeatableRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
