package org.eclipse.paho.android.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.content.LocalBroadcastManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

public class MqttService extends Service implements MqttTraceHandler {
    static final String TAG = "MqttService";
    private volatile boolean backgroundDataEnabled;
    private BackgroundDataPreferenceReceiver backgroundDataPreferenceMonitor;
    private Map<String, MqttConnection> connections;
    MessageStore messageStore;
    private MqttServiceBinder mqttServiceBinder;
    private NetworkConnectionIntentReceiver networkConnectionMonitor;
    private String traceCallbackId;
    private boolean traceEnabled;

    private class BackgroundDataPreferenceReceiver extends BroadcastReceiver {
        private BackgroundDataPreferenceReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) MqttService.this.getSystemService("connectivity");
            MqttService.this.traceDebug(MqttService.TAG, "Reconnect since BroadcastReceiver.");
            if (!cm.getBackgroundDataSetting()) {
                MqttService.this.backgroundDataEnabled = false;
                MqttService.this.notifyClientsOffline();
            } else if (!MqttService.this.backgroundDataEnabled) {
                MqttService.this.backgroundDataEnabled = true;
                MqttService.this.reconnect();
            }
        }
    }

    private class NetworkConnectionIntentReceiver extends BroadcastReceiver {
        private NetworkConnectionIntentReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            MqttService.this.traceDebug(MqttService.TAG, "Internal network status receive.");
            WakeLock wl = ((PowerManager) MqttService.this.getSystemService("power")).newWakeLock(1, "MQTT");
            wl.acquire();
            MqttService.this.traceDebug(MqttService.TAG, "Reconnect for Network recovery.");
            if (MqttService.this.isOnline()) {
                MqttService.this.traceDebug(MqttService.TAG, "Online,reconnect.");
                MqttService.this.reconnect();
            } else {
                MqttService.this.notifyClientsOffline();
            }
            wl.release();
        }
    }

    public MqttService() {
        this.traceEnabled = false;
        this.backgroundDataEnabled = true;
        this.connections = new ConcurrentHashMap();
    }

    void callbackToActivity(String clientHandle, Status status, Bundle dataBundle) {
        Intent callbackIntent = new Intent(MqttServiceConstants.CALLBACK_TO_ACTIVITY);
        if (clientHandle != null) {
            callbackIntent.putExtra(MqttServiceConstants.CALLBACK_CLIENT_HANDLE, clientHandle);
        }
        callbackIntent.putExtra(MqttServiceConstants.CALLBACK_STATUS, status);
        if (dataBundle != null) {
            callbackIntent.putExtras(dataBundle);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(callbackIntent);
    }

    public String getClient(String serverURI, String clientId, String contextId, MqttClientPersistence persistence) {
        String clientHandle = serverURI + ":" + clientId + ":" + contextId;
        if (!this.connections.containsKey(clientHandle)) {
            this.connections.put(clientHandle, new MqttConnection(this, serverURI, clientId, persistence, clientHandle));
        }
        return clientHandle;
    }

    public void connect(String clientHandle, MqttConnectOptions connectOptions, String invocationContext, String activityToken) throws MqttSecurityException, MqttException {
        getConnection(clientHandle).connect(connectOptions, invocationContext, activityToken);
    }

    void reconnect() {
        traceDebug(TAG, "Reconnect to server, client size=" + this.connections.size());
        for (MqttConnection client : this.connections.values()) {
            traceDebug("Reconnect Client:", client.getClientId() + '/' + client.getServerURI());
            if (isOnline()) {
                client.reconnect();
            }
        }
    }

    public void close(String clientHandle) {
        getConnection(clientHandle).close();
    }

    public void disconnect(String clientHandle, String invocationContext, String activityToken) {
        getConnection(clientHandle).disconnect(invocationContext, activityToken);
        this.connections.remove(clientHandle);
        stopSelf();
    }

    public void disconnect(String clientHandle, long quiesceTimeout, String invocationContext, String activityToken) {
        getConnection(clientHandle).disconnect(quiesceTimeout, invocationContext, activityToken);
        this.connections.remove(clientHandle);
        stopSelf();
    }

    public boolean isConnected(String clientHandle) {
        return getConnection(clientHandle).isConnected();
    }

    public IMqttDeliveryToken publish(String clientHandle, String topic, byte[] payload, int qos, boolean retained, String invocationContext, String activityToken) throws MqttPersistenceException, MqttException {
        return getConnection(clientHandle).publish(topic, payload, qos, retained, invocationContext, activityToken);
    }

    public IMqttDeliveryToken publish(String clientHandle, String topic, MqttMessage message, String invocationContext, String activityToken) throws MqttPersistenceException, MqttException {
        return getConnection(clientHandle).publish(topic, message, invocationContext, activityToken);
    }

    public void subscribe(String clientHandle, String topic, int qos, String invocationContext, String activityToken) {
        getConnection(clientHandle).subscribe(topic, qos, invocationContext, activityToken);
    }

    public void subscribe(String clientHandle, String[] topic, int[] qos, String invocationContext, String activityToken) {
        getConnection(clientHandle).subscribe(topic, qos, invocationContext, activityToken);
    }

    public void unsubscribe(String clientHandle, String topic, String invocationContext, String activityToken) {
        getConnection(clientHandle).unsubscribe(topic, invocationContext, activityToken);
    }

    public void unsubscribe(String clientHandle, String[] topic, String invocationContext, String activityToken) {
        getConnection(clientHandle).unsubscribe(topic, invocationContext, activityToken);
    }

    public IMqttDeliveryToken[] getPendingDeliveryTokens(String clientHandle) {
        return getConnection(clientHandle).getPendingDeliveryTokens();
    }

    private MqttConnection getConnection(String clientHandle) {
        MqttConnection client = (MqttConnection) this.connections.get(clientHandle);
        if (client != null) {
            return client;
        }
        throw new IllegalArgumentException("Invalid ClientHandle");
    }

    public Status acknowledgeMessageArrival(String clientHandle, String id) {
        if (this.messageStore.discardArrived(clientHandle, id)) {
            return Status.OK;
        }
        return Status.ERROR;
    }

    public void onCreate() {
        super.onCreate();
        this.mqttServiceBinder = new MqttServiceBinder(this);
        this.messageStore = new DatabaseMessageStore(this, this);
    }

    public void onDestroy() {
        for (MqttConnection disconnect : this.connections.values()) {
            disconnect.disconnect(null, null);
        }
        if (this.mqttServiceBinder != null) {
            this.mqttServiceBinder = null;
        }
        unregisterBroadcastReceivers();
        if (this.messageStore != null) {
            this.messageStore.close();
        }
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        this.mqttServiceBinder.setActivityToken(intent.getStringExtra(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN));
        return this.mqttServiceBinder;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        registerBroadcastReceivers();
        return 1;
    }

    public void setTraceCallbackId(String traceCallbackId) {
        this.traceCallbackId = traceCallbackId;
    }

    public void setTraceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
    }

    public boolean isTraceEnabled() {
        return this.traceEnabled;
    }

    public void traceDebug(String tag, String message) {
        traceCallback(MqttServiceConstants.TRACE_DEBUG, tag, message);
    }

    public void traceError(String tag, String message) {
        traceCallback(MqttServiceConstants.TRACE_ERROR, tag, message);
    }

    private void traceCallback(String severity, String tag, String message) {
        if (this.traceCallbackId != null && this.traceEnabled) {
            Bundle dataBundle = new Bundle();
            dataBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.TRACE_ACTION);
            dataBundle.putString(MqttServiceConstants.CALLBACK_TRACE_SEVERITY, severity);
            dataBundle.putString(MqttServiceConstants.CALLBACK_TRACE_TAG, tag);
            dataBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, message);
            callbackToActivity(this.traceCallbackId, Status.ERROR, dataBundle);
        }
    }

    public void traceException(String tag, String message, Exception e) {
        if (this.traceCallbackId != null) {
            Bundle dataBundle = new Bundle();
            dataBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.TRACE_ACTION);
            dataBundle.putString(MqttServiceConstants.CALLBACK_TRACE_SEVERITY, MqttServiceConstants.TRACE_EXCEPTION);
            dataBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, message);
            dataBundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, e);
            dataBundle.putString(MqttServiceConstants.CALLBACK_TRACE_TAG, tag);
            callbackToActivity(this.traceCallbackId, Status.ERROR, dataBundle);
        }
    }

    private void registerBroadcastReceivers() {
        if (this.networkConnectionMonitor == null) {
            this.networkConnectionMonitor = new NetworkConnectionIntentReceiver();
            registerReceiver(this.networkConnectionMonitor, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
        if (VERSION.SDK_INT < 14) {
            this.backgroundDataEnabled = ((ConnectivityManager) getSystemService("connectivity")).getBackgroundDataSetting();
            if (this.backgroundDataPreferenceMonitor == null) {
                this.backgroundDataPreferenceMonitor = new BackgroundDataPreferenceReceiver();
                registerReceiver(this.backgroundDataPreferenceMonitor, new IntentFilter("android.net.conn.BACKGROUND_DATA_SETTING_CHANGED"));
            }
        }
    }

    private void unregisterBroadcastReceivers() {
        if (this.networkConnectionMonitor != null) {
            unregisterReceiver(this.networkConnectionMonitor);
            this.networkConnectionMonitor = null;
        }
        if (VERSION.SDK_INT < 14 && this.backgroundDataPreferenceMonitor != null) {
            unregisterReceiver(this.backgroundDataPreferenceMonitor);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService("connectivity");
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected() && this.backgroundDataEnabled) {
            return true;
        }
        return false;
    }

    public void notifyClientsOffline() {
        for (MqttConnection offline : this.connections.values()) {
            offline.offline();
        }
    }
}
