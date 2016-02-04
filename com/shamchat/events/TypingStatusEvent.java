package com.shamchat.events;

public final class TypingStatusEvent {
    public String groupId;
    public boolean typing;
    public String username;

    public TypingStatusEvent(String groupId, String username, boolean isTyping) {
        this.groupId = groupId;
        this.username = username;
        this.typing = isTyping;
    }

    public TypingStatusEvent(String username, boolean isTyping) {
        this.username = username;
        this.typing = isTyping;
    }
}
