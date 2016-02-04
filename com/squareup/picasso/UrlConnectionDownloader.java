package com.squareup.picasso;

import android.content.Context;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Build.VERSION;
import com.squareup.picasso.Downloader.Response;
import com.squareup.picasso.Downloader.ResponseException;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public final class UrlConnectionDownloader implements Downloader {
    private static final ThreadLocal<StringBuilder> CACHE_HEADER_BUILDER;
    static volatile Object cache;
    private static final Object lock;
    private final Context context;

    /* renamed from: com.squareup.picasso.UrlConnectionDownloader.1 */
    static class C12381 extends ThreadLocal<StringBuilder> {
        C12381() {
        }

        protected final /* bridge */ /* synthetic */ Object initialValue() {
            return new StringBuilder();
        }
    }

    static {
        lock = new Object();
        CACHE_HEADER_BUILDER = new C12381();
    }

    public UrlConnectionDownloader(Context context) {
        this.context = context.getApplicationContext();
    }

    public final Response load(Uri uri, int networkPolicy) throws IOException {
        if (VERSION.SDK_INT >= 14) {
            Context context = this.context;
            if (cache == null) {
                try {
                    synchronized (lock) {
                        if (cache == null) {
                            File createDefaultCacheDir = Utils.createDefaultCacheDir(context);
                            Object installed = HttpResponseCache.getInstalled();
                            if (installed == null) {
                                installed = HttpResponseCache.install(createDefaultCacheDir, Utils.calculateDiskCacheSize(createDefaultCacheDir));
                            }
                            cache = installed;
                        }
                    }
                } catch (IOException e) {
                }
            }
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(20000);
        connection.setUseCaches(true);
        if (networkPolicy != 0) {
            String headerValue;
            if (NetworkPolicy.isOfflineOnly(networkPolicy)) {
                headerValue = "only-if-cached,max-age=2147483647";
            } else {
                StringBuilder builder = (StringBuilder) CACHE_HEADER_BUILDER.get();
                builder.setLength(0);
                if (!NetworkPolicy.shouldReadFromDiskCache(networkPolicy)) {
                    builder.append("no-cache");
                }
                if (!NetworkPolicy.shouldWriteToDiskCache(networkPolicy)) {
                    if (builder.length() > 0) {
                        builder.append(',');
                    }
                    builder.append("no-store");
                }
                headerValue = builder.toString();
            }
            connection.setRequestProperty("Cache-Control", headerValue);
        }
        int responseCode = connection.getResponseCode();
        if (responseCode >= 300) {
            connection.disconnect();
            throw new ResponseException(responseCode + " " + connection.getResponseMessage(), networkPolicy, responseCode);
        }
        long contentLength = (long) connection.getHeaderFieldInt("Content-Length", -1);
        return new Response(connection.getInputStream(), Utils.parseResponseSourceHeader(connection.getHeaderField("X-Android-Response-Source")), contentLength);
    }

    public final void shutdown() {
        if (VERSION.SDK_INT >= 14 && cache != null) {
            try {
                ((HttpResponseCache) cache).close();
            } catch (IOException e) {
            }
        }
    }
}
