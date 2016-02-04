package com.google.android.gms.internal;

import android.support.v7.appcompat.C0170R;
import android.text.TextUtils;
import com.kyleduo.switchbutton.C0473R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class zznu {
    private static final Pattern zzamo;
    private static final Pattern zzamp;

    static {
        zzamo = Pattern.compile("\\\\.");
        zzamp = Pattern.compile("[\\\\\"/\b\f\n\r\t]");
    }

    public static String zzcO(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        Matcher matcher = zzamp.matcher(str);
        StringBuffer stringBuffer = null;
        while (matcher.find()) {
            if (stringBuffer == null) {
                stringBuffer = new StringBuffer();
            }
            switch (matcher.group().charAt(0)) {
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    matcher.appendReplacement(stringBuffer, "\\\\b");
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    matcher.appendReplacement(stringBuffer, "\\\\t");
                    break;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    matcher.appendReplacement(stringBuffer, "\\\\n");
                    break;
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    matcher.appendReplacement(stringBuffer, "\\\\f");
                    break;
                case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                    matcher.appendReplacement(stringBuffer, "\\\\r");
                    break;
                case C0170R.styleable.Theme_actionModePasteDrawable /*34*/:
                    matcher.appendReplacement(stringBuffer, "\\\\\\\"");
                    break;
                case C0170R.styleable.Theme_spinnerDropDownItemStyle /*47*/:
                    matcher.appendReplacement(stringBuffer, "\\\\/");
                    break;
                case C0170R.styleable.Theme_alertDialogButtonGroupStyle /*92*/:
                    matcher.appendReplacement(stringBuffer, "\\\\\\\\");
                    break;
                default:
                    break;
            }
        }
        if (stringBuffer == null) {
            return str;
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }
}
