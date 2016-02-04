package com.rokhgroup.mqtt;

import android.content.Context;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class Connection {
    public MqttAndroidClient client;
    String clientHandle;
    String clientId;
    MqttConnectOptions conOpt;
    private Context context;
    private ArrayList<String> history;
    String host;
    private ArrayList<PropertyChangeListener> listeners;
    long persistenceId;
    int port;
    boolean sslConnection;
    int status$6abac5f4;

    /* renamed from: com.rokhgroup.mqtt.Connection.1 */
    static /* synthetic */ class C06731 {
        static final /* synthetic */ int[] $SwitchMap$com$rokhgroup$mqtt$Connection$ConnectionStatus;

        static {
            $SwitchMap$com$rokhgroup$mqtt$Connection$ConnectionStatus = new int[ConnectionStatus.values$52e8c67a().length];
            try {
                $SwitchMap$com$rokhgroup$mqtt$Connection$ConnectionStatus[ConnectionStatus.CONNECTED$6abac5f4 - 1] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$Connection$ConnectionStatus[ConnectionStatus.DISCONNECTED$6abac5f4 - 1] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$Connection$ConnectionStatus[ConnectionStatus.NONE$6abac5f4 - 1] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$Connection$ConnectionStatus[ConnectionStatus.CONNECTING$6abac5f4 - 1] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$Connection$ConnectionStatus[ConnectionStatus.DISCONNECTING$6abac5f4 - 1] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$rokhgroup$mqtt$Connection$ConnectionStatus[ConnectionStatus.ERROR$6abac5f4 - 1] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    public enum ConnectionStatus {
        ;

        static {
            CONNECTING$6abac5f4 = 1;
            CONNECTED$6abac5f4 = 2;
            DISCONNECTING$6abac5f4 = 3;
            DISCONNECTED$6abac5f4 = 4;
            ERROR$6abac5f4 = 5;
            NONE$6abac5f4 = 6;
            $VALUES$6b2ef867 = new int[]{CONNECTING$6abac5f4, CONNECTED$6abac5f4, DISCONNECTING$6abac5f4, DISCONNECTED$6abac5f4, ERROR$6abac5f4, NONE$6abac5f4};
        }

        public static int[] values$52e8c67a() {
            return (int[]) $VALUES$6b2ef867.clone();
        }
    }

    public Connection(String clientHandle, String clientId, String host, int port, Context context, MqttAndroidClient client, boolean sslConnection) {
        this.clientHandle = null;
        this.clientId = null;
        this.host = null;
        this.port = 0;
        this.status$6abac5f4 = ConnectionStatus.NONE$6abac5f4;
        this.history = null;
        this.client = null;
        this.listeners = new ArrayList();
        this.context = null;
        this.sslConnection = false;
        this.persistenceId = -1;
        this.clientHandle = clientHandle;
        this.clientId = clientId;
        this.host = host;
        this.port = port;
        this.context = context;
        this.client = client;
        this.sslConnection = sslConnection;
        this.history = new ArrayList();
        StringBuffer sb = new StringBuffer();
        sb.append("Client: ");
        sb.append(clientId);
        sb.append(" created");
        addAction(sb.toString());
    }

    public final void addAction(String action) {
        this.history.add(action + this.context.getString(2131493204, new String[]{new SimpleDateFormat(this.context.getString(2131493203)).format(new Date())}));
        notifyListeners(new PropertyChangeEvent(this, "history", null, null));
    }

    public final void changeConnectionStatus$19646d7f(int connectionStatus) {
        this.status$6abac5f4 = connectionStatus;
        notifyListeners(new PropertyChangeEvent(this, "connectionStatus", null, null));
    }

    public final String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.clientId);
        sb.append("\n ");
        switch (C06731.$SwitchMap$com$rokhgroup$mqtt$Connection$ConnectionStatus[this.status$6abac5f4 - 1]) {
            case Logger.SEVERE /*1*/:
                sb.append(this.context.getString(2131493069));
                break;
            case Logger.WARNING /*2*/:
                sb.append(this.context.getString(2131493102));
                break;
            case Logger.INFO /*3*/:
                sb.append(this.context.getString(2131493230));
                break;
            case Logger.CONFIG /*4*/:
                sb.append(this.context.getString(2131493070));
                break;
            case Logger.FINE /*5*/:
                sb.append(this.context.getString(2131493103));
                break;
            case Logger.FINER /*6*/:
                sb.append(this.context.getString(2131493071));
                break;
        }
        sb.append(" ");
        sb.append(this.host);
        return sb.toString();
    }

    public final boolean equals(Object o) {
        if (!(o instanceof Connection)) {
            return false;
        }
        return this.clientHandle.equals(((Connection) o).clientHandle);
    }

    public final void registerChangeListener(PropertyChangeListener listener) {
        this.listeners.add(listener);
    }

    private void notifyListeners(PropertyChangeEvent propertyChangeEvent) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((PropertyChangeListener) it.next()).propertyChange(propertyChangeEvent);
        }
    }
}
