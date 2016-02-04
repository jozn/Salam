package gnu.inet.encoding;

public final class PunycodeException extends Exception {
    public static String BAD_INPUT;
    public static String OVERFLOW;

    static {
        OVERFLOW = "Overflow.";
        BAD_INPUT = "Bad input.";
    }

    public PunycodeException(String str) {
        super(str);
    }
}
