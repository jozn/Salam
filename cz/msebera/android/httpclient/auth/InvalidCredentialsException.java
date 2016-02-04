package cz.msebera.android.httpclient.auth;

public final class InvalidCredentialsException extends AuthenticationException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
