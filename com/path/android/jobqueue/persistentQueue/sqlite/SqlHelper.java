package com.path.android.jobqueue.persistentQueue.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.path.android.jobqueue.log.JqLog;

public final class SqlHelper {
    String FIND_BY_ID_QUERY;
    String FIND_BY_TAG_QUERY;
    final int columnCount;
    SQLiteStatement countStatement;
    final SQLiteDatabase db;
    SQLiteStatement deleteStatement;
    SQLiteStatement insertOrReplaceStatement;
    private SQLiteStatement insertStatement;
    SQLiteStatement insertTagsStatement;
    SQLiteStatement nextJobDelayedUntilWithNetworkStatement;
    SQLiteStatement nextJobDelayedUntilWithoutNetworkStatement;
    SQLiteStatement onJobFetchedForRunningStatement;
    final String primaryKeyColumnName;
    final long sessionId;
    final String tableName;
    final int tagsColumnCount;
    final String tagsTableName;

    public static class ForeignKey {
        final String targetFieldName;
        final String targetTable;

        public ForeignKey(String targetTable, String targetFieldName) {
            this.targetTable = targetTable;
            this.targetFieldName = targetFieldName;
        }
    }

    public static class Order {
        final Property property;
        final Type type;

        public enum Type {
            ASC,
            DESC
        }

        public Order(Property property, Type type) {
            this.property = property;
            this.type = type;
        }
    }

    public static class Property {
        public final int columnIndex;
        final String columnName;
        public final ForeignKey foreignKey;
        final String type;

        public Property(String columnName, String type, int columnIndex) {
            this(columnName, type, columnIndex, null);
        }

        public Property(String columnName, String type, int columnIndex, ForeignKey foreignKey) {
            this.columnName = columnName;
            this.type = type;
            this.columnIndex = columnIndex;
            this.foreignKey = foreignKey;
        }
    }

    public SqlHelper(SQLiteDatabase db, String tableName, String primaryKeyColumnName, String tagsTableName, long sessionId) {
        this.db = db;
        this.tableName = tableName;
        this.columnCount = 9;
        this.primaryKeyColumnName = primaryKeyColumnName;
        this.sessionId = sessionId;
        this.tagsColumnCount = 3;
        this.tagsTableName = tagsTableName;
        this.FIND_BY_ID_QUERY = "SELECT * FROM " + tableName + " WHERE " + DbOpenHelper.ID_COLUMN.columnName + " = ?";
        this.FIND_BY_TAG_QUERY = "SELECT * FROM " + tableName + " WHERE " + DbOpenHelper.ID_COLUMN.columnName + " IN ( SELECT " + DbOpenHelper.TAGS_JOB_ID_COLUMN.columnName + " FROM " + tagsTableName + " WHERE " + DbOpenHelper.TAGS_NAME_COLUMN.columnName + " = ?)";
    }

    public static String create(String tableName, Property primaryKey, Property... properties) {
        int i$;
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        builder.append(tableName).append(" (");
        builder.append(primaryKey.columnName).append(" ");
        builder.append(primaryKey.type);
        builder.append("  primary key autoincrement ");
        Property[] arr$ = properties;
        int len$ = properties.length;
        for (i$ = 0; i$ < len$; i$++) {
            Property property = arr$[i$];
            builder.append(", `").append(property.columnName).append("` ").append(property.type);
        }
        arr$ = properties;
        len$ = properties.length;
        for (i$ = 0; i$ < len$; i$++) {
            property = arr$[i$];
            if (property.foreignKey != null) {
                ForeignKey key = property.foreignKey;
                builder.append(", FOREIGN KEY(`").append(property.columnName).append("`) REFERENCES ").append(key.targetTable).append("(`").append(key.targetFieldName).append("`) ON DELETE CASCADE");
            }
        }
        builder.append(" );");
        JqLog.m38d(builder.toString(), new Object[0]);
        return builder.toString();
    }

    public static String drop(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    public final SQLiteStatement getInsertStatement() {
        if (this.insertStatement == null) {
            StringBuilder builder = new StringBuilder("INSERT INTO ").append(this.tableName);
            builder.append(" VALUES (");
            for (int i = 0; i < this.columnCount; i++) {
                if (i != 0) {
                    builder.append(",");
                }
                builder.append("?");
            }
            builder.append(")");
            this.insertStatement = this.db.compileStatement(builder.toString());
        }
        return this.insertStatement;
    }
}
