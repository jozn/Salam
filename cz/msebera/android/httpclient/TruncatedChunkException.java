package cz.msebera.android.httpclient;

public final class TruncatedChunkException extends MalformedChunkCodingException {
    public TruncatedChunkException(String message) {
        super(message);
    }
}
