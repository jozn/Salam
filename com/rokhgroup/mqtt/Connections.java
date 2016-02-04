package com.rokhgroup.mqtt;

import android.content.Context;
import java.util.HashMap;

public final class Connections {
    private static Connections instance;
    HashMap<String, Connection> connections;
    Persistence persistence;

    static {
        instance = null;
    }

    private Connections(Context context) {
        this.connections = null;
        this.persistence = null;
        this.connections = new HashMap();
        this.persistence = new Persistence(context);
        try {
            for (Connection c : this.persistence.restoreConnections(context)) {
                this.connections.put(c.clientHandle, c);
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Connections getInstance(Context context) {
        Connections connections;
        synchronized (Connections.class) {
            if (instance == null) {
                instance = new Connections(context);
            }
            connections = instance;
        }
        return connections;
    }

    public final Connection getConnection(String handle) {
        return (Connection) this.connections.get(handle);
    }
}
