package gnu.inet.encoding;

public final class Punycode {
    public static final int adapt(int i, int i2, boolean z) {
        int i3;
        if (z) {
            i3 = i / 700;
        } else {
            i3 = i / 2;
        }
        int i4 = (i3 / i2) + i3;
        i3 = 0;
        while (i4 > 455) {
            i4 /= 35;
            i3 += 36;
        }
        return i3 + ((i4 * 36) / (i4 + 38));
    }

    public static final int digit2codepoint(int i) throws PunycodeException {
        if (i < 26) {
            return i + 97;
        }
        if (i < 36) {
            return (i - 26) + 48;
        }
        throw new PunycodeException(PunycodeException.BAD_INPUT);
    }
}
