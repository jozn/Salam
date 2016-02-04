package com.shamchat.events;

public final class RemoveFromGroupMembersList {
    private String groupId;
    public String threadId;
    public int userPositionInListView;

    public RemoveFromGroupMembersList(String threadId, String groupId, int userPositionInListView) {
        this.threadId = threadId;
        this.groupId = groupId;
        this.userPositionInListView = userPositionInListView;
    }
}
