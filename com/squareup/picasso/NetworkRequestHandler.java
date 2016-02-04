package com.squareup.picasso;

import android.graphics.Bitmap;
import android.net.NetworkInfo;
import com.squareup.picasso.Downloader.Response;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.RequestHandler.Result;
import java.io.IOException;
import java.io.InputStream;

final class NetworkRequestHandler extends RequestHandler {
    private final Downloader downloader;
    private final Stats stats;

    static class ContentLengthException extends IOException {
        public ContentLengthException(String message) {
            super(message);
        }
    }

    public NetworkRequestHandler(Downloader downloader, Stats stats) {
        this.downloader = downloader;
        this.stats = stats;
    }

    public final boolean canHandleRequest(Request data) {
        String scheme = data.uri.getScheme();
        return "http".equals(scheme) || "https".equals(scheme);
    }

    public final Result load$71fa0c91(Request request) throws IOException {
        Response response = this.downloader.load(request.uri, request.networkPolicy);
        LoadedFrom loadedFrom = response.cached ? LoadedFrom.DISK : LoadedFrom.NETWORK;
        Bitmap bitmap = response.bitmap;
        if (bitmap != null) {
            return new Result(bitmap, loadedFrom);
        }
        InputStream is = response.stream;
        if (is == null) {
            return null;
        }
        if (loadedFrom == LoadedFrom.DISK && response.contentLength == 0) {
            Utils.closeQuietly(is);
            throw new ContentLengthException("Received response with 0 content-length header.");
        }
        if (loadedFrom == LoadedFrom.NETWORK && response.contentLength > 0) {
            Stats stats = this.stats;
            stats.handler.sendMessage(stats.handler.obtainMessage(4, Long.valueOf(response.contentLength)));
        }
        return new Result(is, loadedFrom);
    }

    final int getRetryCount() {
        return 2;
    }

    final boolean shouldRetry$552f0f64(NetworkInfo info) {
        return info == null || info.isConnected();
    }

    final boolean supportsReplay() {
        return true;
    }
}
