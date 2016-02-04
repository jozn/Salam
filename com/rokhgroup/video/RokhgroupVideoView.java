package com.rokhgroup.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class RokhgroupVideoView extends VideoView {
    public int videoHeight;
    public int videoWidth;

    public RokhgroupVideoView(Context context) {
        super(context);
    }

    public RokhgroupVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RokhgroupVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(this.videoWidth, widthMeasureSpec);
        int height = getDefaultSize(this.videoHeight, heightMeasureSpec);
        if (this.videoWidth > 0 && this.videoHeight > 0) {
            if (this.videoWidth * height > this.videoHeight * width) {
                height = (this.videoHeight * width) / this.videoWidth;
            } else if (this.videoWidth * height < this.videoHeight * width) {
                width = (this.videoWidth * height) / this.videoHeight;
            }
        }
        setMeasuredDimension(width, height);
    }

    protected void onWindowVisibilityChanged(int visibility) {
        if (visibility == 0) {
            super.onWindowVisibilityChanged(visibility);
        }
    }
}
