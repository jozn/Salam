package gnu.inet.encoding;

public final class Stringprep {
    private static final char[] RFC3920_NODEPREP_PROHIBIT;

    public static String nameprep(String str) throws StringprepException, NullPointerException {
        return nameprep$185c6b75(str);
    }

    public static String nameprep$185c6b75(String str) throws StringprepException, NullPointerException {
        if (str == null) {
            throw new NullPointerException();
        }
        StringBuffer stringBuffer = new StringBuffer(str);
        if (contains(stringBuffer, RFC3454.A1)) {
            throw new StringprepException(StringprepException.CONTAINS_UNASSIGNED);
        }
        filter(stringBuffer, RFC3454.B1);
        map(stringBuffer, RFC3454.B2search, RFC3454.B2replace);
        StringBuffer stringBuffer2 = new StringBuffer(NFKC.normalizeNFKC(stringBuffer.toString()));
        if (contains(stringBuffer2, RFC3454.C12) || contains(stringBuffer2, RFC3454.C22) || contains(stringBuffer2, RFC3454.C3) || contains(stringBuffer2, RFC3454.C4) || contains(stringBuffer2, RFC3454.C5) || contains(stringBuffer2, RFC3454.C6) || contains(stringBuffer2, RFC3454.C7) || contains(stringBuffer2, RFC3454.C8)) {
            throw new StringprepException(StringprepException.CONTAINS_PROHIBITED);
        }
        boolean contains = contains(stringBuffer2, RFC3454.D1);
        boolean contains2 = contains(stringBuffer2, RFC3454.D2);
        if (contains && contains2) {
            throw new StringprepException(StringprepException.BIDI_BOTHRAL);
        } else if (!contains || (contains(stringBuffer2.charAt(0), RFC3454.D1) && contains(stringBuffer2.charAt(stringBuffer2.length() - 1), RFC3454.D1))) {
            return stringBuffer2.toString();
        } else {
            throw new StringprepException(StringprepException.BIDI_LTRAL);
        }
    }

    static {
        RFC3920_NODEPREP_PROHIBIT = new char[]{'\"', '&', '\'', '/', ':', '<', '>', '@'};
    }

    public static String nodeprep(String str) throws StringprepException, NullPointerException {
        if (str == null) {
            throw new NullPointerException();
        }
        StringBuffer stringBuffer = new StringBuffer(str);
        if (contains(stringBuffer, RFC3454.A1)) {
            throw new StringprepException(StringprepException.CONTAINS_UNASSIGNED);
        }
        filter(stringBuffer, RFC3454.B1);
        map(stringBuffer, RFC3454.B2search, RFC3454.B2replace);
        StringBuffer stringBuffer2 = new StringBuffer(NFKC.normalizeNFKC(stringBuffer.toString()));
        if (contains(stringBuffer2, RFC3454.C11) || contains(stringBuffer2, RFC3454.C12) || contains(stringBuffer2, RFC3454.C21) || contains(stringBuffer2, RFC3454.C22) || contains(stringBuffer2, RFC3454.C3) || contains(stringBuffer2, RFC3454.C4) || contains(stringBuffer2, RFC3454.C5) || contains(stringBuffer2, RFC3454.C6) || contains(stringBuffer2, RFC3454.C7) || contains(stringBuffer2, RFC3454.C8) || contains(stringBuffer2, RFC3920_NODEPREP_PROHIBIT)) {
            throw new StringprepException(StringprepException.CONTAINS_PROHIBITED);
        }
        boolean contains = contains(stringBuffer2, RFC3454.D1);
        boolean contains2 = contains(stringBuffer2, RFC3454.D2);
        if (contains && contains2) {
            throw new StringprepException(StringprepException.BIDI_BOTHRAL);
        } else if (!contains || (contains(stringBuffer2.charAt(0), RFC3454.D1) && contains(stringBuffer2.charAt(stringBuffer2.length() - 1), RFC3454.D1))) {
            return stringBuffer2.toString();
        } else {
            throw new StringprepException(StringprepException.BIDI_LTRAL);
        }
    }

    private static boolean contains(StringBuffer stringBuffer, char[] cArr) {
        for (char c : cArr) {
            for (int i = 0; i < stringBuffer.length(); i++) {
                if (c == stringBuffer.charAt(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean contains(StringBuffer stringBuffer, char[][] cArr) {
        for (char[] cArr2 : cArr) {
            char c;
            int i;
            if (1 == cArr2.length) {
                c = cArr2[0];
                for (i = 0; i < stringBuffer.length(); i++) {
                    if (c == stringBuffer.charAt(i)) {
                        return true;
                    }
                }
                continue;
            } else if (2 == cArr2.length) {
                c = cArr2[0];
                char c2 = cArr2[1];
                i = 0;
                while (i < stringBuffer.length()) {
                    if (c <= stringBuffer.charAt(i) && c2 >= stringBuffer.charAt(i)) {
                        return true;
                    }
                    i++;
                }
                continue;
            } else {
                continue;
            }
        }
        return false;
    }

    private static boolean contains(char c, char[][] cArr) {
        for (char[] cArr2 : cArr) {
            if (1 == cArr2.length) {
                if (c == cArr2[0]) {
                    return true;
                }
            } else if (2 == cArr2.length) {
                char c2 = cArr2[0];
                char c3 = cArr2[1];
                if (c2 <= c && c3 >= c) {
                    return true;
                }
            } else {
                continue;
            }
        }
        return false;
    }

    private static void filter(StringBuffer stringBuffer, char[] cArr) {
        for (char c : cArr) {
            int i = 0;
            while (i < stringBuffer.length()) {
                if (c == stringBuffer.charAt(i)) {
                    stringBuffer.deleteCharAt(i);
                } else {
                    i++;
                }
            }
        }
    }

    private static void map(StringBuffer stringBuffer, char[] cArr, String[] strArr) {
        for (int i = 0; i < cArr.length; i++) {
            char c = cArr[i];
            int i2 = 0;
            while (i2 < stringBuffer.length()) {
                if (c == stringBuffer.charAt(i2)) {
                    stringBuffer.deleteCharAt(i2);
                    if (strArr[i] != null) {
                        stringBuffer.insert(i2, strArr[i]);
                        i2 += strArr[i].length() - 1;
                    }
                } else {
                    i2++;
                }
            }
        }
    }
}
