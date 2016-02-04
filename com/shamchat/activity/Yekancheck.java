package com.shamchat.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class Yekancheck extends CheckBox {
    public Yekancheck(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Yekancheck(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Yekancheck(Context context) {
        super(context);
        init();
    }

    private void init() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Yekan.ttf"), 0);
    }
}
