package cz.msebera.android.httpclient.cookie;

public final class CookieRestrictionViolationException extends MalformedCookieException {
    public CookieRestrictionViolationException(String message) {
        super(message);
    }
}
