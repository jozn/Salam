package com.nostra13.universalimageloader.core.decode;

import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

public final class ImageDecodingInfo {
    private final boolean considerExifParams;
    private final Options decodingOptions;
    private final ImageDownloader downloader;
    private final Object extraForDownloader;
    private final String imageKey;
    private final int imageScaleType$641b8ab2;
    private final String imageUri;
    private final ImageSize targetSize;
    private final int viewScaleType$3b550fbc;

    public ImageDecodingInfo(String imageKey, String imageUri, ImageSize targetSize, int viewScaleType, ImageDownloader downloader, DisplayImageOptions displayOptions) {
        this.imageKey = imageKey;
        this.imageUri = imageUri;
        this.targetSize = targetSize;
        this.imageScaleType$641b8ab2 = displayOptions.imageScaleType$641b8ab2;
        this.viewScaleType$3b550fbc = viewScaleType;
        this.downloader = downloader;
        this.extraForDownloader = displayOptions.extraForDownloader;
        this.considerExifParams = displayOptions.considerExifParams;
        this.decodingOptions = new Options();
        Options options = displayOptions.decodingOptions;
        Options options2 = this.decodingOptions;
        options2.inDensity = options.inDensity;
        options2.inDither = options.inDither;
        options2.inInputShareable = options.inInputShareable;
        options2.inJustDecodeBounds = options.inJustDecodeBounds;
        options2.inPreferredConfig = options.inPreferredConfig;
        options2.inPurgeable = options.inPurgeable;
        options2.inSampleSize = options.inSampleSize;
        options2.inScaled = options.inScaled;
        options2.inScreenDensity = options.inScreenDensity;
        options2.inTargetDensity = options.inTargetDensity;
        options2.inTempStorage = options.inTempStorage;
        if (VERSION.SDK_INT >= 10) {
            options2.inPreferQualityOverSpeed = options.inPreferQualityOverSpeed;
        }
        if (VERSION.SDK_INT >= 11) {
            options2.inBitmap = options.inBitmap;
            options2.inMutable = options.inMutable;
        }
    }
}
