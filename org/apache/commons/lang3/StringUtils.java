package org.apache.commons.lang3;

import java.util.regex.Pattern;

public final class StringUtils {
    private static final Pattern WHITESPACE_PATTERN;

    static {
        WHITESPACE_PATTERN = Pattern.compile("(?: |\\u00A0|\\s|[\\s&&[^ ]])\\s*");
    }

    private static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static int countMatches(CharSequence str, CharSequence sub) {
        if (isEmpty(str) || isEmpty(sub)) {
            return 0;
        }
        int count = 0;
        int idx = 0;
        while (true) {
            idx = str.toString().indexOf(sub.toString(), idx);
            if (idx == -1) {
                return count;
            }
            count++;
            idx += sub.length();
        }
    }
}
