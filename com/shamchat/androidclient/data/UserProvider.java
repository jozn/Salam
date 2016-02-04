package com.shamchat.androidclient.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.kyleduo.switchbutton.C0473R;
import com.shamchat.activity.ChatActivity;
import com.shamchat.androidclient.SHAMChatApplication;
import com.shamchat.androidclient.service.SmackableImp;
import com.shamchat.androidclient.util.PreferenceConstants.AllowDeniedStatus;
import com.shamchat.androidclient.util.PreferenceConstants.EnableDisableStatus;
import com.shamchat.androidclient.util.PreferenceConstants.FeatureAlertStatus;
import com.shamchat.models.FriendGroup;
import com.shamchat.models.FriendGroupMember;
import com.shamchat.models.User;
import com.shamchat.models.User.BooleanStatus;
import com.shamchat.models.UserNotification;
import com.shamchat.utils.Utils;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.conn.scheme.PlainSocketFactory;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.impl.conn.tsccm.ThreadSafeClientConnManager;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpParams;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.json.JSONObject;

public class UserProvider extends ContentProvider {
    public static final Uri CONTENT_URI_GROUP;
    public static final Uri CONTENT_URI_GROUP_MEMBER;
    public static final Uri CONTENT_URI_NOTIFICATION;
    public static final Uri CONTENT_URI_USER;
    private static final UriMatcher URI_MATCHER;
    private SQLiteOpenHelper mOpenHelper;

    /* renamed from: com.shamchat.androidclient.data.UserProvider.1 */
    static /* synthetic */ class C10681 {
        static final /* synthetic */ int[] f25xc22fdcb9;

        static {
            f25xc22fdcb9 = new int[UserNotificationUpdateType.values$1577f3ac().length];
            try {
                f25xc22fdcb9[UserNotificationUpdateType.NOTIFICATION_SOUND$16247172 - 1] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f25xc22fdcb9[UserNotificationUpdateType.NOTIFICATION_TIMING_START$16247172 - 1] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f25xc22fdcb9[UserNotificationUpdateType.NOTIFICATION_TIMING_END$16247172 - 1] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f25xc22fdcb9[UserNotificationUpdateType.MESSAGE_ALERT_STATUS$16247172 - 1] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f25xc22fdcb9[UserNotificationUpdateType.MOVEMENT_UPDATE_STATUS$16247172 - 1] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f25xc22fdcb9[UserNotificationUpdateType.OTHER_FEATURE_ALERT_STATUS$16247172 - 1] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f25xc22fdcb9[UserNotificationUpdateType.SOUND_ALERT_STATUS$16247172 - 1] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f25xc22fdcb9[UserNotificationUpdateType.VIBRATE_STATUS$16247172 - 1] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    public static final class GroupConstants implements BaseColumns {
        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList();
            tmpList.add(FriendGroup.DB_RECORD_OWNER);
            return tmpList;
        }
    }

    public static final class GroupMemberConstants implements BaseColumns {
        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList();
            tmpList.add(FriendGroupMember.DB_GROUP);
            return tmpList;
        }
    }

    public static final class NotificationConstants implements BaseColumns {
        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList();
            tmpList.add(ChatActivity.INTENT_EXTRA_USER_ID);
            return tmpList;
        }
    }

