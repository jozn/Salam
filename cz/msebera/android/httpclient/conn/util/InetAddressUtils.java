package cz.msebera.android.httpclient.conn.util;

import java.util.regex.Pattern;

public final class InetAddressUtils {
    private static final Pattern IPV4_MAPPED_IPV6_PATTERN;
    private static final Pattern IPV4_PATTERN;
    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN;
    private static final Pattern IPV6_STD_PATTERN;

    static {
        IPV4_PATTERN = Pattern.compile("^(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
        IPV4_MAPPED_IPV6_PATTERN = Pattern.compile("^::[fF]{4}:(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){1}(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){2}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
        IPV6_STD_PATTERN = Pattern.compile("^[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}$");
        IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)::(([0-9A-Fa-f]{1,4}(:[0-9A-Fa-f]{1,4}){0,5})?)$");
    }

    public static boolean isIPv4Address(String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6Address(String input) {
        if (!IPV6_STD_PATTERN.matcher(input).matches()) {
            int i = 0;
            for (int i2 = 0; i2 < input.length(); i2++) {
                if (input.charAt(i2) == ':') {
                    i++;
                }
            }
            boolean z = i <= 7 && IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
            if (!z) {
                return false;
            }
        }
        return true;
    }
}
