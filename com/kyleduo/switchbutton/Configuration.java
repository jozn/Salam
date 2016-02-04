package com.kyleduo.switchbutton;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public final class Configuration implements Cloneable {
    private float density;
    Rect mInsetBounds;
    float mMeasureFactor;
    private int mOffColor;
    Drawable mOffDrawable;
    private int mOnColor;
    Drawable mOnDrawable;
    float mRadius;
    private int mThumbColor;
    Drawable mThumbDrawable;
    int mThumbHeight;
    int mThumbMarginBottom;
    int mThumbMarginLeft;
    int mThumbMarginRight;
    int mThumbMarginTop;
    private int mThumbPressedColor;
    int mThumbWidth;
    private int mVelocity;

    static class Default {
        static int DEFAULT_INNER_BOUNDS;
        static float DEFAULT_MEASURE_FACTOR;
        static int DEFAULT_OFF_COLOR;
        static int DEFAULT_ON_COLOR;
        static int DEFAULT_RADIUS;
        static int DEFAULT_THUMB_COLOR;
        static int DEFAULT_THUMB_MARGIN;
        static int DEFAULT_THUMB_PRESSED_COLOR;

        static {
            DEFAULT_OFF_COLOR = Color.parseColor("#E3E3E3");
            DEFAULT_ON_COLOR = Color.parseColor("#02BFE7");
            DEFAULT_THUMB_COLOR = Color.parseColor("#FFFFFF");
            DEFAULT_THUMB_PRESSED_COLOR = Color.parseColor("#fafafa");
            DEFAULT_THUMB_MARGIN = 2;
            DEFAULT_RADIUS = 999;
            DEFAULT_MEASURE_FACTOR = 2.0f;
            DEFAULT_INNER_BOUNDS = 0;
        }
    }

    static class Limit {
        static int MIN_THUMB_SIZE;

        static {
            MIN_THUMB_SIZE = 24;
        }
    }

    private Configuration() {
        this.mOnDrawable = null;
        this.mOffDrawable = null;
        this.mThumbDrawable = null;
        this.mOnColor = Default.DEFAULT_ON_COLOR;
        this.mOffColor = Default.DEFAULT_OFF_COLOR;
        this.mThumbColor = Default.DEFAULT_THUMB_COLOR;
        this.mThumbPressedColor = Default.DEFAULT_THUMB_PRESSED_COLOR;
        this.mThumbMarginTop = 0;
        this.mThumbMarginBottom = 0;
        this.mThumbMarginLeft = 0;
        this.mThumbMarginRight = 0;
        this.mThumbWidth = -1;
        this.mThumbHeight = -1;
        this.mVelocity = -1;
        this.mRadius = -1.0f;
        this.mMeasureFactor = 0.0f;
    }

    public static Configuration getDefault(float density) {
        Configuration defaultConfiguration = new Configuration();
        defaultConfiguration.density = density;
        defaultConfiguration.setThumbMarginInPixel(defaultConfiguration.getDefaultThumbMarginInPixel());
        defaultConfiguration.mInsetBounds = new Rect(Default.DEFAULT_INNER_BOUNDS, Default.DEFAULT_INNER_BOUNDS, Default.DEFAULT_INNER_BOUNDS, Default.DEFAULT_INNER_BOUNDS);
        return defaultConfiguration;
    }

    public final void setThumbMarginInPixel(int top, int bottom, int left, int right) {
        this.mThumbMarginTop = top;
        this.mThumbMarginBottom = bottom;
        this.mThumbMarginLeft = left;
        this.mThumbMarginRight = right;
    }

    public final void setThumbMarginInPixel(int marginInPixel) {
        setThumbMarginInPixel(marginInPixel, marginInPixel, marginInPixel, marginInPixel);
    }

    public final int getDefaultThumbMarginInPixel() {
        return (int) (((float) Default.DEFAULT_THUMB_MARGIN) * this.density);
    }

    public final float getRadius() {
        if (this.mRadius < 0.0f) {
            return (float) Default.DEFAULT_RADIUS;
        }
        return this.mRadius;
    }

    public final int getShrinkX() {
        return this.mInsetBounds.left + this.mInsetBounds.right;
    }

    public final int getShrinkY() {
        return this.mInsetBounds.top + this.mInsetBounds.bottom;
    }

    public final boolean needShrink() {
        return ((this.mInsetBounds.left + this.mInsetBounds.right) + this.mInsetBounds.top) + this.mInsetBounds.bottom != 0;
    }

    final int getThumbWidth() {
        int width = this.mThumbWidth;
        if (width < 0) {
            if (this.mThumbDrawable != null) {
                width = this.mThumbDrawable.getIntrinsicWidth();
                if (width > 0) {
                    return width;
                }
            }
            if (this.density <= 0.0f) {
                throw new IllegalArgumentException("density must be a positive number");
            }
            width = (int) (((float) Limit.MIN_THUMB_SIZE) * this.density);
        }
        return width;
    }

    final int getThumbHeight() {
        int height = this.mThumbHeight;
        if (height < 0) {
            if (this.mThumbDrawable != null) {
                height = this.mThumbDrawable.getIntrinsicHeight();
                if (height > 0) {
                    return height;
                }
            }
            if (this.density <= 0.0f) {
                throw new IllegalArgumentException("density must be a positive number");
            }
            height = (int) (((float) Limit.MIN_THUMB_SIZE) * this.density);
        }
        return height;
    }
}
