package cz.msebera.android.httpclient.client;

public final class CircularRedirectException extends RedirectException {
    public CircularRedirectException(String message) {
        super(message);
    }
}
