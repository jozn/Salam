package org.jivesoftware.smack;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.ResourceBindingNotOfferedException;
import org.jivesoftware.smack.SmackException.SecurityRequiredException;
import org.jivesoftware.smack.XMPPConnection.FromMode;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.compression.XMPPInputOutputStream;
import org.jivesoftware.smack.debugger.SmackDebugger;
import org.jivesoftware.smack.filter.IQReplyFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.Bind;
import org.jivesoftware.smack.packet.Bind.Feature;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Mechanisms;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.RosterVer;
import org.jivesoftware.smack.packet.Session;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.provider.StreamFeatureProvider;
import org.jivesoftware.smack.rosterstore.RosterStore;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jxmpp.util.XmppStringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class AbstractXMPPConnection implements XMPPConnection {
    public static final Logger LOGGER;
    private static final AtomicInteger connectionCounter;
    public boolean anonymous;
    public boolean authenticated;
    protected final Collection<PacketCollector> collectors;
    public XMPPInputOutputStream compressionHandler;
    public final ConnectionConfiguration config;
    protected final int connectionCounterValue;
    protected final Collection<ConnectionListener> connectionListeners;
    protected final Lock connectionLock;
    public SmackDebugger debugger;
    private final ScheduledExecutorService executorService;
    private int fromMode$569b0e80;
    public String host;
    protected final Map<PacketInterceptor, InterceptorWrapper> interceptors;
    public final SynchronizationPoint<Exception> lastFeaturesReceived;
    public long lastStanzaReceived;
    public long packetReplyTimeout;
    public int port;
    public Reader reader;
    protected final Map<PacketListener, ListenerWrapper> recvListeners;
    private final ScheduledExecutorService removeCallbacksService;
    private Roster roster;
    public SASLAuthentication saslAuthentication;
    public final SynchronizationPoint<SmackException> saslFeatureReceived;
    protected final Map<PacketListener, ListenerWrapper> sendListeners;
    protected final Map<String, PacketExtension> streamFeatures;
    public String user;
    public boolean wasAuthenticated;
    public Writer writer;

    /* renamed from: org.jivesoftware.smack.AbstractXMPPConnection.1 */
    class C12751 implements PacketListener {
        final /* synthetic */ PacketListener val$callback;
        final /* synthetic */ ExceptionCallback val$exceptionCallback;

        C12751(PacketListener packetListener, ExceptionCallback exceptionCallback) {
            this.val$callback = packetListener;
            this.val$exceptionCallback = exceptionCallback;
        }

        public final void processPacket(Packet packet) throws NotConnectedException {
            try {
                XMPPErrorException.ifHasErrorThenThrow(packet);
                this.val$callback.processPacket(packet);
                AbstractXMPPConnection.this.removePacketListener(this);
            } catch (XMPPErrorException e) {
                if (this.val$exceptionCallback != null) {
                    this.val$exceptionCallback.processException(e);
                }
                AbstractXMPPConnection.this.removePacketListener(this);
            } catch (Throwable th) {
                AbstractXMPPConnection.this.removePacketListener(this);
            }
        }
    }

    /* renamed from: org.jivesoftware.smack.AbstractXMPPConnection.2 */
    class C12762 implements Runnable {
        final /* synthetic */ ExceptionCallback val$exceptionCallback;
        final /* synthetic */ PacketListener val$packetListener;

        C12762(PacketListener packetListener, ExceptionCallback exceptionCallback) {
            this.val$packetListener = packetListener;
            this.val$exceptionCallback = exceptionCallback;
        }

        public final void run() {
            if (AbstractXMPPConnection.this.removePacketListener(this.val$packetListener) && this.val$exceptionCallback != null) {
                this.val$exceptionCallback.processException(new NoResponseException());
            }
        }
    }

    /* renamed from: org.jivesoftware.smack.AbstractXMPPConnection.3 */
    static /* synthetic */ class C12773 {
        static final /* synthetic */ int[] $SwitchMap$org$jivesoftware$smack$XMPPConnection$FromMode;

        static {
            $SwitchMap$org$jivesoftware$smack$XMPPConnection$FromMode = new int[FromMode.values$3ec90f06().length];
            try {
                $SwitchMap$org$jivesoftware$smack$XMPPConnection$FromMode[FromMode.OMITTED$569b0e80 - 1] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$XMPPConnection$FromMode[FromMode.USER$569b0e80 - 1] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$XMPPConnection$FromMode[FromMode.UNCHANGED$569b0e80 - 1] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    protected static class InterceptorWrapper {
        PacketFilter packetFilter;
        PacketInterceptor packetInterceptor;

        public InterceptorWrapper(PacketInterceptor packetInterceptor, PacketFilter packetFilter) {
            this.packetInterceptor = packetInterceptor;
            this.packetFilter = packetFilter;
        }

        public final boolean equals(Object object) {
            if (object == null) {
                return false;
            }
            if (object instanceof InterceptorWrapper) {
                return ((InterceptorWrapper) object).packetInterceptor.equals(this.packetInterceptor);
            }
            if (object instanceof PacketInterceptor) {
                return object.equals(this.packetInterceptor);
            }
            return false;
        }
    }

    private class ListenerNotification implements Runnable {
        private Packet packet;

        public ListenerNotification(Packet packet) {
            this.packet = packet;
        }

        public final void run() {
            for (ListenerWrapper listenerWrapper : AbstractXMPPConnection.this.recvListeners.values()) {
                try {
                    listenerWrapper.notifyListener(this.packet);
                } catch (NotConnectedException e) {
                    AbstractXMPPConnection.LOGGER.log(Level.WARNING, "Got not connected exception, aborting", e);
                    return;
                } catch (Exception e2) {
                    AbstractXMPPConnection.LOGGER.log(Level.SEVERE, "Exception in packet listener", e2);
                }
            }
        }
    }

    protected static class ListenerWrapper {
        private PacketFilter packetFilter;
        private PacketListener packetListener;

        public ListenerWrapper(PacketListener packetListener, PacketFilter packetFilter) {
            this.packetListener = packetListener;
            this.packetFilter = packetFilter;
        }

        public final void notifyListener(Packet packet) throws NotConnectedException {
            if (this.packetFilter == null || this.packetFilter.accept(packet)) {
                this.packetListener.processPacket(packet);
            }
        }
    }

    private static final class SmackExecutorThreadFactory implements ThreadFactory {
        private final int connectionCounterValue;
        private int count;

        private SmackExecutorThreadFactory(int connectionCounterValue) {
            this.count = 0;
            this.connectionCounterValue = connectionCounterValue;
        }

        public final Thread newThread(Runnable runnable) {
            StringBuilder stringBuilder = new StringBuilder("Smack Executor Service ");
            int i = this.count;
            this.count = i + 1;
            Thread thread = new Thread(runnable, stringBuilder.append(i).append(" (").append(this.connectionCounterValue).append(")").toString());
            thread.setDaemon(true);
            return thread;
        }
    }

    public abstract void connectInternal() throws SmackException, IOException, XMPPException;

    public abstract String getUser();

    public abstract boolean isAuthenticated();

    public abstract boolean isConnected();

    public abstract void send(PlainStreamElement plainStreamElement) throws NotConnectedException;

    public abstract void sendPacketInternal(Packet packet) throws NotConnectedException;

    public abstract void shutdown();

    static {
        LOGGER = Logger.getLogger(AbstractXMPPConnection.class.getName());
        connectionCounter = new AtomicInteger(0);
        SmackConfiguration.getVersion();
    }

    public static Collection<ConnectionCreationListener> getConnectionCreationListeners() {
        return XMPPConnectionRegistry.getConnectionCreationListeners();
    }

    public AbstractXMPPConnection(ConnectionConfiguration configuration) {
        this.connectionListeners = new CopyOnWriteArrayList();
        this.collectors = new ConcurrentLinkedQueue();
        this.recvListeners = new ConcurrentHashMap();
        this.sendListeners = new ConcurrentHashMap();
        this.interceptors = new ConcurrentHashMap();
        this.connectionLock = new ReentrantLock();
        this.streamFeatures = new HashMap();
        this.packetReplyTimeout = (long) SmackConfiguration.getDefaultPacketReplyTimeout();
        this.debugger = null;
        this.lastFeaturesReceived = new SynchronizationPoint(this);
        this.saslFeatureReceived = new SynchronizationPoint(this);
        this.saslAuthentication = new SASLAuthentication(this);
        this.connectionCounterValue = connectionCounter.getAndIncrement();
        this.fromMode$569b0e80 = FromMode.OMITTED$569b0e80;
        this.executorService = new ScheduledThreadPoolExecutor(1, new SmackExecutorThreadFactory((byte) 0));
        this.authenticated = false;
        this.wasAuthenticated = false;
        this.anonymous = false;
        this.removeCallbacksService = new ScheduledThreadPoolExecutor(1, new SmackExecutorThreadFactory((byte) 0));
        this.config = configuration;
    }

    public final String getServiceName() {
        return this.config.serviceName;
    }

    public final void connect() throws SmackException, IOException, XMPPException {
        this.saslAuthentication.init();
        this.saslFeatureReceived.init();
        this.lastFeaturesReceived.init();
        connectInternal();
    }

    public final void bindResourceAndEstablishSession(String resource) throws XMPPErrorException, IOException, SmackException {
        LOGGER.finer("Waiting for last features to be received before continuing with resource binding");
        this.lastFeaturesReceived.checkIfSuccessOrWait();
        if (hasFeature("bind", "urn:ietf:params:xml:ns:xmpp-bind")) {
            Packet bindResource = Bind.newSet(resource);
            PacketCollector packetCollector = createPacketCollector(new PacketIDFilter(bindResource));
            try {
                sendPacket(bindResource);
                this.user = ((Bind) packetCollector.nextResultOrThrow()).jid;
                setServiceName(XmppStringUtils.parseDomain(this.user));
                if (hasFeature("session", "urn:ietf:params:xml:ns:xmpp-session") && !this.config.legacySessionDisabled) {
                    Packet session = new Session();
                    packetCollector = createPacketCollector(new PacketIDFilter(session));
                    try {
                        sendPacket(session);
                        packetCollector.nextResultOrThrow();
                        return;
                    } catch (NotConnectedException e) {
                        packetCollector.cancel();
                        throw e;
                    }
                }
                return;
            } catch (NotConnectedException e2) {
                packetCollector.cancel();
                throw e2;
            }
        }
        throw new ResourceBindingNotOfferedException();
    }

    public final void afterSuccessfulLogin(boolean anonymous, boolean resumed) throws NotConnectedException {
        this.authenticated = true;
        this.anonymous = anonymous;
        for (ConnectionListener authenticated : this.connectionListeners) {
            authenticated.authenticated(this);
        }
        if (this.config.sendPresence && !resumed) {
            sendPacket(new Presence(Type.available));
        }
    }

    public final boolean isAnonymous() {
        return this.anonymous;
    }

    public final void setServiceName(String serviceName) {
        this.config.serviceName = XmppStringUtils.parseDomain(serviceName);
    }

    protected final Lock getConnectionLock() {
        return this.connectionLock;
    }

    public final void sendPacket(Packet packet) throws NotConnectedException {
        if (!isConnected()) {
            throw new NotConnectedException();
        } else if (packet == null) {
            throw new IllegalArgumentException("Packet must not be null");
        } else {
            switch (C12773.$SwitchMap$org$jivesoftware$smack$XMPPConnection$FromMode[this.fromMode$569b0e80 - 1]) {
                case org.eclipse.paho.client.mqttv3.logging.Logger.SEVERE /*1*/:
                    packet.from = null;
                    break;
                case org.eclipse.paho.client.mqttv3.logging.Logger.WARNING /*2*/:
                    packet.from = getUser();
                    break;
            }
            firePacketInterceptors(packet);
            sendPacketInternal(packet);
            firePacketSendingListeners(packet);
        }
    }

    public final Roster getRoster() {
        if (this.anonymous) {
            throw new IllegalStateException("Anonymous users can't have a roster");
        }
        synchronized (this) {
            if (this.roster == null) {
                this.roster = new Roster(this);
            }
            if (isAuthenticated()) {
                if (!this.roster.rosterInitialized && this.config.rosterLoadedAtLogin) {
                    try {
                        synchronized (this.roster) {
                            long waitTime = this.packetReplyTimeout;
                            long start = System.currentTimeMillis();
                            while (!this.roster.rosterInitialized && waitTime > 0) {
                                this.roster.wait(waitTime);
                                long now = System.currentTimeMillis();
                                waitTime -= now - start;
                                start = now;
                            }
                        }
                    } catch (InterruptedException e) {
                    }
                }
                return this.roster;
            }
            Roster roster = this.roster;
            return roster;
        }
    }

    public final void disconnect() throws NotConnectedException {
        disconnect(new Presence(Type.unavailable));
    }

    private synchronized void disconnect(Presence unavailablePresence) throws NotConnectedException {
        if (isConnected()) {
            sendPacket(unavailablePresence);
            shutdown();
            for (ConnectionListener connectionClosed : this.connectionListeners) {
                try {
                    connectionClosed.connectionClosed();
                } catch (Throwable e) {
                    LOGGER.log(Level.SEVERE, "Error in listener while closing connection", e);
                }
            }
        }
    }

    public final void addConnectionListener(ConnectionListener connectionListener) {
        if (connectionListener != null && !this.connectionListeners.contains(connectionListener)) {
            this.connectionListeners.add(connectionListener);
        }
    }

    public final void removeConnectionListener(ConnectionListener connectionListener) {
        this.connectionListeners.remove(connectionListener);
    }

    public final Collection<ConnectionListener> getConnectionListeners() {
        return this.connectionListeners;
    }

    public final PacketCollector createPacketCollectorAndSend(IQ packet) throws NotConnectedException {
        PacketCollector packetCollector = createPacketCollector(new IQReplyFilter(packet, this));
        try {
            sendPacket(packet);
            return packetCollector;
        } catch (NotConnectedException e) {
            packetCollector.cancel();
            throw e;
        }
    }

    public final PacketCollector createPacketCollector(PacketFilter packetFilter) {
        PacketCollector collector = new PacketCollector(this, packetFilter);
        this.collectors.add(collector);
        return collector;
    }

    public final void removePacketCollector(PacketCollector collector) {
        this.collectors.remove(collector);
    }

    public final void addPacketListener(PacketListener packetListener, PacketFilter packetFilter) {
        if (packetListener == null) {
            throw new NullPointerException("Packet listener is null.");
        }
        this.recvListeners.put(packetListener, new ListenerWrapper(packetListener, packetFilter));
    }

    public final boolean removePacketListener(PacketListener packetListener) {
        return this.recvListeners.remove(packetListener) != null;
    }

    public final void addPacketSendingListener(PacketListener packetListener, PacketFilter packetFilter) {
        if (packetListener == null) {
            throw new NullPointerException("Packet listener is null.");
        }
        this.sendListeners.put(packetListener, new ListenerWrapper(packetListener, packetFilter));
    }

    private void firePacketSendingListeners(Packet packet) {
        for (ListenerWrapper listenerWrapper : this.sendListeners.values()) {
            try {
                listenerWrapper.notifyListener(packet);
            } catch (NotConnectedException e) {
                LOGGER.log(Level.WARNING, "Got not connected exception, aborting");
                return;
            }
        }
    }

    public final void addPacketInterceptor(PacketInterceptor packetInterceptor, PacketFilter packetFilter) {
        this.interceptors.put(packetInterceptor, new InterceptorWrapper(packetInterceptor, packetFilter));
    }

    private void firePacketInterceptors(Packet packet) {
        if (packet != null) {
            for (InterceptorWrapper interceptorWrapper : this.interceptors.values()) {
                if (interceptorWrapper.packetFilter == null || interceptorWrapper.packetFilter.accept(packet)) {
                    interceptorWrapper.packetInterceptor.interceptPacket(packet);
                }
            }
        }
    }

    public final long getPacketReplyTimeout() {
        return this.packetReplyTimeout;
    }

    public final void processPacket(Packet packet) {
        if (packet != null) {
            for (PacketCollector packetCollector : this.collectors) {
                if (packet != null && (packetCollector.packetFilter == null || packetCollector.packetFilter.accept(packet))) {
                    while (!packetCollector.resultQueue.offer(packet)) {
                        packetCollector.resultQueue.poll();
                    }
                }
            }
            this.executorService.submit(new ListenerNotification(packet));
        }
    }

    public final void callConnectionConnectedListener() {
        for (ConnectionListener connected : this.connectionListeners) {
            connected.connected(this);
        }
    }

    public final void callConnectionClosedOnErrorListener(Exception e) {
        LOGGER.log(Level.WARNING, "Connection closed with error", e);
        for (ConnectionListener listener : this.connectionListeners) {
            try {
                listener.connectionClosedOnError(e);
            } catch (Exception e2) {
                LOGGER.log(Level.SEVERE, "Error in listener while closing connection", e2);
            }
        }
    }

    public final int getConnectionCounter() {
        return this.connectionCounterValue;
    }

    protected void finalize() throws Throwable {
        try {
            this.executorService.shutdownNow();
            this.removeCallbacksService.shutdownNow();
        } finally {
            super.finalize();
        }
    }

    public final RosterStore getRosterStore() {
        return this.config.rosterStore;
    }

    public final boolean isRosterLoadedAtLogin() {
        return this.config.rosterLoadedAtLogin;
    }

    public final void parseFeatures(XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
        this.streamFeatures.clear();
        int initialDepth = parser.getDepth();
        while (true) {
            int eventType = parser.next();
            if (eventType == 2 && parser.getDepth() == initialDepth + 1) {
                PacketExtension streamFeature = null;
                String name = parser.getName();
                String namespace = parser.getNamespace();
                Object obj = -1;
                int i;
                switch (name.hashCode()) {
                    case -676919238:
                        if (name.equals("mechanisms")) {
                            obj = 1;
                            break;
                        }
                        break;
                    case 116643:
                        if (name.equals("ver")) {
                            obj = 4;
                            break;
                        }
                        break;
                    case 3023933:
                        if (name.equals("bind")) {
                            i = 2;
                            break;
                        }
                        break;
                    case 1316817241:
                        if (name.equals("starttls")) {
                            obj = null;
                            break;
                        }
                        break;
                    case 1431984486:
                        if (name.equals("compression")) {
                            obj = 5;
                            break;
                        }
                        break;
                    case 1984987798:
                        if (name.equals("session")) {
                            i = 3;
                            break;
                        }
                        break;
                }
                switch (obj) {
                    case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                        streamFeature = PacketParserUtils.parseStartTlsFeature(parser);
                        break;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.SEVERE /*1*/:
                        streamFeature = new Mechanisms(PacketParserUtils.parseMechanisms(parser));
                        break;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.WARNING /*2*/:
                        streamFeature = Feature.INSTANCE;
                        break;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.INFO /*3*/:
                        streamFeature = Session.Feature.INSTANCE;
                        break;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.CONFIG /*4*/:
                        if (!namespace.equals("urn:xmpp:features:rosterver")) {
                            LOGGER.severe("Unkown Roster Versioning Namespace: " + namespace + ". Roster versioning not enabled");
                            break;
                        } else {
                            streamFeature = RosterVer.INSTANCE;
                            break;
                        }
                    case org.eclipse.paho.client.mqttv3.logging.Logger.FINE /*5*/:
                        streamFeature = PacketParserUtils.parseCompressionFeature(parser);
                        break;
                    default:
                        StreamFeatureProvider provider = ProviderManager.getStreamFeatureProvider(name, namespace);
                        if (provider != null) {
                            streamFeature = provider.parseStreamFeature(parser);
                            break;
                        }
                        break;
                }
                if (streamFeature != null) {
                    this.streamFeatures.put(XmppStringUtils.generateKey(streamFeature.getElementName(), streamFeature.getNamespace()), streamFeature);
                }
            } else if (eventType == 3 && parser.getDepth() == initialDepth) {
                if (hasFeature("mechanisms", "urn:ietf:params:xml:ns:xmpp-sasl") && (!hasFeature("starttls", "urn:ietf:params:xml:ns:xmpp-tls") || this.config.securityMode$21bc7c29 == SecurityMode.disabled$21bc7c29)) {
                    this.saslFeatureReceived.reportSuccess();
                }
                if (hasFeature("bind", "urn:ietf:params:xml:ns:xmpp-bind") && !(hasFeature("compression", "http://jabber.org/protocol/compress") && this.config.compressionEnabled)) {
                    this.lastFeaturesReceived.reportSuccess();
                }
                afterFeaturesReceived();
                return;
            }
        }
    }

    public void afterFeaturesReceived() throws SecurityRequiredException, NotConnectedException {
    }

    public final <F extends PacketExtension> F getFeature(String element, String namespace) {
        return (PacketExtension) this.streamFeatures.get(XmppStringUtils.generateKey(element, namespace));
    }

    public final boolean hasFeature(String element, String namespace) {
        return getFeature(element, namespace) != null;
    }

    public final void sendIqWithResponseCallback(IQ iqRequest, PacketListener callback) throws NotConnectedException {
        sendIqWithResponseCallback(iqRequest, callback, null);
    }

    public final void sendIqWithResponseCallback(IQ iqRequest, PacketListener callback, ExceptionCallback exceptionCallback) throws NotConnectedException {
        long j = this.packetReplyTimeout;
        PacketFilter iQReplyFilter = new IQReplyFilter(iqRequest, this);
        if (iqRequest == null) {
            throw new IllegalArgumentException("stanza must not be null");
        } else if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        } else {
            PacketListener c12751 = new C12751(callback, exceptionCallback);
            this.removeCallbacksService.schedule(new C12762(c12751, exceptionCallback), j, TimeUnit.MILLISECONDS);
            addPacketListener(c12751, iQReplyFilter);
            sendPacket(iqRequest);
        }
    }

    public final long getLastStanzaReceived() {
        return this.lastStanzaReceived;
    }
}
