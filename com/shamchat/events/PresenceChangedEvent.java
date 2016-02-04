package com.shamchat.events;

public final class PresenceChangedEvent {
    private int newStatusType;
    public String userId;

    public PresenceChangedEvent(String userId, int newStatusType) {
        this.userId = userId;
        this.newStatusType = newStatusType;
    }
}
