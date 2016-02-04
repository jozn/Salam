package cz.msebera.android.httpclient.impl.auth;

import cz.msebera.android.httpclient.auth.AuthenticationException;

public final class NTLMEngineException extends AuthenticationException {
    public NTLMEngineException(String message) {
        super(message);
    }

    public NTLMEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
