package org.eclipse.paho.client.mqttv3;

import android.support.v7.appcompat.BuildConfig;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.net.SocketFactory;
import org.eclipse.paho.client.mqttv3.util.Debug;

public class MqttConnectOptions {
    public static final boolean CLEAN_SESSION_DEFAULT = true;
    public static final int CONNECTION_TIMEOUT_DEFAULT = 30;
    public static final int KEEP_ALIVE_INTERVAL_DEFAULT = 60;
    public static final int MQTT_VERSION_3_1 = 3;
    public static final int MQTT_VERSION_3_1_1 = 4;
    public static final int MQTT_VERSION_DEFAULT = 0;
    protected static final int URI_TYPE_LOCAL = 2;
    protected static final int URI_TYPE_SSL = 1;
    protected static final int URI_TYPE_TCP = 0;
    private int MqttVersion;
    private boolean cleanSession;
    private int connectionTimeout;
    private int keepAliveInterval;
    private char[] password;
    private String[] serverURIs;
    private SocketFactory socketFactory;
    private Properties sslClientProps;
    private String userName;
    private String willDestination;
    private MqttMessage willMessage;

    public MqttConnectOptions() {
        this.keepAliveInterval = KEEP_ALIVE_INTERVAL_DEFAULT;
        this.willDestination = null;
        this.willMessage = null;
        this.sslClientProps = null;
        this.cleanSession = CLEAN_SESSION_DEFAULT;
        this.connectionTimeout = CONNECTION_TIMEOUT_DEFAULT;
        this.serverURIs = null;
        this.MqttVersion = MQTT_VERSION_DEFAULT;
    }

    public char[] getPassword() {
        return this.password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        if (userName == null || !userName.trim().equals(BuildConfig.VERSION_NAME)) {
            this.userName = userName;
            return;
        }
        throw new IllegalArgumentException();
    }

    public void setWill(MqttTopic topic, byte[] payload, int qos, boolean retained) {
        String topicS = topic.getName();
        validateWill(topicS, payload);
        setWill(topicS, new MqttMessage(payload), qos, retained);
    }

    public void setWill(String topic, byte[] payload, int qos, boolean retained) {
        validateWill(topic, payload);
        setWill(topic, new MqttMessage(payload), qos, retained);
    }

    private void validateWill(String dest, Object payload) {
        if (dest == null || payload == null) {
            throw new IllegalArgumentException();
        }
        MqttTopic.validate(dest, false);
    }

    protected void setWill(String topic, MqttMessage msg, int qos, boolean retained) {
        this.willDestination = topic;
        this.willMessage = msg;
        this.willMessage.setQos(qos);
        this.willMessage.setRetained(retained);
        this.willMessage.setMutable(false);
    }

    public int getKeepAliveInterval() {
        return this.keepAliveInterval;
    }

    public int getMqttVersion() {
        return this.MqttVersion;
    }

    public void setKeepAliveInterval(int keepAliveInterval) throws IllegalArgumentException {
        if (keepAliveInterval < 0) {
            throw new IllegalArgumentException();
        }
        this.keepAliveInterval = keepAliveInterval;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        if (connectionTimeout < 0) {
            throw new IllegalArgumentException();
        }
        this.connectionTimeout = connectionTimeout;
    }

    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public String getWillDestination() {
        return this.willDestination;
    }

    public MqttMessage getWillMessage() {
        return this.willMessage;
    }

    public Properties getSSLProperties() {
        return this.sslClientProps;
    }

    public void setSSLProperties(Properties props) {
        this.sslClientProps = props;
    }

    public boolean isCleanSession() {
        return this.cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public String[] getServerURIs() {
        return this.serverURIs;
    }

    public void setServerURIs(String[] array) {
        for (int i = MQTT_VERSION_DEFAULT; i < array.length; i += URI_TYPE_SSL) {
            validateURI(array[i]);
        }
        this.serverURIs = array;
    }

    protected static int validateURI(String srvURI) {
        try {
            URI vURI = new URI(srvURI);
            if (!vURI.getPath().equals(BuildConfig.VERSION_NAME)) {
                throw new IllegalArgumentException(srvURI);
            } else if (vURI.getScheme().equals("tcp")) {
                return MQTT_VERSION_DEFAULT;
            } else {
                if (vURI.getScheme().equals("ssl")) {
                    return URI_TYPE_SSL;
                }
                if (vURI.getScheme().equals("local")) {
                    return URI_TYPE_LOCAL;
                }
                throw new IllegalArgumentException(srvURI);
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(srvURI);
        }
    }

    public void setMqttVersion(int MqttVersion) throws IllegalArgumentException {
        if (MqttVersion == 0 || MqttVersion == MQTT_VERSION_3_1 || MqttVersion == MQTT_VERSION_3_1_1) {
            this.MqttVersion = MqttVersion;
            return;
        }
        throw new IllegalArgumentException();
    }

    public Properties getDebug() {
        Properties p = new Properties();
        p.put("MqttVersion", new Integer(getMqttVersion()));
        p.put("CleanSession", Boolean.valueOf(isCleanSession()));
        p.put("ConTimeout", new Integer(getConnectionTimeout()));
        p.put("KeepAliveInterval", new Integer(getKeepAliveInterval()));
        p.put("UserName", getUserName() == null ? "null" : getUserName());
        p.put("WillDestination", getWillDestination() == null ? "null" : getWillDestination());
        if (getSocketFactory() == null) {
            p.put("SocketFactory", "null");
        } else {
            p.put("SocketFactory", getSocketFactory());
        }
        if (getSSLProperties() == null) {
            p.put("SSLProperties", "null");
        } else {
            p.put("SSLProperties", getSSLProperties());
        }
        return p;
    }

    public String toString() {
        return Debug.dumpProperties(getDebug(), "Connection options");
    }
}
