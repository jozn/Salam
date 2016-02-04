package com.shamchat.events;

public final class UpdateGroupMembersList {
    private String groupId;
    public String threadId;

    public UpdateGroupMembersList(String threadId, String groupId) {
        this.threadId = threadId;
        this.groupId = groupId;
    }
}
