package com.rokhgroup.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

public class RokhImageView extends ImageView {
    public RokhImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            int height;
            int width;
            if (MeasureSpec.getMode(heightMeasureSpec) == 1073741824) {
                height = MeasureSpec.getSize(heightMeasureSpec);
                width = (int) Math.ceil((double) ((((float) height) * ((float) drawable.getIntrinsicWidth())) / ((float) drawable.getIntrinsicHeight())));
            } else {
                width = MeasureSpec.getSize(widthMeasureSpec);
                height = (int) Math.ceil((double) ((((float) width) * ((float) drawable.getIntrinsicHeight())) / ((float) drawable.getIntrinsicWidth())));
            }
            setMeasuredDimension(width, height);
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
