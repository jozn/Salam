package org.eclipse.paho.android.service;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.paho.android.service.MessageStore.StoredMessage;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

class MqttConnection implements MqttCallback {
    private static final String NOT_CONNECTED = "not connected";
    private static final String TAG = "MqttConnection";
    private boolean cleanSession;
    private String clientHandle;
    private String clientId;
    private MqttConnectOptions connectOptions;
    private volatile boolean disconnected;
    private volatile boolean isConnecting;
    private MqttAsyncClient myClient;
    private MqttClientPersistence persistence;
    private String reconnectActivityToken;
    private Map<IMqttDeliveryToken, String> savedActivityTokens;
    private Map<IMqttDeliveryToken, String> savedInvocationContexts;
    private Map<IMqttDeliveryToken, MqttMessage> savedSentMessages;
    private Map<IMqttDeliveryToken, String> savedTopics;
    private String serverURI;
    private MqttService service;
    private String wakeLockTag;
    private WakeLock wakelock;

    private class MqttConnectionListener implements IMqttActionListener {
        private final Bundle resultBundle;

        private MqttConnectionListener(Bundle resultBundle) {
            this.resultBundle = resultBundle;
        }

        public void onSuccess(IMqttToken asyncActionToken) {
            MqttConnection.this.service.callbackToActivity(MqttConnection.this.clientHandle, Status.OK, this.resultBundle);
        }

        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            this.resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, exception.getLocalizedMessage());
            this.resultBundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, exception);
            MqttConnection.this.service.callbackToActivity(MqttConnection.this.clientHandle, Status.ERROR, this.resultBundle);
        }
    }

    /* renamed from: org.eclipse.paho.android.service.MqttConnection.1 */
    class C12681 extends MqttConnectionListener {
        final /* synthetic */ Bundle val$resultBundle;

        C12681(Bundle x0, Bundle bundle) {
            this.val$resultBundle = bundle;
            super(x0, null);
        }

        public void onSuccess(IMqttToken asyncActionToken) {
            MqttConnection.this.doAfterConnectSuccess(this.val$resultBundle);
            MqttConnection.this.service.traceDebug(MqttConnection.TAG, "connect success!");
        }

        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            this.val$resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, exception.getLocalizedMessage());
            this.val$resultBundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, exception);
            MqttConnection.this.service.traceError(MqttConnection.TAG, "connect fail, call connect to reconnect.reason:" + exception.getMessage());
            MqttConnection.this.doAfterConnectFail(this.val$resultBundle);
        }
    }

    /* renamed from: org.eclipse.paho.android.service.MqttConnection.2 */
    class C12692 implements IMqttActionListener {
        C12692() {
        }

        public void onSuccess(IMqttToken asyncActionToken) {
        }

        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        }
    }

    /* renamed from: org.eclipse.paho.android.service.MqttConnection.3 */
    class C12703 extends MqttConnectionListener {
        final /* synthetic */ Bundle val$resultBundle;

        C12703(Bundle x0, Bundle bundle) {
            this.val$resultBundle = bundle;
            super(x0, null);
        }

        public void onSuccess(IMqttToken asyncActionToken) {
            MqttConnection.this.service.traceDebug(MqttConnection.TAG, "Reconnect Success!");
            MqttConnection.this.service.traceDebug(MqttConnection.TAG, "DeliverBacklog when reconnect.");
            MqttConnection.this.doAfterConnectSuccess(this.val$resultBundle);
        }

        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            this.val$resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, exception.getLocalizedMessage());
            this.val$resultBundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, exception);
            MqttConnection.this.service.callbackToActivity(MqttConnection.this.clientHandle, Status.ERROR, this.val$resultBundle);
            MqttConnection.this.doAfterConnectFail(this.val$resultBundle);
        }
    }

    public String getServerURI() {
        return this.serverURI;
    }

    public void setServerURI(String serverURI) {
        this.serverURI = serverURI;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MqttConnectOptions getConnectOptions() {
        return this.connectOptions;
    }

    public void setConnectOptions(MqttConnectOptions connectOptions) {
        this.connectOptions = connectOptions;
    }

    public String getClientHandle() {
        return this.clientHandle;
    }

    public void setClientHandle(String clientHandle) {
        this.clientHandle = clientHandle;
    }

    MqttConnection(MqttService service, String serverURI, String clientId, MqttClientPersistence persistence, String clientHandle) {
        this.persistence = null;
        this.reconnectActivityToken = null;
        this.myClient = null;
        this.service = null;
        this.disconnected = true;
        this.cleanSession = true;
        this.isConnecting = false;
        this.savedTopics = new HashMap();
        this.savedSentMessages = new HashMap();
        this.savedActivityTokens = new HashMap();
        this.savedInvocationContexts = new HashMap();
        this.wakelock = null;
        this.wakeLockTag = null;
        this.serverURI = serverURI.toString();
        this.service = service;
        this.clientId = clientId;
        this.persistence = persistence;
        this.clientHandle = clientHandle;
        StringBuffer buff = new StringBuffer(getClass().getCanonicalName());
        buff.append(" ");
        buff.append(clientId);
        buff.append(" ");
        buff.append("on host ");
        buff.append(serverURI);
        this.wakeLockTag = buff.toString();
    }

    public void connect(MqttConnectOptions options, String invocationContext, String activityToken) {
        this.connectOptions = options;
        this.reconnectActivityToken = activityToken;
        if (options != null) {
            this.cleanSession = options.isCleanSession();
        }
        if (this.connectOptions.isCleanSession()) {
            this.service.messageStore.clearArrivedMessages(this.clientHandle);
        }
        this.service.traceDebug(TAG, "Connecting {" + this.serverURI + "} as {" + this.clientId + "}");
        Bundle resultBundle = new Bundle();
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, activityToken);
        resultBundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, invocationContext);
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.CONNECT_ACTION);
        try {
            if (this.persistence == null) {
                File myDir = this.service.getExternalFilesDir(TAG);
                if (myDir == null) {
                    myDir = this.service.getDir(TAG, 0);
                    if (myDir == null) {
                        resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, "Error! No external and internal storage available");
                        resultBundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, new MqttPersistenceException());
                        this.service.callbackToActivity(this.clientHandle, Status.ERROR, resultBundle);
                        return;
                    }
                }
                this.persistence = new MqttDefaultFilePersistence(myDir.getAbsolutePath());
            }
            IMqttActionListener listener = new C12681(resultBundle, resultBundle);
            if (this.myClient == null) {
                this.myClient = new MqttAsyncClient(this.serverURI, this.clientId, this.persistence, new AlarmPingSender(this.service));
                this.myClient.setCallback(this);
                this.service.traceDebug(TAG, "Do Real connect!");
                setConnectingState(true);
                this.myClient.connect(this.connectOptions, invocationContext, listener);
            } else if (this.isConnecting) {
                this.service.traceDebug(TAG, "myClient != null and the client is connecting. Connect return directly.");
                this.service.traceDebug(TAG, "Connect return:isConnecting:" + this.isConnecting + ".disconnected:" + this.disconnected);
            } else if (this.disconnected) {
                this.service.traceDebug(TAG, "myClient != null and the client is not connected");
                this.service.traceDebug(TAG, "Do Real connect!");
                setConnectingState(true);
                this.myClient.connect(this.connectOptions, invocationContext, listener);
            } else {
                this.service.traceDebug(TAG, "myClient != null and the client is connected and notify!");
                doAfterConnectSuccess(resultBundle);
            }
        } catch (Exception e) {
            handleException(resultBundle, e);
        }
    }

    private void doAfterConnectSuccess(Bundle resultBundle) {
        acquireWakeLock();
        this.service.callbackToActivity(this.clientHandle, Status.OK, resultBundle);
        deliverBacklog();
        setConnectingState(false);
        this.disconnected = false;
        releaseWakeLock();
    }

    private void doAfterConnectFail(Bundle resultBundle) {
        acquireWakeLock();
        this.disconnected = true;
        setConnectingState(false);
        this.service.callbackToActivity(this.clientHandle, Status.ERROR, resultBundle);
        releaseWakeLock();
    }

    private void handleException(Bundle resultBundle, Exception e) {
        resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, e.getLocalizedMessage());
        resultBundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, e);
        this.service.callbackToActivity(this.clientHandle, Status.ERROR, resultBundle);
    }

    private void deliverBacklog() {
        Iterator<StoredMessage> backlog = this.service.messageStore.getAllArrivedMessages(this.clientHandle);
        while (backlog.hasNext()) {
            StoredMessage msgArrived = (StoredMessage) backlog.next();
            Bundle resultBundle = messageToBundle(msgArrived.getMessageId(), msgArrived.getTopic(), msgArrived.getMessage());
            resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.MESSAGE_ARRIVED_ACTION);
            this.service.callbackToActivity(this.clientHandle, Status.OK, resultBundle);
        }
    }

    private Bundle messageToBundle(String messageId, String topic, MqttMessage message) {
        Bundle result = new Bundle();
        result.putString(MqttServiceConstants.CALLBACK_MESSAGE_ID, messageId);
        result.putString(MqttServiceConstants.CALLBACK_DESTINATION_NAME, topic);
        result.putParcelable(MqttServiceConstants.CALLBACK_MESSAGE_PARCEL, new ParcelableMqttMessage(message));
        return result;
    }

    void close() {
        this.service.traceDebug(TAG, "close()");
        try {
            if (this.myClient != null) {
                this.myClient.close();
            }
        } catch (MqttException e) {
            handleException(new Bundle(), e);
        }
    }

    void disconnect(long quiesceTimeout, String invocationContext, String activityToken) {
        this.service.traceDebug(TAG, "disconnect()");
        this.disconnected = true;
        Bundle resultBundle = new Bundle();
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, activityToken);
        resultBundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, invocationContext);
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.DISCONNECT_ACTION);
        if (this.myClient == null || !this.myClient.isConnected()) {
            resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, NOT_CONNECTED);
            this.service.traceError(MqttServiceConstants.DISCONNECT_ACTION, NOT_CONNECTED);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, resultBundle);
        } else {
            try {
                this.myClient.disconnect(quiesceTimeout, invocationContext, new MqttConnectionListener(resultBundle, null));
            } catch (Exception e) {
                handleException(resultBundle, e);
            }
        }
        if (this.connectOptions.isCleanSession()) {
            this.service.messageStore.clearArrivedMessages(this.clientHandle);
        }
        releaseWakeLock();
    }

    void disconnect(String invocationContext, String activityToken) {
        this.service.traceDebug(TAG, "disconnect()");
        this.disconnected = true;
        Bundle resultBundle = new Bundle();
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, activityToken);
        resultBundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, invocationContext);
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.DISCONNECT_ACTION);
        if (this.myClient == null || !this.myClient.isConnected()) {
            resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, NOT_CONNECTED);
            this.service.traceError(MqttServiceConstants.DISCONNECT_ACTION, NOT_CONNECTED);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, resultBundle);
        } else {
            try {
                this.myClient.disconnect(invocationContext, new MqttConnectionListener(resultBundle, null));
            } catch (Exception e) {
                handleException(resultBundle, e);
            }
        }
        if (this.connectOptions.isCleanSession()) {
            this.service.messageStore.clearArrivedMessages(this.clientHandle);
        }
        releaseWakeLock();
    }

    public boolean isConnected() {
        if (this.myClient != null) {
            return this.myClient.isConnected();
        }
        return false;
    }

    public IMqttDeliveryToken publish(String topic, byte[] payload, int qos, boolean retained, String invocationContext, String activityToken) {
        Exception e;
        Bundle resultBundle = new Bundle();
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.SEND_ACTION);
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, activityToken);
        resultBundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, invocationContext);
        if (this.myClient == null || !this.myClient.isConnected()) {
            resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, NOT_CONNECTED);
            this.service.traceError(MqttServiceConstants.SEND_ACTION, NOT_CONNECTED);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, resultBundle);
            return null;
        }
        IMqttActionListener listener = new MqttConnectionListener(resultBundle, null);
        IMqttDeliveryToken publish;
        try {
            MqttMessage message = new MqttMessage(payload);
            message.setQos(qos);
            message.setRetained(retained);
            publish = this.myClient.publish(topic, payload, qos, retained, invocationContext, listener);
            try {
                storeSendDetails(topic, message, publish, invocationContext, activityToken);
                return publish;
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Exception e3) {
            e = e3;
            publish = null;
            handleException(resultBundle, e);
            return publish;
        }
    }

    public IMqttDeliveryToken publish(String topic, MqttMessage message, String invocationContext, String activityToken) {
        Bundle resultBundle = new Bundle();
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.SEND_ACTION);
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, activityToken);
        resultBundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, invocationContext);
        IMqttDeliveryToken sendToken = null;
        if (this.myClient == null || !this.myClient.isConnected()) {
            resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, NOT_CONNECTED);
            this.service.traceError(MqttServiceConstants.SEND_ACTION, NOT_CONNECTED);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, resultBundle);
            return null;
        }
        try {
            sendToken = this.myClient.publish(topic, message, (Object) invocationContext, new MqttConnectionListener(resultBundle, null));
            storeSendDetails(topic, message, sendToken, invocationContext, activityToken);
            return sendToken;
        } catch (Exception e) {
            handleException(resultBundle, e);
            return sendToken;
        }
    }

    public void subscribe(String topic, int qos, String invocationContext, String activityToken) {
        this.service.traceDebug(TAG, "subscribe({" + topic + "}," + qos + ",{" + invocationContext + "}, {" + activityToken + "}");
        Bundle resultBundle = new Bundle();
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.SUBSCRIBE_ACTION);
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, activityToken);
        resultBundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, invocationContext);
        if (this.myClient == null || !this.myClient.isConnected()) {
            resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, NOT_CONNECTED);
            this.service.traceError(MqttServiceConstants.SUBSCRIBE_ACTION, NOT_CONNECTED);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, resultBundle);
            return;
        }
        try {
            this.myClient.subscribe(topic, qos, (Object) invocationContext, new MqttConnectionListener(resultBundle, null));
        } catch (Exception e) {
            handleException(resultBundle, e);
        }
    }

    public void subscribe(String[] topic, int[] qos, String invocationContext, String activityToken) {
        this.service.traceDebug(TAG, "subscribe({" + topic + "}," + qos + ",{" + invocationContext + "}, {" + activityToken + "}");
        Bundle resultBundle = new Bundle();
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.SUBSCRIBE_ACTION);
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, activityToken);
        resultBundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, invocationContext);
        if (this.myClient == null || !this.myClient.isConnected()) {
            resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, NOT_CONNECTED);
            this.service.traceError(MqttServiceConstants.SUBSCRIBE_ACTION, NOT_CONNECTED);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, resultBundle);
            return;
        }
        try {
            this.myClient.subscribe(topic, qos, (Object) invocationContext, new MqttConnectionListener(resultBundle, null));
        } catch (Exception e) {
            handleException(resultBundle, e);
        }
    }

    void unsubscribe(String topic, String invocationContext, String activityToken) {
        this.service.traceDebug(TAG, "unsubscribe({" + topic + "},{" + invocationContext + "}, {" + activityToken + "})");
        Bundle resultBundle = new Bundle();
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.UNSUBSCRIBE_ACTION);
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, activityToken);
        resultBundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, invocationContext);
        if (this.myClient == null || !this.myClient.isConnected()) {
            resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, NOT_CONNECTED);
            this.service.traceError(MqttServiceConstants.SUBSCRIBE_ACTION, NOT_CONNECTED);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, resultBundle);
            return;
        }
        try {
            this.myClient.unsubscribe(topic, (Object) invocationContext, new MqttConnectionListener(resultBundle, null));
        } catch (Exception e) {
            handleException(resultBundle, e);
        }
    }

    void unsubscribe(String[] topic, String invocationContext, String activityToken) {
        this.service.traceDebug(TAG, "unsubscribe({" + topic + "},{" + invocationContext + "}, {" + activityToken + "})");
        Bundle resultBundle = new Bundle();
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.UNSUBSCRIBE_ACTION);
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, activityToken);
        resultBundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, invocationContext);
        if (this.myClient == null || !this.myClient.isConnected()) {
            resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, NOT_CONNECTED);
            this.service.traceError(MqttServiceConstants.SUBSCRIBE_ACTION, NOT_CONNECTED);
            this.service.callbackToActivity(this.clientHandle, Status.ERROR, resultBundle);
            return;
        }
        try {
            this.myClient.unsubscribe(topic, (Object) invocationContext, new MqttConnectionListener(resultBundle, null));
        } catch (Exception e) {
            handleException(resultBundle, e);
        }
    }

    public IMqttDeliveryToken[] getPendingDeliveryTokens() {
        return this.myClient.getPendingDeliveryTokens();
    }

    public void connectionLost(Throwable why) {
        this.service.traceDebug(TAG, "connectionLost(" + why.getMessage() + ")");
        this.disconnected = true;
        try {
            this.myClient.disconnect(null, new C12692());
        } catch (Exception e) {
        }
        Bundle resultBundle = new Bundle();
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.ON_CONNECTION_LOST_ACTION);
        if (why != null) {
            resultBundle.putString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE, why.getMessage());
            if (why instanceof MqttException) {
                resultBundle.putSerializable(MqttServiceConstants.CALLBACK_EXCEPTION, why);
            }
            resultBundle.putString(MqttServiceConstants.CALLBACK_EXCEPTION_STACK, Log.getStackTraceString(why));
        }
        this.service.callbackToActivity(this.clientHandle, Status.OK, resultBundle);
        releaseWakeLock();
    }

    public void deliveryComplete(IMqttDeliveryToken messageToken) {
        this.service.traceDebug(TAG, "deliveryComplete(" + messageToken + ")");
        MqttMessage message = (MqttMessage) this.savedSentMessages.remove(messageToken);
        if (message != null) {
            String activityToken = (String) this.savedActivityTokens.remove(messageToken);
            String invocationContext = (String) this.savedInvocationContexts.remove(messageToken);
            Bundle resultBundle = messageToBundle(null, (String) this.savedTopics.remove(messageToken), message);
            if (activityToken != null) {
                resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.SEND_ACTION);
                resultBundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, activityToken);
                resultBundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, invocationContext);
                this.service.callbackToActivity(this.clientHandle, Status.OK, resultBundle);
            }
            resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.MESSAGE_DELIVERED_ACTION);
            this.service.callbackToActivity(this.clientHandle, Status.OK, resultBundle);
        }
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        this.service.traceDebug(TAG, "messageArrived(" + topic + ",{" + message.toString() + "})");
        String messageId = this.service.messageStore.storeArrived(this.clientHandle, topic, message);
        Bundle resultBundle = messageToBundle(messageId, topic, message);
        resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.MESSAGE_ARRIVED_ACTION);
        resultBundle.putString(MqttServiceConstants.CALLBACK_MESSAGE_ID, messageId);
        this.service.callbackToActivity(this.clientHandle, Status.OK, resultBundle);
    }

    private void storeSendDetails(String topic, MqttMessage msg, IMqttDeliveryToken messageToken, String invocationContext, String activityToken) {
        this.savedTopics.put(messageToken, topic);
        this.savedSentMessages.put(messageToken, msg);
        this.savedActivityTokens.put(messageToken, activityToken);
        this.savedInvocationContexts.put(messageToken, invocationContext);
    }

    private void acquireWakeLock() {
        if (this.wakelock == null) {
            this.wakelock = ((PowerManager) this.service.getSystemService("power")).newWakeLock(1, this.wakeLockTag);
        }
        this.wakelock.acquire();
    }

    private void releaseWakeLock() {
        if (this.wakelock != null && this.wakelock.isHeld()) {
            this.wakelock.release();
        }
    }

    void offline() {
        if (!this.disconnected && !this.cleanSession) {
            connectionLost(new Exception("Android offline"));
        }
    }

    synchronized void reconnect() {
        if (this.isConnecting) {
            this.service.traceDebug(TAG, "The client is connecting. Reconnect return directly.");
        } else if (!this.service.isOnline()) {
            this.service.traceDebug(TAG, "The network is not reachable. Will not do reconnect");
        } else if (this.disconnected && !this.cleanSession) {
            this.service.traceDebug(TAG, "Do Real Reconnect!");
            Bundle resultBundle = new Bundle();
            resultBundle.putString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN, this.reconnectActivityToken);
            resultBundle.putString(MqttServiceConstants.CALLBACK_INVOCATION_CONTEXT, null);
            resultBundle.putString(MqttServiceConstants.CALLBACK_ACTION, MqttServiceConstants.CONNECT_ACTION);
            try {
                this.myClient.connect(this.connectOptions, null, new C12703(resultBundle, resultBundle));
                setConnectingState(true);
            } catch (MqttException e) {
                this.service.traceError(TAG, "Cannot reconnect to remote server." + e.getMessage());
                setConnectingState(false);
                handleException(resultBundle, e);
            }
        }
    }

    synchronized void setConnectingState(boolean isConnecting) {
        this.isConnecting = isConnecting;
    }
}
