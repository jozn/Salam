package cz.msebera.android.httpclient.auth;

import cz.msebera.android.httpclient.ProtocolException;

public class AuthenticationException extends ProtocolException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
