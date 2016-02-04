package com.rokhgroup.mqtt;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import com.shamchat.activity.AddFavoriteTextActivity;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public final class Persistence extends SQLiteOpenHelper implements BaseColumns {
    public Persistence(Context context) {
        super(context, "connections.db", null, 1);
    }

    public final void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS connections (_id INTEGER PRIMARY KEY,host TEXT,clientID TEXT,port INTEGER,ssl INTEGER,timeout INTEGER,keepalive INTEGER,username TEXT,password TEXT,cleanSession INTEGER,topic TEXT,message TEXT,qos INTEGER,retained INTEGER);");
    }

    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS connections");
    }

    public final void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public final List<Connection> restoreConnections(Context context) throws PersistenceException {
        String[] connectionColumns = new String[]{"host", "port", "clientID", "ssl", "keepalive", "cleanSession", "timeout", "username", "password", "topic", AddFavoriteTextActivity.EXTRA_MESSAGE, MqttServiceConstants.RETAINED, MqttServiceConstants.QOS, "_id"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("connections", connectionColumns, null, null, null, null, "host");
        ArrayList<Connection> arrayList = new ArrayList(c.getCount());
        int i = 0;
        while (i < c.getCount()) {
            if (c.moveToNext()) {
                String str;
                String str2;
                Long id = Long.valueOf(c.getLong(c.getColumnIndexOrThrow("_id")));
                String host = c.getString(c.getColumnIndexOrThrow("host"));
                String clientID = c.getString(c.getColumnIndexOrThrow("clientID"));
                int port = c.getInt(c.getColumnIndexOrThrow("port"));
                String username = c.getString(c.getColumnIndexOrThrow("username"));
                String password = c.getString(c.getColumnIndexOrThrow("password"));
                String topic = c.getString(c.getColumnIndexOrThrow("topic"));
                String message = c.getString(c.getColumnIndexOrThrow(AddFavoriteTextActivity.EXTRA_MESSAGE));
                int qos = c.getInt(c.getColumnIndexOrThrow(MqttServiceConstants.QOS));
                int keepAlive = c.getInt(c.getColumnIndexOrThrow("keepalive"));
                int timeout = c.getInt(c.getColumnIndexOrThrow("timeout"));
                boolean cleanSession = c.getInt(c.getColumnIndexOrThrow("cleanSession")) == 1;
                boolean retained = c.getInt(c.getColumnIndexOrThrow(MqttServiceConstants.RETAINED)) == 1;
                boolean ssl = c.getInt(c.getColumnIndexOrThrow("ssl")) == 1;
                MqttConnectOptions opts = new MqttConnectOptions();
                opts.setCleanSession(cleanSession);
                opts.setKeepAliveInterval(keepAlive);
                opts.setConnectionTimeout(timeout);
                opts.setPassword(password != null ? password.toCharArray() : null);
                opts.setUserName(username);
                if (topic != null) {
                    opts.setWill(topic, message.getBytes(), qos, retained);
                }
                if (ssl) {
                    str = "ssl://" + host + ":" + port;
                    str2 = str + clientID;
                } else {
                    str = "tcp://" + host + ":" + port;
                    str2 = str + clientID;
                }
                Connection connection = new Connection(str2, clientID, host, port, context, new MqttAndroidClient(context, str, clientID), ssl);
                connection.conOpt = opts;
                connection.persistenceId = id.longValue();
                arrayList.add(connection);
                i++;
            } else {
                throw new PersistenceException("Failed restoring connection - count: " + c.getCount() + "loop iteration: " + i);
            }
        }
        c.close();
        db.close();
        return arrayList;
    }
}
