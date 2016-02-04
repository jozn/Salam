package cz.msebera.android.httpclient.client;

public final class HttpResponseException extends ClientProtocolException {
    private final int statusCode;

    public HttpResponseException(int statusCode, String s) {
        super(s);
        this.statusCode = statusCode;
    }
}
