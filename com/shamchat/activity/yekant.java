package com.shamchat.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class yekant extends TextView {
    Context cnt;

    public yekant(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.cnt = context;
        init();
    }

    public yekant(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public yekant(Context context) {
        super(context);
        init();
    }

    private void init() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/rmedium.ttf"), 0);
    }
}
