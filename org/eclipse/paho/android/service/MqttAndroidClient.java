package org.eclipse.paho.android.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.SparseArray;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

public class MqttAndroidClient extends BroadcastReceiver implements IMqttAsyncClient {
    private static final int BIND_SERVICE_FLAG = 0;
    private static final String SERVICE_NAME = "org.eclipse.paho.android.service.MqttService";
    private static ExecutorService pool;
    private volatile boolean bindedService;
    private MqttCallback callback;
    private String clientHandle;
    private String clientId;
    private MqttConnectOptions connectOptions;
    private IMqttToken connectToken;
    private Ack messageAck;
    private MqttService mqttService;
    Context myContext;
    private MqttClientPersistence persistence;
    private volatile boolean registerReceiver;
    private String serverURI;
    private MyServiceConnection serviceConnection;
    private SparseArray<IMqttToken> tokenMap;
    private int tokenNumber;
    private MqttTraceHandler traceCallback;
    private boolean traceEnabled;

    /* renamed from: org.eclipse.paho.android.service.MqttAndroidClient.1 */
    class C12671 implements Runnable {
        C12671() {
        }

        public void run() {
            MqttAndroidClient.this.doConnect();
            MqttAndroidClient.this.registerReceiver(MqttAndroidClient.this);
        }
    }

    public enum Ack {
        AUTO_ACK,
        MANUAL_ACK
    }

    private final class MyServiceConnection implements ServiceConnection {
        private MyServiceConnection() {
        }

        public final void onServiceConnected(ComponentName name, IBinder binder) {
            MqttAndroidClient.this.mqttService = ((MqttServiceBinder) binder).getService();
            MqttAndroidClient.this.bindedService = true;
            MqttAndroidClient.this.doConnect();
        }

        public final void onServiceDisconnected(ComponentName name) {
            MqttAndroidClient.this.mqttService = null;
        }
    }

    static {
        pool = Executors.newCachedThreadPool();
    }

    public MqttAndroidClient(Context context, String serverURI, String clientId) {
        this(context, serverURI, clientId, null, Ack.AUTO_ACK);
    }

    public MqttAndroidClient(Context ctx, String serverURI, String clientId, Ack ackType) {
        this(ctx, serverURI, clientId, null, ackType);
    }

    public MqttAndroidClient(Context ctx, String serverURI, String clientId, MqttClientPersistence persistence) {
        this(ctx, serverURI, clientId, persistence, Ack.AUTO_ACK);
    }

    public MqttAndroidClient(Context context, String serverURI, String clientId, MqttClientPersistence persistence, Ack ackType) {
        this.serviceConnection = new MyServiceConnection();
        this.tokenMap = new SparseArray();
        this.tokenNumber = BIND_SERVICE_FLAG;
        this.persistence = null;
        this.traceEnabled = false;
        this.registerReceiver = false;
        this.bindedService = false;
        this.myContext = context;
        this.serverURI = serverURI;
        this.clientId = clientId;
        this.persistence = persistence;
        this.messageAck = ackType;
    }

    public boolean isConnected() {
        if (this.mqttService != null) {
            return this.mqttService.isConnected(this.clientHandle);
        }
        return false;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getServerURI() {
        return this.serverURI;
    }

    public void close() {
        if (this.clientHandle == null) {
            this.clientHandle = this.mqttService.getClient(this.serverURI, this.clientId, this.myContext.getApplicationInfo().packageName, this.persistence);
        }
        this.mqttService.close(this.clientHandle);
    }

    public IMqttToken connect() throws MqttException {
        return connect(null, null);
    }

    public IMqttToken connect(MqttConnectOptions options) throws MqttException {
        return connect(options, null, null);
    }

    public IMqttToken connect(Object userContext, IMqttActionListener callback) throws MqttException {
        return connect(new MqttConnectOptions(), userContext, callback);
    }

    public IMqttToken connect(MqttConnectOptions options, Object userContext, IMqttActionListener callback) throws MqttException {
        IMqttToken token = new MqttTokenAndroid(this, userContext, callback);
        this.connectOptions = options;
        this.connectToken = token;
        if (this.mqttService == null) {
            Intent serviceStartIntent = new Intent();
            serviceStartIntent.setClassName(this.myContext, SERVICE_NAME);
            if (this.myContext.startService(serviceStartIntent) == null) {
                IMqttActionListener listener = token.getActionCallback();
                if (listener != null) {
                    listener.onFailure(token, new RuntimeException("cannot start service org.eclipse.paho.android.service.MqttService"));
                }
            }
            this.myContext.startService(serviceStartIntent);
            this.myContext.bindService(serviceStartIntent, this.serviceConnection, 1);
            registerReceiver(this);
        } else {
            pool.execute(new C12671());
        }
        return token;
    }

    private void registerReceiver(BroadcastReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MqttServiceConstants.CALLBACK_TO_ACTIVITY);
        LocalBroadcastManager.getInstance(this.myContext).registerReceiver(receiver, filter);
        this.registerReceiver = true;
    }

