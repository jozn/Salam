package com.shamchat.androidclient.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
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
import com.shamchat.activity.ChatActivity;
import com.shamchat.androidclient.SHAMChatApplication;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class MomentProvider extends ContentProvider {
    public static final Uri CONTENT_URI_COMMENT;
    public static final Uri CONTENT_URI_LIKE;
    public static final Uri CONTENT_URI_MOMENT;
    private static final UriMatcher URI_MATCHER;
    private String currentUserId;
    private SQLiteOpenHelper mOpenHelper;

    public static final class MomentCommentConstants implements BaseColumns {
        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList();
            tmpList.add("comment_id");
            return tmpList;
        }
    }

    public static final class MomentConstants implements BaseColumns {
        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList();
            tmpList.add("post_id");
            return tmpList;
        }
    }

    public static final class MomentLikeConstants implements BaseColumns {
        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList();
            tmpList.add("like_id");
            return tmpList;
        }
    }

    static {
        CONTENT_URI_MOMENT = Uri.parse("content://org.zamin.androidclient.provider.Moments/moment");
        CONTENT_URI_LIKE = Uri.parse("content://org.zamin.androidclient.provider.Moments/moment_like");
        CONTENT_URI_COMMENT = Uri.parse("content://org.zamin.androidclient.provider.Moments/moment_comment");
        UriMatcher uriMatcher = new UriMatcher(-1);
        URI_MATCHER = uriMatcher;
        uriMatcher.addURI("org.zamin.androidclient.provider.Moments", "moment", 1);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Moments", "moment/#", 2);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Moments", "moment_like", 3);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Moments", "moment_like/#", 4);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Moments", "moment_comment", 5);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Moments", "moment_comment/#", 6);
    }

    public MomentProvider() {
        this.currentUserId = null;
    }

    public int delete(Uri url, String where, String[] whereArgs) {
        int count;
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        String segment;
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                count = db.delete("moment", where, whereArgs);
                break;
            case Logger.WARNING /*2*/:
                segment = (String) url.getPathSegments().get(1);
                if (TextUtils.isEmpty(where)) {
                    where = "_id=" + segment;
                } else {
                    where = "_id=" + segment + " AND (" + where + ")";
                }
                count = db.delete("moment", where, whereArgs);
                break;
            case Logger.INFO /*3*/:
                count = db.delete("moment_like", where, whereArgs);
                break;
            case Logger.CONFIG /*4*/:
                segment = (String) url.getPathSegments().get(1);
                if (TextUtils.isEmpty(where)) {
                    where = "_id=" + segment;
                } else {
                    where = "_id=" + segment + " AND (" + where + ")";
                }
                count = db.delete("moment_like", where, whereArgs);
                break;
            case Logger.FINE /*5*/:
                count = db.delete("moment_comment", where, whereArgs);
                break;
            case Logger.FINER /*6*/:
                segment = (String) url.getPathSegments().get(1);
                if (TextUtils.isEmpty(where)) {
                    where = "_id=" + segment;
                } else {
                    where = "_id=" + segment + " AND (" + where + ")";
                }
                count = db.delete("moment_comment", where, whereArgs);
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
                return "vnd.android.cursor.dir/vnd.zamin.moment";
            case Logger.WARNING /*2*/:
                return "vnd.android.cursor.item/vnd.zamin.moment";
            case Logger.INFO /*3*/:
                return "vnd.android.cursor.dir/vnd.zamin.momentLike";
            case Logger.CONFIG /*4*/:
                return "vnd.android.cursor.item/vnd.zamin.momentLike";
            case Logger.FINE /*5*/:
                return "vnd.android.cursor.dir/vnd.zamin.momentComment";
            case Logger.FINER /*6*/:
                return "vnd.android.cursor.item/vnd.zamin.momentComment";
            default:
                throw new IllegalArgumentException("Unknown URL");
        }
    }

    public Uri insert(Uri url, ContentValues initialValues) {
        if (URI_MATCHER.match(url) == 1 || URI_MATCHER.match(url) == 3 || URI_MATCHER.match(url) == 5) {
            boolean notify = true;
            SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
            ContentValues values = initialValues != null ? new ContentValues(initialValues) : new ContentValues();
            Iterator it;
            String colName;
            long rowId;
            Uri noteUri;
            switch (URI_MATCHER.match(url)) {
                case Logger.SEVERE /*1*/:
                    it = MomentConstants.getRequiredColumns().iterator();
                    while (it.hasNext()) {
                        colName = (String) it.next();
                        if (!values.containsKey(colName)) {
                            throw new IllegalArgumentException("Missing column: " + colName);
                        }
                    }
                    rowId = db.insert("moment", BuildConfig.VERSION_NAME, values);
                    if (rowId < 0) {
                        throw new SQLException("Failed to insert row into " + url);
                    }
                    noteUri = ContentUris.withAppendedId(CONTENT_URI_MOMENT, rowId);
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                case Logger.INFO /*3*/:
                    it = MomentLikeConstants.getRequiredColumns().iterator();
                    while (it.hasNext()) {
                        colName = (String) it.next();
                        if (!values.containsKey(colName)) {
                            throw new IllegalArgumentException("Missing column: " + colName);
                        }
                    }
                    rowId = db.insert("moment_like", BuildConfig.VERSION_NAME, values);
                    if (rowId < 0) {
                        throw new SQLException("Failed to insert row into Group" + url);
                    }
                    if (rowId > 0) {
                        Log.d("zamin.MomentProvider", "Group successfully entered at " + rowId);
                    }
                    noteUri = ContentUris.withAppendedId(CONTENT_URI_LIKE, rowId);
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                case Logger.FINE /*5*/:
                    it = MomentCommentConstants.getRequiredColumns().iterator();
                    while (it.hasNext()) {
                        colName = (String) it.next();
                        if (!values.containsKey(colName)) {
                            throw new IllegalArgumentException("Missing column: " + colName);
                        }
                    }
                    try {
                        if (this.currentUserId == null) {
                            this.currentUserId = SHAMChatApplication.getConfig().userId;
                        }
                        if (values.get(ChatActivity.INTENT_EXTRA_USER_ID).equals(this.currentUserId)) {
                            notify = false;
                        }
                    } catch (Exception e) {
                    }
                    rowId = db.insert("moment_comment", BuildConfig.VERSION_NAME, values);
                    if (rowId < 0) {
                        throw new SQLException("Failed to insert row into group_members" + url);
                    }
                    if (rowId > 0) {
                        Log.d("zamin.MomentProvider", "Group Members successfully entered at " + rowId);
                    }
                    noteUri = ContentUris.withAppendedId(CONTENT_URI_COMMENT, rowId);
                    if (!notify) {
                        return noteUri;
                    }
                    getContext().getContentResolver().notifyChange(noteUri, null);
                    return noteUri;
                default:
                    throw new IllegalArgumentException("Cannot insert from URL: " + url);
            }
        }
        throw new IllegalArgumentException("Cannot insert into URL: " + url);
    }

    public boolean onCreate() {
        this.mOpenHelper = MomentDatabaseHelper.getMomentDatabaseHelper(getContext());
        System.out.println("MOMENT PROVIDER ONCREATE CALLED>>>>>>>>>");
        return true;
    }

    public int bulkInsert(Uri uri, ContentValues[] values) {
        int i = 0;
        if (URI_MATCHER.match(uri) == 1 || URI_MATCHER.match(uri) == 3 || URI_MATCHER.match(uri) == 5) {
            int rowId = 0;
            SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
            int length;
            switch (URI_MATCHER.match(uri)) {
                case Logger.SEVERE /*1*/:
                    db.beginTransaction();
                    length = values.length;
                    while (i < length) {
                        rowId += (int) db.insert("moment", BuildConfig.VERSION_NAME, values[i]);
                        i++;
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    getContext().getContentResolver().notifyChange(CONTENT_URI_MOMENT, null);
                    break;
                case Logger.INFO /*3*/:
                    db.beginTransaction();
                    length = values.length;
                    while (i < length) {
                        rowId += (int) db.insert("moment_like", BuildConfig.VERSION_NAME, values[i]);
                        i++;
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    getContext().getContentResolver().notifyChange(CONTENT_URI_LIKE, null);
                    break;
                case Logger.FINE /*5*/:
                    db.beginTransaction();
                    length = values.length;
                    while (i < length) {
                        rowId += (int) db.insert("moment_comment", BuildConfig.VERSION_NAME, values[i]);
                        i++;
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    getContext().getContentResolver().notifyChange(CONTENT_URI_COMMENT, null);
                    break;
            }
            return rowId;
        }
        throw new IllegalArgumentException("Cannot insert into URL: " + uri);
    }

    public Cursor query(Uri url, String[] projectionIn, String selection, String[] selectionArgs, String sortOrder) {
        String orderBy;
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                qBuilder.setTables("moment");
                break;
            case Logger.WARNING /*2*/:
                qBuilder.setTables("moment");
                qBuilder.appendWhere("_id=");
                qBuilder.appendWhere((CharSequence) url.getPathSegments().get(1));
                break;
            case Logger.INFO /*3*/:
                qBuilder.setTables("moment_like");
                break;
            case Logger.CONFIG /*4*/:
                qBuilder.setTables("moment_like");
                qBuilder.appendWhere("_id=");
                qBuilder.appendWhere((CharSequence) url.getPathSegments().get(1));
                break;
            case Logger.FINE /*5*/:
                qBuilder.setTables("moment_comment");
                break;
            case Logger.FINER /*6*/:
                qBuilder.setTables("moment_comment");
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
            Log.i("zamin.MomentProvider", "UserProvider.query: failed");
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
                count = db.update("moment", values, where, whereArgs);
                break;
            case Logger.WARNING /*2*/:
                rowId = Long.parseLong((String) url.getPathSegments().get(1));
                count = db.update("moment", values, "_id=" + rowId, null);
                if (count > 0) {
                    Log.d("zamin.MomentProvider", "User successfully updated " + rowId);
                    break;
                }
                break;
            case Logger.INFO /*3*/:
                count = db.update("moment_like", values, where, whereArgs);
                if (count > 0) {
                    Log.d("zamin.MomentProvider", "Message successfully updated " + count);
                    break;
                }
                break;
            case Logger.CONFIG /*4*/:
                rowId = Long.parseLong((String) url.getPathSegments().get(1));
                count = db.update("moment_like", values, "_id=" + rowId, null);
                break;
            case Logger.FINE /*5*/:
                count = db.update("moment_comment", values, where, whereArgs);
                if (count > 0) {
                    Log.d("zamin.MomentProvider", "Thread successfully updated " + count);
                    break;
                }
                break;
            case Logger.FINER /*6*/:
                rowId = Long.parseLong((String) url.getPathSegments().get(1));
                count = db.update("moment_comment", values, where, whereArgs);
                break;
            default:
                throw new UnsupportedOperationException("Cannot update URL: " + url);
        }
        Log.i("zamin.MomentProvider", "*** notifyChange() rowId: " + rowId + " url " + url);
        getContext().getContentResolver().notifyChange(url, null);
        return count;
    }
}
