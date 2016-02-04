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
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class StickerProvider extends ContentProvider {
    public static final Uri CONTENT_URI_STICKER;
    private static final UriMatcher URI_MATCHER;
    private SQLiteOpenHelper mOpenHelper;

    public static final class StickerConstants implements BaseColumns {
        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList();
            tmpList.add("pack_id");
            tmpList.add("pack_name");
            tmpList.add("download_url");
            tmpList.add("thumnail_url");
            return tmpList;
        }
    }

    public static class StickerDatabaseHelper extends SQLiteOpenHelper {
        public StickerDatabaseHelper(Context context) {
            super(context, "sticker.db", null, 1);
        }

        public final void onCreate(SQLiteDatabase db) {
            Log.i("shamchat.StickerProvider", "creating new chat table");
            db.execSQL("CREATE TABLE stickers (_id INTEGER PRIMARY KEY AUTOINCREMENT, pack_id TEXT, pack_name TEXT, pack_desc TEXT , download_url TEXT, thumnail_url TEXT, local_file_url TEXT, sticker_pack_icon TEXT, is_sticker_downloaded INTEGER NOT NULL DEFAULT 0);");
        }

        public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("shamchat.StickerProvider", "onUpgrade: from " + oldVersion + " to " + newVersion);
        }
    }

    static {
        CONTENT_URI_STICKER = Uri.parse("content://com.shamchat.androidclient.provider.Stickers/stickers");
        UriMatcher uriMatcher = new UriMatcher(-1);
        URI_MATCHER = uriMatcher;
        uriMatcher.addURI("com.shamchat.androidclient.provider.Stickers", "stickers", 1);
        URI_MATCHER.addURI("com.shamchat.androidclient.provider.Stickers", "stickers/#", 2);
    }

    public int delete(Uri url, String where, String[] whereArgs) {
        int count;
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                count = db.delete("stickers", where, whereArgs);
                break;
            case Logger.WARNING /*2*/:
                String segment = (String) url.getPathSegments().get(1);
                if (TextUtils.isEmpty(where)) {
                    where = "_id=" + segment;
                } else {
                    where = "_id=" + segment + " AND (" + where + ")";
                }
                count = db.delete("stickers", where, whereArgs);
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
                return "vnd.android.cursor.dir/vnd.shamchat.stickers";
            case Logger.WARNING /*2*/:
                return "vnd.android.cursor.item/vnd.shamchat.stickers";
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
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                Iterator it = StickerConstants.getRequiredColumns().iterator();
                while (it.hasNext()) {
                    String colName = (String) it.next();
                    if (!values.containsKey(colName)) {
                        throw new IllegalArgumentException("Missing column: " + colName);
                    }
                }
                long rowId = db.insert("stickers", BuildConfig.VERSION_NAME, values);
                if (rowId < 0) {
                    throw new SQLException("Failed to insert row into TABLE_NAME_STICKER" + url);
                }
                if (rowId > 0) {
                    Log.d("shamchat.StickerProvider", "Sticker successfully entered at " + rowId);
                }
                Uri noteUri = ContentUris.withAppendedId(CONTENT_URI_STICKER, rowId);
                getContext().getContentResolver().notifyChange(noteUri, null);
                return noteUri;
            default:
                throw new IllegalArgumentException("Cannot insert from URL: " + url);
        }
    }

    public boolean onCreate() {
        this.mOpenHelper = new StickerDatabaseHelper(getContext());
        return true;
    }

    public Cursor query(Uri url, String[] projectionIn, String selection, String[] selectionArgs, String sortOrder) {
        String orderBy;
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                qBuilder.setTables("stickers");
                break;
            case Logger.WARNING /*2*/:
                qBuilder.setTables("stickers");
                qBuilder.appendWhere("pack_id=");
                qBuilder.appendWhere((CharSequence) url.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URL " + url);
        }
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = "pack_id ASC";
        } else {
            orderBy = sortOrder;
        }
        Cursor ret = qBuilder.query(this.mOpenHelper.getReadableDatabase(), projectionIn, selection, selectionArgs, null, null, orderBy);
        if (ret == null) {
            Log.i("shamchat.StickerProvider", "ChatProvider.query: failed");
        } else {
            ret.setNotificationUri(getContext().getContentResolver(), url);
        }
        return ret;
    }

    public int update(Uri url, ContentValues values, String where, String[] whereArgs) {
        int match = URI_MATCHER.match(url);
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        int count;
        switch (match) {
            case Logger.SEVERE /*1*/:
                count = db.update("stickers", values, where, whereArgs);
                if (count > 0) {
                    Log.d("shamchat.StickerProvider", "TABLE_NAME_STICKERS successfully updated " + count);
                }
                Log.i("shamchat.StickerProvider", "*** notifyChange() rowId: 0 url " + url);
                getContext().getContentResolver().notifyChange(url, null);
                return count;
            case Logger.WARNING /*2*/:
                long rowId = Long.parseLong((String) url.getPathSegments().get(1));
                count = db.update("stickers", values, "pack_id=" + rowId, null);
                getContext().getContentResolver().notifyChange(ContentUris.withAppendedId(CONTENT_URI_STICKER, rowId), null);
                getContext().getContentResolver().notifyChange(url, null);
                return count;
            default:
                throw new UnsupportedOperationException("Cannot update URL: " + url);
        }
    }
}
