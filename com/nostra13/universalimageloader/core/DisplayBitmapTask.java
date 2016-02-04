package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.utils.C0496L;

final class DisplayBitmapTask implements Runnable {
    private final Bitmap bitmap;
    private final BitmapDisplayer displayer;
    private final ImageLoaderEngine engine;
    private final ImageAware imageAware;
    private final String imageUri;
    private final ImageLoadingListener listener;
    private final LoadedFrom loadedFrom;
    boolean loggingEnabled;
    private final String memoryCacheKey;

    public DisplayBitmapTask(Bitmap bitmap, ImageLoadingInfo imageLoadingInfo, ImageLoaderEngine engine, LoadedFrom loadedFrom) {
        this.bitmap = bitmap;
        this.imageUri = imageLoadingInfo.uri;
        this.imageAware = imageLoadingInfo.imageAware;
        this.memoryCacheKey = imageLoadingInfo.memoryCacheKey;
        this.displayer = imageLoadingInfo.options.displayer;
        this.listener = imageLoadingInfo.listener;
        this.engine = engine;
        this.loadedFrom = loadedFrom;
    }

    public final void run() {
        if (this.imageAware.isCollected()) {
            if (this.loggingEnabled) {
                C0496L.m29d("ImageAware was collected by GC. Task is cancelled. [%s]", this.memoryCacheKey);
            }
            this.imageAware.getWrappedView();
            return;
        }
        int i;
        if (this.memoryCacheKey.equals(this.engine.getLoadingUriForView(this.imageAware))) {
            i = 0;
        } else {
            i = 1;
        }
        if (i != 0) {
            if (this.loggingEnabled) {
                C0496L.m29d("ImageAware is reused for another image. Task is cancelled. [%s]", this.memoryCacheKey);
            }
            this.imageAware.getWrappedView();
            return;
        }
        if (this.loggingEnabled) {
            C0496L.m29d("Display image in ImageAware (loaded from %1$s) [%2$s]", this.loadedFrom, this.memoryCacheKey);
        }
        this.displayer.display$44f2d737(this.bitmap, this.imageAware);
        this.imageAware.getWrappedView();
        this.engine.cancelDisplayTaskFor(this.imageAware);
    }
}
