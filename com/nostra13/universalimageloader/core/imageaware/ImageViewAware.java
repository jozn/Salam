package com.nostra13.universalimageloader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.utils.C0496L;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public final class ImageViewAware implements ImageAware {
    protected boolean checkActualViewSize;
    protected Reference<ImageView> imageViewRef;

    public final /* bridge */ /* synthetic */ View getWrappedView() {
        return (ImageView) this.imageViewRef.get();
    }

    public ImageViewAware(ImageView imageView) {
        this(imageView, (byte) 0);
    }

    private ImageViewAware(ImageView imageView, byte b) {
        this.imageViewRef = new WeakReference(imageView);
        this.checkActualViewSize = true;
    }

    public final int getWidth() {
        ImageView imageView = (ImageView) this.imageViewRef.get();
        if (imageView == null) {
            return 0;
        }
        LayoutParams params = imageView.getLayoutParams();
        int width = 0;
        if (!(!this.checkActualViewSize || params == null || params.width == -2)) {
            width = imageView.getWidth();
        }
        if (width <= 0 && params != null) {
            width = params.width;
        }
        if (width <= 0) {
            return getImageViewFieldValue(imageView, "mMaxWidth");
        }
        return width;
    }

    public final int getHeight() {
        ImageView imageView = (ImageView) this.imageViewRef.get();
        if (imageView == null) {
            return 0;
        }
        LayoutParams params = imageView.getLayoutParams();
        int height = 0;
        if (!(!this.checkActualViewSize || params == null || params.height == -2)) {
            height = imageView.getHeight();
        }
        if (height <= 0 && params != null) {
            height = params.height;
        }
        if (height <= 0) {
            return getImageViewFieldValue(imageView, "mMaxHeight");
        }
        return height;
    }

    public final int getScaleType$7c19a5b() {
        ImageView imageView = (ImageView) this.imageViewRef.get();
        if (imageView != null) {
            return ViewScaleType.fromImageView$11a60ad5(imageView);
        }
        return 0;
    }

    public final boolean isCollected() {
        return this.imageViewRef.get() == null;
    }

    public final int getId() {
        ImageView imageView = (ImageView) this.imageViewRef.get();
        return imageView == null ? super.hashCode() : imageView.hashCode();
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = ((Integer) field.get(object)).intValue();
            if (fieldValue <= 0 || fieldValue >= ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) {
                return 0;
            }
            return fieldValue;
        } catch (Throwable e) {
            C0496L.m31e(e);
            return 0;
        }
    }

    public final boolean setImageDrawable(Drawable drawable) {
        ImageView imageView = (ImageView) this.imageViewRef.get();
        if (imageView == null) {
            return false;
        }
        imageView.setImageDrawable(drawable);
        return true;
    }

    public final boolean setImageBitmap(Bitmap bitmap) {
        ImageView imageView = (ImageView) this.imageViewRef.get();
        if (imageView == null) {
            return false;
        }
        imageView.setImageBitmap(bitmap);
        return true;
    }
}
