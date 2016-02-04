package com.shamchat.androidclient.data;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class MomentDatabaseHelper extends SQLiteOpenHelper {
    private static MomentDatabaseHelper momentDatabaseHelper;

    public static synchronized MomentDatabaseHelper getMomentDatabaseHelper(Context c) {
        MomentDatabaseHelper momentDatabaseHelper;
        synchronized (MomentDatabaseHelper.class) {
            if (momentDatabaseHelper != null) {
                System.out.println("Returning old mom");
                momentDatabaseHelper = momentDatabaseHelper;
            } else {
                momentDatabaseHelper = new MomentDatabaseHelper(c);
                System.out.println("Returning new mom");
                momentDatabaseHelper = momentDatabaseHelper;
            }
        }
        return momentDatabaseHelper;
    }

    private MomentDatabaseHelper(Context context) {
        super(context, "moment.db", null, 3);
    }

    public final void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS moment ( _id INTEGER PRIMARY KEY AUTOINCREMENT,post_id TEXT NOT NULL UNIQUE,server_id TEXT ,user_id TEXT,post_text TEXT,posted_image_url TEXT,posted_sticker_url TEXT ,posted_video_url TEXT ,posted_location TEXT,post_last_updated_time DATETIME DEFAULT (datetime('now','localtime')), posted_datetime DATETIME DEFAULT (datetime('now','localtime')), post_action_requested TEXT NOT NULL DEFAULT 'CREATE' , post_status INTEGER NOT NULL DEFAULT 1 ,sent_status INTEGER NOT NULL DEFAULT 0);");
        db.execSQL("CREATE TABLE IF NOT EXISTS moment_like ( _id INTEGER PRIMARY KEY AUTOINCREMENT,like_id TEXT NOT NULL UNIQUE,post_id TEXT NOT NULL,user_id TEXT,liked_datetime DATETIME DEFAULT (datetime('now','localtime')), updated_datetime DATETIME DEFAULT (datetime('now','localtime')), post_action_requested TEXT NOT NULL DEFAULT 'CREATE' , like_status INTEGER NOT NULL DEFAULT 1 ,sent_status INTEGER NOT NULL DEFAULT 0);");
        db.execSQL("CREATE TABLE IF NOT EXISTS moment_comment ( _id INTEGER PRIMARY KEY AUTOINCREMENT,comment_id TEXT NOT NULL UNIQUE,post_id TEXT NOT NULL,user_id TEXT,commented_text TEXT,comment_datetime DATETIME DEFAULT (datetime('now','localtime')), updated_datetime DATETIME DEFAULT (datetime('now','localtime')), post_action_requested TEXT NOT NULL DEFAULT 'CREATE' , comment_status INTEGER NOT NULL DEFAULT 1 ,sent_status INTEGER NOT NULL DEFAULT 0);");
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

    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case Logger.SEVERE /*1*/:
                db.execSQL("ALTER TABLE moment ADD server_id TEXT ");
            case Logger.WARNING /*2*/:
                db.execSQL("DELETE FROM moment");
            default:
        }
    }
}
