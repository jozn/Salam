package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.utils.C0496L;

final class ProcessAndDisplayImageTask implements Runnable {
    private final Bitmap bitmap;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    private final ImageLoadingInfo imageLoadingInfo;

    public ProcessAndDisplayImageTask(ImageLoaderEngine engine, Bitmap bitmap, ImageLoadingInfo imageLoadingInfo, Handler handler) {
        this.engine = engine;
        this.bitmap = bitmap;
        this.imageLoadingInfo = imageLoadingInfo;
        this.handler = handler;
    }

    public final void run() {
        if (this.engine.configuration.writeLogs) {
            C0496L.m29d("PostProcess image before displaying [%s]", this.imageLoadingInfo.memoryCacheKey);
        }
        DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(this.imageLoadingInfo.options.postProcessor.process$34dbf037(), this.imageLoadingInfo, this.engine, LoadedFrom.MEMORY_CACHE);
        displayBitmapTask.loggingEnabled = this.engine.configuration.writeLogs;
        if (this.imageLoadingInfo.options.isSyncLoading) {
            displayBitmapTask.run();
        } else {
            this.handler.post(displayBitmapTask);
        }
    }
}
