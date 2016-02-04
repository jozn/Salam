package org.eclipse.paho.android.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Iterator;
import java.util.UUID;
import org.eclipse.paho.android.service.MessageStore.StoredMessage;
import org.eclipse.paho.client.mqttv3.MqttMessage;

class DatabaseMessageStore implements MessageStore {
    private static final String ARRIVED_MESSAGE_TABLE_NAME = "MqttArrivedMessageTable";
    private static final String MTIMESTAMP = "mtimestamp";
    private static String TAG;
    private SQLiteDatabase db;
    private MQTTDatabaseHelper mqttDb;
    private MqttTraceHandler traceHandler;

    /* renamed from: org.eclipse.paho.android.service.DatabaseMessageStore.1 */
    class C12661 implements Iterator<StoredMessage> {
        private Cursor f38c;
        private boolean hasNext;
        final /* synthetic */ String val$clientHandle;

        C12661(String str) {
            this.val$clientHandle = str;
            DatabaseMessageStore.this.db = DatabaseMessageStore.this.mqttDb.getWritableDatabase();
            if (this.val$clientHandle == null) {
                this.f38c = DatabaseMessageStore.this.db.query(DatabaseMessageStore.ARRIVED_MESSAGE_TABLE_NAME, null, null, null, null, null, "mtimestamp ASC");
            } else {
                this.f38c = DatabaseMessageStore.this.db.query(DatabaseMessageStore.ARRIVED_MESSAGE_TABLE_NAME, null, "clientHandle='" + this.val$clientHandle + "'", null, null, null, "mtimestamp ASC");
            }
            this.hasNext = this.f38c.moveToFirst();
        }

        public boolean hasNext() {
            if (!this.hasNext) {
                this.f38c.close();
            }
            return this.hasNext;
        }

        public StoredMessage next() {
            String messageId = this.f38c.getString(this.f38c.getColumnIndex(MqttServiceConstants.MESSAGE_ID));
            String clientHandle = this.f38c.getString(this.f38c.getColumnIndex(MqttServiceConstants.CLIENT_HANDLE));
            String topic = this.f38c.getString(this.f38c.getColumnIndex(MqttServiceConstants.DESTINATION_NAME));
            byte[] payload = this.f38c.getBlob(this.f38c.getColumnIndex(MqttServiceConstants.PAYLOAD));
            int qos = this.f38c.getInt(this.f38c.getColumnIndex(MqttServiceConstants.QOS));
            boolean retained = Boolean.parseBoolean(this.f38c.getString(this.f38c.getColumnIndex(MqttServiceConstants.RETAINED)));
            boolean dup = Boolean.parseBoolean(this.f38c.getString(this.f38c.getColumnIndex(MqttServiceConstants.DUPLICATE)));
            MqttMessageHack message = new MqttMessageHack(payload);
            message.setQos(qos);
            message.setRetained(retained);
            message.setDuplicate(dup);
            this.hasNext = this.f38c.moveToNext();
            return new DbStoredData(messageId, clientHandle, topic, message);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        protected void finalize() throws Throwable {
            this.f38c.close();
            super.finalize();
        }
    }

    private class DbStoredData implements StoredMessage {
        private String clientHandle;
        private MqttMessage message;
        private String messageId;
        private String topic;

        DbStoredData(String messageId, String clientHandle, String topic, MqttMessage message) {
            this.messageId = messageId;
            this.topic = topic;
            this.message = message;
        }

        public String getMessageId() {
            return this.messageId;
        }

        public String getClientHandle() {
            return this.clientHandle;
        }

        public String getTopic() {
            return this.topic;
        }

        public MqttMessage getMessage() {
            return this.message;
        }
    }

    private static class MQTTDatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "mqttAndroidService.db";
        private static final int DATABASE_VERSION = 1;
        private static String TAG;
        private MqttTraceHandler traceHandler;

        static {
            TAG = "MQTTDatabaseHelper";
        }

        public MQTTDatabaseHelper(MqttTraceHandler traceHandler, Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.traceHandler = null;
            this.traceHandler = traceHandler;
        }

        public void onCreate(SQLiteDatabase database) {
            String createArrivedTableStatement = "CREATE TABLE MqttArrivedMessageTable(messageId TEXT PRIMARY KEY, clientHandle TEXT, destinationName TEXT, payload BLOB, qos INTEGER, retained TEXT, duplicate TEXT, mtimestamp INTEGER);";
            this.traceHandler.traceDebug(TAG, "onCreate {" + createArrivedTableStatement + "}");
            try {
                database.execSQL(createArrivedTableStatement);
                this.traceHandler.traceDebug(TAG, "created the table");
            } catch (SQLException e) {
                this.traceHandler.traceException(TAG, "onCreate", e);
                throw e;
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            this.traceHandler.traceDebug(TAG, "onUpgrade");
            try {
                db.execSQL("DROP TABLE IF EXISTS MqttArrivedMessageTable");
                onCreate(db);
                this.traceHandler.traceDebug(TAG, "onUpgrade complete");
            } catch (SQLException e) {
                this.traceHandler.traceException(TAG, "onUpgrade", e);
                throw e;
            }
        }
    }

