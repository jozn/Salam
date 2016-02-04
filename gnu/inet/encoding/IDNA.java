package gnu.inet.encoding;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;

public final class IDNA {
    public static String toASCII$4ff2de3f(String str) throws IDNAException {
        int i;
        Object obj;
        for (i = 0; i < str.length(); i++) {
            if (str.charAt(i) > '\u007f') {
                obj = 1;
                break;
            }
        }
        obj = null;
        if (obj != null) {
            try {
                str = Stringprep.nameprep$185c6b75(str);
            } catch (StringprepException e) {
                throw new IDNAException(e);
            }
        }
        for (i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt <= ',' || ((charAt >= '.' && charAt <= '/') || ((charAt >= ':' && charAt <= '@') || ((charAt >= '[' && charAt <= '`') || (charAt >= '{' && charAt <= '\u007f'))))) {
                throw new IDNAException(IDNAException.CONTAINS_NON_LDH);
            }
        }
        if (str.startsWith("-") || str.endsWith("-")) {
            throw new IDNAException(IDNAException.CONTAINS_HYPHEN);
        }
        for (i = 0; i < str.length(); i++) {
            if (str.charAt(i) > '\u007f') {
                obj = 1;
                break;
            }
        }
        obj = null;
        if (obj != null) {
            if (str.startsWith("xn--")) {
                throw new IDNAException(IDNAException.CONTAINS_ACE_PREFIX);
            }
            int i2 = 72;
            StringBuffer stringBuffer = new StringBuffer();
            int i3 = 0;
            i = 0;
            while (i3 < str.length()) {
                try {
                    char charAt2 = str.charAt(i3);
                    if ((charAt2 < '\u0080' ? 1 : null) != null) {
                        stringBuffer.append(charAt2);
                        i++;
                    }
                    i3++;
                } catch (PunycodeException e2) {
                    throw new IDNAException(e2);
                }
            }
            if (i > 0) {
                stringBuffer.append('-');
            }
            int i4 = 0;
            char c = AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
            int i5;
            for (int i6 = i; i6 < str.length(); i6 = i5) {
                char c2 = '\uffff';
                i3 = 0;
                while (i3 < str.length()) {
                    charAt = str.charAt(i3);
                    if (charAt < c || charAt >= c2) {
                        charAt = c2;
                    }
                    i3++;
                    c2 = charAt;
                }
                if (c2 - c > (ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED - i4) / (i6 + 1)) {
                    throw new PunycodeException(PunycodeException.OVERFLOW);
                }
                i3 = i4 + ((c2 - c) * (i6 + 1));
                i5 = i6;
                i6 = i2;
                i2 = i3;
                for (int i7 = 0; i7 < str.length(); i7++) {
                    char charAt3 = str.charAt(i7);
                    if (charAt3 < c2) {
                        i2++;
                        if (i2 == 0) {
                            throw new PunycodeException(PunycodeException.OVERFLOW);
                        }
                    }
                    if (charAt3 == c2) {
                        i4 = 36;
                        int i8 = i2;
                        while (true) {
                            i3 = i4 <= i6 ? 1 : i4 >= i6 + 26 ? 26 : i4 - i6;
                            if (i8 < i3) {
                                break;
                            }
                            stringBuffer.append((char) Punycode.digit2codepoint(((i8 - i3) % (36 - i3)) + i3));
                            i8 = (i8 - i3) / (36 - i3);
                            i4 += 36;
                        }
                        stringBuffer.append((char) Punycode.digit2codepoint(i8));
                        i6 = Punycode.adapt(i2, i5 + 1, i5 == i);
                        i2 = 0;
                        i5++;
                    }
                }
                i4 = i2 + 1;
                c = c2 + 1;
                i2 = i6;
            }
            str = "xn--" + stringBuffer.toString();
        }
        if (str.length() > 0 && str.length() <= 63) {
            return str;
        }
        throw new IDNAException(IDNAException.TOO_LONG);
    }
}
