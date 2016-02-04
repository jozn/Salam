package com.shamchat.androidclient.data;

import android.content.ContentProvider;
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
import android.text.TextUtils;
import android.util.Log;
import com.shamchat.activity.AddFavoriteTextActivity;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class ChatProvider extends ContentProvider {
    public static final Uri CONTENT_URI;
    private static final UriMatcher URI_MATCHER;
    private SQLiteOpenHelper mOpenHelper;

    public static final class ChatConstants implements BaseColumns {
        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList();
            tmpList.add("date");
            tmpList.add("from_me");
            tmpList.add("jid");
            tmpList.add(AddFavoriteTextActivity.EXTRA_MESSAGE);
            return tmpList;
        }
    }

    public static class ChatDatabaseHelper extends SQLiteOpenHelper {
        public ChatDatabaseHelper(Context context) {
            super(context, "zamin.db", null, 5);
        }

        public final void onCreate(SQLiteDatabase db) {
            Log.i("zamin.ChatProvider", "creating new chat table");
            db.execSQL("CREATE TABLE chats (_id INTEGER PRIMARY KEY AUTOINCREMENT,date INTEGER,from_me INTEGER,jid TEXT,message TEXT,read INTEGER,pid TEXT);");
        }

        public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("zamin.ChatProvider", "onUpgrade: from " + oldVersion + " to " + newVersion);
            switch (oldVersion) {
                case Logger.INFO /*3*/:
                    db.execSQL("UPDATE chats SET READ=1");
                    break;
                case Logger.CONFIG /*4*/:
                    break;
                default:
                    db.execSQL("DROP TABLE IF EXISTS chats");
                    onCreate(db);
                    return;
            }
            db.execSQL("ALTER TABLE chats ADD pid TEXT");
        }
    }

    static {
        CONTENT_URI = Uri.parse("content://org.zamin.androidclient.provider.Chats/chats");
        UriMatcher uriMatcher = new UriMatcher(-1);
        URI_MATCHER = uriMatcher;
        uriMatcher.addURI("org.zamin.androidclient.provider.Chats", "chats", 1);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Chats", "chats/#", 2);
    }

    public int delete(Uri url, String where, String[] whereArgs) {
        int count;
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                count = db.delete("chats", where, whereArgs);
                break;
            case Logger.WARNING /*2*/:
                String segment = (String) url.getPathSegments().get(1);
                if (TextUtils.isEmpty(where)) {
                    where = "_id=" + segment;
                } else {
                    where = "_id=" + segment + " AND (" + where + ")";
                }
                count = db.delete("chats", where, whereArgs);
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
                return "vnd.android.cursor.dir/vnd.zamin.chat";
            case Logger.WARNING /*2*/:
                return "vnd.android.cursor.item/vnd.zamin.chat";
            default:
                throw new IllegalArgumentException("Unknown URL");
        }
    }

    public Uri insert(Uri url, ContentValues initialValues) {
        if (URI_MATCHER.match(url) != 1) {
            throw new IllegalArgumentException("Cannot insert into URL: " + url);
        }
        ContentValues values = initialValues != null ? new ContentValues(initialValues) : new ContentValues();
        Iterator it = ChatConstants.getRequiredColumns().iterator();
        while (it.hasNext()) {
            String colName = (String) it.next();
            if (!values.containsKey(colName)) {
                throw new IllegalArgumentException("Missing column: " + colName);
            }
        }
        long rowId = this.mOpenHelper.getWritableDatabase().insert("chats", "date", values);
        if (rowId < 0) {
            throw new SQLException("Failed to insert row into " + url);
        }
        Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(noteUri, null);
        return noteUri;
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
                qBuilder.setTables("chats");
                break;
            case Logger.WARNING /*2*/:
                qBuilder.setTables("chats");
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
            Log.i("zamin.ChatProvider", "ChatProvider.query: failed");
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
                count = db.update("chats", values, where, whereArgs);
                break;
            case Logger.WARNING /*2*/:
                rowId = Long.parseLong((String) url.getPathSegments().get(1));
                count = db.update("chats", values, "_id=" + rowId, null);
                break;
            default:
                throw new UnsupportedOperationException("Cannot update URL: " + url);
        }
        Log.i("zamin.ChatProvider", "*** notifyChange() rowId: " + rowId + " url " + url);
        getContext().getContentResolver().notifyChange(url, null);
        return count;
    }
}
