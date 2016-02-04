package cz.msebera.android.httpclient.auth;

import cz.msebera.android.httpclient.ProtocolException;

public final class MalformedChallengeException extends ProtocolException {
    public MalformedChallengeException(String message) {
        super(message);
    }
}
