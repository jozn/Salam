package org.eclipse.paho.client.mqttv3.internal;

import java.util.Enumeration;
import java.util.Properties;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnack;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttDisconnect;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class ClientComms {
    public static String BUILD_LEVEL = null;
    private static final String CLASS_NAME;
    private static final byte CLOSED = (byte) 4;
    private static final byte CONNECTED = (byte) 0;
    private static final byte CONNECTING = (byte) 1;
    private static final byte DISCONNECTED = (byte) 3;
    private static final byte DISCONNECTING = (byte) 2;
    public static String VERSION;
    static Class class$0;
    private static final Logger log;
    private CommsCallback callback;
    private IMqttAsyncClient client;
    private ClientState clientState;
    private boolean closePending;
    private Object conLock;
    private MqttConnectOptions conOptions;
    private byte conState;
    private int networkModuleIndex;
    private NetworkModule[] networkModules;
    private MqttClientPersistence persistence;
    private MqttPingSender pingSender;
    private CommsReceiver receiver;
    private CommsSender sender;
    private boolean stoppingComms;
    private CommsTokenStore tokenStore;

    private class ConnectBG implements Runnable {
        Thread cBg;
        ClientComms clientComms;
        MqttConnect conPacket;
        MqttToken conToken;
        final ClientComms this$0;

        ConnectBG(ClientComms clientComms, ClientComms cc, MqttToken cToken, MqttConnect cPacket) {
            this.this$0 = clientComms;
            this.clientComms = null;
            this.cBg = null;
            this.clientComms = cc;
            this.conToken = cToken;
            this.conPacket = cPacket;
            this.cBg = new Thread(this, new StringBuffer("MQTT Con: ").append(clientComms.getClient().getClientId()).toString());
        }

        void start() {
            this.cBg.start();
        }

        public void run() {
            MqttException mqttEx = null;
            ClientComms.access$0().fine(ClientComms.access$1(), "connectBG:run", "220");
            try {
                MqttDeliveryToken[] toks = ClientComms.access$2(this.this$0).getOutstandingDelTokens();
                for (MqttDeliveryToken mqttDeliveryToken : toks) {
                    mqttDeliveryToken.internalTok.setException(null);
                }
                ClientComms.access$2(this.this$0).saveToken(this.conToken, this.conPacket);
                NetworkModule networkModule = ClientComms.access$3(this.this$0)[ClientComms.access$4(this.this$0)];
                networkModule.start();
                ClientComms.access$6(this.this$0, new CommsReceiver(this.clientComms, ClientComms.access$5(this.this$0), ClientComms.access$2(this.this$0), networkModule.getInputStream()));
                ClientComms.access$7(this.this$0).start(new StringBuffer("MQTT Rec: ").append(this.this$0.getClient().getClientId()).toString());
                ClientComms.access$8(this.this$0, new CommsSender(this.clientComms, ClientComms.access$5(this.this$0), ClientComms.access$2(this.this$0), networkModule.getOutputStream()));
                ClientComms.access$9(this.this$0).start(new StringBuffer("MQTT Snd: ").append(this.this$0.getClient().getClientId()).toString());
                ClientComms.access$10(this.this$0).start(new StringBuffer("MQTT Call: ").append(this.this$0.getClient().getClientId()).toString());
                this.this$0.internalSend(this.conPacket, this.conToken);
            } catch (MqttException ex) {
                ClientComms.access$0().fine(ClientComms.access$1(), "connectBG:run", "212", null, ex);
                mqttEx = ex;
            } catch (Throwable ex2) {
                ClientComms.access$0().fine(ClientComms.access$1(), "connectBG:run", "209", null, ex2);
                mqttEx = ExceptionHelper.createMqttException(ex2);
            }
            if (mqttEx != null) {
                this.this$0.shutdownConnection(this.conToken, mqttEx);
            }
        }
    }

    private class DisconnectBG implements Runnable {
        Thread dBg;
        MqttDisconnect disconnect;
        long quiesceTimeout;
        final ClientComms this$0;
        MqttToken token;

        DisconnectBG(ClientComms clientComms, MqttDisconnect disconnect, long quiesceTimeout, MqttToken token) {
            this.this$0 = clientComms;
            this.dBg = null;
            this.disconnect = disconnect;
            this.quiesceTimeout = quiesceTimeout;
            this.token = token;
        }

        void start() {
            this.dBg = new Thread(this, new StringBuffer("MQTT Disc: ").append(this.this$0.getClient().getClientId()).toString());
            this.dBg.start();
        }

        public void run() {
            ClientComms.access$0().fine(ClientComms.access$1(), "disconnectBG:run", "221");
            ClientComms.access$5(this.this$0).quiesce(this.quiesceTimeout);
            try {
                this.this$0.internalSend(this.disconnect, this.token);
                this.token.internalTok.waitUntilSent();
            } catch (MqttException e) {
            } catch (Throwable th) {
                this.token.internalTok.markComplete(null, null);
                this.this$0.shutdownConnection(this.token, null);
            }
            this.token.internalTok.markComplete(null, null);
            this.this$0.shutdownConnection(this.token, null);
        }
    }

    static {
        VERSION = "${project.version}";
        BUILD_LEVEL = "L${build.level}";
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.ClientComms");
                class$0 = cls;
            } catch (Throwable e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
        log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    }

    static String access$1() {
        return CLASS_NAME;
    }

    static Logger access$0() {
        return log;
    }

    static int access$4(ClientComms clientComms) {
        return clientComms.networkModuleIndex;
    }

    static NetworkModule[] access$3(ClientComms clientComms) {
        return clientComms.networkModules;
    }

    static void access$6(ClientComms clientComms, CommsReceiver commsReceiver) {
        clientComms.receiver = commsReceiver;
    }

    static CommsReceiver access$7(ClientComms clientComms) {
        return clientComms.receiver;
    }

    static void access$8(ClientComms clientComms, CommsSender commsSender) {
        clientComms.sender = commsSender;
    }

    static CommsSender access$9(ClientComms clientComms) {
        return clientComms.sender;
    }

    static CommsCallback access$10(ClientComms clientComms) {
        return clientComms.callback;
    }

    static ClientState access$5(ClientComms clientComms) {
        return clientComms.clientState;
    }

    static CommsTokenStore access$2(ClientComms clientComms) {
        return clientComms.tokenStore;
    }

    public ClientComms(IMqttAsyncClient client, MqttClientPersistence persistence, MqttPingSender pingSender) throws MqttException {
        this.stoppingComms = false;
        this.conState = DISCONNECTED;
        this.conLock = new Object();
        this.closePending = false;
        this.conState = DISCONNECTED;
        this.client = client;
        this.persistence = persistence;
        this.pingSender = pingSender;
        this.pingSender.init(this);
        this.tokenStore = new CommsTokenStore(getClient().getClientId());
        this.callback = new CommsCallback(this);
        this.clientState = new ClientState(persistence, this.tokenStore, this.callback, this, pingSender);
        this.callback.setClientState(this.clientState);
        log.setResourceName(getClient().getClientId());
    }

    CommsReceiver getReceiver() {
        return this.receiver;
    }

    void internalSend(MqttWireMessage message, MqttToken token) throws MqttException {
        log.fine(CLASS_NAME, "internalSend", "200", new Object[]{message.getKey(), message, token});
        if (token.getClient() == null) {
            token.internalTok.setClient(getClient());
            try {
                this.clientState.send(message, token);
                return;
            } catch (MqttException e) {
                if (message instanceof MqttPublish) {
                    this.clientState.undo((MqttPublish) message);
                }
                throw e;
            }
        }
        log.fine(CLASS_NAME, "internalSend", "213", new Object[]{message.getKey(), message, token});
        throw new MqttException(32201);
    }

    public void sendNoWait(MqttWireMessage message, MqttToken token) throws MqttException {
        if (isConnected() || ((!isConnected() && (message instanceof MqttConnect)) || (isDisconnecting() && (message instanceof MqttDisconnect)))) {
            internalSend(message, token);
        } else {
            log.fine(CLASS_NAME, "sendNoWait", "208");
            throw ExceptionHelper.createMqttException(32104);
        }
    }

    public void close() throws MqttException {
        synchronized (this.conLock) {
            if (!isClosed()) {
                if (!isDisconnected()) {
                    log.fine(CLASS_NAME, "close", "224");
                    if (isConnecting()) {
                        throw new MqttException(32110);
                    } else if (isConnected()) {
                        throw ExceptionHelper.createMqttException(32100);
                    } else if (isDisconnecting()) {
                        this.closePending = true;
                        return;
                    }
                }
                this.conState = CLOSED;
                this.clientState.close();
                this.clientState = null;
                this.callback = null;
                this.persistence = null;
                this.sender = null;
                this.pingSender = null;
                this.receiver = null;
                this.networkModules = null;
                this.conOptions = null;
                this.tokenStore = null;
            }
        }
    }

    public void connect(MqttConnectOptions options, MqttToken token) throws MqttException {
        synchronized (this.conLock) {
            if (!isDisconnected() || this.closePending) {
                log.fine(CLASS_NAME, MqttServiceConstants.CONNECT_ACTION, "207", new Object[]{new Byte(this.conState)});
                if (isClosed() || this.closePending) {
                    throw new MqttException(32111);
                } else if (isConnecting()) {
                    throw new MqttException(32110);
                } else if (isDisconnecting()) {
                    throw new MqttException(32102);
                } else {
                    throw ExceptionHelper.createMqttException(32100);
                }
            }
            log.fine(CLASS_NAME, MqttServiceConstants.CONNECT_ACTION, "214");
            this.conState = CONNECTING;
            this.conOptions = options;
            MqttConnect connect = new MqttConnect(this.client.getClientId(), options.getMqttVersion(), options.isCleanSession(), options.getKeepAliveInterval(), options.getUserName(), options.getPassword(), options.getWillMessage(), options.getWillDestination());
            this.clientState.setKeepAliveSecs((long) options.getKeepAliveInterval());
            this.clientState.setCleanSession(options.isCleanSession());
            this.tokenStore.open();
            new ConnectBG(this, this, token, connect).start();
        }
    }

    public void connectComplete(MqttConnack cack, MqttException mex) throws MqttException {
        int rc = cack.getReturnCode();
        synchronized (this.conLock) {
            if (rc == 0) {
                log.fine(CLASS_NAME, "connectComplete", "215");
                this.conState = CONNECTED;
                return;
            }
            log.fine(CLASS_NAME, "connectComplete", "204", new Object[]{new Integer(rc)});
            throw mex;
        }
    }

    public void shutdownConnection(MqttToken token, MqttException reason) {
        int i = 1;
        synchronized (this.conLock) {
            if (this.stoppingComms || this.closePending) {
                return;
            }
            boolean wasConnected;
            this.stoppingComms = true;
            log.fine(CLASS_NAME, "shutdownConnection", "216");
            if (isConnected() || isDisconnecting()) {
                wasConnected = true;
            } else {
                wasConnected = false;
            }
            this.conState = DISCONNECTING;
            if (!(token == null || token.isComplete())) {
                token.internalTok.setException(reason);
            }
            if (this.callback != null) {
                this.callback.stop();
            }
            try {
                if (this.networkModules != null) {
                    NetworkModule networkModule = this.networkModules[this.networkModuleIndex];
                    if (networkModule != null) {
                        networkModule.stop();
                    }
                }
            } catch (Exception e) {
            }
            if (this.receiver != null) {
                this.receiver.stop();
            }
            this.tokenStore.quiesce(new MqttException(32102));
            MqttToken endToken = handleOldTokens(token, reason);
            try {
                this.clientState.disconnected(reason);
            } catch (Exception e2) {
            }
            if (this.sender != null) {
                this.sender.stop();
            }
            if (this.pingSender != null) {
                this.pingSender.stop();
            }
            try {
                if (this.persistence != null) {
                    this.persistence.close();
                }
            } catch (Exception e3) {
            }
            synchronized (this.conLock) {
                log.fine(CLASS_NAME, "shutdownConnection", "217");
                this.conState = DISCONNECTED;
                this.stoppingComms = false;
            }
            int i2 = endToken != null ? 1 : 0;
            if (this.callback == null) {
                i = 0;
            }
            if ((i2 & i) != 0) {
                this.callback.asyncOperationComplete(endToken);
            }
            if (wasConnected && this.callback != null) {
                this.callback.connectionLost(reason);
            }
            synchronized (this.conLock) {
                if (this.closePending) {
                    try {
                        close();
                    } catch (Exception e4) {
                    }
                }
            }
        }
    }

    private MqttToken handleOldTokens(MqttToken token, MqttException reason) {
        log.fine(CLASS_NAME, "handleOldTokens", "222");
        MqttToken tokToNotifyLater = null;
        if (token != null) {
            try {
                if (this.tokenStore.getToken(token.internalTok.getKey()) == null) {
                    this.tokenStore.saveToken(token, token.internalTok.getKey());
                }
            } catch (Exception e) {
            }
        }
        Enumeration toksToNotE = this.clientState.resolveOldTokens(reason).elements();
        while (toksToNotE.hasMoreElements()) {
            MqttToken tok = (MqttToken) toksToNotE.nextElement();
            if (tok.internalTok.getKey().equals(MqttDisconnect.KEY) || tok.internalTok.getKey().equals(MqttConnect.KEY)) {
                tokToNotifyLater = tok;
            } else {
                this.callback.asyncOperationComplete(tok);
            }
        }
        return tokToNotifyLater;
    }

    public void disconnect(MqttDisconnect disconnect, long quiesceTimeout, MqttToken token) throws MqttException {
        synchronized (this.conLock) {
            if (isClosed()) {
                log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "223");
                throw ExceptionHelper.createMqttException(32111);
            } else if (isDisconnected()) {
                log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "211");
                throw ExceptionHelper.createMqttException(32101);
            } else if (isDisconnecting()) {
                log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "219");
                throw ExceptionHelper.createMqttException(32102);
            } else if (Thread.currentThread() == this.callback.getThread()) {
                log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "210");
                throw ExceptionHelper.createMqttException(32107);
            } else {
                log.fine(CLASS_NAME, MqttServiceConstants.DISCONNECT_ACTION, "218");
                this.conState = DISCONNECTING;
                new DisconnectBG(this, disconnect, quiesceTimeout, token).start();
            }
        }
    }

    public void disconnectForcibly(long quiesceTimeout, long disconnectTimeout) throws MqttException {
        this.clientState.quiesce(quiesceTimeout);
        MqttToken token = new MqttToken(this.client.getClientId());
        try {
            internalSend(new MqttDisconnect(), token);
            token.waitForCompletion(disconnectTimeout);
        } catch (Exception e) {
        } catch (Throwable th) {
            token.internalTok.markComplete(null, null);
            shutdownConnection(token, null);
        }
        token.internalTok.markComplete(null, null);
        shutdownConnection(token, null);
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.conLock) {
            z = this.conState == null;
        }
        return z;
    }

    public boolean isConnecting() {
        boolean z = true;
        synchronized (this.conLock) {
            if (this.conState != CONNECTING) {
                z = false;
            }
        }
        return z;
    }

    public boolean isDisconnected() {
        boolean z;
        synchronized (this.conLock) {
            z = this.conState == 3;
        }
        return z;
    }

    public boolean isDisconnecting() {
        boolean z;
        synchronized (this.conLock) {
            z = this.conState == 2;
        }
        return z;
    }

    public boolean isClosed() {
        boolean z;
        synchronized (this.conLock) {
            z = this.conState == 4;
        }
        return z;
    }

    public void setCallback(MqttCallback mqttCallback) {
        this.callback.setCallback(mqttCallback);
    }

    protected MqttTopic getTopic(String topic) {
        return new MqttTopic(topic, this);
    }

    public void setNetworkModuleIndex(int index) {
        this.networkModuleIndex = index;
    }

    public int getNetworkModuleIndex() {
        return this.networkModuleIndex;
    }

    public NetworkModule[] getNetworkModules() {
        return this.networkModules;
    }

    public void setNetworkModules(NetworkModule[] networkModules) {
        this.networkModules = networkModules;
    }

    public MqttDeliveryToken[] getPendingDeliveryTokens() {
        return this.tokenStore.getOutstandingDelTokens();
    }

    protected void deliveryComplete(MqttPublish msg) throws MqttPersistenceException {
        this.clientState.deliveryComplete(msg);
    }

    public IMqttAsyncClient getClient() {
        return this.client;
    }

    public long getKeepAlive() {
        return this.clientState.getKeepAlive();
    }

    public ClientState getClientState() {
        return this.clientState;
    }

    public MqttConnectOptions getConOptions() {
        return this.conOptions;
    }

    public Properties getDebug() {
        Properties props = new Properties();
        props.put("conState", new Integer(this.conState));
        props.put("serverURI", getClient().getServerURI());
        props.put("callback", this.callback);
        props.put("stoppingComms", new Boolean(this.stoppingComms));
        return props;
    }

    public MqttToken checkForActivity() {
        MqttToken token = null;
        try {
            token = this.clientState.checkForActivity();
        } catch (MqttException e) {
            handleRunException(e);
        } catch (Exception e2) {
            handleRunException(e2);
        }
        return token;
    }

    private void handleRunException(Exception ex) {
        MqttException mex;
        log.fine(CLASS_NAME, "handleRunException", "804", null, ex);
        if (ex instanceof MqttException) {
            mex = (MqttException) ex;
        } else {
            mex = new MqttException(32109, ex);
        }
        shutdownConnection(null, mex);
    }
}
