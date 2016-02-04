package com.path.android.jobqueue.persistentQueue.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobHolder;
import com.path.android.jobqueue.JobQueue;
import com.path.android.jobqueue.log.JqLog;
import com.path.android.jobqueue.persistentQueue.sqlite.SqlHelper.Order;
import com.path.android.jobqueue.persistentQueue.sqlite.SqlHelper.Order.Type;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class SqliteJobQueue implements JobQueue {
    SQLiteDatabase db;
    DbOpenHelper dbOpenHelper;
    JobSerializer jobSerializer;
    QueryCache nextJobsQueryCache;
    Set<Long> pendingCancelations;
    QueryCache readyJobsQueryCache;
    private final long sessionId;
    SqlHelper sqlHelper;

    private static class InvalidJobException extends Exception {
        private InvalidJobException() {
        }
    }

    public interface JobSerializer {
        <T extends Job> T deserialize(byte[] bArr) throws IOException, ClassNotFoundException;

        byte[] serialize(Object obj) throws IOException;
    }

    public static class JavaSerializer implements JobSerializer {
        public final byte[] serialize(Object object) throws IOException {
            Throwable th;
            if (object == null) {
                return null;
            }
            ByteArrayOutputStream bos = null;
            try {
                ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
                try {
                    new ObjectOutputStream(bos2).writeObject(object);
                    byte[] toByteArray = bos2.toByteArray();
                    bos2.close();
                    return toByteArray;
                } catch (Throwable th2) {
                    th = th2;
                    bos = bos2;
                    if (bos != null) {
                        bos.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (bos != null) {
                    bos.close();
                }
                throw th;
            }
        }

        public final <T extends Job> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
            Throwable th;
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            ObjectInputStream in = null;
            try {
                ObjectInputStream in2 = new ObjectInputStream(new ByteArrayInputStream(bytes));
                try {
                    Job job = (Job) in2.readObject();
                    in2.close();
                    return job;
                } catch (Throwable th2) {
                    th = th2;
                    in = in2;
                    if (in != null) {
                        in.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (in != null) {
                    in.close();
                }
                throw th;
            }
        }
    }

    public SqliteJobQueue(Context context, long sessionId, String id, JobSerializer jobSerializer, boolean inTestMode) {
        this.pendingCancelations = new HashSet();
        this.sessionId = sessionId;
        this.dbOpenHelper = new DbOpenHelper(context, inTestMode ? null : "db_" + id);
        this.db = this.dbOpenHelper.getWritableDatabase();
        this.sqlHelper = new SqlHelper(this.db, "job_holder", DbOpenHelper.ID_COLUMN.columnName, "job_holder_tags", sessionId);
        this.jobSerializer = jobSerializer;
        this.readyJobsQueryCache = new QueryCache();
        this.nextJobsQueryCache = new QueryCache();
        this.sqlHelper.db.execSQL("UPDATE job_holder SET " + DbOpenHelper.DELAY_UNTIL_NS_COLUMN.columnName + "=?", new Object[]{Long.valueOf(Long.MIN_VALUE)});
    }

    public final long insert(JobHolder jobHolder) {
        if (jobHolder.hasTags()) {
            return insertWithTags(jobHolder);
        }
        long id;
        SQLiteStatement stmt = this.sqlHelper.getInsertStatement();
        synchronized (stmt) {
            stmt.clearBindings();
            bindValues(stmt, jobHolder);
            id = stmt.executeInsert();
        }
        jobHolder.setId(Long.valueOf(id));
        return id;
    }

    private long insertWithTags(JobHolder jobHolder) {
        long id;
        SQLiteStatement stmt = this.sqlHelper.getInsertStatement();
        SqlHelper sqlHelper = this.sqlHelper;
        if (sqlHelper.insertTagsStatement == null) {
            StringBuilder stringBuilder = new StringBuilder("INSERT INTO job_holder_tags");
            stringBuilder.append(" VALUES (");
            for (int i = 0; i < sqlHelper.tagsColumnCount; i++) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append("?");
            }
            stringBuilder.append(")");
            sqlHelper.insertTagsStatement = sqlHelper.db.compileStatement(stringBuilder.toString());
        }
        SQLiteStatement tagsStmt = sqlHelper.insertTagsStatement;
        synchronized (stmt) {
            this.db.beginTransaction();
            try {
                stmt.clearBindings();
                bindValues(stmt, jobHolder);
                id = stmt.executeInsert();
                for (String tag : jobHolder.getTags()) {
                    tagsStmt.clearBindings();
                    tagsStmt.bindLong(DbOpenHelper.TAGS_JOB_ID_COLUMN.columnIndex + 1, id);
                    tagsStmt.bindString(DbOpenHelper.TAGS_NAME_COLUMN.columnIndex + 1, tag);
                    tagsStmt.executeInsert();
                }
                this.db.setTransactionSuccessful();
                this.db.endTransaction();
            } catch (Throwable th) {
                this.db.endTransaction();
            }
        }
        jobHolder.setId(Long.valueOf(id));
        return id;
    }

    private void bindValues(SQLiteStatement stmt, JobHolder jobHolder) {
        if (jobHolder.getId() != null) {
            stmt.bindLong(DbOpenHelper.ID_COLUMN.columnIndex + 1, jobHolder.getId().longValue());
        }
        stmt.bindLong(DbOpenHelper.PRIORITY_COLUMN.columnIndex + 1, (long) jobHolder.getPriority());
        if (jobHolder.getGroupId() != null) {
            stmt.bindString(DbOpenHelper.GROUP_ID_COLUMN.columnIndex + 1, jobHolder.getGroupId());
        }
        stmt.bindLong(DbOpenHelper.RUN_COUNT_COLUMN.columnIndex + 1, (long) jobHolder.getRunCount());
        byte[] job = safeSerialize(jobHolder.job);
        if (job != null) {
            stmt.bindBlob(DbOpenHelper.BASE_JOB_COLUMN.columnIndex + 1, job);
        }
        stmt.bindLong(DbOpenHelper.CREATED_NS_COLUMN.columnIndex + 1, jobHolder.getCreatedNs());
        stmt.bindLong(DbOpenHelper.DELAY_UNTIL_NS_COLUMN.columnIndex + 1, jobHolder.getDelayUntilNs());
        stmt.bindLong(DbOpenHelper.RUNNING_SESSION_ID_COLUMN.columnIndex + 1, jobHolder.getRunningSessionId());
        stmt.bindLong(DbOpenHelper.REQUIRES_NETWORK_COLUMN.columnIndex + 1, jobHolder.requiresNetwork() ? 1 : 0);
    }

    public final long insertOrReplace(JobHolder jobHolder) {
        if (jobHolder.getId() == null) {
            return insert(jobHolder);
        }
        long id;
        jobHolder.setRunningSessionId(Long.MIN_VALUE);
        SqlHelper sqlHelper = this.sqlHelper;
        if (sqlHelper.insertOrReplaceStatement == null) {
            StringBuilder append = new StringBuilder("INSERT OR REPLACE INTO ").append(sqlHelper.tableName);
            append.append(" VALUES (");
            for (int i = 0; i < sqlHelper.columnCount; i++) {
                if (i != 0) {
                    append.append(",");
                }
                append.append("?");
            }
            append.append(")");
            sqlHelper.insertOrReplaceStatement = sqlHelper.db.compileStatement(append.toString());
        }
        SQLiteStatement stmt = sqlHelper.insertOrReplaceStatement;
        synchronized (stmt) {
            stmt.clearBindings();
            bindValues(stmt, jobHolder);
            id = stmt.executeInsert();
        }
        jobHolder.setId(Long.valueOf(id));
        return id;
    }

    public final void remove(JobHolder jobHolder) {
        if (jobHolder.getId() == null) {
            JqLog.m39e("called remove with null job id.", new Object[0]);
        } else {
            delete(jobHolder.getId());
        }
    }

    private void delete(Long id) {
        this.pendingCancelations.remove(id);
        SqlHelper sqlHelper = this.sqlHelper;
        if (sqlHelper.deleteStatement == null) {
            sqlHelper.deleteStatement = sqlHelper.db.compileStatement("DELETE FROM " + sqlHelper.tableName + " WHERE " + sqlHelper.primaryKeyColumnName + " = ?");
        }
        SQLiteStatement stmt = sqlHelper.deleteStatement;
        synchronized (stmt) {
            stmt.clearBindings();
            stmt.bindLong(1, id.longValue());
            stmt.execute();
        }
    }

    public final int count() {
        int simpleQueryForLong;
        SqlHelper sqlHelper = this.sqlHelper;
        if (sqlHelper.countStatement == null) {
            sqlHelper.countStatement = sqlHelper.db.compileStatement("SELECT COUNT(*) FROM " + sqlHelper.tableName + " WHERE " + DbOpenHelper.RUNNING_SESSION_ID_COLUMN.columnName + " != ?");
        }
        SQLiteStatement stmt = sqlHelper.countStatement;
        synchronized (stmt) {
            stmt.clearBindings();
            stmt.bindLong(1, this.sessionId);
            simpleQueryForLong = (int) stmt.simpleQueryForLong();
        }
        return simpleQueryForLong;
    }

    public final int countReadyJobs(boolean hasNetwork, Collection<String> excludeGroups) {
        int i = 0;
        String sql = this.readyJobsQueryCache.get(hasNetwork, excludeGroups);
        if (sql == null) {
            sql = "SELECT SUM(case WHEN " + DbOpenHelper.GROUP_ID_COLUMN.columnName + " is null then group_cnt else 1 end) from (" + ("SELECT count(*) group_cnt, " + DbOpenHelper.GROUP_ID_COLUMN.columnName + " FROM job_holder WHERE " + createReadyJobWhereSql(hasNetwork, excludeGroups, true)) + ")";
            this.readyJobsQueryCache.set(sql, hasNetwork, excludeGroups);
        }
        Cursor cursor = this.db.rawQuery(sql, new String[]{Long.toString(this.sessionId), Long.toString(System.nanoTime())});
        try {
            if (cursor.moveToNext()) {
                i = cursor.getInt(0);
                cursor.close();
            }
            return i;
        } finally {
            cursor.close();
        }
    }

    public final JobHolder nextJobAndIncRunCount(boolean hasNetwork, Collection<String> excludeGroups) {
        String selectQuery = this.nextJobsQueryCache.get(hasNetwork, excludeGroups);
        if (selectQuery == null) {
            String where = createReadyJobWhereSql(hasNetwork, excludeGroups, false);
            SqlHelper sqlHelper = this.sqlHelper;
            Integer valueOf = Integer.valueOf(1);
            Order[] orderArr = new Order[]{new Order(DbOpenHelper.PRIORITY_COLUMN, Type.DESC), new Order(DbOpenHelper.CREATED_NS_COLUMN, Type.ASC), new Order(DbOpenHelper.ID_COLUMN, Type.ASC)};
            StringBuilder stringBuilder = new StringBuilder("SELECT * FROM ");
            stringBuilder.append(sqlHelper.tableName);
            if (where != null) {
                stringBuilder.append(" WHERE ").append(where);
            }
            Object obj = 1;
            for (int i = 0; i < 3; i++) {
                Order order = orderArr[i];
                if (obj != null) {
                    stringBuilder.append(" ORDER BY ");
                } else {
                    stringBuilder.append(",");
                }
                obj = null;
                stringBuilder.append(order.property.columnName).append(" ").append(order.type);
            }
            if (valueOf != null) {
                stringBuilder.append(" LIMIT ").append(valueOf);
            }
            selectQuery = stringBuilder.toString();
            this.nextJobsQueryCache.set(selectQuery, hasNetwork, excludeGroups);
        }
        Cursor cursor = this.db.rawQuery(selectQuery, new String[]{Long.toString(this.sessionId), Long.toString(System.nanoTime())});
        JobHolder holder;
        try {
            if (cursor.moveToNext()) {
                Job safeDeserialize = safeDeserialize(cursor.getBlob(DbOpenHelper.BASE_JOB_COLUMN.columnIndex));
                if (safeDeserialize == null) {
                    throw new InvalidJobException();
                }
                holder = new JobHolder(Long.valueOf(cursor.getLong(DbOpenHelper.ID_COLUMN.columnIndex)), cursor.getInt(DbOpenHelper.PRIORITY_COLUMN.columnIndex), cursor.getString(DbOpenHelper.GROUP_ID_COLUMN.columnIndex), cursor.getInt(DbOpenHelper.RUN_COUNT_COLUMN.columnIndex), safeDeserialize, cursor.getLong(DbOpenHelper.CREATED_NS_COLUMN.columnIndex), cursor.getLong(DbOpenHelper.DELAY_UNTIL_NS_COLUMN.columnIndex), cursor.getLong(DbOpenHelper.RUNNING_SESSION_ID_COLUMN.columnIndex));
                sqlHelper = this.sqlHelper;
                if (sqlHelper.onJobFetchedForRunningStatement == null) {
                    sqlHelper.onJobFetchedForRunningStatement = sqlHelper.db.compileStatement("UPDATE " + sqlHelper.tableName + " SET " + DbOpenHelper.RUN_COUNT_COLUMN.columnName + " = ? , " + DbOpenHelper.RUNNING_SESSION_ID_COLUMN.columnName + " = ?  WHERE " + sqlHelper.primaryKeyColumnName + " = ? ");
                }
                SQLiteStatement sQLiteStatement = sqlHelper.onJobFetchedForRunningStatement;
                holder.setRunCount(holder.getRunCount() + 1);
                holder.setRunningSessionId(this.sessionId);
                synchronized (sQLiteStatement) {
                    sQLiteStatement.clearBindings();
                    sQLiteStatement.bindLong(1, (long) holder.getRunCount());
                    sQLiteStatement.bindLong(2, this.sessionId);
                    sQLiteStatement.bindLong(3, holder.getId().longValue());
                    sQLiteStatement.execute();
                }
                cursor.close();
                return holder;
            }
            cursor.close();
            return null;
        } catch (InvalidJobException e) {
            delete(Long.valueOf(cursor.getLong(0)));
            holder = nextJobAndIncRunCount(true, null);
            cursor.close();
            return holder;
        } catch (Throwable th) {
            cursor.close();
        }
    }

    private static String createReadyJobWhereSql(boolean hasNetwork, Collection<String> excludeGroups, boolean groupByRunningGroup) {
        String where = DbOpenHelper.RUNNING_SESSION_ID_COLUMN.columnName + " != ?  AND " + DbOpenHelper.DELAY_UNTIL_NS_COLUMN.columnName + " <= ? ";
        if (!hasNetwork) {
            where = where + " AND " + DbOpenHelper.REQUIRES_NETWORK_COLUMN.columnName + " != 1 ";
        }
        String groupConstraint = null;
        if (excludeGroups != null && excludeGroups.size() > 0) {
            groupConstraint = DbOpenHelper.GROUP_ID_COLUMN.columnName + " IS NULL OR " + DbOpenHelper.GROUP_ID_COLUMN.columnName + " NOT IN('" + joinStrings("','", excludeGroups) + "')";
        }
        if (groupByRunningGroup) {
            where = where + " GROUP BY " + DbOpenHelper.GROUP_ID_COLUMN.columnName;
            if (groupConstraint != null) {
                return where + " HAVING " + groupConstraint;
            }
            return where;
        } else if (groupConstraint != null) {
            return where + " AND ( " + groupConstraint + " )";
        } else {
            return where;
        }
    }

    private static String joinStrings(String glue, Collection<String> strings) {
        StringBuilder builder = new StringBuilder();
        for (String str : strings) {
            if (builder.length() != 0) {
                builder.append(glue);
            }
            builder.append(str);
        }
        return builder.toString();
    }

    public final Long getNextJobDelayUntilNs(boolean hasNetwork) {
        SQLiteStatement stmt;
        Long valueOf;
        SqlHelper sqlHelper;
        if (hasNetwork) {
            sqlHelper = this.sqlHelper;
            if (sqlHelper.nextJobDelayedUntilWithNetworkStatement == null) {
                sqlHelper.nextJobDelayedUntilWithNetworkStatement = sqlHelper.db.compileStatement("SELECT " + DbOpenHelper.DELAY_UNTIL_NS_COLUMN.columnName + " FROM " + sqlHelper.tableName + " WHERE " + DbOpenHelper.RUNNING_SESSION_ID_COLUMN.columnName + " != " + sqlHelper.sessionId + " ORDER BY " + DbOpenHelper.DELAY_UNTIL_NS_COLUMN.columnName + " ASC LIMIT 1");
            }
            stmt = sqlHelper.nextJobDelayedUntilWithNetworkStatement;
        } else {
            sqlHelper = this.sqlHelper;
            if (sqlHelper.nextJobDelayedUntilWithoutNetworkStatement == null) {
                sqlHelper.nextJobDelayedUntilWithoutNetworkStatement = sqlHelper.db.compileStatement("SELECT " + DbOpenHelper.DELAY_UNTIL_NS_COLUMN.columnName + " FROM " + sqlHelper.tableName + " WHERE " + DbOpenHelper.RUNNING_SESSION_ID_COLUMN.columnName + " != " + sqlHelper.sessionId + " AND " + DbOpenHelper.REQUIRES_NETWORK_COLUMN.columnName + " != 1 ORDER BY " + DbOpenHelper.DELAY_UNTIL_NS_COLUMN.columnName + " ASC LIMIT 1");
            }
            stmt = sqlHelper.nextJobDelayedUntilWithoutNetworkStatement;
        }
        synchronized (stmt) {
            try {
                stmt.clearBindings();
                valueOf = Long.valueOf(stmt.simpleQueryForLong());
            } catch (SQLiteDoneException e) {
                valueOf = null;
            }
        }
        return valueOf;
    }

    private Job safeDeserialize(byte[] bytes) {
        try {
            return this.jobSerializer.deserialize(bytes);
        } catch (Throwable th) {
            JqLog.m40e(th, "error while deserializing job", new Object[0]);
            return null;
        }
    }

    private byte[] safeSerialize(Object object) {
        try {
            return this.jobSerializer.serialize(object);
        } catch (Throwable th) {
            JqLog.m40e(th, "error while serializing object %s", object.getClass().getSimpleName());
            return null;
        }
    }
}
