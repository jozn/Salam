package com.squareup.picasso;

import android.graphics.Bitmap;
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;

public interface Downloader {

    public static class Response {
        final Bitmap bitmap;
        final boolean cached;
        final long contentLength;
        final InputStream stream;

        public Response(InputStream stream, boolean loadedFromCache, long contentLength) {
            if (stream == null) {
                throw new IllegalArgumentException("Stream may not be null.");
            }
            this.stream = stream;
            this.bitmap = null;
            this.cached = loadedFromCache;
            this.contentLength = contentLength;
        }
    }

    public static class ResponseException extends IOException {
        final boolean localCacheOnly;
        final int responseCode;

        public ResponseException(String message, int networkPolicy, int responseCode) {
            super(message);
            this.localCacheOnly = NetworkPolicy.isOfflineOnly(networkPolicy);
            this.responseCode = responseCode;
        }
    }

    Response load(Uri uri, int i) throws IOException;

    void shutdown();
}
