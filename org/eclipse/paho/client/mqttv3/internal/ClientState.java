package org.eclipse.paho.client.mqttv3.internal;

import android.support.v7.appcompat.BuildConfig;
import java.io.EOFException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistable;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttAck;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnack;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPingReq;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPingResp;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubAck;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubComp;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubRec;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubRel;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class ClientState {
    private static final String CLASS_NAME;
    private static final int DEFAULT_MAX_INFLIGHT = 10;
    private static final int MAX_MSG_ID = 65535;
    private static final int MIN_MSG_ID = 1;
    private static final String PERSISTENCE_CONFIRMED_PREFIX = "sc-";
    private static final String PERSISTENCE_RECEIVED_PREFIX = "r-";
    private static final String PERSISTENCE_SENT_PREFIX = "s-";
    static Class class$0;
    private static final Logger log;
    private int actualInFlight;
    private CommsCallback callback;
    private boolean cleanSession;
    private ClientComms clientComms;
    private boolean connected;
    private int inFlightPubRels;
    private Hashtable inUseMsgIds;
    private Hashtable inboundQoS2;
    private long keepAlive;
    private long lastInboundActivity;
    private long lastOutboundActivity;
    private long lastPing;
    private int maxInflight;
    private int nextMsgId;
    private Hashtable outboundQoS1;
    private Hashtable outboundQoS2;
    private volatile Vector pendingFlows;
    private volatile Vector pendingMessages;
    private MqttClientPersistence persistence;
    private MqttWireMessage pingCommand;
    private int pingOutstanding;
    private Object pingOutstandingLock;
    private MqttPingSender pingSender;
    private Object queueLock;
    private Object quiesceLock;
    private boolean quiescing;
    private CommsTokenStore tokenStore;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.ClientState");
                class$0 = cls;
            } catch (Throwable e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
        log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    }

    protected ClientState(MqttClientPersistence persistence, CommsTokenStore tokenStore, CommsCallback callback, ClientComms clientComms, MqttPingSender pingSender) throws MqttException {
        this.nextMsgId = 0;
        this.clientComms = null;
        this.callback = null;
        this.maxInflight = DEFAULT_MAX_INFLIGHT;
        this.actualInFlight = 0;
        this.inFlightPubRels = 0;
        this.queueLock = new Object();
        this.quiesceLock = new Object();
        this.quiescing = false;
        this.lastOutboundActivity = 0;
        this.lastInboundActivity = 0;
        this.lastPing = 0;
        this.pingOutstandingLock = new Object();
        this.pingOutstanding = 0;
        this.connected = false;
        this.outboundQoS2 = null;
        this.outboundQoS1 = null;
        this.inboundQoS2 = null;
        this.pingSender = null;
        log.setResourceName(clientComms.getClient().getClientId());
        log.finer(CLASS_NAME, "<Init>", BuildConfig.VERSION_NAME);
        this.inUseMsgIds = new Hashtable();
        this.pendingMessages = new Vector(this.maxInflight);
        this.pendingFlows = new Vector();
        this.outboundQoS2 = new Hashtable();
        this.outboundQoS1 = new Hashtable();
        this.inboundQoS2 = new Hashtable();
        this.pingCommand = new MqttPingReq();
        this.inFlightPubRels = 0;
        this.actualInFlight = 0;
        this.persistence = persistence;
        this.callback = callback;
        this.tokenStore = tokenStore;
        this.clientComms = clientComms;
        this.pingSender = pingSender;
        restoreState();
    }

    protected void setKeepAliveSecs(long keepAliveSecs) {
        this.keepAlive = 1000 * keepAliveSecs;
    }

    protected long getKeepAlive() {
        return this.keepAlive;
    }

    protected void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    private String getSendPersistenceKey(MqttWireMessage message) {
        return new StringBuffer(PERSISTENCE_SENT_PREFIX).append(message.getMessageId()).toString();
    }

    private String getSendConfirmPersistenceKey(MqttWireMessage message) {
        return new StringBuffer(PERSISTENCE_CONFIRMED_PREFIX).append(message.getMessageId()).toString();
    }

    private String getReceivedPersistenceKey(MqttWireMessage message) {
        return new StringBuffer(PERSISTENCE_RECEIVED_PREFIX).append(message.getMessageId()).toString();
    }

    protected void clearState() throws MqttException {
        log.fine(CLASS_NAME, "clearState", ">");
        this.persistence.clear();
        this.inUseMsgIds.clear();
        this.pendingMessages.clear();
        this.pendingFlows.clear();
        this.outboundQoS2.clear();
        this.outboundQoS1.clear();
        this.inboundQoS2.clear();
        this.tokenStore.clear();
    }

    private MqttWireMessage restoreMessage(String key, MqttPersistable persistable) throws MqttException {
        MqttWireMessage message = null;
        try {
            message = MqttWireMessage.createWireMessage(persistable);
        } catch (MqttException ex) {
            Object[] objArr = new Object[MIN_MSG_ID];
            objArr[0] = key;
            log.fine(CLASS_NAME, "restoreMessage", "602", objArr, ex);
            if (!(ex.getCause() instanceof EOFException)) {
                throw ex;
            } else if (key != null) {
                this.persistence.remove(key);
            }
        }
        log.fine(CLASS_NAME, "restoreMessage", "601", new Object[]{key, message});
        return message;
    }

    private void insertInOrder(Vector list, MqttWireMessage newMsg) {
        int newMsgId = newMsg.getMessageId();
        for (int i = 0; i < list.size(); i += MIN_MSG_ID) {
            if (((MqttWireMessage) list.elementAt(i)).getMessageId() > newMsgId) {
                list.insertElementAt(newMsg, i);
                return;
            }
        }
        list.addElement(newMsg);
    }

    private Vector reOrder(Vector list) {
        Vector newList = new Vector();
        if (list.size() != 0) {
            int i;
            int previousMsgId = 0;
            int largestGap = 0;
            int largestGapMsgIdPosInList = 0;
            for (i = 0; i < list.size(); i += MIN_MSG_ID) {
                int currentMsgId = ((MqttWireMessage) list.elementAt(i)).getMessageId();
                if (currentMsgId - previousMsgId > largestGap) {
                    largestGap = currentMsgId - previousMsgId;
                    largestGapMsgIdPosInList = i;
                }
                previousMsgId = currentMsgId;
            }
            if ((MAX_MSG_ID - previousMsgId) + ((MqttWireMessage) list.elementAt(0)).getMessageId() > largestGap) {
                largestGapMsgIdPosInList = 0;
            }
            for (i = largestGapMsgIdPosInList; i < list.size(); i += MIN_MSG_ID) {
                newList.addElement(list.elementAt(i));
            }
            for (i = 0; i < largestGapMsgIdPosInList; i += MIN_MSG_ID) {
                newList.addElement(list.elementAt(i));
            }
        }
        return newList;
    }

    protected void restoreState() throws MqttException {
        Enumeration messageKeys = this.persistence.keys();
        int highestMsgId = this.nextMsgId;
        Vector orphanedPubRels = new Vector();
        log.fine(CLASS_NAME, "restoreState", "600");
        while (messageKeys.hasMoreElements()) {
            String key = (String) messageKeys.nextElement();
            MqttWireMessage message = restoreMessage(key, this.persistence.get(key));
            if (message != null) {
                if (key.startsWith(PERSISTENCE_RECEIVED_PREFIX)) {
                    log.fine(CLASS_NAME, "restoreState", "604", new Object[]{key, message});
                    this.inboundQoS2.put(new Integer(message.getMessageId()), message);
                } else if (key.startsWith(PERSISTENCE_SENT_PREFIX)) {
                    MqttPublish sendMessage = (MqttPublish) message;
                    highestMsgId = Math.max(sendMessage.getMessageId(), highestMsgId);
                    if (this.persistence.containsKey(getSendConfirmPersistenceKey(sendMessage))) {
                        MqttPubRel confirmMessage = (MqttPubRel) restoreMessage(key, this.persistence.get(getSendConfirmPersistenceKey(sendMessage)));
                        if (confirmMessage != null) {
                            log.fine(CLASS_NAME, "restoreState", "605", new Object[]{key, message});
                            this.outboundQoS2.put(new Integer(confirmMessage.getMessageId()), confirmMessage);
                        } else {
                            log.fine(CLASS_NAME, "restoreState", "606", new Object[]{key, message});
                        }
                    } else {
                        sendMessage.setDuplicate(true);
                        if (sendMessage.getMessage().getQos() == 2) {
                            log.fine(CLASS_NAME, "restoreState", "607", new Object[]{key, message});
                            this.outboundQoS2.put(new Integer(sendMessage.getMessageId()), sendMessage);
                        } else {
                            log.fine(CLASS_NAME, "restoreState", "608", new Object[]{key, message});
                            this.outboundQoS1.put(new Integer(sendMessage.getMessageId()), sendMessage);
                        }
                    }
                    this.tokenStore.restoreToken(sendMessage).internalTok.setClient(this.clientComms.getClient());
                    this.inUseMsgIds.put(new Integer(sendMessage.getMessageId()), new Integer(sendMessage.getMessageId()));
                } else if (key.startsWith(PERSISTENCE_CONFIRMED_PREFIX)) {
                    if (!this.persistence.containsKey(getSendPersistenceKey((MqttPubRel) message))) {
                        orphanedPubRels.addElement(key);
                    }
                }
            }
        }
        messageKeys = orphanedPubRels.elements();
        while (messageKeys.hasMoreElements()) {
            key = (String) messageKeys.nextElement();
            Object[] objArr = new Object[MIN_MSG_ID];
            objArr[0] = key;
            log.fine(CLASS_NAME, "restoreState", "609", objArr);
            this.persistence.remove(key);
        }
        this.nextMsgId = highestMsgId;
    }

    private void restoreInflightMessages() {
        this.pendingMessages = new Vector(this.maxInflight);
        this.pendingFlows = new Vector();
        Enumeration keys = this.outboundQoS2.keys();
        while (keys.hasMoreElements()) {
            Object[] objArr;
            Object key = keys.nextElement();
            MqttWireMessage msg = (MqttWireMessage) this.outboundQoS2.get(key);
            if (msg instanceof MqttPublish) {
                objArr = new Object[MIN_MSG_ID];
                objArr[0] = key;
                log.fine(CLASS_NAME, "restoreInflightMessages", "610", objArr);
                msg.setDuplicate(true);
                insertInOrder(this.pendingMessages, (MqttPublish) msg);
            } else if (msg instanceof MqttPubRel) {
                objArr = new Object[MIN_MSG_ID];
                objArr[0] = key;
                log.fine(CLASS_NAME, "restoreInflightMessages", "611", objArr);
                insertInOrder(this.pendingFlows, (MqttPubRel) msg);
            }
        }
        keys = this.outboundQoS1.keys();
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            MqttPublish msg2 = (MqttPublish) this.outboundQoS1.get(key);
            msg2.setDuplicate(true);
            objArr = new Object[MIN_MSG_ID];
            objArr[0] = key;
            log.fine(CLASS_NAME, "restoreInflightMessages", "612", objArr);
            insertInOrder(this.pendingMessages, msg2);
        }
        this.pendingFlows = reOrder(this.pendingFlows);
        this.pendingMessages = reOrder(this.pendingMessages);
    }

    public void send(MqttWireMessage message, MqttToken token) throws MqttException {
        if (message.isMessageIdRequired() && message.getMessageId() == 0) {
            message.setMessageId(getNextMessageId());
        }
        if (token != null) {
            try {
                token.internalTok.setMessageID(message.getMessageId());
            } catch (Exception e) {
            }
        }
        if (message instanceof MqttPublish) {
            synchronized (this.queueLock) {
                if (this.actualInFlight >= this.maxInflight) {
                    Object[] objArr = new Object[MIN_MSG_ID];
                    objArr[0] = new Integer(this.actualInFlight);
                    log.fine(CLASS_NAME, MqttServiceConstants.SEND_ACTION, "613", objArr);
                    throw new MqttException(32202);
                }
                log.fine(CLASS_NAME, MqttServiceConstants.SEND_ACTION, "628", new Object[]{new Integer(message.getMessageId()), new Integer(((MqttPublish) message).getMessage().getQos()), message});
                switch (((MqttPublish) message).getMessage().getQos()) {
                    case MIN_MSG_ID /*1*/:
                        this.outboundQoS1.put(new Integer(message.getMessageId()), message);
                        this.persistence.put(getSendPersistenceKey(message), (MqttPublish) message);
                        break;
                    case Logger.WARNING /*2*/:
                        this.outboundQoS2.put(new Integer(message.getMessageId()), message);
                        this.persistence.put(getSendPersistenceKey(message), (MqttPublish) message);
                        break;
                }
                this.tokenStore.saveToken(token, message);
                this.pendingMessages.addElement(message);
                this.queueLock.notifyAll();
            }
            return;
        }
        log.fine(CLASS_NAME, MqttServiceConstants.SEND_ACTION, "615", new Object[]{new Integer(message.getMessageId()), message});
        if (message instanceof MqttConnect) {
            synchronized (this.queueLock) {
                this.tokenStore.saveToken(token, message);
                this.pendingFlows.insertElementAt(message, 0);
                this.queueLock.notifyAll();
            }
            return;
        }
        if (message instanceof MqttPingReq) {
            this.pingCommand = message;
        } else if (message instanceof MqttPubRel) {
            this.outboundQoS2.put(new Integer(message.getMessageId()), message);
            this.persistence.put(getSendConfirmPersistenceKey(message), (MqttPubRel) message);
        } else if (message instanceof MqttPubComp) {
            this.persistence.remove(getReceivedPersistenceKey(message));
        }
        synchronized (this.queueLock) {
            if (!(message instanceof MqttAck)) {
                this.tokenStore.saveToken(token, message);
            }
            this.pendingFlows.addElement(message);
            this.queueLock.notifyAll();
        }
    }

    protected void undo(MqttPublish message) throws MqttPersistenceException {
        synchronized (this.queueLock) {
            log.fine(CLASS_NAME, "undo", "618", new Object[]{new Integer(message.getMessageId()), new Integer(message.getMessage().getQos())});
            if (message.getMessage().getQos() == MIN_MSG_ID) {
                this.outboundQoS1.remove(new Integer(message.getMessageId()));
            } else {
                this.outboundQoS2.remove(new Integer(message.getMessageId()));
            }
            this.pendingMessages.removeElement(message);
            this.persistence.remove(getSendPersistenceKey(message));
            this.tokenStore.removeToken((MqttWireMessage) message);
            checkQuiesceLock();
        }
    }

    public MqttToken checkForActivity() throws MqttException {
        log.fine(CLASS_NAME, "checkForActivity", "616", new Object[0]);
        synchronized (this.quiesceLock) {
            if (this.quiescing) {
                return null;
            }
            MqttToken mqttToken = null;
            getKeepAlive();
            if (!this.connected || this.keepAlive <= 0) {
                return null;
            }
            long time = System.currentTimeMillis();
            synchronized (this.pingOutstandingLock) {
                Object[] objArr;
                if (this.pingOutstanding <= 0 || time - this.lastInboundActivity < this.keepAlive + 100) {
                    try {
                        if (this.pingOutstanding != 0 || time - this.lastOutboundActivity < 2 * this.keepAlive) {
                            long nextPingTime;
                            if ((this.pingOutstanding != 0 || time - this.lastInboundActivity < this.keepAlive - 100) && time - this.lastOutboundActivity < this.keepAlive - 100) {
                                log.fine(CLASS_NAME, "checkForActivity", "634", null);
                                nextPingTime = Math.max(1, getKeepAlive() - (time - this.lastOutboundActivity));
                            } else {
                                objArr = new Object[3];
                                objArr[0] = new Long(this.keepAlive);
                                objArr[MIN_MSG_ID] = new Long(this.lastOutboundActivity);
                                objArr[2] = new Long(this.lastInboundActivity);
                                log.fine(CLASS_NAME, "checkForActivity", "620", objArr);
                                MqttToken token = new MqttToken(this.clientComms.getClient().getClientId());
                                try {
                                    this.tokenStore.saveToken(token, this.pingCommand);
                                    this.pendingFlows.insertElementAt(this.pingCommand, 0);
                                    nextPingTime = getKeepAlive();
                                    notifyQueueLock();
                                    mqttToken = token;
                                } catch (Throwable th) {
                                    Throwable th2 = th;
                                    mqttToken = token;
                                    throw th2;
                                }
                            }
                            Object[] objArr2 = new Object[MIN_MSG_ID];
                            objArr2[0] = new Long(nextPingTime);
                            log.fine(CLASS_NAME, "checkForActivity", "624", objArr2);
                            this.pingSender.schedule(nextPingTime);
                            return mqttToken;
                        }
                        objArr = new Object[5];
                        objArr[0] = new Long(this.keepAlive);
                        objArr[MIN_MSG_ID] = new Long(this.lastOutboundActivity);
                        objArr[2] = new Long(this.lastInboundActivity);
                        objArr[3] = new Long(time);
                        objArr[4] = new Long(this.lastPing);
                        log.severe(CLASS_NAME, "checkForActivity", "642", objArr);
                        throw ExceptionHelper.createMqttException(32002);
                    } catch (Throwable th3) {
                        th2 = th3;
                        throw th2;
                    }
                }
                objArr = new Object[5];
                objArr[0] = new Long(this.keepAlive);
                objArr[MIN_MSG_ID] = new Long(this.lastOutboundActivity);
                objArr[2] = new Long(this.lastInboundActivity);
                objArr[3] = new Long(time);
                objArr[4] = new Long(this.lastPing);
                log.severe(CLASS_NAME, "checkForActivity", "619", objArr);
                throw ExceptionHelper.createMqttException(32000);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage get() throws org.eclipse.paho.client.mqttv3.MqttException {
        /*
        r11 = this;
        r1 = 0;
        r3 = r11.queueLock;
        monitor-enter(r3);
    L_0x0004:
        if (r1 == 0) goto L_0x0008;
    L_0x0006:
        monitor-exit(r3);	 Catch:{ all -> 0x00a2 }
    L_0x0007:
        return r1;
    L_0x0008:
        r2 = r11.pendingMessages;	 Catch:{ all -> 0x00a2 }
        r2 = r2.isEmpty();	 Catch:{ all -> 0x00a2 }
        if (r2 == 0) goto L_0x0018;
    L_0x0010:
        r2 = r11.pendingFlows;	 Catch:{ all -> 0x00a2 }
        r2 = r2.isEmpty();	 Catch:{ all -> 0x00a2 }
        if (r2 != 0) goto L_0x0026;
    L_0x0018:
        r2 = r11.pendingFlows;	 Catch:{ all -> 0x00a2 }
        r2 = r2.isEmpty();	 Catch:{ all -> 0x00a2 }
        if (r2 == 0) goto L_0x0041;
    L_0x0020:
        r2 = r11.actualInFlight;	 Catch:{ all -> 0x00a2 }
        r4 = r11.maxInflight;	 Catch:{ all -> 0x00a2 }
        if (r2 < r4) goto L_0x0041;
    L_0x0026:
        r2 = log;	 Catch:{ InterruptedException -> 0x00f1 }
        r4 = CLASS_NAME;	 Catch:{ InterruptedException -> 0x00f1 }
        r5 = "get";
        r6 = "644";
        r2.fine(r4, r5, r6);	 Catch:{ InterruptedException -> 0x00f1 }
        r2 = r11.queueLock;	 Catch:{ InterruptedException -> 0x00f1 }
        r2.wait();	 Catch:{ InterruptedException -> 0x00f1 }
        r2 = log;	 Catch:{ InterruptedException -> 0x00f1 }
        r4 = CLASS_NAME;	 Catch:{ InterruptedException -> 0x00f1 }
        r5 = "get";
        r6 = "647";
        r2.fine(r4, r5, r6);	 Catch:{ InterruptedException -> 0x00f1 }
    L_0x0041:
        r2 = r11.connected;	 Catch:{ all -> 0x00a2 }
        if (r2 != 0) goto L_0x0068;
    L_0x0045:
        r2 = r11.pendingFlows;	 Catch:{ all -> 0x00a2 }
        r2 = r2.isEmpty();	 Catch:{ all -> 0x00a2 }
        if (r2 != 0) goto L_0x005a;
    L_0x004d:
        r2 = r11.pendingFlows;	 Catch:{ all -> 0x00a2 }
        r4 = 0;
        r2 = r2.elementAt(r4);	 Catch:{ all -> 0x00a2 }
        r2 = (org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage) r2;	 Catch:{ all -> 0x00a2 }
        r2 = r2 instanceof org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;	 Catch:{ all -> 0x00a2 }
        if (r2 != 0) goto L_0x0068;
    L_0x005a:
        r2 = log;	 Catch:{ all -> 0x00a2 }
        r4 = CLASS_NAME;	 Catch:{ all -> 0x00a2 }
        r5 = "get";
        r6 = "621";
        r2.fine(r4, r5, r6);	 Catch:{ all -> 0x00a2 }
        monitor-exit(r3);	 Catch:{ all -> 0x00a2 }
        r1 = 0;
        goto L_0x0007;
    L_0x0068:
        r2 = r11.pendingFlows;	 Catch:{ all -> 0x00a2 }
        r2 = r2.isEmpty();	 Catch:{ all -> 0x00a2 }
        if (r2 != 0) goto L_0x00a5;
    L_0x0070:
        r2 = r11.pendingFlows;	 Catch:{ all -> 0x00a2 }
        r4 = 0;
        r2 = r2.remove(r4);	 Catch:{ all -> 0x00a2 }
        r0 = r2;
        r0 = (org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage) r0;	 Catch:{ all -> 0x00a2 }
        r1 = r0;
        r2 = r1 instanceof org.eclipse.paho.client.mqttv3.internal.wire.MqttPubRel;	 Catch:{ all -> 0x00a2 }
        if (r2 == 0) goto L_0x009d;
    L_0x007f:
        r2 = r11.inFlightPubRels;	 Catch:{ all -> 0x00a2 }
        r2 = r2 + 1;
        r11.inFlightPubRels = r2;	 Catch:{ all -> 0x00a2 }
        r2 = log;	 Catch:{ all -> 0x00a2 }
        r4 = CLASS_NAME;	 Catch:{ all -> 0x00a2 }
        r5 = "get";
        r6 = "617";
        r7 = 1;
        r7 = new java.lang.Object[r7];	 Catch:{ all -> 0x00a2 }
        r8 = 0;
        r9 = new java.lang.Integer;	 Catch:{ all -> 0x00a2 }
        r10 = r11.inFlightPubRels;	 Catch:{ all -> 0x00a2 }
        r9.<init>(r10);	 Catch:{ all -> 0x00a2 }
        r7[r8] = r9;	 Catch:{ all -> 0x00a2 }
        r2.fine(r4, r5, r6, r7);	 Catch:{ all -> 0x00a2 }
    L_0x009d:
        r11.checkQuiesceLock();	 Catch:{ all -> 0x00a2 }
        goto L_0x0004;
    L_0x00a2:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x00a2 }
        throw r2;
    L_0x00a5:
        r2 = r11.pendingMessages;	 Catch:{ all -> 0x00a2 }
        r2 = r2.isEmpty();	 Catch:{ all -> 0x00a2 }
        if (r2 != 0) goto L_0x0004;
    L_0x00ad:
        r2 = r11.actualInFlight;	 Catch:{ all -> 0x00a2 }
        r4 = r11.maxInflight;	 Catch:{ all -> 0x00a2 }
        if (r2 >= r4) goto L_0x00e4;
    L_0x00b3:
        r2 = r11.pendingMessages;	 Catch:{ all -> 0x00a2 }
        r4 = 0;
        r2 = r2.elementAt(r4);	 Catch:{ all -> 0x00a2 }
        r0 = r2;
        r0 = (org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage) r0;	 Catch:{ all -> 0x00a2 }
        r1 = r0;
        r2 = r11.pendingMessages;	 Catch:{ all -> 0x00a2 }
        r4 = 0;
        r2.removeElementAt(r4);	 Catch:{ all -> 0x00a2 }
        r2 = r11.actualInFlight;	 Catch:{ all -> 0x00a2 }
        r2 = r2 + 1;
        r11.actualInFlight = r2;	 Catch:{ all -> 0x00a2 }
        r2 = log;	 Catch:{ all -> 0x00a2 }
        r4 = CLASS_NAME;	 Catch:{ all -> 0x00a2 }
        r5 = "get";
        r6 = "623";
        r7 = 1;
        r7 = new java.lang.Object[r7];	 Catch:{ all -> 0x00a2 }
        r8 = 0;
        r9 = new java.lang.Integer;	 Catch:{ all -> 0x00a2 }
        r10 = r11.actualInFlight;	 Catch:{ all -> 0x00a2 }
        r9.<init>(r10);	 Catch:{ all -> 0x00a2 }
        r7[r8] = r9;	 Catch:{ all -> 0x00a2 }
        r2.fine(r4, r5, r6, r7);	 Catch:{ all -> 0x00a2 }
        goto L_0x0004;
    L_0x00e4:
        r2 = log;	 Catch:{ all -> 0x00a2 }
        r4 = CLASS_NAME;	 Catch:{ all -> 0x00a2 }
        r5 = "get";
        r6 = "622";
        r2.fine(r4, r5, r6);	 Catch:{ all -> 0x00a2 }
        goto L_0x0004;
    L_0x00f1:
        r2 = move-exception;
        goto L_0x0041;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.internal.ClientState.get():org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage");
    }

    public void setKeepAliveInterval(long interval) {
        this.keepAlive = interval;
    }

    public void notifySentBytes(int sentBytesCount) {
        if (sentBytesCount > 0) {
            this.lastOutboundActivity = System.currentTimeMillis();
        }
        Object[] objArr = new Object[MIN_MSG_ID];
        objArr[0] = new Integer(sentBytesCount);
        log.fine(CLASS_NAME, "notifySentBytes", "631", objArr);
    }

    protected void notifySent(MqttWireMessage message) {
        this.lastOutboundActivity = System.currentTimeMillis();
        Object[] objArr = new Object[MIN_MSG_ID];
        objArr[0] = message.getKey();
        log.fine(CLASS_NAME, "notifySent", "625", objArr);
        MqttToken token = this.tokenStore.getToken(message);
        token.internalTok.notifySent();
        if (message instanceof MqttPingReq) {
            synchronized (this.pingOutstandingLock) {
                long time = System.currentTimeMillis();
                synchronized (this.pingOutstandingLock) {
                    this.lastPing = time;
                    this.pingOutstanding += MIN_MSG_ID;
                }
                Object[] objArr2 = new Object[MIN_MSG_ID];
                objArr2[0] = new Integer(this.pingOutstanding);
                log.fine(CLASS_NAME, "notifySent", "635", objArr2);
            }
        } else if ((message instanceof MqttPublish) && ((MqttPublish) message).getMessage().getQos() == 0) {
            token.internalTok.markComplete(null, null);
            this.callback.asyncOperationComplete(token);
            decrementInFlight();
            releaseMessageId(message.getMessageId());
            this.tokenStore.removeToken(message);
            checkQuiesceLock();
        }
    }

    private void decrementInFlight() {
        synchronized (this.queueLock) {
            this.actualInFlight--;
            Object[] objArr = new Object[MIN_MSG_ID];
            objArr[0] = new Integer(this.actualInFlight);
            log.fine(CLASS_NAME, "decrementInFlight", "646", objArr);
            if (!checkQuiesceLock()) {
                this.queueLock.notifyAll();
            }
        }
    }

    protected boolean checkQuiesceLock() {
        int tokC = this.tokenStore.count();
        if (!this.quiescing || tokC != 0 || this.pendingFlows.size() != 0 || !this.callback.isQuiesced()) {
            return false;
        }
        log.fine(CLASS_NAME, "checkQuiesceLock", "626", new Object[]{new Boolean(this.quiescing), new Integer(this.actualInFlight), new Integer(this.pendingFlows.size()), new Integer(this.inFlightPubRels), Boolean.valueOf(this.callback.isQuiesced()), new Integer(tokC)});
        synchronized (this.quiesceLock) {
            this.quiesceLock.notifyAll();
        }
        return true;
    }

    public void notifyReceivedBytes(int receivedBytesCount) {
        if (receivedBytesCount > 0) {
            this.lastInboundActivity = System.currentTimeMillis();
        }
        Object[] objArr = new Object[MIN_MSG_ID];
        objArr[0] = new Integer(receivedBytesCount);
        log.fine(CLASS_NAME, "notifyReceivedBytes", "630", objArr);
    }

    protected void notifyReceivedAck(MqttAck ack) throws MqttException {
        this.lastInboundActivity = System.currentTimeMillis();
        log.fine(CLASS_NAME, "notifyReceivedAck", "627", new Object[]{new Integer(ack.getMessageId()), ack});
        MqttToken token = this.tokenStore.getToken((MqttWireMessage) ack);
        if (ack instanceof MqttPubRec) {
            send(new MqttPubRel((MqttPubRec) ack), token);
        } else if ((ack instanceof MqttPubAck) || (ack instanceof MqttPubComp)) {
            notifyResult(ack, token, null);
        } else if (ack instanceof MqttPingResp) {
            synchronized (this.pingOutstandingLock) {
                this.pingOutstanding = Math.max(0, this.pingOutstanding - 1);
                notifyResult(ack, token, null);
                if (this.pingOutstanding == 0) {
                    this.tokenStore.removeToken((MqttWireMessage) ack);
                }
            }
            Object[] objArr = new Object[MIN_MSG_ID];
            objArr[0] = new Integer(this.pingOutstanding);
            log.fine(CLASS_NAME, "notifyReceivedAck", "636", objArr);
        } else if (ack instanceof MqttConnack) {
            int rc = ((MqttConnack) ack).getReturnCode();
            if (rc == 0) {
                synchronized (this.queueLock) {
                    if (this.cleanSession) {
                        clearState();
                        this.tokenStore.saveToken(token, (MqttWireMessage) ack);
                    }
                    this.inFlightPubRels = 0;
                    this.actualInFlight = 0;
                    restoreInflightMessages();
                    connected();
                }
                this.clientComms.connectComplete((MqttConnack) ack, null);
                notifyResult(ack, token, null);
                this.tokenStore.removeToken((MqttWireMessage) ack);
                synchronized (this.queueLock) {
                    this.queueLock.notifyAll();
                }
            } else {
                throw ExceptionHelper.createMqttException(rc);
            }
        } else {
            notifyResult(ack, token, null);
            releaseMessageId(ack.getMessageId());
            this.tokenStore.removeToken((MqttWireMessage) ack);
        }
        checkQuiesceLock();
    }

    protected void notifyReceivedMsg(MqttWireMessage message) throws MqttException {
        this.lastInboundActivity = System.currentTimeMillis();
        log.fine(CLASS_NAME, "notifyReceivedMsg", "651", new Object[]{new Integer(message.getMessageId()), message});
        if (!this.quiescing) {
            if (message instanceof MqttPublish) {
                MqttPublish send = (MqttPublish) message;
                switch (send.getMessage().getQos()) {
                    case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    case MIN_MSG_ID /*1*/:
                        if (this.callback != null) {
                            this.callback.messageArrived(send);
                        }
                    case Logger.WARNING /*2*/:
                        this.persistence.put(getReceivedPersistenceKey(message), (MqttPublish) message);
                        this.inboundQoS2.put(new Integer(send.getMessageId()), send);
                        send(new MqttPubRec(send), null);
                    default:
                }
            } else if (message instanceof MqttPubRel) {
                MqttPublish sendMsg = (MqttPublish) this.inboundQoS2.get(new Integer(message.getMessageId()));
                if (sendMsg == null) {
                    send(new MqttPubComp(message.getMessageId()), null);
                } else if (this.callback != null) {
                    this.callback.messageArrived(sendMsg);
                }
            }
        }
    }

    protected void notifyComplete(MqttToken token) throws MqttException {
        MqttWireMessage message = token.internalTok.getWireMessage();
        if (message != null && (message instanceof MqttAck)) {
            log.fine(CLASS_NAME, "notifyComplete", "629", new Object[]{new Integer(message.getMessageId()), token, message});
            MqttAck ack = (MqttAck) message;
            if (ack instanceof MqttPubAck) {
                this.persistence.remove(getSendPersistenceKey(message));
                this.outboundQoS1.remove(new Integer(ack.getMessageId()));
                decrementInFlight();
                releaseMessageId(message.getMessageId());
                this.tokenStore.removeToken(message);
                Object[] objArr = new Object[MIN_MSG_ID];
                objArr[0] = new Integer(ack.getMessageId());
                log.fine(CLASS_NAME, "notifyComplete", "650", objArr);
            } else if (ack instanceof MqttPubComp) {
                this.persistence.remove(getSendPersistenceKey(message));
                this.persistence.remove(getSendConfirmPersistenceKey(message));
                this.outboundQoS2.remove(new Integer(ack.getMessageId()));
                this.inFlightPubRels--;
                decrementInFlight();
                releaseMessageId(message.getMessageId());
                this.tokenStore.removeToken(message);
                log.fine(CLASS_NAME, "notifyComplete", "645", new Object[]{new Integer(ack.getMessageId()), new Integer(this.inFlightPubRels)});
            }
            checkQuiesceLock();
        }
    }

    protected void notifyResult(MqttWireMessage ack, MqttToken token, MqttException ex) {
        token.internalTok.markComplete(ack, ex);
        if (!(ack == null || !(ack instanceof MqttAck) || (ack instanceof MqttPubRec))) {
            log.fine(CLASS_NAME, "notifyResult", "648", new Object[]{token.internalTok.getKey(), ack, ex});
            this.callback.asyncOperationComplete(token);
        }
        if (ack == null) {
            log.fine(CLASS_NAME, "notifyResult", "649", new Object[]{token.internalTok.getKey(), ex});
            this.callback.asyncOperationComplete(token);
        }
    }

    public void connected() {
        log.fine(CLASS_NAME, "connected", "631");
        this.connected = true;
        this.pingSender.start();
    }

    public Vector resolveOldTokens(MqttException reason) {
        Object[] objArr = new Object[MIN_MSG_ID];
        objArr[0] = reason;
        log.fine(CLASS_NAME, "resolveOldTokens", "632", objArr);
        MqttException shutReason = reason;
        if (reason == null) {
            shutReason = new MqttException(32102);
        }
        Vector outT = this.tokenStore.getOutstandingTokens();
        Enumeration outTE = outT.elements();
        while (outTE.hasMoreElements()) {
            MqttToken tok = (MqttToken) outTE.nextElement();
            synchronized (tok) {
                if (!(tok.isComplete() || tok.internalTok.isCompletePending() || tok.getException() != null)) {
                    tok.internalTok.setException(shutReason);
                }
            }
            if (!(tok instanceof MqttDeliveryToken)) {
                this.tokenStore.removeToken(tok.internalTok.getKey());
            }
        }
        return outT;
    }

    public void disconnected(MqttException reason) {
        Object[] objArr = new Object[MIN_MSG_ID];
        objArr[0] = reason;
        log.fine(CLASS_NAME, "disconnected", "633", objArr);
        this.connected = false;
        try {
            if (this.cleanSession) {
                clearState();
            }
            this.pendingMessages.clear();
            this.pendingFlows.clear();
            synchronized (this.pingOutstandingLock) {
                this.pingOutstanding = 0;
            }
        } catch (MqttException e) {
        }
    }

    private synchronized void releaseMessageId(int msgId) {
        this.inUseMsgIds.remove(new Integer(msgId));
    }

    private synchronized int getNextMessageId() throws MqttException {
        int startingMessageId = this.nextMsgId;
        int loopCount = 0;
        do {
            this.nextMsgId += MIN_MSG_ID;
            if (this.nextMsgId > MAX_MSG_ID) {
                this.nextMsgId = MIN_MSG_ID;
            }
            if (this.nextMsgId == startingMessageId) {
                loopCount += MIN_MSG_ID;
                if (loopCount == 2) {
                    throw ExceptionHelper.createMqttException(32001);
                }
            }
        } while (this.inUseMsgIds.containsKey(new Integer(this.nextMsgId)));
        Integer id = new Integer(this.nextMsgId);
        this.inUseMsgIds.put(id, id);
        return this.nextMsgId;
    }

    public void quiesce(long timeout) {
        if (timeout > 0) {
            Object[] objArr = new Object[MIN_MSG_ID];
            objArr[0] = new Long(timeout);
            log.fine(CLASS_NAME, "quiesce", "637", objArr);
            synchronized (this.queueLock) {
                this.quiescing = true;
            }
            this.callback.quiesce();
            notifyQueueLock();
            synchronized (this.quiesceLock) {
                try {
                    if (this.tokenStore.count() > 0 || this.pendingFlows.size() > 0 || !this.callback.isQuiesced()) {
                        log.fine(CLASS_NAME, "quiesce", "639", new Object[]{new Integer(this.actualInFlight), new Integer(this.pendingFlows.size()), new Integer(this.inFlightPubRels), new Integer(tokc)});
                        this.quiesceLock.wait(timeout);
                    }
                } catch (InterruptedException e) {
                }
            }
            synchronized (this.queueLock) {
                this.pendingMessages.clear();
                this.pendingFlows.clear();
                this.quiescing = false;
                this.actualInFlight = 0;
            }
            log.fine(CLASS_NAME, "quiesce", "640");
        }
    }

    public void notifyQueueLock() {
        synchronized (this.queueLock) {
            log.fine(CLASS_NAME, "notifyQueueLock", "638");
            this.queueLock.notifyAll();
        }
    }

    protected void deliveryComplete(MqttPublish message) throws MqttPersistenceException {
        Object[] objArr = new Object[MIN_MSG_ID];
        objArr[0] = new Integer(message.getMessageId());
        log.fine(CLASS_NAME, "deliveryComplete", "641", objArr);
        this.persistence.remove(getReceivedPersistenceKey(message));
        this.inboundQoS2.remove(new Integer(message.getMessageId()));
    }

    protected void close() {
        this.inUseMsgIds.clear();
        this.pendingMessages.clear();
        this.pendingFlows.clear();
        this.outboundQoS2.clear();
        this.outboundQoS1.clear();
        this.inboundQoS2.clear();
        this.tokenStore.clear();
        this.inUseMsgIds = null;
        this.pendingMessages = null;
        this.pendingFlows = null;
        this.outboundQoS2 = null;
        this.outboundQoS1 = null;
        this.inboundQoS2 = null;
        this.tokenStore = null;
        this.callback = null;
        this.clientComms = null;
        this.persistence = null;
        this.pingCommand = null;
    }

    public Properties getDebug() {
        Properties props = new Properties();
        props.put("In use msgids", this.inUseMsgIds);
        props.put("pendingMessages", this.pendingMessages);
        props.put("pendingFlows", this.pendingFlows);
        props.put("maxInflight", new Integer(this.maxInflight));
        props.put("nextMsgID", new Integer(this.nextMsgId));
        props.put("actualInFlight", new Integer(this.actualInFlight));
        props.put("inFlightPubRels", new Integer(this.inFlightPubRels));
        props.put("quiescing", Boolean.valueOf(this.quiescing));
        props.put("pingoutstanding", new Integer(this.pingOutstanding));
        props.put("lastOutboundActivity", new Long(this.lastOutboundActivity));
        props.put("lastInboundActivity", new Long(this.lastInboundActivity));
        props.put("outboundQoS2", this.outboundQoS2);
        props.put("outboundQoS1", this.outboundQoS1);
        props.put("inboundQoS2", this.inboundQoS2);
        props.put("tokens", this.tokenStore);
        return props;
    }
}
