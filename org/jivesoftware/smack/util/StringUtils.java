package org.jivesoftware.smack.util;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.appcompat.C0170R;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Random;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public final class StringUtils {
    public static final char[] HEX_CHARS;
    private static MessageDigest digest;
    private static char[] numbersAndLetters;
    private static Random randGen;

    static {
        HEX_CHARS = "0123456789abcdef".toCharArray();
        digest = null;
        randGen = new Random();
        numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    }

    public static CharSequence escapeForXML(String string) {
        if (string == null) {
            return null;
        }
        char[] input = string.toCharArray();
        int len = input.length;
        StringBuilder out = new StringBuilder((int) (((double) len) * 1.3d));
        int last = 0;
        int i = 0;
        while (i < len) {
            CharSequence toAppend = null;
            switch (input[i]) {
                case C0170R.styleable.Theme_actionModePasteDrawable /*34*/:
                    toAppend = "&quot;";
                    break;
                case C0170R.styleable.Theme_actionModeWebSearchDrawable /*38*/:
                    toAppend = "&amp;";
                    break;
                case C0170R.styleable.Theme_actionModePopupWindowStyle /*39*/:
                    toAppend = "&apos;";
                    break;
                case MqttConnectOptions.KEEP_ALIVE_INTERVAL_DEFAULT /*60*/:
                    toAppend = "&lt;";
                    break;
                case C0170R.styleable.Theme_editTextColor /*62*/:
                    toAppend = "&gt;";
                    break;
            }
            if (toAppend != null) {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                out.append(toAppend);
                i++;
                last = i;
            } else {
                i++;
            }
        }
        if (last == 0) {
            return string;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out;
    }

    public static String encodeHex(byte[] bytes) {
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & MotionEventCompat.ACTION_MASK;
            hexChars[j * 2] = HEX_CHARS[v >>> 4];
            hexChars[(j * 2) + 1] = HEX_CHARS[v & 15];
        }
        return new String(hexChars);
    }

    public static byte[] toBytes(String string) {
        try {
            return string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not supported by platform", e);
        }
    }

    public static String randomString(int length) {
        if (length <= 0) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(numbersAndLetters.length)];
        }
        return new String(randBuffer);
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isNullOrEmpty(cs);
    }

    public static boolean isNullOrEmpty(CharSequence cs) {
        return cs == null || isEmpty(cs);
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs.length() == 0;
    }

    public static String collectionToString(Collection<String> collection) {
        StringBuilder sb = new StringBuilder();
        for (String s : collection) {
            sb.append(s);
            sb.append(" ");
        }
        String res = sb.toString();
        return res.substring(0, res.length() - 1);
    }

    public static String returnIfNotEmptyTrimmed(String string) {
        if (string == null) {
            return null;
        }
        String trimmedString = string.trim();
        if (trimmedString.length() <= 0) {
            return null;
        }
        return trimmedString;
    }
}
