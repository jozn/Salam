package com.shamchat.androidclient.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.widget.TextView;

public final class FontUtil {
    private static LruCache<ShamFont, Typeface> fontCache;

    public enum ShamFont {
        Neometric("fonts/Yekan.ttf"),
        TCM("fonts/Yekan.ttf");
        
        final String fontFile;

        private ShamFont(String fontFile) {
            this.fontFile = fontFile;
        }
    }

    static {
        fontCache = new LruCache(ShamFont.values().length);
    }

    public static void applyFont(TextView view) {
        ShamFont shamFont = ShamFont.TCM;
        Context context = view.getContext();
        Typeface typeface = (Typeface) fontCache.get(shamFont);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), shamFont.fontFile);
            fontCache.put(shamFont, typeface);
        }
        view.setTypeface(typeface);
    }
}