    private class MqttMessageHack extends MqttMessage {
        public MqttMessageHack(byte[] payload) {
            super(payload);
        }

        protected void setDuplicate(boolean dup) {
            super.setDuplicate(dup);
        }
    }

    static {
        TAG = "DatabaseMessageStore";
    }

    public DatabaseMessageStore(MqttService service, Context context) {
        this.db = null;
        this.mqttDb = null;
        this.traceHandler = null;
        this.traceHandler = service;
        this.mqttDb = new MQTTDatabaseHelper(this.traceHandler, context);
        this.traceHandler.traceDebug(TAG, "DatabaseMessageStore<init> complete");
    }

    public String storeArrived(String clientHandle, String topic, MqttMessage message) {
        this.db = this.mqttDb.getWritableDatabase();
        this.traceHandler.traceDebug(TAG, "storeArrived{" + clientHandle + "}, {" + message.toString() + "}");
        byte[] payload = message.getPayload();
        int qos = message.getQos();
        boolean retained = message.isRetained();
        boolean duplicate = message.isDuplicate();
        ContentValues values = new ContentValues();
        String id = UUID.randomUUID().toString();
        values.put(MqttServiceConstants.MESSAGE_ID, id);
        values.put(MqttServiceConstants.CLIENT_HANDLE, clientHandle);
        values.put(MqttServiceConstants.DESTINATION_NAME, topic);
        values.put(MqttServiceConstants.PAYLOAD, payload);
        values.put(MqttServiceConstants.QOS, Integer.valueOf(qos));
        values.put(MqttServiceConstants.RETAINED, Boolean.valueOf(retained));
        values.put(MqttServiceConstants.DUPLICATE, Boolean.valueOf(duplicate));
        values.put(MTIMESTAMP, Long.valueOf(System.currentTimeMillis()));
        try {
            this.db.insertOrThrow(ARRIVED_MESSAGE_TABLE_NAME, null, values);
            this.traceHandler.traceDebug(TAG, "storeArrived: inserted message with id of {" + id + "} - Number of messages in database for this clientHandle = " + getArrivedRowCount(clientHandle));
            return id;
        } catch (SQLException e) {
            this.traceHandler.traceException(TAG, "onUpgrade", e);
            throw e;
        }
    }

    private int getArrivedRowCount(String clientHandle) {
        Cursor c = this.db.query(ARRIVED_MESSAGE_TABLE_NAME, new String[]{"COUNT(*)"}, "clientHandle='" + clientHandle + "'", null, null, null, null);
        int count = 0;
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }

    public boolean discardArrived(String clientHandle, String id) {
        this.db = this.mqttDb.getWritableDatabase();
        this.traceHandler.traceDebug(TAG, "discardArrived{" + clientHandle + "}, {" + id + "}");
        try {
            int rows = this.db.delete(ARRIVED_MESSAGE_TABLE_NAME, "messageId='" + id + "' AND clientHandle='" + clientHandle + "'", null);
            if (rows != 1) {
                this.traceHandler.traceError(TAG, "discardArrived - Error deleting message {" + id + "} from database: Rows affected = " + rows);
                return false;
            }
            this.traceHandler.traceDebug(TAG, "discardArrived - Message deleted successfully. - messages in db for this clientHandle " + getArrivedRowCount(clientHandle));
            return true;
        } catch (SQLException e) {
            this.traceHandler.traceException(TAG, "discardArrived", e);
            throw e;
        }
    }

    public Iterator<StoredMessage> getAllArrivedMessages(String clientHandle) {
        return new C12661(clientHandle);
    }

    public void clearArrivedMessages(String clientHandle) {
        int rows;
        this.db = this.mqttDb.getWritableDatabase();
        if (clientHandle == null) {
            this.traceHandler.traceDebug(TAG, "clearArrivedMessages: clearing the table");
            rows = this.db.delete(ARRIVED_MESSAGE_TABLE_NAME, null, null);
        } else {
            this.traceHandler.traceDebug(TAG, "clearArrivedMessages: clearing the table of " + clientHandle + " messages");
            rows = this.db.delete(ARRIVED_MESSAGE_TABLE_NAME, "clientHandle='" + clientHandle + "'", null);
        }
        this.traceHandler.traceDebug(TAG, "clearArrivedMessages: rows affected = " + rows);
    }

    public void close() {
        if (this.db != null) {
            this.db.close();
        }
    }
}
