package com.shamchat.events;

public final class CloseGroupActivityEvent {
    private String groupId;
    public String threadId;

    public CloseGroupActivityEvent(String threadId, String groupId) {
        this.threadId = threadId;
        this.groupId = groupId;
    }
}
