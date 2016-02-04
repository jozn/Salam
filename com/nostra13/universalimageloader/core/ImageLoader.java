package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoaderEngine.C04911;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.utils.C0496L;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;

public class ImageLoader {
    public static final String TAG;
    private static volatile ImageLoader instance;
    private ImageLoaderConfiguration configuration;
    private final ImageLoadingListener emptyListener;
    private ImageLoaderEngine engine;

    static {
        TAG = ImageLoader.class.getSimpleName();
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    protected ImageLoader() {
        this.emptyListener = new SimpleImageLoadingListener();
    }

    public final void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        int i = 0;
        ImageAware imageViewAware = new ImageViewAware(imageView);
        if (this.configuration == null) {
            throw new IllegalStateException("ImageLoader must be init with configuration before using");
        }
        ImageLoadingListener imageLoadingListener = this.emptyListener;
        DisplayImageOptions displayImageOptions = options == null ? this.configuration.defaultDisplayImageOptions : options;
        if (TextUtils.isEmpty(uri)) {
            this.engine.cancelDisplayTaskFor(imageViewAware);
            imageViewAware.getWrappedView();
            if (!(displayImageOptions.imageForEmptyUri == null && displayImageOptions.imageResForEmptyUri == 0)) {
                i = 1;
            }
            if (i != 0) {
                imageViewAware.setImageDrawable(displayImageOptions.imageResForEmptyUri != 0 ? this.configuration.resources.getDrawable(displayImageOptions.imageResForEmptyUri) : displayImageOptions.imageForEmptyUri);
            } else {
                imageViewAware.setImageDrawable(null);
            }
            imageViewAware.getWrappedView();
            return;
        }
        ImageLoaderConfiguration imageLoaderConfiguration = this.configuration;
        DisplayMetrics displayMetrics = imageLoaderConfiguration.resources.getDisplayMetrics();
        int i2 = imageLoaderConfiguration.maxImageWidthForMemoryCache;
        if (i2 <= 0) {
            i2 = displayMetrics.widthPixels;
        }
        int i3 = imageLoaderConfiguration.maxImageHeightForMemoryCache;
        if (i3 <= 0) {
            i3 = displayMetrics.heightPixels;
        }
        ImageSize defineTargetSizeForView = ImageSizeUtils.defineTargetSizeForView(imageViewAware, new ImageSize(i2, i3));
        String str = "_" + defineTargetSizeForView.width + "x" + defineTargetSizeForView.height;
        this.engine.cacheKeysForImageAwares.put(Integer.valueOf(imageViewAware.getId()), str);
        imageViewAware.getWrappedView();
        Bitmap bitmap = (Bitmap) this.configuration.memoryCache.get$7713a341();
        if (bitmap == null || bitmap.isRecycled()) {
            i2 = (displayImageOptions.imageOnLoading == null && displayImageOptions.imageResOnLoading == 0) ? 0 : 1;
            if (i2 != 0) {
                imageViewAware.setImageDrawable(displayImageOptions.imageResOnLoading != 0 ? this.configuration.resources.getDrawable(displayImageOptions.imageResOnLoading) : displayImageOptions.imageOnLoading);
            } else if (displayImageOptions.resetViewBeforeLoading) {
                imageViewAware.setImageDrawable(null);
            }
            LoadAndDisplayImageTask loadAndDisplayImageTask = new LoadAndDisplayImageTask(this.engine, new ImageLoadingInfo(uri, imageViewAware, defineTargetSizeForView, str, displayImageOptions, imageLoadingListener, this.engine.getLockForUri(uri)), displayImageOptions.getHandler());
            if (displayImageOptions.isSyncLoading) {
                loadAndDisplayImageTask.run();
                return;
            }
            ImageLoaderEngine imageLoaderEngine = this.engine;
            imageLoaderEngine.taskDistributor.execute(new C04911(loadAndDisplayImageTask));
            return;
        }
        if (this.configuration.writeLogs) {
            C0496L.m29d("Load image from memory cache [%s]", str);
        }
        if (displayImageOptions.shouldPostProcess()) {
            Runnable processAndDisplayImageTask = new ProcessAndDisplayImageTask(this.engine, bitmap, new ImageLoadingInfo(uri, imageViewAware, defineTargetSizeForView, str, displayImageOptions, imageLoadingListener, this.engine.getLockForUri(uri)), displayImageOptions.getHandler());
            if (displayImageOptions.isSyncLoading) {
                processAndDisplayImageTask.run();
                return;
            }
            imageLoaderEngine = this.engine;
            imageLoaderEngine.initExecutorsIfNeed();
            imageLoaderEngine.taskExecutorForCachedImages.execute(processAndDisplayImageTask);
            return;
        }
        BitmapDisplayer bitmapDisplayer = displayImageOptions.displayer;
        LoadedFrom loadedFrom = LoadedFrom.MEMORY_CACHE;
        bitmapDisplayer.display$44f2d737(bitmap, imageViewAware);
        imageViewAware.getWrappedView();
    }
}
