package com.rokhgroup.adapters.item;

public final class UserSearchItem {
    public boolean FOLLOW_W_USER;
    public String USER_AVATAR;
    public String USER_ID;
    public String USER_NAME;

    public UserSearchItem(String userId, String userName, String userAvatar, boolean followWuser) {
        this.USER_ID = userId;
        this.USER_NAME = userName;
        this.USER_AVATAR = userAvatar;
        this.FOLLOW_W_USER = followWuser;
    }
}
