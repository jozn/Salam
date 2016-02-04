package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.FailReason.FailType;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.utils.C0496L;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.nostra13.universalimageloader.utils.IoUtils.CopyListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

final class LoadAndDisplayImageTask implements CopyListener, Runnable {
    final ImageLoaderConfiguration configuration;
    private final ImageDecoder decoder;
    private final ImageDownloader downloader;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    final ImageAware imageAware;
    private final ImageLoadingInfo imageLoadingInfo;
    final ImageLoadingListener listener;
    private LoadedFrom loadedFrom;
    private final String memoryCacheKey;
    private final ImageDownloader networkDeniedDownloader;
    final DisplayImageOptions options;
    final ImageLoadingProgressListener progressListener;
    private final ImageDownloader slowNetworkDownloader;
    private final ImageSize targetSize;
    final String uri;
    private final boolean writeLogs;

    /* renamed from: com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.1 */
    class C04921 implements Runnable {
        final /* synthetic */ int val$current;
        final /* synthetic */ int val$total;

        C04921(int i, int i2) {
            this.val$current = i;
            this.val$total = i2;
        }

        public final void run() {
            LoadAndDisplayImageTask.this.imageAware.getWrappedView();
        }
    }

    /* renamed from: com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.2 */
    class C04932 implements Runnable {
        final /* synthetic */ Throwable val$failCause;
        final /* synthetic */ int val$failType$5b0e695f;

        C04932(int i, Throwable th) {
            this.val$failType$5b0e695f = i;
            this.val$failCause = th;
        }

        public final void run() {
            DisplayImageOptions displayImageOptions = LoadAndDisplayImageTask.this.options;
            Object obj = (displayImageOptions.imageOnFail == null && displayImageOptions.imageResOnFail == 0) ? null : 1;
            if (obj != null) {
                ImageAware imageAware = LoadAndDisplayImageTask.this.imageAware;
                displayImageOptions = LoadAndDisplayImageTask.this.options;
                imageAware.setImageDrawable(displayImageOptions.imageResOnFail != 0 ? LoadAndDisplayImageTask.this.configuration.resources.getDrawable(displayImageOptions.imageResOnFail) : displayImageOptions.imageOnFail);
            }
            LoadAndDisplayImageTask.this.imageAware.getWrappedView();
            FailReason failReason = new FailReason(this.val$failType$5b0e695f, this.val$failCause);
        }
    }

    /* renamed from: com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.3 */
    class C04943 implements Runnable {
        C04943() {
        }

        public final void run() {
            LoadAndDisplayImageTask.this.imageAware.getWrappedView();
        }
    }

    class TaskCancelledException extends Exception {
        TaskCancelledException() {
        }
    }

    public LoadAndDisplayImageTask(ImageLoaderEngine engine, ImageLoadingInfo imageLoadingInfo, Handler handler) {
        this.loadedFrom = LoadedFrom.NETWORK;
        this.engine = engine;
        this.imageLoadingInfo = imageLoadingInfo;
        this.handler = handler;
        this.configuration = engine.configuration;
        this.downloader = this.configuration.downloader;
        this.networkDeniedDownloader = this.configuration.networkDeniedDownloader;
        this.slowNetworkDownloader = this.configuration.slowNetworkDownloader;
        this.decoder = this.configuration.decoder;
        this.writeLogs = this.configuration.writeLogs;
        this.uri = imageLoadingInfo.uri;
        this.memoryCacheKey = imageLoadingInfo.memoryCacheKey;
        this.imageAware = imageLoadingInfo.imageAware;
        this.targetSize = imageLoadingInfo.targetSize;
        this.options = imageLoadingInfo.options;
        this.listener = imageLoadingInfo.listener;
        this.progressListener = imageLoadingInfo.progressListener;
    }

