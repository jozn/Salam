package org.eclipse.paho.client.mqttv3;

import android.support.v4.internal.view.SupportMenu;
import android.support.v7.appcompat.BuildConfig;
import java.util.Hashtable;
import java.util.Properties;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.eclipse.paho.client.mqttv3.internal.ClientComms;
import org.eclipse.paho.client.mqttv3.internal.ConnectActionListener;
import org.eclipse.paho.client.mqttv3.internal.ExceptionHelper;
import org.eclipse.paho.client.mqttv3.internal.LocalNetworkModule;
import org.eclipse.paho.client.mqttv3.internal.NetworkModule;
import org.eclipse.paho.client.mqttv3.internal.SSLNetworkModule;
import org.eclipse.paho.client.mqttv3.internal.TCPNetworkModule;
import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttDisconnect;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttSubscribe;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttUnsubscribe;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.client.mqttv3.util.Debug;

public class MqttAsyncClient implements IMqttAsyncClient {
    private static final String CLASS_NAME;
    private static final String CLIENT_ID_PREFIX = "paho";
    private static final long DISCONNECT_TIMEOUT = 10000;
    private static final char MAX_HIGH_SURROGATE = '\udbff';
    private static final char MIN_HIGH_SURROGATE = '\ud800';
    private static final long QUIESCE_TIMEOUT = 30000;
    static Class class$0;
    private static final Logger log;
    private String clientId;
    protected ClientComms comms;
    private MqttClientPersistence persistence;
    private String serverURI;
    private Hashtable topics;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.MqttAsyncClient");
                class$0 = cls;
            } catch (Throwable e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
        log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    }

    public MqttAsyncClient(String serverURI, String clientId) throws MqttException {
        this(serverURI, clientId, new MqttDefaultFilePersistence());
    }

    public MqttAsyncClient(String serverURI, String clientId, MqttClientPersistence persistence) throws MqttException {
        this(serverURI, clientId, persistence, new TimerPingSender());
    }

    public MqttAsyncClient(String serverURI, String clientId, MqttClientPersistence persistence, MqttPingSender pingSender) throws MqttException {
        log.setResourceName(clientId);
        if (clientId == null) {
            throw new IllegalArgumentException("Null clientId");
        }
        int clientIdLength = 0;
        int i = 0;
        while (i < clientId.length() - 1) {
            if (Character_isHighSurrogate(clientId.charAt(i))) {
                i++;
            }
            clientIdLength++;
            i++;
        }
        if (clientIdLength > SupportMenu.USER_MASK) {
            throw new IllegalArgumentException("ClientId longer than 65535 characters");
        }
        MqttConnectOptions.validateURI(serverURI);
        this.serverURI = serverURI;
        this.clientId = clientId;
        this.persistence = persistence;
        if (this.persistence == null) {
            this.persistence = new MemoryPersistence();
        }
        log.fine(CLASS_NAME, "MqttAsyncClient", "101", new Object[]{clientId, serverURI, persistence});
        this.persistence.open(clientId, serverURI);
        this.comms = new ClientComms(this, this.persistence, pingSender);
        this.persistence.close();
        this.topics = new Hashtable();
    }

    protected static boolean Character_isHighSurrogate(char ch) {
        return ch >= MIN_HIGH_SURROGATE && ch <= MAX_HIGH_SURROGATE;
    }

    protected NetworkModule[] createNetworkModules(String address, MqttConnectOptions options) throws MqttException, MqttSecurityException {
        log.fine(CLASS_NAME, "createNetworkModules", "116", new Object[]{address});
        String[] serverURIs = options.getServerURIs();
        String[] array = serverURIs == null ? new String[]{address} : serverURIs.length == 0 ? new String[]{address} : serverURIs;
        NetworkModule[] networkModules = new NetworkModule[array.length];
        for (int i = 0; i < array.length; i++) {
            networkModules[i] = createNetworkModule(array[i], options);
        }
        log.fine(CLASS_NAME, "createNetworkModules", "108");
        return networkModules;
    }

    private NetworkModule createNetworkModule(String address, MqttConnectOptions options) throws MqttException, MqttSecurityException {
        log.fine(CLASS_NAME, "createNetworkModule", "115", new Object[]{address});
        SocketFactory factory = options.getSocketFactory();
        String shortAddress;
        String host;
        int port;
        NetworkModule netModule;
        switch (MqttConnectOptions.validateURI(address)) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                shortAddress = address.substring(6);
                host = getHostName(shortAddress);
                port = getPort(shortAddress, 1883);
                if (factory == null) {
                    factory = SocketFactory.getDefault();
                } else if (factory instanceof SSLSocketFactory) {
                    throw ExceptionHelper.createMqttException(32105);
                }
                netModule = new TCPNetworkModule(factory, host, port, this.clientId);
                ((TCPNetworkModule) netModule).setConnectTimeout(options.getConnectionTimeout());
                return netModule;
            case Logger.SEVERE /*1*/:
                shortAddress = address.substring(6);
                host = getHostName(shortAddress);
                port = getPort(shortAddress, 8883);
                SSLSocketFactoryFactory factoryFactory = null;
                if (factory == null) {
                    factoryFactory = new SSLSocketFactoryFactory();
                    Properties sslClientProps = options.getSSLProperties();
                    if (sslClientProps != null) {
                        factoryFactory.initialize(sslClientProps, null);
                    }
                    factory = factoryFactory.createSocketFactory(null);
                } else if (!(factory instanceof SSLSocketFactory)) {
                    throw ExceptionHelper.createMqttException(32105);
                }
                netModule = new SSLNetworkModule((SSLSocketFactory) factory, host, port, this.clientId);
                ((SSLNetworkModule) netModule).setSSLhandshakeTimeout(options.getConnectionTimeout());
                if (factoryFactory == null) {
                    return netModule;
                }
                String[] enabledCiphers = factoryFactory.getEnabledCipherSuites(null);
                if (enabledCiphers == null) {
                    return netModule;
                }
                ((SSLNetworkModule) netModule).setEnabledCiphers(enabledCiphers);
                return netModule;
            case Logger.WARNING /*2*/:
                return new LocalNetworkModule(address.substring(8));
            default:
                return null;
        }
    }

    private int getPort(String uri, int defaultPort) {
        int portIndex = uri.lastIndexOf(58);
        if (portIndex == -1) {
            return defaultPort;
        }
        return Integer.parseInt(uri.substring(portIndex + 1));
    }

    private String getHostName(String uri) {
        int schemeIndex = uri.lastIndexOf(47);
        int portIndex = uri.lastIndexOf(58);
        if (portIndex == -1) {
            portIndex = uri.length();
        }
        return uri.substring(schemeIndex + 1, portIndex);
    }

    public IMqttToken connect(Object userContext, IMqttActionListener callback) throws MqttException, MqttSecurityException {
        return connect(new MqttConnectOptions(), userContext, callback);
    }

    public IMqttToken connect() throws MqttException, MqttSecurityException {
        return connect(null, null);
    }

    public IMqttToken connect(MqttConnectOptions options) throws MqttException, MqttSecurityException {
        return connect(options, null, null);
    }

    public IMqttToken connect(MqttConnectOptions options, Object userContext, IMqttActionListener callback) throws MqttException, MqttSecurityException {
        if (this.comms.isConnected()) {
            throw ExceptionHelper.createMqttException(32100);
        } else if (this.comms.isConnecting()) {
            throw new MqttException(32110);
        } else if (this.comms.isDisconnecting()) {
            throw new MqttException(32102);
        } else if (this.comms.isClosed()) {
            throw new MqttException(32111);
        } else {
            Logger logger = log;
            String str = CLASS_NAME;
            String str2 = MqttServiceConstants.CONNECT_ACTION;
            String str3 = "103";
            Object[] objArr = new Object[8];
            objArr[0] = Boolean.valueOf(options.isCleanSession());
            objArr[1] = new Integer(options.getConnectionTimeout());
            objArr[2] = new Integer(options.getKeepAliveInterval());
            objArr[3] = options.getUserName();
            objArr[4] = options.getPassword() == null ? "[null]" : "[notnull]";
            objArr[5] = options.getWillMessage() == null ? "[null]" : "[notnull]";
            objArr[6] = userContext;
            objArr[7] = callback;
            logger.fine(str, str2, str3, objArr);
            this.comms.setNetworkModules(createNetworkModules(this.serverURI, options));
            MqttToken userToken = new MqttToken(getClientId());
            ConnectActionListener connectActionListener = new ConnectActionListener(this, this.persistence, this.comms, options, userToken, userContext, callback);
            userToken.setActionCallback(connectActionListener);
            userToken.setUserContext(this);
            this.comms.setNetworkModuleIndex(0);
            connectActionListener.connect();
            return userToken;
        }
    }

    public IMqttToken disconnect(Object userContext, IMqttActionListener callback) throws MqttException {
        return disconnect(QUIESCE_TIMEOUT, userContext, callback);
    }

    public IMqttToken disconnect() throws MqttException {
        return disconnect(null, null);
    }

    public IMqttToken disconnect(long quiesceTimeout) throws MqttException {
        return disconnect(quiesceTimeout, null, null);
    }

    public IMqttToken disconnect(long quiesceTimeout, Object userContext, IMqttActionListener callback) throws MqttException {
        log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "104", new Object[]{new Long(quiesceTimeout), userContext, callback});
        MqttToken token = new MqttToken(getClientId());
        token.setActionCallback(callback);
        token.setUserContext(userContext);
        try {
            this.comms.disconnect(new MqttDisconnect(), quiesceTimeout, token);
            log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "108");
            return token;
        } catch (MqttException ex) {
            log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "105", null, ex);
            throw ex;
        }
    }

    public void disconnectForcibly() throws MqttException {
        disconnectForcibly(QUIESCE_TIMEOUT, DISCONNECT_TIMEOUT);
    }

    public void disconnectForcibly(long disconnectTimeout) throws MqttException {
        disconnectForcibly(QUIESCE_TIMEOUT, disconnectTimeout);
    }

    public void disconnectForcibly(long quiesceTimeout, long disconnectTimeout) throws MqttException {
        this.comms.disconnectForcibly(quiesceTimeout, disconnectTimeout);
    }

    public boolean isConnected() {
        return this.comms.isConnected();
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getServerURI() {
        return this.serverURI;
    }

    protected MqttTopic getTopic(String topic) {
        MqttTopic.validate(topic, false);
        MqttTopic result = (MqttTopic) this.topics.get(topic);
        if (result != null) {
            return result;
        }
        result = new MqttTopic(topic, this.comms);
        this.topics.put(topic, result);
        return result;
    }

    public IMqttToken checkPing(Object userContext, IMqttActionListener callback) throws MqttException {
        log.fine(CLASS_NAME, "ping", "117");
        MqttToken token = this.comms.checkForActivity();
        log.fine(CLASS_NAME, "ping", "118");
        return token;
    }

    public IMqttToken subscribe(String topicFilter, int qos, Object userContext, IMqttActionListener callback) throws MqttException {
        return subscribe(new String[]{topicFilter}, new int[]{qos}, userContext, callback);
    }

    public IMqttToken subscribe(String topicFilter, int qos) throws MqttException {
        return subscribe(new String[]{topicFilter}, new int[]{qos}, null, null);
    }

    public IMqttToken subscribe(String[] topicFilters, int[] qos) throws MqttException {
        return subscribe(topicFilters, qos, null, null);
    }

    public IMqttToken subscribe(String[] topicFilters, int[] qos, Object userContext, IMqttActionListener callback) throws MqttException {
        if (topicFilters.length != qos.length) {
            throw new IllegalArgumentException();
        }
        String subs = BuildConfig.VERSION_NAME;
        for (int i = 0; i < topicFilters.length; i++) {
            if (i > 0) {
                subs = new StringBuffer(String.valueOf(subs)).append(", ").toString();
            }
            subs = new StringBuffer(String.valueOf(subs)).append("topic=").append(topicFilters[i]).append(" qos=").append(qos[i]).toString();
            MqttTopic.validate(topicFilters[i], true);
        }
        log.fine(CLASS_NAME, MqttServiceConstants.SUBSCRIBE_ACTION, "106", new Object[]{subs, userContext, callback});
        MqttToken token = new MqttToken(getClientId());
        token.setActionCallback(callback);
        token.setUserContext(userContext);
        token.internalTok.setTopics(topicFilters);
        this.comms.sendNoWait(new MqttSubscribe(topicFilters, qos), token);
        log.fine(CLASS_NAME, MqttServiceConstants.SUBSCRIBE_ACTION, "109");
        return token;
    }

    public IMqttToken unsubscribe(String topicFilter, Object userContext, IMqttActionListener callback) throws MqttException {
        return unsubscribe(new String[]{topicFilter}, userContext, callback);
    }

    public IMqttToken unsubscribe(String topicFilter) throws MqttException {
        return unsubscribe(new String[]{topicFilter}, null, null);
    }

    public IMqttToken unsubscribe(String[] topicFilters) throws MqttException {
        return unsubscribe(topicFilters, null, null);
    }

    public IMqttToken unsubscribe(String[] topicFilters, Object userContext, IMqttActionListener callback) throws MqttException {
        String subs = BuildConfig.VERSION_NAME;
        for (int i = 0; i < topicFilters.length; i++) {
            if (i > 0) {
                subs = new StringBuffer(String.valueOf(subs)).append(", ").toString();
            }
            subs = new StringBuffer(String.valueOf(subs)).append(topicFilters[i]).toString();
            MqttTopic.validate(topicFilters[i], true);
        }
        log.fine(CLASS_NAME, MqttServiceConstants.UNSUBSCRIBE_ACTION, "107", new Object[]{subs, userContext, callback});
        MqttToken token = new MqttToken(getClientId());
        token.setActionCallback(callback);
        token.setUserContext(userContext);
        token.internalTok.setTopics(topicFilters);
        this.comms.sendNoWait(new MqttUnsubscribe(topicFilters), token);
        log.fine(CLASS_NAME, MqttServiceConstants.UNSUBSCRIBE_ACTION, "110");
        return token;
    }

    public void setCallback(MqttCallback callback) {
        this.comms.setCallback(callback);
    }

    public static String generateClientId() {
        return new StringBuffer(CLIENT_ID_PREFIX).append(System.nanoTime()).toString();
    }

    public IMqttDeliveryToken[] getPendingDeliveryTokens() {
        return this.comms.getPendingDeliveryTokens();
    }

    public IMqttDeliveryToken publish(String topic, byte[] payload, int qos, boolean retained, Object userContext, IMqttActionListener callback) throws MqttException, MqttPersistenceException {
        MqttMessage message = new MqttMessage(payload);
        message.setQos(qos);
        message.setRetained(retained);
        return publish(topic, message, userContext, callback);
    }

    public IMqttDeliveryToken publish(String topic, byte[] payload, int qos, boolean retained) throws MqttException, MqttPersistenceException {
        return publish(topic, payload, qos, retained, null, null);
    }

    public IMqttDeliveryToken publish(String topic, MqttMessage message) throws MqttException, MqttPersistenceException {
        return publish(topic, message, null, null);
    }

    public IMqttDeliveryToken publish(String topic, MqttMessage message, Object userContext, IMqttActionListener callback) throws MqttException, MqttPersistenceException {
        log.fine(CLASS_NAME, "publish", "111", new Object[]{topic, userContext, callback});
        MqttTopic.validate(topic, false);
        MqttDeliveryToken token = new MqttDeliveryToken(getClientId());
        token.setActionCallback(callback);
        token.setUserContext(userContext);
        token.setMessage(message);
        token.internalTok.setTopics(new String[]{topic});
        this.comms.sendNoWait(new MqttPublish(topic, message), token);
        log.fine(CLASS_NAME, "publish", "112");
        return token;
    }

    public void close() throws MqttException {
        log.fine(CLASS_NAME, "close", "113");
        this.comms.close();
        log.fine(CLASS_NAME, "close", "114");
    }

    public Debug getDebug() {
        return new Debug(this.clientId, this.comms);
    }
}
