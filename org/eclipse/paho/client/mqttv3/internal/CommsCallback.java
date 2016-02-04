package org.eclipse.paho.client.mqttv3.internal;

import java.util.Vector;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubAck;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPubComp;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class CommsCallback implements Runnable {
    private static final String CLASS_NAME;
    private static final int INBOUND_QUEUE_SIZE = 10;
    static Class class$0;
    private static final Logger log;
    private Thread callbackThread;
    private ClientComms clientComms;
    private ClientState clientState;
    private Vector completeQueue;
    private Object lifecycle;
    private Vector messageQueue;
    private MqttCallback mqttCallback;
    private boolean quiescing;
    public boolean running;
    private Object spaceAvailable;
    private Object workAvailable;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.CommsCallback");
                class$0 = cls;
            } catch (Throwable e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
        log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    }

    CommsCallback(ClientComms clientComms) {
        this.running = false;
        this.quiescing = false;
        this.lifecycle = new Object();
        this.workAvailable = new Object();
        this.spaceAvailable = new Object();
        this.clientComms = clientComms;
        this.messageQueue = new Vector(INBOUND_QUEUE_SIZE);
        this.completeQueue = new Vector(INBOUND_QUEUE_SIZE);
        log.setResourceName(clientComms.getClient().getClientId());
    }

    public void setClientState(ClientState clientState) {
        this.clientState = clientState;
    }

    public void start(String threadName) {
        synchronized (this.lifecycle) {
            if (!this.running) {
                this.messageQueue.clear();
                this.completeQueue.clear();
                this.running = true;
                this.quiescing = false;
                this.callbackThread = new Thread(this, threadName);
                this.callbackThread.start();
            }
        }
    }

    public void stop() {
        synchronized (this.lifecycle) {
            if (this.running) {
                log.fine(CLASS_NAME, "stop", "700");
                this.running = false;
                if (!Thread.currentThread().equals(this.callbackThread)) {
                    try {
                        synchronized (this.workAvailable) {
                            log.fine(CLASS_NAME, "stop", "701");
                            this.workAvailable.notifyAll();
                        }
                        this.callbackThread.join();
                    } catch (InterruptedException e) {
                    }
                }
            }
            this.callbackThread = null;
            log.fine(CLASS_NAME, "stop", "703");
        }
    }

    public void setCallback(MqttCallback mqttCallback) {
        this.mqttCallback = mqttCallback;
    }

    public void run() {
        while (this.running) {
            try {
                synchronized (this.workAvailable) {
                    if (this.running && this.messageQueue.isEmpty() && this.completeQueue.isEmpty()) {
                        log.fine(CLASS_NAME, "run", "704");
                        this.workAvailable.wait();
                    }
                }
            } catch (InterruptedException e) {
            }
            try {
                if (this.running) {
                    MqttToken token = null;
                    synchronized (this.completeQueue) {
                        if (!this.completeQueue.isEmpty()) {
                            token = (MqttToken) this.completeQueue.elementAt(0);
                            this.completeQueue.removeElementAt(0);
                        }
                    }
                    if (token != null) {
                        handleActionComplete(token);
                    }
                    MqttPublish message = null;
                    synchronized (this.messageQueue) {
                        if (!this.messageQueue.isEmpty()) {
                            message = (MqttPublish) this.messageQueue.elementAt(0);
                            this.messageQueue.removeElementAt(0);
                        }
                    }
                    if (message != null) {
                        handleMessage(message);
                    }
                }
                if (this.quiescing) {
                    this.clientState.checkQuiesceLock();
                }
            } catch (Throwable ex) {
                try {
                    log.fine(CLASS_NAME, "run", "714", null, ex);
                    this.running = false;
                    this.clientComms.shutdownConnection(null, new MqttException(ex));
                } catch (Throwable th) {
                    synchronized (this.spaceAvailable) {
                    }
                    log.fine(CLASS_NAME, "run", "706");
                    this.spaceAvailable.notifyAll();
                }
            }
            synchronized (this.spaceAvailable) {
                log.fine(CLASS_NAME, "run", "706");
                this.spaceAvailable.notifyAll();
            }
        }
    }

    private void handleActionComplete(MqttToken token) throws MqttException {
        synchronized (token) {
            log.fine(CLASS_NAME, "handleActionComplete", "705", new Object[]{token.internalTok.getKey()});
            token.internalTok.notifyComplete();
            if (!token.internalTok.isNotified()) {
                if (this.mqttCallback != null && (token instanceof MqttDeliveryToken) && token.isComplete()) {
                    this.mqttCallback.deliveryComplete((MqttDeliveryToken) token);
                }
                fireActionEvent(token);
            }
            if (token.isComplete() && ((token instanceof MqttDeliveryToken) || (token.getActionCallback() instanceof IMqttActionListener))) {
                token.internalTok.setNotified(true);
            }
            if (token.isComplete()) {
                this.clientState.notifyComplete(token);
            }
        }
    }

    public void connectionLost(MqttException cause) {
        try {
            if (this.mqttCallback != null && cause != null) {
                log.fine(CLASS_NAME, "connectionLost", "708", new Object[]{cause});
                this.mqttCallback.connectionLost(cause);
            }
        } catch (Throwable t) {
            log.fine(CLASS_NAME, "connectionLost", "720", new Object[]{t});
        }
    }

    public void fireActionEvent(MqttToken token) {
        if (token != null) {
            IMqttActionListener asyncCB = token.getActionCallback();
            if (asyncCB == null) {
                return;
            }
            if (token.getException() == null) {
                log.fine(CLASS_NAME, "fireActionEvent", "716", new Object[]{token.internalTok.getKey()});
                asyncCB.onSuccess(token);
                return;
            }
            log.fine(CLASS_NAME, "fireActionEvent", "716", new Object[]{token.internalTok.getKey()});
            asyncCB.onFailure(token, token.getException());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void messageArrived(org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish r6) {
        /*
        r5 = this;
        r0 = r5.mqttCallback;
        if (r0 == 0) goto L_0x0037;
    L_0x0004:
        r1 = r5.spaceAvailable;
        monitor-enter(r1);
    L_0x0007:
        r0 = r5.running;	 Catch:{ all -> 0x004d }
        if (r0 == 0) goto L_0x0019;
    L_0x000b:
        r0 = r5.quiescing;	 Catch:{ all -> 0x004d }
        if (r0 != 0) goto L_0x0019;
    L_0x000f:
        r0 = r5.messageQueue;	 Catch:{ all -> 0x004d }
        r0 = r0.size();	 Catch:{ all -> 0x004d }
        r2 = 10;
        if (r0 >= r2) goto L_0x0038;
    L_0x0019:
        monitor-exit(r1);	 Catch:{ all -> 0x004d }
        r0 = r5.quiescing;
        if (r0 != 0) goto L_0x0037;
    L_0x001e:
        r0 = r5.messageQueue;
        r0.addElement(r6);
        r1 = r5.workAvailable;
        monitor-enter(r1);
        r0 = log;	 Catch:{ all -> 0x0050 }
        r2 = CLASS_NAME;	 Catch:{ all -> 0x0050 }
        r3 = "messageArrived";
        r4 = "710";
        r0.fine(r2, r3, r4);	 Catch:{ all -> 0x0050 }
        r0 = r5.workAvailable;	 Catch:{ all -> 0x0050 }
        r0.notifyAll();	 Catch:{ all -> 0x0050 }
        monitor-exit(r1);	 Catch:{ all -> 0x0050 }
    L_0x0037:
        return;
    L_0x0038:
        r0 = log;	 Catch:{ InterruptedException -> 0x004b }
        r2 = CLASS_NAME;	 Catch:{ InterruptedException -> 0x004b }
        r3 = "messageArrived";
        r4 = "709";
        r0.fine(r2, r3, r4);	 Catch:{ InterruptedException -> 0x004b }
        r0 = r5.spaceAvailable;	 Catch:{ InterruptedException -> 0x004b }
        r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        r0.wait(r2);	 Catch:{ InterruptedException -> 0x004b }
        goto L_0x0007;
    L_0x004b:
        r0 = move-exception;
        goto L_0x0007;
    L_0x004d:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x004d }
        throw r0;
    L_0x0050:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0050 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.internal.CommsCallback.messageArrived(org.eclipse.paho.client.mqttv3.internal.wire.MqttPublish):void");
    }

    public void quiesce() {
        this.quiescing = true;
        synchronized (this.spaceAvailable) {
            log.fine(CLASS_NAME, "quiesce", "711");
            this.spaceAvailable.notifyAll();
        }
    }

    public boolean isQuiesced() {
        if (this.quiescing && this.completeQueue.size() == 0 && this.messageQueue.size() == 0) {
            return true;
        }
        return false;
    }

    private void handleMessage(MqttPublish publishMessage) throws MqttException, Exception {
        if (this.mqttCallback != null) {
            log.fine(CLASS_NAME, "handleMessage", "713", new Object[]{new Integer(publishMessage.getMessageId()), publishMessage.getTopicName()});
            this.mqttCallback.messageArrived(destName, publishMessage.getMessage());
            if (publishMessage.getMessage().getQos() == 1) {
                this.clientComms.internalSend(new MqttPubAck(publishMessage), new MqttToken(this.clientComms.getClient().getClientId()));
            } else if (publishMessage.getMessage().getQos() == 2) {
                this.clientComms.deliveryComplete(publishMessage);
                this.clientComms.internalSend(new MqttPubComp(publishMessage), new MqttToken(this.clientComms.getClient().getClientId()));
            }
        }
    }

    public void asyncOperationComplete(MqttToken token) {
        if (this.running) {
            this.completeQueue.addElement(token);
            synchronized (this.workAvailable) {
                log.fine(CLASS_NAME, "asyncOperationComplete", "715", new Object[]{token.internalTok.getKey()});
                this.workAvailable.notifyAll();
            }
            return;
        }
        try {
            handleActionComplete(token);
        } catch (Throwable ex) {
            log.fine(CLASS_NAME, "asyncOperationComplete", "719", null, ex);
            this.clientComms.shutdownConnection(null, new MqttException(ex));
        }
    }

    protected Thread getThread() {
        return this.callbackThread;
    }
}
