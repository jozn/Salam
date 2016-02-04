package com.rokhgroup.utils;

import android.text.TextPaint;
import android.text.style.URLSpan;

public class URLSpanNoUnderLine extends URLSpan {
    public URLSpanNoUnderLine(String url) {
        super(url);
    }

    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }
}
