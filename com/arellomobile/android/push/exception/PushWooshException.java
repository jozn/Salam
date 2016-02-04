package com.arellomobile.android.push.exception;

public final class PushWooshException extends Exception {
    public PushWooshException(Exception exception) {
        super(exception);
    }

    public PushWooshException(String str) {
        super(str);
    }
}
