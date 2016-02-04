package com.shamchat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import com.shamchat.androidclient.SHAMChatApplication;

public class homat extends TextView {
    Context cnt;
    private SharedPreferences sp;

    public homat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.cnt = context;
        init();
    }

    public homat(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public homat(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.sp = SHAMChatApplication.getMyApplicationContext().getSharedPreferences("setting", 0);
        this.sp.getInt("sizee", 1);
        this.sp.getInt("font", 1);
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/homa.ttf"), 0);
    }
}
