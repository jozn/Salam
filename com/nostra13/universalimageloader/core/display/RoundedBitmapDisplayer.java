package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

public final class RoundedBitmapDisplayer implements BitmapDisplayer {
    protected final int cornerRadius;
    protected final int margin;

    protected static class RoundedDrawable extends Drawable {
        protected final BitmapShader bitmapShader;
        protected final float cornerRadius;
        protected final RectF mRect;
        protected final int margin;
        protected final Paint paint;

        RoundedDrawable(Bitmap bitmap, int cornerRadius, int margin) {
            this.mRect = new RectF();
            this.cornerRadius = (float) cornerRadius;
            this.margin = margin;
            this.bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
            this.paint = new Paint();
            this.paint.setAntiAlias(true);
            this.paint.setShader(this.bitmapShader);
        }

        protected final void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            this.mRect.set((float) this.margin, (float) this.margin, (float) (bounds.width() - this.margin), (float) (bounds.height() - this.margin));
        }

        public final void draw(Canvas canvas) {
            canvas.drawRoundRect(this.mRect, this.cornerRadius, this.cornerRadius, this.paint);
        }

        public final int getOpacity() {
            return -3;
        }

        public final void setAlpha(int alpha) {
            this.paint.setAlpha(alpha);
        }

        public final void setColorFilter(ColorFilter cf) {
            this.paint.setColorFilter(cf);
        }
    }

    public RoundedBitmapDisplayer() {
        this((byte) 0);
    }

    private RoundedBitmapDisplayer(byte b) {
        this.cornerRadius = 20;
        this.margin = 0;
    }

    public final void display$44f2d737(Bitmap bitmap, ImageAware imageAware) {
        if (imageAware instanceof ImageViewAware) {
            imageAware.setImageDrawable(new RoundedDrawable(bitmap, this.cornerRadius, this.margin));
            return;
        }
        throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
    }
}
