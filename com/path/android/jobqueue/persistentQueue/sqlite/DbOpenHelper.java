package com.path.android.jobqueue.persistentQueue.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.path.android.jobqueue.persistentQueue.sqlite.SqlHelper.ForeignKey;
import com.path.android.jobqueue.persistentQueue.sqlite.SqlHelper.Property;
import com.shamchat.activity.AddFavoriteTextActivity;
import com.shamchat.activity.ChatInitialForGroupChatActivity;

public final class DbOpenHelper extends SQLiteOpenHelper {
    static final Property BASE_JOB_COLUMN;
    static final Property CREATED_NS_COLUMN;
    static final Property DELAY_UNTIL_NS_COLUMN;
    static final Property GROUP_ID_COLUMN;
    static final Property ID_COLUMN;
    static final Property PRIORITY_COLUMN;
    static final Property REQUIRES_NETWORK_COLUMN;
    static final Property RUNNING_SESSION_ID_COLUMN;
    static final Property RUN_COUNT_COLUMN;
    static final Property TAGS_ID_COLUMN;
    static final Property TAGS_JOB_ID_COLUMN;
    static final Property TAGS_NAME_COLUMN;

    static {
        ID_COLUMN = new Property("_id", "integer", 0);
        PRIORITY_COLUMN = new Property("priority", "integer", 1);
        GROUP_ID_COLUMN = new Property(ChatInitialForGroupChatActivity.INTENT_EXTRA_GROUP_ID, AddFavoriteTextActivity.EXTRA_RESULT_TEXT, 2);
        RUN_COUNT_COLUMN = new Property("run_count", "integer", 3);
        BASE_JOB_COLUMN = new Property("base_job", "byte", 4);
        CREATED_NS_COLUMN = new Property("created_ns", "long", 5);
        DELAY_UNTIL_NS_COLUMN = new Property("delay_until_ns", "long", 6);
        RUNNING_SESSION_ID_COLUMN = new Property("running_session_id", "long", 7);
        REQUIRES_NETWORK_COLUMN = new Property("requires_network", "integer", 8);
        TAGS_ID_COLUMN = new Property("_id", "integer", 0);
        TAGS_JOB_ID_COLUMN = new Property("job_id", "integer", 1, new ForeignKey("job_holder", ID_COLUMN.columnName));
        TAGS_NAME_COLUMN = new Property("tag_name", AddFavoriteTextActivity.EXTRA_RESULT_TEXT, 2);
    }

    public DbOpenHelper(Context context, String name) {
        super(context, name, null, 4);
    }

    public final void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SqlHelper.create("job_holder", ID_COLUMN, PRIORITY_COLUMN, GROUP_ID_COLUMN, RUN_COUNT_COLUMN, BASE_JOB_COLUMN, CREATED_NS_COLUMN, DELAY_UNTIL_NS_COLUMN, RUNNING_SESSION_ID_COLUMN, REQUIRES_NETWORK_COLUMN));
        sqLiteDatabase.execSQL(SqlHelper.create("job_holder_tags", TAGS_ID_COLUMN, TAGS_JOB_ID_COLUMN, TAGS_NAME_COLUMN));
        sqLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS TAG_NAME_INDEX ON job_holder_tags(" + TAGS_NAME_COLUMN.columnName + ")");
    }

    public final void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SqlHelper.drop("job_holder"));
        sqLiteDatabase.execSQL(SqlHelper.drop("job_holder_tags"));
        sqLiteDatabase.execSQL("DROP INDEX IF EXISTS TAG_NAME_INDEX");
        onCreate(sqLiteDatabase);
    }
}
