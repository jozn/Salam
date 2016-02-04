package com.nostra13.universalimageloader.utils;

import android.opengl.GLES10;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public final class ImageSizeUtils {
    private static ImageSize maxBitmapSize;

    static {
        int[] maxTextureSize = new int[1];
        GLES10.glGetIntegerv(3379, maxTextureSize, 0);
        int maxBitmapDimension = Math.max(maxTextureSize[0], AccessibilityNodeInfoCompat.ACTION_PREVIOUS_HTML_ELEMENT);
        maxBitmapSize = new ImageSize(maxBitmapDimension, maxBitmapDimension);
    }

    public static ImageSize defineTargetSizeForView(ImageAware imageAware, ImageSize maxImageSize) {
        int width = imageAware.getWidth();
        if (width <= 0) {
            width = maxImageSize.width;
        }
        int height = imageAware.getHeight();
        if (height <= 0) {
            height = maxImageSize.height;
        }
        return new ImageSize(width, height);
    }
}
