package com.shamchat.models;

import java.util.Date;

public final class FriendGroupMember {
    public static String DB_FRIEND;
    public static String DB_FRIEND_DID_JOIN;
    public static String DB_FRIEND_IS_ADMIN;
    public static String DB_GROUP;
    public static String DB_ID;
    public static String PHONE_NUMBER;
    public boolean didJoin;
    public String friendId;
    public String groupID;
    public String id;
    public boolean isAdmin;
    public String phoneNumber;
    public User user;

    static {
        DB_ID = "friend_group_member_id";
        DB_GROUP = "friend_group_id";
        DB_FRIEND = "friend_id";
        DB_FRIEND_DID_JOIN = "friend_did_join";
        DB_FRIEND_IS_ADMIN = "friend_is_admin";
        PHONE_NUMBER = "phone_number";
    }

    public FriendGroupMember(String groupId, String friendId) {
        this.groupID = groupId;
        this.friendId = friendId;
    }

    public final FriendGroupMember assignUniqueId(String ownerId) {
        this.id = "M" + ownerId + "_" + new Date().getTime();
        return this;
    }

    public final String toString() {
        return "Id:" + this.id + " GroupId:" + this.groupID + " FriendId:" + this.friendId;
    }
}