    private void doConnect() {
        if (this.clientHandle == null) {
            this.clientHandle = this.mqttService.getClient(this.serverURI, this.clientId, this.myContext.getApplicationInfo().packageName, this.persistence);
        }
        this.mqttService.setTraceEnabled(this.traceEnabled);
        this.mqttService.setTraceCallbackId(this.clientHandle);
        try {
            this.mqttService.connect(this.clientHandle, this.connectOptions, null, storeToken(this.connectToken));
        } catch (MqttException e) {
            IMqttActionListener listener = this.connectToken.getActionCallback();
            if (listener != null) {
                listener.onFailure(this.connectToken, e);
            }
        }
    }

    public IMqttToken disconnect() throws MqttException {
        IMqttToken token = new MqttTokenAndroid(this, null, null);
        this.mqttService.disconnect(this.clientHandle, null, storeToken(token));
        return token;
    }

    public IMqttToken disconnect(long quiesceTimeout) throws MqttException {
        IMqttToken token = new MqttTokenAndroid(this, null, null);
        this.mqttService.disconnect(this.clientHandle, quiesceTimeout, null, storeToken(token));
        return token;
    }

    public IMqttToken disconnect(Object userContext, IMqttActionListener callback) throws MqttException {
        IMqttToken token = new MqttTokenAndroid(this, userContext, callback);
        this.mqttService.disconnect(this.clientHandle, null, storeToken(token));
        return token;
    }

    public IMqttToken disconnect(long quiesceTimeout, Object userContext, IMqttActionListener callback) throws MqttException {
        IMqttToken token = new MqttTokenAndroid(this, userContext, callback);
        this.mqttService.disconnect(this.clientHandle, quiesceTimeout, null, storeToken(token));
        return token;
    }

    public IMqttDeliveryToken publish(String topic, byte[] payload, int qos, boolean retained) throws MqttException, MqttPersistenceException {
        return publish(topic, payload, qos, retained, null, null);
    }

    public IMqttDeliveryToken publish(String topic, MqttMessage message) throws MqttException, MqttPersistenceException {
        return publish(topic, message, null, null);
    }

    public IMqttDeliveryToken publish(String topic, byte[] payload, int qos, boolean retained, Object userContext, IMqttActionListener callback) throws MqttException, MqttPersistenceException {
        MqttMessage message = new MqttMessage(payload);
        message.setQos(qos);
        message.setRetained(retained);
        MqttDeliveryTokenAndroid token = new MqttDeliveryTokenAndroid(this, userContext, callback, message);
        token.setDelegate(this.mqttService.publish(this.clientHandle, topic, payload, qos, retained, null, storeToken(token)));
        return token;
    }

    public IMqttDeliveryToken publish(String topic, MqttMessage message, Object userContext, IMqttActionListener callback) throws MqttException, MqttPersistenceException {
        MqttDeliveryTokenAndroid token = new MqttDeliveryTokenAndroid(this, userContext, callback, message);
        token.setDelegate(this.mqttService.publish(this.clientHandle, topic, message, null, storeToken(token)));
        return token;
    }

    public IMqttToken subscribe(String topic, int qos) throws MqttException, MqttSecurityException {
        return subscribe(topic, qos, null, null);
    }

    public IMqttToken subscribe(String[] topic, int[] qos) throws MqttException, MqttSecurityException {
        return subscribe(topic, qos, null, null);
    }

    public IMqttToken subscribe(String topic, int qos, Object userContext, IMqttActionListener callback) throws MqttException {
        IMqttToken token = new MqttTokenAndroid(this, userContext, callback, new String[]{topic});
        this.mqttService.subscribe(this.clientHandle, topic, qos, null, storeToken(token));
        return token;
    }

    public IMqttToken subscribe(String[] topic, int[] qos, Object userContext, IMqttActionListener callback) throws MqttException {
        IMqttToken token = new MqttTokenAndroid(this, userContext, callback, topic);
        this.mqttService.subscribe(this.clientHandle, topic, qos, null, storeToken(token));
        return token;
    }

    public IMqttToken unsubscribe(String topic) throws MqttException {
        return unsubscribe(topic, null, null);
    }

