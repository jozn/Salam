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
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class RosterProvider extends ContentProvider {
    public static final Uri CONTENT_URI;
    public static final Uri GROUPS_URI;
    public static final String[] ROSTER_ALL_COLUMNS;
    private static final UriMatcher URI_MATCHER;
    long last_notify;
    private Runnable mNotifyChange;
    private Handler mNotifyHandler;
    private SQLiteOpenHelper mOpenHelper;

    /* renamed from: com.shamchat.androidclient.data.RosterProvider.1 */
    class C10671 implements Runnable {
        C10671() {
        }

        public final void run() {
            Log.d("zamin.RosterProvider", "notifying change");
            RosterProvider.this.getContext().getContentResolver().notifyChange(RosterProvider.CONTENT_URI, null);
            RosterProvider.this.getContext().getContentResolver().notifyChange(RosterProvider.GROUPS_URI, null);
        }
    }

    public static final class RosterConstants implements BaseColumns {
        public static ArrayList<String> getRequiredColumns() {
            ArrayList<String> tmpList = new ArrayList();
            tmpList.add("jid");
            tmpList.add("alias");
            tmpList.add("status_mode");
            tmpList.add("status_message");
            tmpList.add("roster_group");
            return tmpList;
        }
    }

    private static class RosterDatabaseHelper extends SQLiteOpenHelper {
        public RosterDatabaseHelper(Context context) {
            super(context, "roster.db", null, 4);
        }

        public final void onCreate(SQLiteDatabase db) {
            Log.i("zamin.RosterProvider", "creating new roster table");
            db.execSQL("CREATE TABLE roster (_id INTEGER PRIMARY KEY AUTOINCREMENT, jid TEXT UNIQUE ON CONFLICT REPLACE, alias TEXT, status_mode INTEGER, user_status INTEGER NOT NULL DEFAULT 0, status_message TEXT, show_in_chat INTEGER NOT NULL DEFAULT 0, roster_group TEXT);");
            db.execSQL("CREATE INDEX idx_roster_group ON roster (roster_group)");
            db.execSQL("CREATE INDEX idx_roster_alias ON roster (alias)");
            db.execSQL("CREATE INDEX idx_roster_status ON roster (status_mode)");
        }

        public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("zamin.RosterProvider", "onUpgrade: from " + oldVersion + " to " + newVersion);
            db.execSQL("DROP TABLE IF EXISTS groups");
            db.execSQL("DROP TABLE IF EXISTS roster");
            onCreate(db);
        }
    }

    public enum YesNoStatus {
        YES(1),
        NO(0);
        
        private int status;

        private YesNoStatus(int status) {
            this.status = 0;
            this.status = status;
        }
    }

    static {
        CONTENT_URI = Uri.parse("content://org.zamin.androidclient.provider.Roster/roster");
        GROUPS_URI = Uri.parse("content://org.zamin.androidclient.provider.Roster/groups");
        URI_MATCHER = new UriMatcher(-1);
        ROSTER_ALL_COLUMNS = new String[]{"jid", "alias", "status_mode", "status_message"};
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Roster", "roster", 1);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Roster", "roster/#", 2);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Roster", "groups", 3);
        URI_MATCHER.addURI("org.zamin.androidclient.provider.Roster", "groups/*", 4);
    }

    public RosterProvider() {
        this.mNotifyChange = new C10671();
        this.mNotifyHandler = new Handler();
        this.last_notify = 0;
    }

    public int delete(Uri url, String where, String[] whereArgs) {
        int count;
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                count = db.delete("roster", where, whereArgs);
                break;
            case Logger.WARNING /*2*/:
                String segment = (String) url.getPathSegments().get(1);
                if (TextUtils.isEmpty(where)) {
                    where = "_id=" + segment;
                } else {
                    where = "_id=" + segment + " AND (" + where + ")";
                }
                count = db.delete("roster", where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot delete from URL: " + url);
        }
        getContext().getContentResolver().notifyChange(GROUPS_URI, null);
        notifyChange();
        return count;
    }

    public String getType(Uri url) {
        switch (URI_MATCHER.match(url)) {
            case Logger.SEVERE /*1*/:
                return "vnd.android.cursor.dir/vnd.zamin.roster";
            case Logger.WARNING /*2*/:
                return "vnd.android.cursor.item/vnd.zamin.roster";
            default:
                throw new IllegalArgumentException("Unknown URL");
        }
    }

    public Uri insert(Uri url, ContentValues initialValues) {
        if (URI_MATCHER.match(url) != 1) {
            throw new IllegalArgumentException("Cannot insert into URL: " + url);
        }
        ContentValues values = initialValues != null ? new ContentValues(initialValues) : new ContentValues();
        Iterator it = RosterConstants.getRequiredColumns().iterator();
        while (it.hasNext()) {
            String colName = (String) it.next();
            if (!values.containsKey(colName)) {
                throw new IllegalArgumentException("Missing column: " + colName);
            }
        }
        long rowId = this.mOpenHelper.getWritableDatabase().insert("roster", "jid", values);
        if (rowId < 0) {
            throw new SQLException("Failed to insert row into " + url);
        }
        Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
        notifyChange();
        return noteUri;
    }

    public int bulkInsert(Uri url, ContentValues[] values) {
        int rowId = 0;
        if (URI_MATCHER.match(url) != 1) {
            throw new IllegalArgumentException("Cannot insert into URL: " + url);
        }
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        for (ContentValues i : values) {
            rowId += (int) db.insert("roster", BuildConfig.VERSION_NAME, i);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return rowId;
    }

    public boolean onCreate() {
        this.mOpenHelper = new RosterDatabaseHelper(getContext());
        return true;
    }

    public Cursor query(Uri url, String[] projectionIn, String selection, String[] selectionArgs, String sortOrder) {
        String orderBy;
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
        int match = URI_MATCHER.match(url);
        String groupBy = null;
        switch (match) {
            case Logger.SEVERE /*1*/:
                qBuilder.setTables("roster main_result");
                break;
            case Logger.WARNING /*2*/:
                qBuilder.setTables("roster main_result");
                qBuilder.appendWhere("_id=");
                qBuilder.appendWhere((CharSequence) url.getPathSegments().get(1));
                break;
            case Logger.INFO /*3*/:
                qBuilder.setTables("roster main_result");
                groupBy = "roster_group";
                break;
            case Logger.CONFIG /*4*/:
                qBuilder.setTables("roster main_result");
                qBuilder.appendWhere("roster_group=");
                qBuilder.appendWhere((CharSequence) url.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URL " + url);
        }
        if (TextUtils.isEmpty(sortOrder) && match == 1) {
            orderBy = "status_mode DESC, alias COLLATE NOCASE";
        } else {
            orderBy = sortOrder;
        }
        Cursor ret = qBuilder.query(this.mOpenHelper.getReadableDatabase(), projectionIn, selection, selectionArgs, groupBy, null, orderBy);
        if (ret == null) {
            Log.i("zamin.RosterProvider", "RosterProvider.query: failed");
        } else {
            ret.setNotificationUri(getContext().getContentResolver(), url);
        }
        return ret;
    }

    public int update(Uri url, ContentValues values, String where, String[] whereArgs) {
        int count;
        int match = URI_MATCHER.match(url);
        SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
        switch (match) {
            case Logger.SEVERE /*1*/:
                count = db.update("roster", values, where, whereArgs);
                break;
            case Logger.WARNING /*2*/:
                count = db.update("roster", values, "_id=" + Long.parseLong((String) url.getPathSegments().get(1)), whereArgs);
                break;
            default:
                throw new UnsupportedOperationException("Cannot update URL: " + url);
        }
        notifyChange();
        return count;
    }

    private void notifyChange() {
        this.mNotifyHandler.removeCallbacks(this.mNotifyChange);
        long ts = System.currentTimeMillis();
        if (ts > this.last_notify + 500) {
            this.mNotifyChange.run();
        } else {
            this.mNotifyHandler.postDelayed(this.mNotifyChange, 200);
        }
        this.last_notify = ts;
    }
}
