package org.eclipse.paho.client.mqttv3.internal;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttToken;

public class ConnectActionListener implements IMqttActionListener {
    private MqttAsyncClient client;
    private ClientComms comms;
    private MqttConnectOptions options;
    private int originalMqttVersion;
    private MqttClientPersistence persistence;
    private IMqttActionListener userCallback;
    private Object userContext;
    private MqttToken userToken;

    public ConnectActionListener(MqttAsyncClient client, MqttClientPersistence persistence, ClientComms comms, MqttConnectOptions options, MqttToken userToken, Object userContext, IMqttActionListener userCallback) {
        this.persistence = persistence;
        this.client = client;
        this.comms = comms;
        this.options = options;
        this.userToken = userToken;
        this.userContext = userContext;
        this.userCallback = userCallback;
        this.originalMqttVersion = options.getMqttVersion();
    }

    public void onSuccess(IMqttToken token) {
        if (this.originalMqttVersion == 0) {
            this.options.setMqttVersion(0);
        }
        this.userToken.internalTok.markComplete(token.getResponse(), null);
        this.userToken.internalTok.notifyComplete();
        if (this.userCallback != null) {
            this.userToken.setUserContext(this.userContext);
            this.userCallback.onSuccess(this.userToken);
        }
    }

    public void onFailure(IMqttToken token, Throwable exception) {
        int numberOfURIs = this.comms.getNetworkModules().length;
        int index = this.comms.getNetworkModuleIndex();
        if (index + 1 < numberOfURIs || (this.originalMqttVersion == 0 && this.options.getMqttVersion() == 4)) {
            if (this.originalMqttVersion != 0) {
                this.comms.setNetworkModuleIndex(index + 1);
            } else if (this.options.getMqttVersion() == 4) {
                this.options.setMqttVersion(3);
            } else {
                this.options.setMqttVersion(4);
                this.comms.setNetworkModuleIndex(index + 1);
            }
            try {
                connect();
                return;
            } catch (MqttPersistenceException e) {
                onFailure(token, e);
                return;
            }
        }
        MqttException ex;
        if (this.originalMqttVersion == 0) {
            this.options.setMqttVersion(0);
        }
        if (exception instanceof MqttException) {
            ex = (MqttException) exception;
        } else {
            ex = new MqttException(exception);
        }
        this.userToken.internalTok.markComplete(null, ex);
        this.userToken.internalTok.notifyComplete();
        if (this.userCallback != null) {
            this.userToken.setUserContext(this.userContext);
            this.userCallback.onFailure(this.userToken, exception);
        }
    }

    public void connect() throws MqttPersistenceException {
        MqttToken token = new MqttToken(this.client.getClientId());
        token.setActionCallback(this);
        token.setUserContext(this);
        this.persistence.open(this.client.getClientId(), this.client.getServerURI());
        if (this.options.isCleanSession()) {
            this.persistence.clear();
        }
        if (this.options.getMqttVersion() == 0) {
            this.options.setMqttVersion(4);
        }
        try {
            this.comms.connect(this.options, token);
        } catch (MqttException e) {
            onFailure(token, e);
        }
    }
}