    public IMqttToken unsubscribe(String[] topic) throws MqttException {
        return unsubscribe(topic, null, null);
    }

    public IMqttToken unsubscribe(String topic, Object userContext, IMqttActionListener callback) throws MqttException {
        IMqttToken token = new MqttTokenAndroid(this, userContext, callback);
        this.mqttService.unsubscribe(this.clientHandle, topic, null, storeToken(token));
        return token;
    }

    public IMqttToken unsubscribe(String[] topic, Object userContext, IMqttActionListener callback) throws MqttException {
        IMqttToken token = new MqttTokenAndroid(this, userContext, callback);
        this.mqttService.unsubscribe(this.clientHandle, topic, null, storeToken(token));
        return token;
    }

    public IMqttDeliveryToken[] getPendingDeliveryTokens() {
        return this.mqttService.getPendingDeliveryTokens(this.clientHandle);
    }

    public void setCallback(MqttCallback callback) {
        this.callback = callback;
    }

    public void setTraceCallback(MqttTraceHandler traceCallback) {
        this.traceCallback = traceCallback;
    }

    public void setTraceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
        if (this.mqttService != null) {
            this.mqttService.setTraceEnabled(traceEnabled);
        }
    }

    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        String handleFromIntent = data.getString(MqttServiceConstants.CALLBACK_CLIENT_HANDLE);
        if (handleFromIntent != null && handleFromIntent.equals(this.clientHandle)) {
            String action = data.getString(MqttServiceConstants.CALLBACK_ACTION);
            if (MqttServiceConstants.CONNECT_ACTION.equals(action)) {
                connectAction(data);
            } else if (MqttServiceConstants.MESSAGE_ARRIVED_ACTION.equals(action)) {
                messageArrivedAction(data);
            } else if (MqttServiceConstants.SUBSCRIBE_ACTION.equals(action)) {
                subscribeAction(data);
            } else if (MqttServiceConstants.UNSUBSCRIBE_ACTION.equals(action)) {
                unSubscribeAction(data);
            } else if (MqttServiceConstants.SEND_ACTION.equals(action)) {
                sendAction(data);
            } else if (MqttServiceConstants.MESSAGE_DELIVERED_ACTION.equals(action)) {
                messageDeliveredAction(data);
            } else if (MqttServiceConstants.ON_CONNECTION_LOST_ACTION.equals(action)) {
                connectionLostAction(data);
            } else if (MqttServiceConstants.DISCONNECT_ACTION.equals(action)) {
                disconnected(data);
            } else if (MqttServiceConstants.TRACE_ACTION.equals(action)) {
                traceAction(data);
            } else {
                this.mqttService.traceError(MqttServiceConstants.WAKELOCK_NETWORK_INTENT, "Callback action doesn't exist.");
            }
        }
    }

    public boolean acknowledgeMessage(String messageId) {
        if (this.messageAck == Ack.MANUAL_ACK && this.mqttService.acknowledgeMessageArrival(this.clientHandle, messageId) == Status.OK) {
            return true;
        }
        return false;
    }

    private void connectAction(Bundle data) {
        IMqttToken token = this.connectToken;
        removeMqttToken(data);
        simpleAction(token, data);
    }

    private void disconnected(Bundle data) {
        this.clientHandle = null;
        IMqttToken token = removeMqttToken(data);
        if (token != null) {
            ((MqttTokenAndroid) token).notifyComplete();
        }
        if (this.callback != null) {
            this.callback.connectionLost(null);
        }
    }

    private void connectionLostAction(Bundle data) {
        if (this.callback != null) {
            this.callback.connectionLost((Exception) data.getSerializable(MqttServiceConstants.CALLBACK_EXCEPTION));
        }
    }

    private void simpleAction(IMqttToken token, Bundle data) {
        if (token == null) {
            this.mqttService.traceError(MqttServiceConstants.WAKELOCK_NETWORK_INTENT, "simpleAction : token is null");
        } else if (((Status) data.getSerializable(MqttServiceConstants.CALLBACK_STATUS)) == Status.OK) {
            ((MqttTokenAndroid) token).notifyComplete();
        } else {
            ((MqttTokenAndroid) token).notifyFailure((Exception) data.getSerializable(MqttServiceConstants.CALLBACK_EXCEPTION));
        }
    }

    private void sendAction(Bundle data) {
        simpleAction(getMqttToken(data), data);
    }

    private void subscribeAction(Bundle data) {
        simpleAction(removeMqttToken(data), data);
    }

    private void unSubscribeAction(Bundle data) {
        simpleAction(removeMqttToken(data), data);
    }

    private void messageDeliveredAction(Bundle data) {
        IMqttToken token = removeMqttToken(data);
        if (token != null && this.callback != null && ((Status) data.getSerializable(MqttServiceConstants.CALLBACK_STATUS)) == Status.OK) {
            this.callback.deliveryComplete((IMqttDeliveryToken) token);
        }
    }

    private void messageArrivedAction(Bundle data) {
        if (this.callback != null) {
            String messageId = data.getString(MqttServiceConstants.CALLBACK_MESSAGE_ID);
            String destinationName = data.getString(MqttServiceConstants.CALLBACK_DESTINATION_NAME);
            ParcelableMqttMessage message = (ParcelableMqttMessage) data.getParcelable(MqttServiceConstants.CALLBACK_MESSAGE_PARCEL);
            try {
                if (this.messageAck == Ack.AUTO_ACK) {
                    this.callback.messageArrived(destinationName, message);
                    this.mqttService.acknowledgeMessageArrival(this.clientHandle, messageId);
                    return;
                }
                message.messageId = messageId;
                this.callback.messageArrived(destinationName, message);
            } catch (Exception e) {
            }
        }
    }

    private void traceAction(Bundle data) {
        if (this.traceCallback != null) {
            String severity = data.getString(MqttServiceConstants.CALLBACK_TRACE_SEVERITY);
            String message = data.getString(MqttServiceConstants.CALLBACK_ERROR_MESSAGE);
            String tag = data.getString(MqttServiceConstants.CALLBACK_TRACE_TAG);
            if (MqttServiceConstants.TRACE_DEBUG.equals(severity)) {
                this.traceCallback.traceDebug(tag, message);
            } else if (MqttServiceConstants.TRACE_ERROR.equals(severity)) {
                this.traceCallback.traceError(tag, message);
            } else {
                this.traceCallback.traceException(tag, message, (Exception) data.getSerializable(MqttServiceConstants.CALLBACK_EXCEPTION));
            }
        }
    }

    private synchronized String storeToken(IMqttToken token) {
        int i;
        this.tokenMap.put(this.tokenNumber, token);
        i = this.tokenNumber;
        this.tokenNumber = i + 1;
        return Integer.toString(i);
    }

    private synchronized IMqttToken removeMqttToken(Bundle data) {
        IMqttToken token;
        String activityToken = data.getString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN);
        if (activityToken != null) {
            int tokenNumber = Integer.parseInt(activityToken);
            token = (IMqttToken) this.tokenMap.get(tokenNumber);
            this.tokenMap.delete(tokenNumber);
        } else {
            token = null;
        }
        return token;
    }

    private synchronized IMqttToken getMqttToken(Bundle data) {
        return (IMqttToken) this.tokenMap.get(Integer.parseInt(data.getString(MqttServiceConstants.CALLBACK_ACTIVITY_TOKEN)));
    }

    public SSLSocketFactory getSSLSocketFactory(InputStream keyStore, String password) throws MqttSecurityException {
        try {
            KeyStore ts = KeyStore.getInstance("BKS");
            ts.load(keyStore, password.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(ts);
            TrustManager[] tm = tmf.getTrustManagers();
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, tm, null);
            return ctx.getSocketFactory();
        } catch (Throwable e) {
            throw new MqttSecurityException(e);
        } catch (Throwable e2) {
            throw new MqttSecurityException(e2);
        } catch (Throwable e22) {
            throw new MqttSecurityException(e22);
        } catch (Throwable e222) {
            throw new MqttSecurityException(e222);
        } catch (Throwable e2222) {
            throw new MqttSecurityException(e2222);
        } catch (Throwable e22222) {
            throw new MqttSecurityException(e22222);
        }
    }

    public void disconnectForcibly() throws MqttException {
        throw new UnsupportedOperationException();
    }

    public void disconnectForcibly(long disconnectTimeout) throws MqttException {
        throw new UnsupportedOperationException();
    }

    public void disconnectForcibly(long quiesceTimeout, long disconnectTimeout) throws MqttException {
        throw new UnsupportedOperationException();
    }

    public void unregisterResources() {
        if (this.myContext != null && this.registerReceiver) {
            synchronized (this) {
                LocalBroadcastManager.getInstance(this.myContext).unregisterReceiver(this);
                this.registerReceiver = false;
            }
            if (this.bindedService) {
                try {
                    this.myContext.unbindService(this.serviceConnection);
                    this.bindedService = false;
                } catch (IllegalArgumentException e) {
                }
            }
        }
    }

    public void registerResources(Context context) {
        if (context != null) {
            this.myContext = context;
            if (!this.registerReceiver) {
                registerReceiver(this);
            }
        }
    }
}
