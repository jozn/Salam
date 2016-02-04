package gnu.inet.encoding;

public final class StringprepException extends Exception {
    public static String BIDI_BOTHRAL;
    public static String BIDI_LTRAL;
    public static String CONTAINS_PROHIBITED;
    public static String CONTAINS_UNASSIGNED;

    static {
        CONTAINS_UNASSIGNED = "Contains unassigned code points.";
        CONTAINS_PROHIBITED = "Contains prohibited code points.";
        BIDI_BOTHRAL = "Contains both R and AL code points.";
        BIDI_LTRAL = "Leading and trailing code points not both R or AL.";
    }

    public StringprepException(String str) {
        super(str);
    }
}