    public static final class UserConstants implements BaseColumns {
        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList();
            tmpList.add("userId");
            return tmpList;
        }
    }

    public static class UserDatabaseHelper extends SQLiteOpenHelper {
        public UserDatabaseHelper(Context context) {
            super(context, "user.db", null, 8);
        }

        public final void onCreate(SQLiteDatabase db) {
            Log.i("zamin.UserProvider", "creating new user table");
            db.execSQL("CREATE TABLE IF NOT EXISTS user ( _id INTEGER PRIMARY KEY AUTOINCREMENT,userId TEXT NOT NULL,chatId TEXT,name TEXT,gender TEXT,profileImageBytes TEXT,onlineStatus TEXT  NOT NULL DEFAULT Offline,email TEXT,emailVerificationStatus TEXT,inAppAlert TEXT,myStatus TEXT, mobileNo TEXT, region TEXT, tempUserId TEXT,newMessageAlert TEXT, cover_photo_byte BLOB, jabberd_resource TEXT, find_me_by_mobile_no INTEGER NOT NULL DEFAULT 1,is_added_to_roster INTEGER NOT NULL DEFAULT 1,is_vcard_downloaded INTEGER NOT NULL DEFAULT 1,profileimage_url TEXT, user_type INTEGER NOT NULL DEFAULT 1, find_me_by_chat_id INTEGER NOT NULL DEFAULT 1);");
            db.execSQL("CREATE TABLE IF NOT EXISTS friend_group (_id INTEGER PRIMARY KEY AUTOINCREMENT," + FriendGroup.DB_ID + " TEXT ," + FriendGroup.DB_NAME + " TEXT," + FriendGroup.CHAT_ROOM_NAME + " TEXT," + FriendGroup.DID_JOIN_ROOM + " INTEGER NOT NULL DEFAULT 0, " + FriendGroup.DID_LEAVE + " INTEGER NOT NULL DEFAULT 0, " + FriendGroup.DB_RECORD_OWNER + " TEXT);");
            db.execSQL("CREATE TABLE IF NOT EXISTS friend_group_member (_id INTEGER PRIMARY KEY AUTOINCREMENT," + FriendGroupMember.DB_ID + " TEXT ," + FriendGroupMember.DB_GROUP + " TEXT," + FriendGroupMember.DB_FRIEND + " TEXT , " + FriendGroupMember.DB_FRIEND_DID_JOIN + " INTEGER NOT NULL DEFAULT 0," + FriendGroupMember.DB_FRIEND_IS_ADMIN + " INTEGER NOT NULL DEFAULT 0, " + FriendGroupMember.PHONE_NUMBER + " TEXT);");
            db.execSQL("CREATE TABLE user_notification (_id INTEGER PRIMARY KEY AUTOINCREMENT,user_id TEXT NOT NULL  UNIQUE , message_alert_status INTEGER NOT NULL  DEFAULT 1, sound_alert_status INTEGER NOT NULL  DEFAULT 1, vibrate_status INTEGER NOT NULL  DEFAULT 0, notification_sound TEXT, notification_timing_start TEXT NOT NULL DEFAULT '0', notification_timing_end TEXT NOT NULL DEFAULT '0', other_further_alert_status INTEGER NOT NULL  DEFAULT 1, movement_update_status INTEGER NOT NULL  DEFAULT 1)");
        }

        public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("zamin.UserProvider", "onUpgrade: from " + oldVersion + " to " + newVersion);
            switch (oldVersion) {
                case Logger.FINE /*5*/:
                    db.execSQL("ALTER TABLE friend_group_member ADD " + FriendGroupMember.DB_FRIEND_DID_JOIN + " INTEGER NOT NULL DEFAULT 0 ");
                    db.execSQL("ALTER TABLE friend_group_member ADD " + FriendGroupMember.DB_FRIEND_IS_ADMIN + " INTEGER NOT NULL DEFAULT 0 ");
                    db.execSQL("ALTER TABLE friend_group_member ADD " + FriendGroupMember.PHONE_NUMBER + " TEXT ");
                case Logger.FINER /*6*/:
                    db.execSQL("ALTER TABLE friend_group_member ADD " + FriendGroupMember.PHONE_NUMBER + " TEXT ");
                case Logger.FINEST /*7*/:
                    db.execSQL("ALTER TABLE friend_group_member ADD " + FriendGroupMember.PHONE_NUMBER + " TEXT ");
                default:
            }
        }

        public final ArrayList<Cursor> getData(String Query) {
            SQLiteDatabase sqlDB = getWritableDatabase();
            String[] columns = new String[]{"mesage"};
            ArrayList<Cursor> alc = new ArrayList(2);
            MatrixCursor Cursor2 = new MatrixCursor(columns);
            alc.add(null);
            alc.add(null);
            try {
                Cursor c = sqlDB.rawQuery(Query, null);
                Cursor2.addRow(new Object[]{"Success"});
                alc.set(1, Cursor2);
                if (c != null && c.getCount() > 0) {
                    alc.set(0, c);
                    c.moveToFirst();
                }
            } catch (SQLException sqlEx) {
                Log.d("printing exception", sqlEx.getMessage());
                Cursor2.addRow(new Object[]{sqlEx.getMessage()});
                alc.set(1, Cursor2);
            } catch (Exception ex) {
                Log.d("printing exception", ex.getMessage());
                Cursor2.addRow(new Object[]{ex.getMessage()});
                alc.set(1, Cursor2);
            }
            return alc;
        }
    }

    public enum UserNotificationUpdateType {
        ;

        public static int[] values$1577f3ac() {
            return (int[]) $VALUES$50041c93.clone();
        }

        static {
            NOTIFICATION_SOUND$16247172 = 1;
            NOTIFICATION_TIMING_START$16247172 = 2;
            NOTIFICATION_TIMING_END$16247172 = 3;
            MESSAGE_ALERT_STATUS$16247172 = 4;
            MOVEMENT_UPDATE_STATUS$16247172 = 5;
            OTHER_FEATURE_ALERT_STATUS$16247172 = 6;
            SOUND_ALERT_STATUS$16247172 = 7;
            VIBRATE_STATUS$16247172 = 8;
            $VALUES$50041c93 = new int[]{NOTIFICATION_SOUND$16247172, NOTIFICATION_TIMING_START$16247172, NOTIFICATION_TIMING_END$16247172, MESSAGE_ALERT_STATUS$16247172, MOVEMENT_UPDATE_STATUS$16247172, OTHER_FEATURE_ALERT_STATUS$16247172, SOUND_ALERT_STATUS$16247172, VIBRATE_STATUS$16247172};
        }
    }

    static {
        CONTENT_URI_USER = Uri.parse("content://org.zamin.androidclient.provider.Users/user");
        CONTENT_URI_GROUP = Uri.parse("content://org.zamin.androidclient.provider.Users/friend_group");
        CONTENT_URI_GROUP_MEMBER = Uri.parse("content://org.zamin.androidclient.provider.Users/friend_group_member");
        CONTENT_URI_NOTIFICATION = Uri.parse("content://org.zamin.androidclient.provider.Users/user_notification");
        UriMatcher uriMatcher = new UriMatcher(-1);
        URI_MATCHER = uriMatcher;
        uriMatcher.addURI("org.zamin.androidclient.provider.Users", "user", 1);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Users", "user/#", 2);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Users", "friend_group", 3);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Users", "friend_group/#", 4);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Users", "friend_group_member", 5);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Users", "friend_group_member/#", 6);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Users", "user_notification", 7);
    }

    public int delete(Uri url, String where, String[] whereArgs) {
        int count;
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        String segment;
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                count = db.delete("user", where, whereArgs);
                break;
            case Logger.WARNING /*2*/:
                segment = (String) url.getPathSegments().get(1);
                if (TextUtils.isEmpty(where)) {
                    where = "_id=" + segment;
                } else {
                    where = "_id=" + segment + " AND (" + where + ")";
                }
                count = db.delete("user", where, whereArgs);
                break;
            case Logger.INFO /*3*/:
                count = db.delete("friend_group", where, whereArgs);
                break;
            case Logger.CONFIG /*4*/:
                segment = (String) url.getPathSegments().get(1);
                if (TextUtils.isEmpty(where)) {
                    where = "_id=" + segment;
                } else {
                    where = "_id=" + segment + " AND (" + where + ")";
                }
                count = db.delete("friend_group", where, whereArgs);
                break;
            case Logger.FINE /*5*/:
                count = db.delete("friend_group_member", where, whereArgs);
                break;
            case Logger.FINER /*6*/:
                segment = (String) url.getPathSegments().get(1);
                if (TextUtils.isEmpty(where)) {
                    where = "_id=" + segment;
                } else {
                    where = "_id=" + segment + " AND (" + where + ")";
                }
                count = db.delete("friend_group_member", where, whereArgs);
                break;
            case Logger.FINEST /*7*/:
                count = db.delete("user_notification", where, whereArgs);
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
                return "vnd.android.cursor.dir/vnd.zamin.user";
            case Logger.WARNING /*2*/:
                return "vnd.android.cursor.item/vnd.zamin.user";
            case Logger.INFO /*3*/:
                return "vnd.android.cursor.dir/vnd.zamin.groups";
            case Logger.CONFIG /*4*/:
                return "vnd.android.cursor.item/vnd.zamin.groups";
            case Logger.FINE /*5*/:
                return "vnd.android.cursor.dir/vnd.zamin.groupmember";
            case Logger.FINER /*6*/:
                return "vnd.android.cursor.item/vnd.zamin.groupmember";
            case Logger.FINEST /*7*/:
                return "vnd.android.cursor.dir/vnd.zamin.notification";
            default:
                throw new IllegalArgumentException("Unknown URL");
        }
    }

    public Uri insert(Uri url, ContentValues initialValues) {
        if (URI_MATCHER.match(url) == 1 || URI_MATCHER.match(url) == 3 || URI_MATCHER.match(url) == 5 || URI_MATCHER.match(url) == 7) {
            SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
            ContentValues values = initialValues != null ? new ContentValues(initialValues) : new ContentValues();
            Iterator it;
            String colName;
            long rowId;
            Uri noteUri;
            switch (URI_MATCHER.match(url)) {
                case Logger.SEVERE /*1*/:
                    it = UserConstants.getRequiredColumns().iterator();
                    while (it.hasNext()) {
                        colName = (String) it.next();
                        if (!values.containsKey(colName)) {
                            throw new IllegalArgumentException("Missing column: " + colName);
                        }
                    }
                    rowId = db.insert("user", BuildConfig.VERSION_NAME, values);
                    if (rowId < 0) {
                        throw new SQLException("Failed to insert row into " + url);
                    }
                    noteUri = ContentUris.withAppendedId(CONTENT_URI_USER, rowId);
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                case Logger.INFO /*3*/:
                    it = GroupConstants.getRequiredColumns().iterator();
                    while (it.hasNext()) {
                        colName = (String) it.next();
                        if (!values.containsKey(colName)) {
                            throw new IllegalArgumentException("Missing column: " + colName);
                        }
                    }
                    rowId = db.insert("friend_group", BuildConfig.VERSION_NAME, values);
                    if (rowId < 0) {
                        throw new SQLException("Failed to insert row into Group" + url);
                    }
                    if (rowId > 0) {
                        Log.d("zamin.UserProvider", "Group successfully entered at " + rowId);
                    }
                    noteUri = ContentUris.withAppendedId(CONTENT_URI_GROUP, rowId);
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                case Logger.FINE /*5*/:
                    it = GroupMemberConstants.getRequiredColumns().iterator();
                    while (it.hasNext()) {
                        colName = (String) it.next();
                        if (!values.containsKey(colName)) {
                            throw new IllegalArgumentException("Missing column: " + colName);
                        }
                    }
                    rowId = db.insert("friend_group_member", BuildConfig.VERSION_NAME, values);
                    if (rowId < 0) {
                        throw new SQLException("Failed to insert row into group_members" + url);
                    }
                    if (rowId > 0) {
                        Log.d("zamin.UserProvider", "Group Members successfully entered at " + rowId);
                    }
                    noteUri = ContentUris.withAppendedId(CONTENT_URI_GROUP_MEMBER, rowId);
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                case Logger.FINEST /*7*/:
                    it = NotificationConstants.getRequiredColumns().iterator();
                    while (it.hasNext()) {
                        colName = (String) it.next();
                        if (!values.containsKey(colName)) {
                            throw new IllegalArgumentException("Missing column: " + colName);
                        }
                    }
                    rowId = db.insert("user_notification", BuildConfig.VERSION_NAME, values);
                    if (rowId < 0) {
                        throw new SQLException("Failed to insert row into group_members" + url);
                    }
                    if (rowId > 0) {
                        Log.d("zamin.UserProvider", "Group Members successfully entered at " + rowId);
                    }
                    noteUri = ContentUris.withAppendedId(CONTENT_URI_NOTIFICATION, rowId);
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                default:
                    throw new IllegalArgumentException("Cannot insert from URL: " + url);
            }
        }
        throw new IllegalArgumentException("Cannot insert into URL: " + url);
    }

    public boolean onCreate() {
        this.mOpenHelper = new UserDatabaseHelper(getContext());
        return true;
    }

    public int bulkInsert(Uri uri, ContentValues[] values) {
        int i = 0;
        if (URI_MATCHER.match(uri) == 1 || URI_MATCHER.match(uri) == 3 || URI_MATCHER.match(uri) == 5 || URI_MATCHER.match(uri) == 7) {
            int rowId = 0;
            SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
            int length;
            switch (URI_MATCHER.match(uri)) {
                case Logger.SEVERE /*1*/:
                    db.beginTransaction();
                    length = values.length;
                    while (i < length) {
                        rowId += (int) db.insert("user", BuildConfig.VERSION_NAME, values[i]);
                        i++;
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    getContext().getContentResolver().notifyChange(CONTENT_URI_USER, null);
                    break;
                case Logger.INFO /*3*/:
                    db.beginTransaction();
                    length = values.length;
                    while (i < length) {
                        rowId += (int) db.insert("friend_group", BuildConfig.VERSION_NAME, values[i]);
                        i++;
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    getContext().getContentResolver().notifyChange(CONTENT_URI_GROUP, null);
                    break;
                case Logger.FINE /*5*/:
                    db.beginTransaction();
                    length = values.length;
                    while (i < length) {
                        rowId += (int) db.insert("friend_group_member", BuildConfig.VERSION_NAME, values[i]);
                        i++;
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    getContext().getContentResolver().notifyChange(CONTENT_URI_GROUP_MEMBER, null);
                    break;
                case Logger.FINEST /*7*/:
                    db.beginTransaction();
                    length = values.length;
                    while (i < length) {
                        rowId += (int) db.insert("user_notification", BuildConfig.VERSION_NAME, values[i]);
                        i++;
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    getContext().getContentResolver().notifyChange(CONTENT_URI_NOTIFICATION, null);
                    break;
            }
            return rowId;
        }
        throw new IllegalArgumentException("Cannot insert into URL: " + uri);
    }

    public Cursor query(Uri url, String[] projectionIn, String selection, String[] selectionArgs, String sortOrder) {
        String orderBy;
        Cursor ret;
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
        boolean isDistinct = false;
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                qBuilder.setTables("user");
                isDistinct = true;
                break;
            case Logger.WARNING /*2*/:
                qBuilder.setTables("user");
                qBuilder.appendWhere("userId=");
                qBuilder.appendWhere((CharSequence) url.getPathSegments().get(1));
                break;
            case Logger.INFO /*3*/:
                qBuilder.setTables("friend_group");
                break;
            case Logger.CONFIG /*4*/:
                qBuilder.setTables("friend_group");
                qBuilder.appendWhere("_id=");
                qBuilder.appendWhere((CharSequence) url.getPathSegments().get(1));
                break;
            case Logger.FINE /*5*/:
                qBuilder.setTables("friend_group_member");
                break;
            case Logger.FINER /*6*/:
                qBuilder.setTables("friend_group_member");
                qBuilder.appendWhere("_id=");
                qBuilder.appendWhere((CharSequence) url.getPathSegments().get(1));
                break;
            case Logger.FINEST /*7*/:
                qBuilder.setTables("user_notification");
                break;
            default:
                throw new IllegalArgumentException("Unknown URL " + url);
        }
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = "_id ASC";
        } else {
            orderBy = sortOrder;
        }
        SQLiteDatabase db = this.mOpenHelper.getReadableDatabase();
        if (isDistinct) {
            ret = db.query(true, "user", projectionIn, selection, selectionArgs, "name", null, "name COLLATE NOCASE ASC", null);
        } else {
            ret = qBuilder.query(db, projectionIn, selection, selectionArgs, null, null, orderBy);
        }
        if (ret == null) {
            Log.i("zamin.UserProvider", "UserProvider.query: failed");
        } else {
            ret.setNotificationUri(getContext().getContentResolver(), url);
        }
        return ret;
    }

    public int update(Uri url, ContentValues values, String where, String[] whereArgs) {
        int count;
        long rowId = 0;
        int match = URI_MATCHER.match(url);
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        switch (match) {
            case Logger.SEVERE /*1*/:
                count = db.update("user", values, where, whereArgs);
                break;
            case Logger.WARNING /*2*/:
                rowId = Long.parseLong((String) url.getPathSegments().get(1));
                count = db.update("user", values, "userId=" + rowId, null);
                if (count > 0) {
                    Log.d("zamin.UserProvider", "User successfully updated " + rowId);
                    break;
                }
                break;
            case Logger.INFO /*3*/:
                count = db.update("friend_group", values, where, whereArgs);
                if (count > 0) {
                    Log.d("zamin.UserProvider", "Message successfully updated " + count);
                    break;
                }
                break;
            case Logger.CONFIG /*4*/:
                rowId = Long.parseLong((String) url.getPathSegments().get(1));
                count = db.update("friend_group", values, "_id=" + rowId, null);
                break;
            case Logger.FINE /*5*/:
                count = db.update("friend_group_member", values, where, whereArgs);
                if (count > 0) {
                    Log.d("zamin.UserProvider", "Thread successfully updated " + count);
                    break;
                }
                break;
            case Logger.FINER /*6*/:
                rowId = Long.parseLong((String) url.getPathSegments().get(1));
                count = db.update("friend_group_member", values, where, whereArgs);
                break;
            case Logger.FINEST /*7*/:
                count = db.update("user_notification", values, where, whereArgs);
                if (count > 0) {
                    Log.d("zamin.UserProvider", "Message successfully updated " + count);
                    break;
                }
                break;
            default:
                throw new UnsupportedOperationException("Cannot update URL: " + url);
        }
        Log.i("zamin.UserProvider", "*** notifyChange() rowId: " + rowId + " url " + url);
        getContext().getContentResolver().notifyChange(url, null);
        return count;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap getProfileImageByUserId(java.lang.String r10) {
        /*
        r4 = 1;
        r5 = 0;
        r6 = 0;
        r0 = com.shamchat.androidclient.SHAMChatApplication.getMyApplicationContext();
        r0 = r0.getContentResolver();
        r1 = CONTENT_URI_USER;
        r2 = new java.lang.String[r4];
        r3 = "profileImageBytes";
        r2[r5] = r3;
        r3 = "userId=?";
        r4 = new java.lang.String[r4];
        r4[r5] = r10;
        r5 = 0;
        r7 = r0.query(r1, r2, r3, r4, r5);
        r7.moveToFirst();
        r9 = new java.io.File;	 Catch:{ Exception -> 0x0059 }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0059 }
        r0.<init>();	 Catch:{ Exception -> 0x0059 }
        r1 = android.os.Environment.getExternalStorageDirectory();	 Catch:{ Exception -> 0x0059 }
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0059 }
        r1 = "/salam/thumbnails/";
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0059 }
        r0 = r0.append(r10);	 Catch:{ Exception -> 0x0059 }
        r1 = ".jpg";
        r0 = r0.append(r1);	 Catch:{ Exception -> 0x0059 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x0059 }
        r9.<init>(r0);	 Catch:{ Exception -> 0x0059 }
        r0 = r9.exists();	 Catch:{ Exception -> 0x0059 }
        if (r0 == 0) goto L_0x0055;
    L_0x004d:
        r0 = r9.getAbsolutePath();	 Catch:{ Exception -> 0x0059 }
        r6 = android.graphics.BitmapFactory.decodeFile(r0);	 Catch:{ Exception -> 0x0059 }
    L_0x0055:
        r7.close();
    L_0x0058:
        return r6;
    L_0x0059:
        r8 = move-exception;
        r0 = java.lang.System.out;	 Catch:{ all -> 0x0072 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0072 }
        r2 = "getProfileImageByUserId ";
        r1.<init>(r2);	 Catch:{ all -> 0x0072 }
        r1 = r1.append(r8);	 Catch:{ all -> 0x0072 }
        r1 = r1.toString();	 Catch:{ all -> 0x0072 }
        r0.println(r1);	 Catch:{ all -> 0x0072 }
        r7.close();
        goto L_0x0058;
    L_0x0072:
        r0 = move-exception;
        r7.close();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shamchat.androidclient.data.UserProvider.getProfileImageByUserId(java.lang.String):android.graphics.Bitmap");
    }

    public static Bitmap getMyProfileImage() {
        Cursor cursor = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(CONTENT_URI_USER, new String[]{"profileImageBytes"}, "userId=?", new String[]{SHAMChatApplication.getConfig().userId}, null);
        cursor.moveToFirst();
        try {
            Bitmap bitmap = Utils.base64ToBitmap(cursor.getString(cursor.getColumnIndex("profileImageBytes")));
            if (bitmap == null) {
                bitmap = getProfileImageByUserId(SHAMChatApplication.getConfig().userId);
            }
            cursor.close();
            return bitmap;
        } catch (Exception e) {
            cursor.close();
            return null;
        }
    }

    public static User userFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        User user = new User();
        user.userId = cursor.getString(cursor.getColumnIndex("userId"));
        user.username = cursor.getString(cursor.getColumnIndex("name"));
        user.chatId = cursor.getString(cursor.getColumnIndex("chatId"));
        user.mobileNo = cursor.getString(cursor.getColumnIndex("mobileNo"));
        user.email = cursor.getString(cursor.getColumnIndex(NotificationCompatApi21.CATEGORY_EMAIL));
        user.gender = cursor.getString(cursor.getColumnIndex("gender"));
        user.emailVerificationStatus = cursor.getString(cursor.getColumnIndex("emailVerificationStatus"));
        user.cityOrRegion = cursor.getString(cursor.getColumnIndex("region"));
        user.coverPhoto = cursor.getString(cursor.getColumnIndex("cover_photo_byte"));
        user.profileImageUrl = cursor.getString(cursor.getColumnIndex("profileimage_url"));
        user.dbRowId = String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")));
        return user;
    }

    public static List<User> usersFromCursor(Cursor cursor) {
        List<User> list = new ArrayList();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                list.add(userFromCursor(cursor));
            }
        }
        return list;
    }

    public static ArrayList<User> usersFromCursorArray(Cursor cursor) {
        ArrayList<User> array = new ArrayList();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                array.add(userFromCursor(cursor));
            }
        }
        return array;
    }

    public static Cursor getUsersInGroup$31479a3c(String groupId) {
        return new UserDatabaseHelper(SHAMChatApplication.getMyApplicationContext()).getWritableDatabase().query(true, "user", null, "userId IN (SELECT " + FriendGroupMember.DB_FRIEND + " FROM friend_group_member WHERE " + FriendGroupMember.DB_GROUP + "=?)", new String[]{groupId}, "name", null, "name ASC", null);
    }

    public static FriendGroup groupFromCursor(Cursor cursor) {
        FriendGroup group = new FriendGroup();
        cursor.moveToFirst();
        group.id = cursor.getString(cursor.getColumnIndex(FriendGroup.DB_ID));
        group.name = cursor.getString(cursor.getColumnIndex(FriendGroup.DB_NAME));
        group.recordOwnerId = cursor.getString(cursor.getColumnIndex(FriendGroup.DB_RECORD_OWNER));
        group.chatRoomName = cursor.getString(cursor.getColumnIndex(FriendGroup.CHAT_ROOM_NAME));
        group.dbRecordId = String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")));
        cursor.close();
        return group;
    }

    public static void updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put("name", user.username);
        values.put("chatId", user.chatId);
        values.put("mobileNo", user.mobileNo);
        values.put(NotificationCompatApi21.CATEGORY_EMAIL, user.email);
        values.put("gender", user.gender);
        values.put("profileImageBytes", user.profileImage);
        values.put("myStatus", user.myStatus);
        values.put("newMessageAlert", user.newMessageAlert);
        values.put("inAppAlert", user.inAppAlert);
        values.put("emailVerificationStatus", user.emailVerificationStatus);
        values.put("tempUserId", user.tmpUserId);
        values.put("region", user.cityOrRegion);
        values.put("jabberd_resource", user.jabberdResource);
        values.put("profileimage_url", user.profileImageUrl);
        if (!(user.findMeByPhoneNo == null || user.findMeByShamId == null)) {
            values.put("find_me_by_mobile_no", Integer.valueOf(user.findMeByPhoneNo.status));
            values.put("find_me_by_chat_id", Integer.valueOf(user.findMeByShamId.status));
        }
        SHAMChatApplication.getMyApplicationContext().getContentResolver().update(Uri.parse(CONTENT_URI_USER.toString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + user.userId), values, null, null);
    }

    public static User getCurrentUser() {
        Cursor cursor = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(Uri.parse(CONTENT_URI_USER.toString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + SHAMChatApplication.getConfig().userId), null, null, null, null);
        cursor.moveToFirst();
        User user = null;
        if (cursor != null && cursor.getCount() > 0) {
            user = new User();
            user.userId = cursor.getString(cursor.getColumnIndex("userId"));
            user.username = cursor.getString(cursor.getColumnIndex("name"));
            user.chatId = cursor.getString(cursor.getColumnIndex("chatId"));
            user.mobileNo = cursor.getString(cursor.getColumnIndex("mobileNo"));
            user.email = cursor.getString(cursor.getColumnIndex(NotificationCompatApi21.CATEGORY_EMAIL));
            user.gender = cursor.getString(cursor.getColumnIndex("gender"));
            user.profileImage = cursor.getString(cursor.getColumnIndex("profileImageBytes"));
            user.myStatus = cursor.getString(cursor.getColumnIndex("myStatus"));
            user.newMessageAlert = cursor.getString(cursor.getColumnIndex("newMessageAlert"));
            user.inAppAlert = cursor.getString(cursor.getColumnIndex("inAppAlert"));
            user.emailVerificationStatus = cursor.getString(cursor.getColumnIndex("emailVerificationStatus"));
            user.tmpUserId = cursor.getString(cursor.getColumnIndex("tempUserId"));
            user.cityOrRegion = cursor.getString(cursor.getColumnIndex("region"));
            user.coverPhoto = cursor.getString(cursor.getColumnIndex("cover_photo_byte"));
            user.jabberdResource = cursor.getString(cursor.getColumnIndex("jabberd_resource"));
            user.profileImageUrl = cursor.getString(cursor.getColumnIndex("profileimage_url"));
            BooleanStatus bool = BooleanStatus.FALSE;
            switch (cursor.getInt(cursor.getColumnIndex("find_me_by_mobile_no"))) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    bool = BooleanStatus.FALSE;
                    break;
                case Logger.SEVERE /*1*/:
                    bool = BooleanStatus.TRUE;
                    break;
            }
            user.findMeByPhoneNo = bool;
            BooleanStatus bool2 = BooleanStatus.FALSE;
            switch (cursor.getInt(cursor.getColumnIndex("find_me_by_chat_id"))) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    bool2 = BooleanStatus.FALSE;
                    break;
                case Logger.SEVERE /*1*/:
                    bool2 = BooleanStatus.TRUE;
                    break;
            }
            user.findMeByShamId = bool2;
        }
        return user;
    }

    public static User getCurrentUserForMyProfile() {
        Cursor cursor = SHAMChatApplication.getMyApplicationContext().getContentResolver().query(Uri.parse(CONTENT_URI_USER.toString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + SHAMChatApplication.getConfig().userId), null, null, null, null);
        cursor.moveToFirst();
        User user = null;
        if (cursor != null && cursor.getCount() > 0) {
            user = new User();
            user.userId = cursor.getString(cursor.getColumnIndex("userId"));
            user.username = cursor.getString(cursor.getColumnIndex("name"));
            user.chatId = cursor.getString(cursor.getColumnIndex("chatId"));
            user.mobileNo = cursor.getString(cursor.getColumnIndex("mobileNo"));
            user.gender = cursor.getString(cursor.getColumnIndex("gender"));
            user.profileImage = cursor.getString(cursor.getColumnIndex("profileImageBytes"));
            user.myStatus = cursor.getString(cursor.getColumnIndex("myStatus"));
            user.cityOrRegion = cursor.getString(cursor.getColumnIndex("region"));
            user.profileImageUrl = cursor.getString(cursor.getColumnIndex("profileimage_url"));
        }
        cursor.close();
        return user;
    }

    public static void updateNotification$3b3d5b85(String userId, int updateType, Object value) {
        ContentValues values = new ContentValues();
        switch (C10681.f25xc22fdcb9[updateType - 1]) {
            case Logger.SEVERE /*1*/:
                values.put("notification_sound", (String) value);
                break;
            case Logger.WARNING /*2*/:
                values.put("notification_timing_start", (String) value);
                break;
            case Logger.INFO /*3*/:
                values.put("notification_timing_end", (String) value);
                break;
            case Logger.CONFIG /*4*/:
                values.put("message_alert_status", Integer.valueOf(((AllowDeniedStatus) value).status));
                break;
            case Logger.FINE /*5*/:
                values.put("movement_update_status", Integer.valueOf(((EnableDisableStatus) value).status));
                break;
            case Logger.FINER /*6*/:
                values.put("other_further_alert_status", Integer.valueOf(((FeatureAlertStatus) value).status));
                break;
            case Logger.FINEST /*7*/:
                values.put("sound_alert_status", Integer.valueOf(((EnableDisableStatus) value).status));
                break;
            case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                values.put("vibrate_status", Integer.valueOf(((EnableDisableStatus) value).status));
                break;
        }
        SHAMChatApplication.getMyApplicationContext().getContentResolver().update(CONTENT_URI_NOTIFICATION, values, "user_id=?", new String[]{userId});
    }

    public static UserNotification userNotificationFromCursor(Cursor cursor) {
        UserNotification userNotification = new UserNotification();
        try {
            FeatureAlertStatus fStatus;
            EnableDisableStatus enableDisableStatus;
            userNotification.userNotificationId = cursor.getInt(cursor.getColumnIndex("_id"));
            userNotification.userId = cursor.getString(cursor.getColumnIndex(ChatActivity.INTENT_EXTRA_USER_ID));
            userNotification.messageAlertStatus = cursor.getInt(cursor.getColumnIndex("message_alert_status")) == 1 ? AllowDeniedStatus.ALLOW : AllowDeniedStatus.DENIED;
            userNotification.soundAlertStatus = cursor.getInt(cursor.getColumnIndex("sound_alert_status")) == 1 ? EnableDisableStatus.ENABLE : EnableDisableStatus.DISABLE;
            userNotification.vibrateStatus = cursor.getInt(cursor.getColumnIndex("vibrate_status")) == 1 ? EnableDisableStatus.ENABLE : EnableDisableStatus.DISABLE;
            userNotification.notificationSound = cursor.getString(cursor.getColumnIndex("notification_sound"));
            userNotification.notificationTimingStart = cursor.getString(cursor.getColumnIndex("notification_timing_start"));
            userNotification.notificationTimingEnd = cursor.getString(cursor.getColumnIndex("notification_timing_end"));
            FeatureAlertStatus featureAlertStatus = FeatureAlertStatus.DISABLE;
            switch (cursor.getInt(8)) {
                case Logger.SEVERE /*1*/:
                    fStatus = FeatureAlertStatus.ENABLE;
                    break;
                case Logger.WARNING /*2*/:
                    fStatus = FeatureAlertStatus.NIGHT_MODE;
                    break;
                default:
                    fStatus = FeatureAlertStatus.DISABLE;
                    break;
            }
            userNotification.otherFeatureAlertStatus = fStatus;
            if (cursor.getInt(cursor.getColumnIndex("movement_update_status")) == 1) {
                enableDisableStatus = EnableDisableStatus.ENABLE;
            } else {
                enableDisableStatus = EnableDisableStatus.DISABLE;
            }
            userNotification.movementUpdateStatus = enableDisableStatus;
        } catch (Exception e) {
            if (SHAMChatApplication.getConfig().userId != null) {
                ContentValues vals = new ContentValues();
                vals.put(ChatActivity.INTENT_EXTRA_USER_ID, SHAMChatApplication.getConfig().userId);
                SHAMChatApplication.getInstance().getContentResolver().insert(CONTENT_URI_NOTIFICATION, vals);
            }
            userNotificationFromCursor(cursor);
        }
        return userNotification;
    }

    public static User getUserDetailsFromServer(String userId) {
        Exception e;
        User user = null;
        try {
            HttpParams params = new BasicHttpParams();
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", new PlainSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            DefaultHttpClient httpclient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schemeRegistry), params);
            Context context = SHAMChatApplication.getMyApplicationContext();
            HttpPost httpPost = new HttpPost(context.getApplicationContext().getResources().getString(2131493167) + "getMyDetails.htm");
            List<NameValuePair> data = new ArrayList();
            data.add(new BasicNameValuePair("userId", userId));
            httpPost.entity = new UrlEncodedFormEntity(data);
            InputStream inputStream = httpclient.execute((HttpUriRequest) httpPost).getEntity().getContent();
            BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder responseStr = new StringBuilder();
            while (true) {
                String responseLineStr = bufferreader.readLine();
                if (responseLineStr == null) {
                    break;
                }
                responseStr.append(responseLineStr);
            }
            bufferreader.close();
            inputStream.close();
            String result = responseStr.toString().trim();
            if (result == null) {
                return null;
            }
            JSONObject jSONObject = new JSONObject(result);
            if (!jSONObject.getString(NotificationCompatApi21.CATEGORY_STATUS).equalsIgnoreCase("success")) {
                return null;
            }
            User user2 = new User(jSONObject.getJSONObject("data"));
            try {
                ContentValues values = new ContentValues();
                values.put("name", user2.username);
                values.put("chatId", user2.chatId);
                values.put("userId", user2.userId);
                values.put("mobileNo", user2.mobileNo);
                values.put(NotificationCompatApi21.CATEGORY_EMAIL, user2.email);
                values.put("gender", user2.gender);
                values.put("profileImageBytes", Base64.decode(user2.profileImage, 0));
                values.put("myStatus", user2.myStatus);
                values.put("region", user2.cityOrRegion);
                values.put("profileimage_url", user2.profileImageUrl);
                ContentResolver contentResolver = context.getContentResolver();
                Uri uri = CONTENT_URI_USER;
                String[] strArr = new String[1];
                strArr[0] = user2.userId;
                if (contentResolver.update(uri, values, "userId=?", strArr) == 0) {
                    context.getContentResolver().insert(CONTENT_URI_USER, values);
                    String str = user2.userId;
                    String str2 = user2.username;
                    SmackableImp.tryToAddRosterEntry$14e1ec6d(Utils.createXmppUserIdByUserId(r0), r0 + MqttTopic.TOPIC_LEVEL_SEPARATOR + user2.mobileNo);
                }
                return user2;
            } catch (Exception e2) {
                e = e2;
                user = user2;
                e.printStackTrace();
                return user;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return user;
        }
    }
}
