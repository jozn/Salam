package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

public final class DisplayImageOptions {
    final boolean cacheInMemory;
    final boolean cacheOnDisc;
    public final boolean considerExifParams;
    public final Options decodingOptions;
    final int delayBeforeLoading;
    final BitmapDisplayer displayer;
    public final Object extraForDownloader;
    final Handler handler;
    final Drawable imageForEmptyUri;
    final Drawable imageOnFail;
    final Drawable imageOnLoading;
    final int imageResForEmptyUri;
    final int imageResOnFail;
    final int imageResOnLoading;
    public final int imageScaleType$641b8ab2;
    final boolean isSyncLoading;
    final BitmapProcessor postProcessor;
    final BitmapProcessor preProcessor;
    final boolean resetViewBeforeLoading;

    public static class Builder {
        public boolean cacheInMemory;
        public boolean cacheOnDisc;
        boolean considerExifParams;
        Options decodingOptions;
        int delayBeforeLoading;
        public BitmapDisplayer displayer;
        Object extraForDownloader;
        Handler handler;
        Drawable imageForEmptyUri;
        Drawable imageOnFail;
        Drawable imageOnLoading;
        public int imageResForEmptyUri;
        public int imageResOnFail;
        public int imageResOnLoading;
        public int imageScaleType$641b8ab2;
        boolean isSyncLoading;
        BitmapProcessor postProcessor;
        BitmapProcessor preProcessor;
        boolean resetViewBeforeLoading;

        public Builder() {
            this.imageResOnLoading = 0;
            this.imageResForEmptyUri = 0;
            this.imageResOnFail = 0;
            this.imageOnLoading = null;
            this.imageForEmptyUri = null;
            this.imageOnFail = null;
            this.resetViewBeforeLoading = false;
            this.cacheInMemory = false;
            this.cacheOnDisc = false;
            this.imageScaleType$641b8ab2 = ImageScaleType.IN_SAMPLE_POWER_OF_2$641b8ab2;
            this.decodingOptions = new Options();
            this.delayBeforeLoading = 0;
            this.considerExifParams = false;
            this.extraForDownloader = null;
            this.preProcessor = null;
            this.postProcessor = null;
            this.displayer = new SimpleBitmapDisplayer();
            this.handler = null;
            this.isSyncLoading = false;
            this.decodingOptions.inPurgeable = true;
            this.decodingOptions.inInputShareable = true;
        }

        public final Builder bitmapConfig(Config bitmapConfig) {
            if (bitmapConfig == null) {
                throw new IllegalArgumentException("bitmapConfig can't be null");
            }
            this.decodingOptions.inPreferredConfig = bitmapConfig;
            return this;
        }

        public final DisplayImageOptions build() {
            return new DisplayImageOptions();
        }
    }

    private DisplayImageOptions(Builder builder) {
        this.imageResOnLoading = builder.imageResOnLoading;
        this.imageResForEmptyUri = builder.imageResForEmptyUri;
        this.imageResOnFail = builder.imageResOnFail;
        this.imageOnLoading = builder.imageOnLoading;
        this.imageForEmptyUri = builder.imageForEmptyUri;
        this.imageOnFail = builder.imageOnFail;
        this.resetViewBeforeLoading = builder.resetViewBeforeLoading;
        this.cacheInMemory = builder.cacheInMemory;
        this.cacheOnDisc = builder.cacheOnDisc;
        this.imageScaleType$641b8ab2 = builder.imageScaleType$641b8ab2;
        this.decodingOptions = builder.decodingOptions;
        this.delayBeforeLoading = builder.delayBeforeLoading;
        this.considerExifParams = builder.considerExifParams;
        this.extraForDownloader = builder.extraForDownloader;
        this.preProcessor = builder.preProcessor;
        this.postProcessor = builder.postProcessor;
        this.displayer = builder.displayer;
        this.handler = builder.handler;
        this.isSyncLoading = builder.isSyncLoading;
    }

    public final boolean shouldPostProcess() {
        return this.postProcessor != null;
    }

    public final Handler getHandler() {
        if (this.isSyncLoading) {
            return null;
        }
        if (this.handler != null) {
            return this.handler;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return new Handler();
        }
        throw new IllegalStateException("ImageLoader.displayImage(...) must be invoked from the main thread or from Looper thread");
    }
}
