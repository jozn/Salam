package com.shamchat.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class Yekanbtn extends Button {
    public Yekanbtn(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Yekanbtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Yekanbtn(Context context) {
        super(context);
        init();
    }

    private void init() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Yekan.ttf"), 0);
    }
}
