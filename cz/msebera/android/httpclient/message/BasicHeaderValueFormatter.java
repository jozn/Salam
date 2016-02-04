package cz.msebera.android.httpclient.message;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.CharArrayBuffer;

public final class BasicHeaderValueFormatter {
    @Deprecated
    public static final BasicHeaderValueFormatter DEFAULT;
    public static final BasicHeaderValueFormatter INSTANCE;

    static {
        DEFAULT = new BasicHeaderValueFormatter();
        INSTANCE = new BasicHeaderValueFormatter();
    }

    public static CharArrayBuffer formatNameValuePair(CharArrayBuffer charBuffer, NameValuePair nvp, boolean quote) {
        Args.notNull(nvp, "Name / value pair");
        CharArrayBuffer buffer = charBuffer;
        charBuffer.ensureCapacity(estimateNameValuePairLen(nvp));
        buffer.append(nvp.getName());
        String value = nvp.getValue();
        if (value != null) {
            buffer.append('=');
            doFormatValue(buffer, value, quote);
        }
        return buffer;
    }

    public static int estimateNameValuePairLen(NameValuePair nvp) {
        if (nvp == null) {
            return 0;
        }
        int result = nvp.getName().length();
        String value = nvp.getValue();
        if (value != null) {
            return result + (value.length() + 3);
        }
        return result;
    }

    public static void doFormatValue(CharArrayBuffer buffer, String value, boolean quote) {
        int i;
        boolean quoteFlag = quote;
        if (!quote) {
            for (i = 0; i < value.length() && !quoteFlag; i++) {
                if (" ;,:@()<>\\\"/[]?={}\t".indexOf(value.charAt(i)) >= 0) {
                    quoteFlag = true;
                } else {
                    quoteFlag = false;
                }
            }
        }
        if (quoteFlag) {
            buffer.append('\"');
        }
        for (i = 0; i < value.length(); i++) {
            Object obj;
            char ch = value.charAt(i);
            if ("\"\\".indexOf(ch) >= 0) {
                obj = 1;
            } else {
                obj = null;
            }
            if (obj != null) {
                buffer.append('\\');
            }
            buffer.append(ch);
        }
        if (quoteFlag) {
            buffer.append('\"');
        }
    }
}
