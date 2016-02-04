package com.nostra13.universalimageloader.core.download;

import android.support.v7.appcompat.BuildConfig;
import java.io.IOException;
import java.io.InputStream;

public interface ImageDownloader {

    public enum Scheme {
        HTTP("http"),
        HTTPS("https"),
        FILE("file"),
        CONTENT("content"),
        ASSETS("assets"),
        DRAWABLE("drawable"),
        UNKNOWN(BuildConfig.VERSION_NAME);
        
        private String scheme;
        private String uriPrefix;

        private Scheme(String scheme) {
            this.scheme = scheme;
            this.uriPrefix = scheme + "://";
        }

        public final String wrap(String path) {
            return this.uriPrefix + path;
        }
    }

    InputStream getStream$3b7c2062() throws IOException;
}
