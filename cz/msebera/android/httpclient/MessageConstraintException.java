package cz.msebera.android.httpclient;

import java.nio.charset.CharacterCodingException;

public final class MessageConstraintException extends CharacterCodingException {
    private final String message;

    public MessageConstraintException(String message) {
        this.message = message;
    }

    public final String getMessage() {
        return this.message;
    }
}
