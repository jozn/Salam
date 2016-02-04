package com.shamchat.models;

public final class ShareIntentItem {
    public String displayName;
    public boolean isGroup;
    public String phoneNumberOrGroupAlias;
    public String userIdOrGroupId;

    public ShareIntentItem(String phoneNumberOrGroupAlias, String userIdOrGroupId, boolean isGroup) {
        this.isGroup = false;
        this.phoneNumberOrGroupAlias = phoneNumberOrGroupAlias;
        this.userIdOrGroupId = userIdOrGroupId;
        this.isGroup = isGroup;
        this.displayName = phoneNumberOrGroupAlias;
    }

    public final String toString() {
        return this.displayName + ":" + this.phoneNumberOrGroupAlias;
    }
}
