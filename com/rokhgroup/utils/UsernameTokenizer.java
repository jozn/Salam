package com.rokhgroup.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView.Tokenizer;

public final class UsernameTokenizer implements Tokenizer {
    public final int findTokenEnd(CharSequence text, int cursor) {
        int len = text.length();
        for (int i = cursor; i < len; i++) {
            if (text.charAt(i) == ' ') {
                return i;
            }
        }
        return len;
    }

    public final int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;
        while (i > 0 && text.charAt(i - 1) != '@') {
            i--;
        }
        if (i <= 0 || text.charAt(i - 1) != '@') {
            return cursor;
        }
        return i;
    }

    public final CharSequence terminateToken(CharSequence text) {
        int i = text.length();
        while (i > 0 && text.charAt(i - 1) == ' ') {
            i--;
        }
        if (!(text instanceof Spanned)) {
            return text + " ";
        }
        SpannableString sp = new SpannableString(text + " ");
        TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
        return sp;
    }
}
