package com.shamchat.androidclient.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.util.Log;
import com.kyleduo.switchbutton.C0473R;
import com.shamchat.activity.ChatActivity;
import com.shamchat.activity.ChatInitialForGroupChatActivity;
import com.shamchat.activity.MessageDetailsActivity;
import com.shamchat.adapters.MyMessageType;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.chat.extension.MessageContentTypeProvider.MessageContentType;
import com.shamchat.models.ChatMessage;
import com.shamchat.models.ChatMessage.MessageStatusType;
import com.shamchat.models.Message;
import com.shamchat.models.Message.MessageType;
import com.shamchat.models.MessageThread;
import com.shamchat.models.User;
import com.shamchat.utils.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class ChatProviderNew extends ContentProvider {
    public static final Uri CONTENT_URI_CHAT;
    public static final Uri CONTENT_URI_THREAD;
    private static final UriMatcher URI_MATCHER;
    private SQLiteOpenHelper mOpenHelper;

    /* renamed from: com.shamchat.androidclient.data.ChatProviderNew.1 */
    static /* synthetic */ class C10661 {
        static final /* synthetic */ int[] f24x39e42954;

        static {
            f24x39e42954 = new int[MessageContentType.values().length];
            try {
                f24x39e42954[MessageContentType.FAVORITE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f24x39e42954[MessageContentType.IMAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f24x39e42954[MessageContentType.INCOMING_CALL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f24x39e42954[MessageContentType.LOCATION.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f24x39e42954[MessageContentType.OUTGOING_CALL.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f24x39e42954[MessageContentType.STICKER.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f24x39e42954[MessageContentType.TEXT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f24x39e42954[MessageContentType.VOICE_RECORD.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f24x39e42954[MessageContentType.VIDEO.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                f24x39e42954[MessageContentType.GROUP_INFO.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    public static class ChatDatabaseHelper extends SQLiteOpenHelper {
        private String CHAT_DATABASE_PATH;
        private String USER_DATABASE_PATH;

        public ChatDatabaseHelper(Context context) {
            super(context, "chat.db", null, 5);
            this.CHAT_DATABASE_PATH = context.getDatabasePath("chat.db").toString();
            this.USER_DATABASE_PATH = context.getDatabasePath("user.db").toString();
        }

        public final void onCreate(SQLiteDatabase db) {
            Log.i("zamin.ChatProviderNew", "creating new chat table");
            db.execSQL("CREATE TABLE IF NOT EXISTS chat_message (_id INTEGER PRIMARY KEY AUTOINCREMENT,thread_id TEXT , message_sender TEXT, message_recipient TEXT, message_content_type INTEGER , text_message TEXT , blob_message BLOB , message_datetime DATETIME DEFAULT (datetime('now','localtime')), delivered_datetime DATETIME, message_status INTEGER NOT NULL DEFAULT 0, message_type INTEGER NOT NULL  DEFAULT 0, senders_mobile_number TEXT , packet_id TEXT, description TEXT, longitude DOUBLE, file_size INTEGER NOT NULL DEFAULT 0, file_url TEXT , uploaded_percentage INTEGER NOT NULL DEFAULT 0, message_last_updated_datetime DATETIME DEFAULT (datetime('now','localtime')), uploaded_file_url TEXT , group_id TEXT , latitude DOUBLE )");
            db.execSQL("CREATE TABLE IF NOT EXISTS message_thread (_id INTEGER PRIMARY KEY AUTOINCREMENT,thread_id TEXT NOT NULL , last_updated_datetime DATETIME NOT NULL , thread_status INTEGER NOT NULL  DEFAULT 1, is_group_chat INTEGER NOT NULL  DEFAULT 0, read_status INTEGER NOT NULL  DEFAULT 0, thread_owner TEXT NOT NULL, last_message TEXT NOT NULL, last_message_content_type INTEGER NOT NULL  DEFAULT 0 , last_message_direction INTEGER NOT NULL , friend_id TEXT NOT NULL)");
            db.execSQL("CREATE TABLE favorite (message_id TEXT PRIMARY KEY NOT NULL, type INTEGER, content TEXT, is_deleted INTEGER NOT NULL DEFAULT 0 , user_id TEXT, time INTEGER);");
        }

        public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("zamin.ChatProviderNew", "onUpgrade: from " + oldVersion + " to " + newVersion);
        }
    }

    public static final class MessageConstants implements BaseColumns {
        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList();
            tmpList.add("packet_id");
            tmpList.add("message_sender");
            tmpList.add("message_recipient");
            tmpList.add(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID);
            return tmpList;
        }
    }

    public static final class ThreadConstants implements BaseColumns {
        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList();
            tmpList.add(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID);
            tmpList.add("friend_id");
            return tmpList;
        }
    }

    static {
        CONTENT_URI_CHAT = Uri.parse("content://org.zamin.androidclient.provider.Messages/chat_message");
        CONTENT_URI_THREAD = Uri.parse("content://org.zamin.androidclient.provider.Messages/message_thread");
        UriMatcher uriMatcher = new UriMatcher(-1);
        URI_MATCHER = uriMatcher;
        uriMatcher.addURI("org.zamin.androidclient.provider.Messages", "chat_message", 1);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Messages", "chat_message/#", 2);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Messages", "message_thread", 3);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Messages", "message_thread/#", 4);
    }

    public int delete(Uri url, String where, String[] whereArgs) {
        int count;
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        String segment;
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                count = db.delete("chat_message", where, whereArgs);
                break;
            case Logger.WARNING /*2*/:
                segment = (String) url.getPathSegments().get(1);
                if (TextUtils.isEmpty(where)) {
                    where = "_id=" + segment;
                } else {
                    where = "_id=" + segment + " AND (" + where + ")";
                }
                count = db.delete("chat_message", where, whereArgs);
                break;
            case Logger.INFO /*3*/:
                count = db.delete("message_thread", where, whereArgs);
                break;
            case Logger.CONFIG /*4*/:
                segment = (String) url.getPathSegments().get(1);
                if (TextUtils.isEmpty(where)) {
                    where = "_id=" + segment;
                } else {
                    where = "_id=" + segment + " AND (" + where + ")";
                }
                count = db.delete("message_thread", where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot delete from URL: " + url);
        }
        getContext().getContentResolver().notifyChange(url, null);
        return count;
    }

    public String getType(Uri url) {
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                return "vnd.android.cursor.dir/vnd.zamin.message";
            case Logger.WARNING /*2*/:
                return "vnd.android.cursor.item/vnd.zamin.message";
            case Logger.INFO /*3*/:
                return "vnd.android.cursor.dir/vnd.zamin.thread";
            case Logger.CONFIG /*4*/:
                return "vnd.android.cursor.item/vnd.zamin.thread";
            default:
                throw new IllegalArgumentException("Unknown URL");
        }
    }

    public Uri insert(Uri url, ContentValues initialValues) {
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        Iterator it;
        String colName;
        long rowId;
        Uri noteUri;
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                it = MessageConstants.getRequiredColumns().iterator();
                while (it.hasNext()) {
                    colName = (String) it.next();
                    if (!values.containsKey(colName)) {
                        throw new IllegalArgumentException("Missing column: " + colName);
                    }
                }
                rowId = db.insert("chat_message", BuildConfig.VERSION_NAME, values);
                if (rowId < 0) {
                    throw new SQLException("Failed to insert row into TABLE_NAME_CHATS" + url);
                }
                if (rowId > 0) {
                    Log.d("zamin.ChatProviderNew", "Message successfully entered at " + rowId);
                }
                noteUri = ContentUris.withAppendedId(CONTENT_URI_CHAT, rowId);
                getContext().getContentResolver().notifyChange(noteUri, null);
                return noteUri;
            case Logger.INFO /*3*/:
                it = ThreadConstants.getRequiredColumns().iterator();
                while (it.hasNext()) {
                    colName = (String) it.next();
                    if (!values.containsKey(colName)) {
                        throw new IllegalArgumentException("Missing column: " + colName);
                    }
                }
                rowId = db.insert("message_thread", BuildConfig.VERSION_NAME, values);
                if (rowId < 0) {
                    throw new SQLException("Failed to insert row into TABLE_NAME_THREADS" + url);
                }
                if (rowId > 0) {
                    Log.d("zamin.ChatProviderNew", "Thread successfully entered at " + rowId);
                }
                noteUri = ContentUris.withAppendedId(CONTENT_URI_THREAD, rowId);
                getContext().getContentResolver().notifyChange(noteUri, null);
                return noteUri;
            default:
                throw new IllegalArgumentException("Cannot insert from URL: " + url);
        }
    }

    public boolean onCreate() {
        this.mOpenHelper = new ChatDatabaseHelper(getContext());
        return true;
    }

    public Cursor query(Uri url, String[] projectionIn, String selection, String[] selectionArgs, String sortOrder) {
        String orderBy;
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                qBuilder.setTables("chat_message");
                break;
            case Logger.WARNING /*2*/:
                qBuilder.setTables("chat_message");
                qBuilder.appendWhere("_id=");
                qBuilder.appendWhere((CharSequence) url.getPathSegments().get(1));
                break;
            case Logger.INFO /*3*/:
                qBuilder.setTables("message_thread");
                break;
            case Logger.CONFIG /*4*/:
                qBuilder.setTables("message_thread");
                qBuilder.appendWhere("_id=");
                qBuilder.appendWhere((CharSequence) url.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URL " + url);
        }
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = "_id ASC";
        } else {
            orderBy = sortOrder;
        }
        Cursor ret = qBuilder.query(this.mOpenHelper.getReadableDatabase(), projectionIn, selection, selectionArgs, null, null, orderBy);
        if (ret == null) {
            Log.i("zamin.ChatProviderNew", "ChatProvider.query: failed");
        } else {
            ret.setNotificationUri(getContext().getContentResolver(), url);
        }
        return ret;
    }

    public int update(Uri url, ContentValues values, String where, String[] whereArgs) {
        int count;
        int match = URI_MATCHER.match(url);
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        long rowId;
        switch (match) {
            case Logger.SEVERE /*1*/:
                count = db.update("chat_message", values, where, whereArgs);
                if (count > 0) {
                    Log.d("zamin.ChatProviderNew", "Message successfully updated " + count);
                    break;
                }
                break;
            case Logger.WARNING /*2*/:
                rowId = Long.parseLong((String) url.getPathSegments().get(1));
                count = db.update("chat_message", values, "_id=" + rowId, null);
                getContext().getContentResolver().notifyChange(ContentUris.withAppendedId(CONTENT_URI_CHAT, rowId), null);
                getContext().getContentResolver().notifyChange(url, null);
                return count;
            case Logger.INFO /*3*/:
                count = db.update("message_thread", values, where, whereArgs);
                if (count > 0) {
                    Log.d("zamin.ChatProviderNew", "Thread successfully updated " + count);
                    break;
                }
                break;
            case Logger.CONFIG /*4*/:
                rowId = Long.parseLong((String) url.getPathSegments().get(1));
                count = db.update("message_thread", values, where, whereArgs);
                getContext().getContentResolver().notifyChange(ContentUris.withAppendedId(CONTENT_URI_THREAD, rowId), null);
                getContext().getContentResolver().notifyChange(url, null);
                return count;
            default:
                throw new UnsupportedOperationException("Cannot update URL: " + url);
        }
        Log.i("zamin.ChatProviderNew", "*** notifyChange() rowId: 0 url " + url);
        getContext().getContentResolver().notifyChange(url, null);
        return count;
    }

    public static ChatMessage getChatMessageByCursor(Cursor cursor) {
        int messageId = cursor.getInt(cursor.getColumnIndex("_id"));
        MessageContentType messageContentType = readMessageContentType(cursor.getInt(cursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_MESSAGE_CONTENT_TYPE)));
        String textMessage = cursor.getString(cursor.getColumnIndex("text_message"));
        byte[] blobMessage = cursor.getBlob(cursor.getColumnIndex("blob_message"));
        String messageDateTime = cursor.getString(cursor.getColumnIndex("message_datetime"));
        int messageStatus = cursor.getInt(cursor.getColumnIndex("message_status"));
        String deliverdDateTime = cursor.getString(cursor.getColumnIndex("delivered_datetime"));
        int messageType = cursor.getInt(cursor.getColumnIndex("message_type"));
        String packetId = cursor.getString(cursor.getColumnIndex("packet_id"));
        String sender = cursor.getString(cursor.getColumnIndex("message_sender"));
        String recipient = cursor.getString(cursor.getColumnIndex("message_recipient"));
        String description = cursor.getString(cursor.getColumnIndex("description"));
        double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
        double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
        String sendersMobileNumber = cursor.getString(cursor.getColumnIndex("senders_mobile_number"));
        long fileSize = cursor.getLong(cursor.getColumnIndex("file_size"));
        int uploadedPercentage = cursor.getInt(cursor.getColumnIndex("uploaded_percentage"));
        String fileUrl = cursor.getString(cursor.getColumnIndex("file_url"));
        String threadId = cursor.getString(cursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID));
        String lastUpdatedDateTime = cursor.getString(cursor.getColumnIndex("message_last_updated_datetime"));
        String uploadedFileUrl = cursor.getString(cursor.getColumnIndex("uploaded_file_url"));
        String tmpUserId = sender;
        System.out.println("INCOMING SENDER " + sender);
        if (sender.startsWith("g")) {
            switch (C10661.f24x39e42954[messageContentType.ordinal()]) {
                case Logger.SEVERE /*1*/:
                    System.out.println("INCOMING CPN FAVORITE " + textMessage);
                    break;
                case Logger.WARNING /*2*/:
                    System.out.println("INCOMING CPN IMAGE  " + textMessage);
                    break;
                case Logger.INFO /*3*/:
                    System.out.println("INCOMING CPN CALL  " + textMessage);
                    break;
                case Logger.CONFIG /*4*/:
                    System.out.println("INCOMING CPN LOCATION  " + textMessage);
                    break;
                case Logger.FINE /*5*/:
                    System.out.println("INCOMING CPN OUTGOING_CALL " + textMessage);
                    break;
                case Logger.FINER /*6*/:
                    System.out.println("INCOMING CPN STICKER " + textMessage);
                    tmpUserId = textMessage.split("-")[1];
                    break;
                case Logger.FINEST /*7*/:
                    System.out.println("INCOMING CPN TEXT " + textMessage);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    System.out.println("INCOMING CPN VOICE_RECORD " + textMessage);
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    System.out.println("INCOMING CPN VIDEO " + textMessage);
                    break;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    System.out.println("INCOMING CPN GROUP_INFO " + textMessage);
                    break;
                default:
                    System.out.println("INCOMING CPN DEFAULT " + textMessage);
                    break;
            }
        }
        tmpUserId = sender;
        Cursor friendCursor = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(UserProvider.CONTENT_URI_USER, null, "userId=?", new String[]{tmpUserId}, null);
        User user = null;
        if (friendCursor == null || friendCursor.getCount() <= 0) {
            System.out.println("INCOMING CPN USER NOT FOUND " + tmpUserId);
        } else {
            friendCursor.moveToFirst();
            System.out.println("INCOMING CPN USER FOUND " + tmpUserId);
            user = new User();
            user.userId = friendCursor.getString(friendCursor.getColumnIndex("userId"));
            user.username = friendCursor.getString(friendCursor.getColumnIndex("name"));
            user.mobileNo = friendCursor.getString(friendCursor.getColumnIndex("mobileNo"));
            user.profileImageUrl = friendCursor.getString(friendCursor.getColumnIndex("profileimage_url"));
        }
        friendCursor.close();
        MyMessageType myMessageType = MyMessageType.HEADER_MSG;
        switch (messageType) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                myMessageType = MyMessageType.OUTGOING_MSG;
                break;
            case Logger.SEVERE /*1*/:
                myMessageType = MyMessageType.INCOMING_MSG;
                break;
        }
        MessageStatusType messageStatusType = MessageStatusType.QUEUED;
        switch (messageStatus) {
            case Logger.SEVERE /*1*/:
                messageStatusType = MessageStatusType.SENDING;
                break;
            case Logger.WARNING /*2*/:
                messageStatusType = MessageStatusType.SENT;
                break;
            case Logger.INFO /*3*/:
                messageStatusType = MessageStatusType.DELIVERED;
                break;
            case Logger.CONFIG /*4*/:
                messageStatusType = MessageStatusType.SEEN;
                break;
            case Logger.FINE /*5*/:
                messageStatusType = MessageStatusType.FAILED;
                break;
        }
        ChatMessage c = new ChatMessage(messageId, threadId, messageContentType, textMessage, blobMessage, messageDateTime, deliverdDateTime, myMessageType, packetId, sender, recipient, description, longitude, latitude, sendersMobileNumber, fileSize, uploadedPercentage, fileUrl, messageStatusType, lastUpdatedDateTime, uploadedFileUrl);
        c.user = user;
        return c;
    }

    public static MessageContentType readMessageContentType(int type) {
        MessageContentType messageType = MessageContentType.TEXT;
        switch (type) {
            case Logger.SEVERE /*1*/:
                return MessageContentType.IMAGE;
            case Logger.WARNING /*2*/:
                return MessageContentType.STICKER;
            case Logger.INFO /*3*/:
                return MessageContentType.VOICE_RECORD;
            case Logger.CONFIG /*4*/:
                return MessageContentType.FAVORITE;
            case Logger.FINE /*5*/:
                return MessageContentType.MESSAGE_WITH_IMOTICONS;
            case Logger.FINER /*6*/:
                return MessageContentType.LOCATION;
            case Logger.FINEST /*7*/:
                return MessageContentType.INCOMING_CALL;
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                return MessageContentType.OUTGOING_CALL;
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                return MessageContentType.VIDEO;
            case C0473R.styleable.SwitchButton_onColor /*10*/:
                return MessageContentType.MISSED_CALL;
            case C0473R.styleable.SwitchButton_offColor /*11*/:
                return MessageContentType.GROUP_INFO;
            default:
                return messageType;
        }
    }

    public static ArrayList<ChatMessage> loadDataSet$4f484b25(String packetId, String threadId, int appendDirection, int limit, String descAsc) {
        User me = getUser(SHAMChatApplication.getConfig().userId);
        ArrayList<ChatMessage> listMsg = new ArrayList();
        if (me != null) {
            int _id = 0;
            SQLiteDatabase db = new ChatDatabaseHelper(SHAMChatApplication.getMyApplicationContext()).getReadableDatabase();
            Cursor cursor = db.query("chat_message", new String[]{"_id"}, "packet_id=? OR _id=?", new String[]{packetId, packetId}, null, null, "_id");
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                _id = cursor.getInt(cursor.getColumnIndex("_id"));
            }
            String[] selectionArgs = new String[]{threadId, String.valueOf(_id)};
            String operator = appendDirection == 0 ? "<" : ">";
            Log.i("scrolldown", "loading from packet number to bottom: " + String.valueOf(_id));
            cursor = db.rawQuery("SELECT * FROM (select * from chat_message where thread_id=?) alias_name WHERE _id " + operator + " ? ORDER BY _id " + descAsc + " limit 0," + limit, selectionArgs);
            if (cursor.moveToFirst()) {
                do {
                    ChatMessage chatMessage = getChatMessageByCursor(cursor);
                    String sender = chatMessage.sender;
                    System.out.println("chat message sender " + chatMessage.sender);
                    if (me.userId.equals(sender)) {
                        chatMessage.user = me;
                    } else {
                        if (!sender.startsWith("g")) {
                            chatMessage.user = getUser(chatMessage.sender);
                        }
                    }
                    listMsg.add(chatMessage);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        return listMsg;
    }

    public static boolean isLastRecord(String packetId, String threadId, int direction) {
        SQLiteDatabase db = new ChatDatabaseHelper(SHAMChatApplication.getMyApplicationContext()).getReadableDatabase();
        String lastMessagepacketId = null;
        Cursor cursor = db.rawQuery("select " + (direction == 0 ? "min" : "max") + "(_id),_id,packet_id from chat_message where thread_id=?", new String[]{threadId});
        if (cursor.moveToFirst()) {
            do {
                cursor.getInt(cursor.getColumnIndex("_id"));
                lastMessagepacketId = cursor.getString(cursor.getColumnIndex("packet_id"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        if (lastMessagepacketId.equals(packetId)) {
            return true;
        }
        return false;
    }

    public static boolean previousToLastRecord$3b99ba1e(String packetId, String threadId) {
        SQLiteDatabase db = new ChatDatabaseHelper(SHAMChatApplication.getMyApplicationContext()).getReadableDatabase();
        String beforeLastMessagepacketId = null;
        Cursor cursor = db.rawQuery("select _id,packet_id from chat_message where thread_id=?  ORDER BY _id " + "Desc" + " limit 1,1", new String[]{threadId});
        if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndex("_id"));
            beforeLastMessagepacketId = cursor.getString(cursor.getColumnIndex("packet_id"));
        }
        cursor.close();
        db.close();
        if (beforeLastMessagepacketId.equals(packetId)) {
            return true;
        }
        return false;
    }

    public static int getUnreadMessagesCount(String threadId) {
        Cursor unreadMessagesCursor = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(CONTENT_URI_CHAT, new String[]{"packet_id"}, "message_status=? AND message_type=? AND thread_id=?", new String[]{MessageStatusType.QUEUED.ordinal(), MyMessageType.INCOMING_MSG.ordinal(), threadId}, null);
        unreadMessagesCursor.close();
        int count = unreadMessagesCursor.getCount();
        unreadMessagesCursor.close();
        return count;
    }

    public static User getUser(String userId) {
        Throwable th;
        ContentResolver contentResolver = SHAMChatApplication.getMyApplicationContext().getContentResolver();
        Cursor friendCursor = null;
        Cursor rosterCursor = null;
        User user = null;
        try {
            friendCursor = contentResolver.query(UserProvider.CONTENT_URI_USER, null, "userId=?", new String[]{userId}, null);
            friendCursor.moveToFirst();
            String friendJID = Utils.createXmppUserIdByUserId(userId);
            rosterCursor = contentResolver.query(RosterProvider.CONTENT_URI, null, "jid=?", new String[]{friendJID}, null);
            if (friendCursor != null && friendCursor.getCount() == 1) {
                User user2 = new User();
                try {
                    user2.userId = userId;
                    user2.username = friendCursor.getString(friendCursor.getColumnIndex("name"));
                    user2.mobileNo = friendCursor.getString(friendCursor.getColumnIndex("mobileNo"));
                    user2.chatId = friendCursor.getString(friendCursor.getColumnIndex("chatId"));
                    user = user2;
                } catch (Throwable th2) {
                    th = th2;
                    user = user2;
                    friendCursor.close();
                    rosterCursor.close();
                    throw th;
                }
            }
            friendCursor.close();
            rosterCursor.close();
            return user;
        } catch (Throwable th3) {
            th = th3;
            friendCursor.close();
            rosterCursor.close();
            throw th;
        }
    }

    public static Message favoriteToCursor(Cursor cursor) {
        Message message = new Message();
        message.messageId = cursor.getString(cursor.getColumnIndex(MessageDetailsActivity.EXTRA_MESSAGE_ID));
        int type = cursor.getInt(cursor.getColumnIndex("type"));
        for (MessageType messageType : MessageType.values()) {
            if (messageType.type == type) {
                message.type = messageType;
                break;
            }
        }
        message.messageContent = cursor.getString(cursor.getColumnIndex("content"));
        message.time = cursor.getLong(cursor.getColumnIndex("time"));
        message.userId = cursor.getString(cursor.getColumnIndex(ChatActivity.INTENT_EXTRA_USER_ID));
        return message;
    }

    public static void insertFavorite(Message message) {
        SQLiteDatabase db = new ChatDatabaseHelper(SHAMChatApplication.getMyApplicationContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MessageDetailsActivity.EXTRA_MESSAGE_ID, message.messageId);
        values.put("type", Integer.valueOf(message.type.type));
        values.put("content", message.messageContent);
        values.put("time", Long.valueOf(message.time));
        values.put(ChatActivity.INTENT_EXTRA_USER_ID, message.userId);
        long row = db.insert("favorite", null, values);
        if (row == -1) {
            Log.e("zamin.ChatProviderNew", "Error inserting Message(" + message + ")");
        }
        Log.v("zamin.ChatProviderNew", "Inserted new Message with id '" + message.messageId + "' at rowid " + row);
        db.close();
    }

    public static void updateFavorite(Message message) {
        SQLiteDatabase db = new ChatDatabaseHelper(SHAMChatApplication.getMyApplicationContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MessageDetailsActivity.EXTRA_MESSAGE_ID, message.messageId);
        values.put("type", Integer.valueOf(message.type.type));
        values.put("content", message.messageContent);
        values.put("time", Long.valueOf(message.time));
        values.put(ChatActivity.INTENT_EXTRA_USER_ID, message.userId);
        if (((long) db.update("favorite", values, "message_id=?", new String[]{message.messageId})) < 1) {
            Log.e("zamin.ChatProviderNew", "Error updating Message(" + message + ")");
        }
        db.close();
    }

    public static boolean removeFavorite(String messageId) {
        SQLiteDatabase db = new ChatDatabaseHelper(SHAMChatApplication.getMyApplicationContext()).getWritableDatabase();
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("is_deleted", Integer.valueOf(1));
            rows = db.update("favorite", values, "message_id = ?", new String[]{messageId});
        } catch (Exception e) {
            Log.w("zamin.ChatProviderNew", "Couldn't remove message with id '" + messageId + "' (not found)");
            e.printStackTrace();
        } finally {
            db.close();
        }
        if (rows >= 0) {
            return true;
        }
        return false;
    }

    public static Message getFavorite(String messageId) {
        SQLiteDatabase db = new ChatDatabaseHelper(SHAMChatApplication.getMyApplicationContext()).getWritableDatabase();
        Cursor cursor = db.query("favorite", null, "message_id=?", new String[]{messageId}, null, null, null);
        Message message = null;
        if (cursor.moveToFirst()) {
            message = favoriteToCursor(cursor);
        }
        cursor.close();
        db.close();
        return message;
    }

    public static String geLasttFavoriteMessageId() {
        SQLiteDatabase db = new ChatDatabaseHelper(SHAMChatApplication.getMyApplicationContext()).getWritableDatabase();
        String messageId = null;
        Cursor cursor = db.query("favorite", new String[]{MessageDetailsActivity.EXTRA_MESSAGE_ID}, null, null, null, null, "message_id DESC", "1");
        if (cursor.moveToFirst()) {
            messageId = cursor.getString(cursor.getColumnIndex(MessageDetailsActivity.EXTRA_MESSAGE_ID));
        }
        cursor.close();
        db.close();
        return messageId;
    }

    public static List<MessageThread> getChatThreadsSorted() {
        ChatDatabaseHelper databaseHelper = new ChatDatabaseHelper(SHAMChatApplication.getMyApplicationContext());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String USER_DATABASE_PATH = databaseHelper.USER_DATABASE_PATH;
        List<MessageThread> messageThreads = new ArrayList();
        db.execSQL("ATTACH DATABASE '" + USER_DATABASE_PATH + "' AS user");
        Cursor cursor = db.rawQuery("SELECT  friend_group_name as username,\n(SELECT COUNT(*) FROM chat_message WHERE chat_message.thread_id = message_thread.thread_id AND chat_message.message_status=0 AND chat_message.message_type=1) AS count,\nfriend_id, thread_id, is_group_chat, last_message, last_message_direction, last_updated_datetime, last_message_content_type\nFROM message_thread\n INNER JOIN user.friend_group\n      ON message_thread.friend_id = user.friend_group.friend_group_id\nUNION ALL\nSELECT  name,\n(SELECT COUNT(*) FROM chat_message WHERE chat_message.thread_id = message_thread.thread_id AND chat_message.message_status=0 AND chat_message.message_type=1) AS count,\nfriend_id, thread_id, is_group_chat, last_message, last_message_direction, last_updated_datetime, last_message_content_type\nFROM message_thread\n INNER JOIN (SELECT * \n             FROM user.user \n             GROUP BY userId \n             ORDER BY userId ) O ON friend_id = userId\nORDER BY last_updated_datetime DESC\n", null);
        if (cursor.moveToFirst()) {
            do {
                MessageThread messageThread = new MessageThread();
                messageThread.threadOwner = SHAMChatApplication.getConfig().userId;
                messageThread.friendId = cursor.getString(cursor.getColumnIndex("friend_id"));
                messageThread.threadId = cursor.getString(cursor.getColumnIndex(ChatInitialForGroupChatActivity.INTENT_EXTRA_THREAD_ID));
                messageThread.isGroupChat = cursor.getInt(cursor.getColumnIndex("is_group_chat")) == 1;
                messageThread.lastMessage = cursor.getString(cursor.getColumnIndex("last_message"));
                messageThread.lastMessageDirection = cursor.getInt(cursor.getColumnIndex("last_message_direction"));
                messageThread.lastUpdatedDate = Utils.getDateFromStringDate(cursor.getString(cursor.getColumnIndex("last_updated_datetime")), "yyyy/MM/dd hh:mm");
                messageThread.lastMessageMedium = cursor.getInt(cursor.getColumnIndex("last_message_content_type"));
                messageThread.username = cursor.getString(cursor.getColumnIndex("username"));
                messageThread.messageCount = cursor.getInt(cursor.getColumnIndex("count"));
                messageThreads.add(messageThread);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return messageThreads;
    }
}
