package com.nostra13.universalimageloader.core.assist;

public final class FailReason {
    private final Throwable cause;
    private final int type$5b0e695f;

    public enum FailType {
        ;

        static {
            IO_ERROR$5b0e695f = 1;
            DECODING_ERROR$5b0e695f = 2;
            NETWORK_DENIED$5b0e695f = 3;
            OUT_OF_MEMORY$5b0e695f = 4;
            UNKNOWN$5b0e695f = 5;
            $VALUES$400a4c66 = new int[]{IO_ERROR$5b0e695f, DECODING_ERROR$5b0e695f, NETWORK_DENIED$5b0e695f, OUT_OF_MEMORY$5b0e695f, UNKNOWN$5b0e695f};
        }
    }

    public FailReason(int type, Throwable cause) {
        this.type$5b0e695f = type;
        this.cause = cause;
    }
}
