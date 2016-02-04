package org.jivesoftware.smack.tcp.sm;

import java.math.BigInteger;

public final class SMUtils {
    private static long MASK_32_BIT;

    static {
        MASK_32_BIT = BigInteger.ONE.shiftLeft(32).subtract(BigInteger.ONE).longValue();
    }

    public static long incrementHeight(long height) {
        return (1 + height) & MASK_32_BIT;
    }

    public static long calculateDelta(long reportedHandledCount, long lastKnownHandledCount) {
        return (reportedHandledCount - lastKnownHandledCount) & MASK_32_BIT;
    }
}
