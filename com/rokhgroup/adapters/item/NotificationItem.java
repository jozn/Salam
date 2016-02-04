package com.rokhgroup.adapters.item;

public final class NotificationItem {
    public String NOTIF_TYPE;
    public String POST_ID;
    public String POST_IMAGE;
    public String POST_TYPE;
    public String USER_AVA;
    public String USER_ID;
    public String USER_NAME;

    public NotificationItem(String userId, String postId, String postImage, String userName, String userAva, String notifType, String postType) {
        this.USER_ID = userId;
        this.POST_ID = postId;
        this.POST_IMAGE = postImage;
        this.USER_AVA = userAva;
        this.USER_NAME = userName;
        this.NOTIF_TYPE = notifType;
        this.POST_TYPE = postType;
    }
}
