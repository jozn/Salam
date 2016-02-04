package com.rokhgroup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

public class RokhCircularImageView extends ImageView {
    private Path mPath;

    /* renamed from: com.rokhgroup.utils.RokhCircularImageView.1 */
    class C06841 extends ViewOutlineProvider {
        C06841() {
        }

        public final void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, RokhCircularImageView.this.getWidth(), RokhCircularImageView.this.getHeight());
        }
    }

    public RokhCircularImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RokhCircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mPath = new Path();
        this.mPath.addRoundRect(new RectF(0.0f, 0.0f, (float) w, (float) h), (float) (w / 2), (float) (h / 2), Direction.CW);
    }

    protected void onDraw(Canvas canvas) {
        canvas.clipPath(this.mPath);
        super.onDraw(canvas);
    }

    public void setImageBitmap(Bitmap bm) {
        clipPathInternal();
        super.setImageBitmap(bm);
    }

    public void setImageDrawable(Drawable drawable) {
        clipPathInternal();
        super.setImageDrawable(drawable);
    }

    private void clipPathInternal() {
        if (VERSION.SDK_INT >= 21) {
            setOutlineProvider(new C06841());
            setClipToOutline(true);
        } else {
            onSizeChanged(getWidth(), getHeight(), 0, 0);
            setLayerType(1, null);
        }
        invalidate();
    }
}
