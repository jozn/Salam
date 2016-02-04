package de.greenrobot.event;

public final class EventBusException extends RuntimeException {
    public EventBusException(String detailMessage) {
        super(detailMessage);
    }

    public EventBusException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
