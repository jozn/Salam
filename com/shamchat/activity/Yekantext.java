package com.shamchat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.shamchat.androidclient.SHAMChatApplication;

public class Yekantext extends TextView {
    private SharedPreferences sp;

    public Yekantext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Yekantext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Yekantext(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.sp = SHAMChatApplication.getMyApplicationContext().getSharedPreferences("setting", 0);
        int font = this.sp.getInt("font", 2);
        this.sp.getInt("sizee", 14);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/rmedium.ttf");
        if (font == 1) {
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/homa.ttf");
        } else if (font == 2) {
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/rmedium.ttf");
        } else if (font == 3) {
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/bnazanin.ttf");
        }
        setTypeface(tf, 0);
    }
}
