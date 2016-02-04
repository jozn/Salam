package org.eclipse.paho.client.mqttv3;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.client.mqttv3.util.Debug;

public class MqttClient implements IMqttClient {
    protected MqttAsyncClient aClient;
    protected long timeToWait;

    public MqttClient(String serverURI, String clientId) throws MqttException {
        this(serverURI, clientId, new MqttDefaultFilePersistence());
    }

    public MqttClient(String serverURI, String clientId, MqttClientPersistence persistence) throws MqttException {
        this.aClient = null;
        this.timeToWait = -1;
        this.aClient = new MqttAsyncClient(serverURI, clientId, persistence);
    }

    public void connect() throws MqttSecurityException, MqttException {
        connect(new MqttConnectOptions());
    }

    public void connect(MqttConnectOptions options) throws MqttSecurityException, MqttException {
        this.aClient.connect(options, null, null).waitForCompletion(getTimeToWait());
    }

    public IMqttToken connectWithResult(MqttConnectOptions options) throws MqttSecurityException, MqttException {
        IMqttToken tok = this.aClient.connect(options, null, null);
        tok.waitForCompletion(getTimeToWait());
        return tok;
    }

    public void disconnect() throws MqttException {
        this.aClient.disconnect().waitForCompletion();
    }

    public void disconnect(long quiesceTimeout) throws MqttException {
        this.aClient.disconnect(quiesceTimeout, null, null).waitForCompletion();
    }

    public void disconnectForcibly() throws MqttException {
        this.aClient.disconnectForcibly();
    }

    public void disconnectForcibly(long disconnectTimeout) throws MqttException {
        this.aClient.disconnectForcibly(disconnectTimeout);
    }

    public void disconnectForcibly(long quiesceTimeout, long disconnectTimeout) throws MqttException {
        this.aClient.disconnectForcibly(quiesceTimeout, disconnectTimeout);
    }

    public void subscribe(String topicFilter) throws MqttException {
        subscribe(new String[]{topicFilter}, new int[]{1});
    }

    public void subscribe(String[] topicFilters) throws MqttException {
        int[] qos = new int[topicFilters.length];
        for (int i = 0; i < qos.length; i++) {
            qos[i] = 1;
        }
        subscribe(topicFilters, qos);
    }

    public void subscribe(String topicFilter, int qos) throws MqttException {
        subscribe(new String[]{topicFilter}, new int[]{qos});
    }

    public void subscribe(String[] topicFilters, int[] qos) throws MqttException {
        IMqttToken tok = this.aClient.subscribe(topicFilters, qos, null, null);
        tok.waitForCompletion(getTimeToWait());
        int[] grantedQos = tok.getGrantedQos();
        for (int i = 0; i < grantedQos.length; i++) {
            qos[i] = grantedQos[i];
        }
        if (grantedQos.length == 1 && qos[0] == 128) {
            throw new MqttException((int) AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
        }
    }

    public void unsubscribe(String topicFilter) throws MqttException {
        unsubscribe(new String[]{topicFilter});
    }

    public void unsubscribe(String[] topicFilters) throws MqttException {
        this.aClient.unsubscribe(topicFilters, null, null).waitForCompletion(getTimeToWait());
    }

    public void publish(String topic, byte[] payload, int qos, boolean retained) throws MqttException, MqttPersistenceException {
        MqttMessage message = new MqttMessage(payload);
        message.setQos(qos);
        message.setRetained(retained);
        publish(topic, message);
    }

    public void publish(String topic, MqttMessage message) throws MqttException, MqttPersistenceException {
        this.aClient.publish(topic, message, null, null).waitForCompletion(getTimeToWait());
    }

    public void setTimeToWait(long timeToWaitInMillis) throws IllegalArgumentException {
        if (timeToWaitInMillis < -1) {
            throw new IllegalArgumentException();
        }
        this.timeToWait = timeToWaitInMillis;
    }

    public long getTimeToWait() {
        return this.timeToWait;
    }

    public void close() throws MqttException {
        this.aClient.close();
    }

    public String getClientId() {
        return this.aClient.getClientId();
    }

    public IMqttDeliveryToken[] getPendingDeliveryTokens() {
        return this.aClient.getPendingDeliveryTokens();
    }

    public String getServerURI() {
        return this.aClient.getServerURI();
    }

    public MqttTopic getTopic(String topic) {
        return this.aClient.getTopic(topic);
    }

    public boolean isConnected() {
        return this.aClient.isConnected();
    }

    public void setCallback(MqttCallback callback) {
        this.aClient.setCallback(callback);
    }

    public static String generateClientId() {
        return MqttAsyncClient.generateClientId();
    }

    public Debug getDebug() {
        return this.aClient.getDebug();
    }
}
