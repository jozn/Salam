package com.shamchat.models;

import android.content.Context;
import android.database.Cursor;
import com.shamchat.androidclient.data.UserProvider;

public final class FriendGroup {
    public static String CHAT_ROOM_NAME;
    public static String DB_ID;
    public static String DB_NAME;
    public static String DB_RECORD_OWNER;
    public static String DID_JOIN_ROOM;
    public static String DID_LEAVE;
    public String chatRoomName;
    public String dbRecordId;
    public String id;
    public String name;
    public String recordOwnerId;

    static {
        DB_ID = "friend_group_id";
        DB_NAME = "friend_group_name";
        DB_RECORD_OWNER = "record_owner_id";
        CHAT_ROOM_NAME = "chat_room_name";
        DID_JOIN_ROOM = "did_join_room";
        DID_LEAVE = "did_leave";
    }

    public FriendGroup(String name, String ownerId) {
        this.name = name;
        this.recordOwnerId = ownerId;
    }

    public final String toString() {
        return "Id:" + this.id + " Name:" + this.name + " Owner:" + this.recordOwnerId;
    }

    public static String getNextAvailableGroupName(Context context) {
        Cursor cursor = context.getContentResolver().query(UserProvider.CONTENT_URI_GROUP, new String[]{DB_ID}, null, null, null);
        int i = cursor.getCount() + 1;
        cursor.close();
        return "Group " + i;
    }
}