    public final void run() {
        Object obj = 1;
        if (!waitIfPaused() && !delayIfNeed()) {
            ReentrantLock loadFromUriLock = this.imageLoadingInfo.loadFromUriLock;
            log("Start display image task [%s]");
            if (loadFromUriLock.isLocked()) {
                log("Image already is loading. Waiting... [%s]");
            }
            loadFromUriLock.lock();
            try {
                checkTaskNotActual();
                Bitmap bmp = (Bitmap) this.configuration.memoryCache.get$7713a341();
                if (bmp == null) {
                    bmp = tryLoadBitmap();
                    if (bmp == null) {
                        loadFromUriLock.unlock();
                        return;
                    }
                    checkTaskNotActual();
                    checkTaskInterrupted();
                    if (this.options.preProcessor == null) {
                        obj = null;
                    }
                    if (obj != null) {
                        log("PreProcess image before caching in memory [%s]");
                        bmp = this.options.preProcessor.process$34dbf037();
                        if (bmp == null) {
                            C0496L.m30e("Pre-processor returned null [%s]", this.memoryCacheKey);
                        }
                    }
                    if (bmp != null && this.options.cacheInMemory) {
                        log("Cache image in memory [%s]");
                    }
                } else {
                    this.loadedFrom = LoadedFrom.MEMORY_CACHE;
                    log("...Get cached bitmap from memory after waiting. [%s]");
                }
                if (bmp != null && this.options.shouldPostProcess()) {
                    log("PostProcess image before displaying [%s]");
                    bmp = this.options.postProcessor.process$34dbf037();
                    if (bmp == null) {
                        C0496L.m30e("Post-processor returned null [%s]", this.memoryCacheKey);
                    }
                }
                checkTaskNotActual();
                checkTaskInterrupted();
                loadFromUriLock.unlock();
                DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bmp, this.imageLoadingInfo, this.engine, this.loadedFrom);
                displayBitmapTask.loggingEnabled = this.writeLogs;
                if (this.options.isSyncLoading) {
                    displayBitmapTask.run();
                } else {
                    this.handler.post(displayBitmapTask);
                }
            } catch (TaskCancelledException e) {
                if (!(this.options.isSyncLoading || isTaskInterrupted())) {
                    this.handler.post(new C04943());
                }
                loadFromUriLock.unlock();
            } catch (Throwable th) {
                loadFromUriLock.unlock();
            }
        }
    }

    private boolean waitIfPaused() {
        AtomicBoolean pause = this.engine.paused;
        if (pause.get()) {
            synchronized (this.engine.pauseLock) {
                if (pause.get()) {
                    log("ImageLoader is paused. Waiting...  [%s]");
                    try {
                        this.engine.pauseLock.wait();
                        log(".. Resume loading [%s]");
                    } catch (InterruptedException e) {
                        C0496L.m30e("Task was interrupted [%s]", this.memoryCacheKey);
                        return true;
                    }
                }
            }
        }
        return isTaskNotActual();
    }

    private boolean delayIfNeed() {
        if (!(this.options.delayBeforeLoading > 0)) {
            return false;
        }
        String str = "Delay %d ms before loading...  [%s]";
        Object[] objArr = new Object[]{Integer.valueOf(this.options.delayBeforeLoading), this.memoryCacheKey};
        if (this.writeLogs) {
            C0496L.m29d(str, objArr);
        }
        try {
            Thread.sleep((long) this.options.delayBeforeLoading);
            return isTaskNotActual();
        } catch (InterruptedException e) {
            C0496L.m30e("Task was interrupted [%s]", this.memoryCacheKey);
            return true;
        }
    }

    private Bitmap tryLoadBitmap() throws TaskCancelledException {
        File imageFile = this.configuration.discCache.get$3b83896e();
        File parentFile = imageFile.getParentFile();
        if (parentFile == null || !(parentFile.exists() || parentFile.mkdirs())) {
            imageFile = this.configuration.reserveDiscCache.get$3b83896e();
            parentFile = imageFile.getParentFile();
            if (!(parentFile == null || parentFile.exists())) {
                parentFile.mkdirs();
            }
        }
        Bitmap bitmap = null;
        try {
            String cacheFileUri = Scheme.FILE.wrap(imageFile.getAbsolutePath());
            if (imageFile.exists()) {
                log("Load image from disc cache [%s]");
                this.loadedFrom = LoadedFrom.DISC_CACHE;
                checkTaskNotActual();
                bitmap = decodeImage(cacheFileUri);
            }
            if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
                log("Load image from network [%s]");
                this.loadedFrom = LoadedFrom.NETWORK;
                String imageUriForDecoding = (this.options.cacheOnDisc && tryCacheImageOnDisc(imageFile)) ? cacheFileUri : this.uri;
                checkTaskNotActual();
                bitmap = decodeImage(imageUriForDecoding);
                if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
                    fireFailEvent$1f63ecd1(FailType.DECODING_ERROR$5b0e695f, null);
                }
            }
        } catch (IllegalStateException e) {
            fireFailEvent$1f63ecd1(FailType.NETWORK_DENIED$5b0e695f, null);
        } catch (TaskCancelledException e2) {
            throw e2;
        } catch (IOException e3) {
            C0496L.m31e(e3);
            fireFailEvent$1f63ecd1(FailType.IO_ERROR$5b0e695f, e3);
            if (imageFile.exists()) {
                imageFile.delete();
            }
        } catch (OutOfMemoryError e4) {
            C0496L.m31e(e4);
            fireFailEvent$1f63ecd1(FailType.OUT_OF_MEMORY$5b0e695f, e4);
        } catch (Throwable e5) {
            C0496L.m31e(e5);
            fireFailEvent$1f63ecd1(FailType.UNKNOWN$5b0e695f, e5);
        }
        return bitmap;
    }

    private Bitmap decodeImage(String imageUri) throws IOException {
        String str = imageUri;
        ImageDecodingInfo imageDecodingInfo = new ImageDecodingInfo(this.memoryCacheKey, str, this.targetSize, this.imageAware.getScaleType$7c19a5b(), getDownloader(), this.options);
        return this.decoder.decode$75d0b8bb();
    }

    private boolean tryCacheImageOnDisc(File targetFile) throws TaskCancelledException {
        log("Cache image on disc [%s]");
        boolean loaded = false;
        try {
            loaded = downloadImage(targetFile);
            if (loaded) {
                int width = this.configuration.maxImageWidthForDiscCache;
                int height = this.configuration.maxImageHeightForDiscCache;
                if (width > 0 || height > 0) {
                    log("Resize image in disc cache [%s]");
                    loaded = resizeAndSaveImage(targetFile, width, height);
                }
            }
        } catch (Throwable e) {
            C0496L.m31e(e);
            if (targetFile.exists()) {
                targetFile.delete();
            }
        }
        return loaded;
    }

    private boolean downloadImage(File targetFile) throws IOException {
        OutputStream os;
        InputStream is = getDownloader().getStream$3b7c2062();
        try {
            os = new BufferedOutputStream(new FileOutputStream(targetFile), AccessibilityNodeInfoCompat.ACTION_PASTE);
            boolean loaded = IoUtils.copyStream$6bf5717a(is, os, this);
            IoUtils.closeSilently(is);
            return loaded;
        } catch (Throwable th) {
        } finally {
            IoUtils.closeSilently(os);
        }
    }

    private boolean resizeAndSaveImage(File targetFile, int maxWidth, int maxHeight) throws IOException {
        ImageSize targetImageSize = new ImageSize(maxWidth, maxHeight);
        Builder builder = new Builder();
        DisplayImageOptions displayImageOptions = this.options;
        builder.imageResOnLoading = displayImageOptions.imageResOnLoading;
        builder.imageResForEmptyUri = displayImageOptions.imageResForEmptyUri;
        builder.imageResOnFail = displayImageOptions.imageResOnFail;
        builder.imageOnLoading = displayImageOptions.imageOnLoading;
        builder.imageForEmptyUri = displayImageOptions.imageForEmptyUri;
        builder.imageOnFail = displayImageOptions.imageOnFail;
        builder.resetViewBeforeLoading = displayImageOptions.resetViewBeforeLoading;
        builder.cacheInMemory = displayImageOptions.cacheInMemory;
        builder.cacheOnDisc = displayImageOptions.cacheOnDisc;
        builder.imageScaleType$641b8ab2 = displayImageOptions.imageScaleType$641b8ab2;
        builder.decodingOptions = displayImageOptions.decodingOptions;
        builder.delayBeforeLoading = displayImageOptions.delayBeforeLoading;
        builder.considerExifParams = displayImageOptions.considerExifParams;
        builder.extraForDownloader = displayImageOptions.extraForDownloader;
        builder.preProcessor = displayImageOptions.preProcessor;
        builder.postProcessor = displayImageOptions.postProcessor;
        builder.displayer = displayImageOptions.displayer;
        builder.handler = displayImageOptions.handler;
        builder.isSyncLoading = displayImageOptions.isSyncLoading;
        builder.imageScaleType$641b8ab2 = ImageScaleType.IN_SAMPLE_INT$641b8ab2;
        ImageDecodingInfo imageDecodingInfo = new ImageDecodingInfo(this.memoryCacheKey, Scheme.FILE.wrap(targetFile.getAbsolutePath()), targetImageSize, ViewScaleType.FIT_INSIDE$3b550fbc, getDownloader(), builder.build());
        Bitmap bmp = this.decoder.decode$75d0b8bb();
        if (!(bmp == null || this.configuration.processorForDiscCache == null)) {
            log("Process image before cache on disc [%s]");
            bmp = this.configuration.processorForDiscCache.process$34dbf037();
            if (bmp == null) {
                C0496L.m30e("Bitmap processor for disc cache returned null [%s]", this.memoryCacheKey);
            }
        }
        if (bmp != null) {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile), AccessibilityNodeInfoCompat.ACTION_PASTE);
            try {
                bmp.compress(this.configuration.imageCompressFormatForDiscCache, this.configuration.imageQualityForDiscCache, os);
                bmp.recycle();
            } finally {
                IoUtils.closeSilently(os);
            }
        }
        return true;
    }

    public final boolean onBytesCopied(int current, int total) {
        if (this.progressListener != null) {
            boolean z;
            if (this.options.isSyncLoading || isTaskInterrupted() || isTaskNotActual()) {
                z = false;
            } else {
                this.handler.post(new C04921(current, total));
                z = true;
            }
            if (!z) {
                return false;
            }
        }
        return true;
    }

    private void fireFailEvent$1f63ecd1(int failType, Throwable failCause) {
        if (!this.options.isSyncLoading && !isTaskInterrupted() && !isTaskNotActual()) {
            this.handler.post(new C04932(failType, failCause));
        }
    }

    private ImageDownloader getDownloader() {
        if (this.engine.networkDenied.get()) {
            return this.networkDeniedDownloader;
        }
        if (this.engine.slowNetwork.get()) {
            return this.slowNetworkDownloader;
        }
        return this.downloader;
    }

    private void checkTaskNotActual() throws TaskCancelledException {
        if (isViewCollected()) {
            throw new TaskCancelledException();
        } else if (isViewReused()) {
            throw new TaskCancelledException();
        }
    }

    private boolean isTaskNotActual() {
        return isViewCollected() || isViewReused();
    }

    private boolean isViewCollected() {
        if (!this.imageAware.isCollected()) {
            return false;
        }
        log("ImageAware was collected by GC. Task is cancelled. [%s]");
        return true;
    }

    private boolean isViewReused() {
        if (!(!this.memoryCacheKey.equals(this.engine.getLoadingUriForView(this.imageAware)))) {
            return false;
        }
        log("ImageAware is reused for another image. Task is cancelled. [%s]");
        return true;
    }

    private void checkTaskInterrupted() throws TaskCancelledException {
        if (isTaskInterrupted()) {
            throw new TaskCancelledException();
        }
    }

    private boolean isTaskInterrupted() {
        if (!Thread.interrupted()) {
            return false;
        }
        log("Task was interrupted [%s]");
        return true;
    }

    private void log(String message) {
        if (this.writeLogs) {
            C0496L.m29d(message, this.memoryCacheKey);
        }
    }
}
