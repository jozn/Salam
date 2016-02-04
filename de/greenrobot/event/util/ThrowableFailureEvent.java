package de.greenrobot.event.util;

public final class ThrowableFailureEvent {
    Object executionContext;
    protected final boolean suppressErrorUi;
    protected final Throwable throwable;

    public final boolean isSuppressErrorUi() {
        return this.suppressErrorUi;
    }
}
