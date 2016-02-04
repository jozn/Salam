package gnu.inet.encoding;

public final class IDNAException extends Exception {
    public static String CONTAINS_ACE_PREFIX;
    public static String CONTAINS_HYPHEN;
    public static String CONTAINS_NON_LDH;
    public static String TOO_LONG;

    static {
        CONTAINS_NON_LDH = "Contains non-LDH characters.";
        CONTAINS_HYPHEN = "Leading or trailing hyphen not allowed.";
        CONTAINS_ACE_PREFIX = "ACE prefix (xn--) not allowed.";
        TOO_LONG = "String too long.";
    }

    public IDNAException(String str) {
        super(str);
    }

    public IDNAException(StringprepException stringprepException) {
        super(stringprepException);
    }

    public IDNAException(PunycodeException punycodeException) {
        super(punycodeException);
    }
}
