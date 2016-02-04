package org.eclipse.paho.client.mqttv3.internal;

import java.io.IOException;
import java.io.OutputStream;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttAck;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttDisconnect;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttOutputStream;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class CommsSender implements Runnable {
    private static final String CLASS_NAME;
    static Class class$0;
    private static final Logger log;
    private ClientComms clientComms;
    private ClientState clientState;
    private Object lifecycle;
    private MqttOutputStream out;
    private boolean running;
    private Thread sendThread;
    private CommsTokenStore tokenStore;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.CommsSender");
                class$0 = cls;
            } catch (Throwable e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
        log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    }

    public CommsSender(ClientComms clientComms, ClientState clientState, CommsTokenStore tokenStore, OutputStream out) {
        this.running = false;
        this.lifecycle = new Object();
        this.clientState = null;
        this.clientComms = null;
        this.tokenStore = null;
        this.sendThread = null;
        this.out = new MqttOutputStream(clientState, out);
        this.clientComms = clientComms;
        this.clientState = clientState;
        this.tokenStore = tokenStore;
        log.setResourceName(clientComms.getClient().getClientId());
    }

    public void start(String threadName) {
        synchronized (this.lifecycle) {
            if (!this.running) {
                this.running = true;
                this.sendThread = new Thread(this, threadName);
                this.sendThread.start();
            }
        }
    }

    public void stop() {
        synchronized (this.lifecycle) {
            log.fine(CLASS_NAME, "stop", "800");
            if (this.running) {
                this.running = false;
                if (!Thread.currentThread().equals(this.sendThread)) {
                    try {
                        this.clientState.notifyQueueLock();
                        this.sendThread.join();
                    } catch (InterruptedException e) {
                    }
                }
            }
            this.sendThread = null;
            log.fine(CLASS_NAME, "stop", "801");
        }
    }

    public void run() {
        MqttWireMessage message = null;
        while (this.running && this.out != null) {
            try {
                message = this.clientState.get();
                if (message != null) {
                    log.fine(CLASS_NAME, "run", "802", new Object[]{message.getKey(), message});
                    if (message instanceof MqttAck) {
                        this.out.write(message);
                        this.out.flush();
                    } else {
                        MqttToken token = this.tokenStore.getToken(message);
                        if (token != null) {
                            synchronized (token) {
                                this.out.write(message);
                                this.out.flush();
                                this.clientState.notifySent(message);
                            }
                        } else {
                            continue;
                        }
                    }
                } else {
                    log.fine(CLASS_NAME, "run", "803");
                    this.running = false;
                }
            } catch (IOException ex) {
                if (!(message instanceof MqttDisconnect)) {
                    throw ex;
                }
            } catch (MqttException me) {
                handleRunException(message, me);
            } catch (Exception ex2) {
                handleRunException(message, ex2);
            }
        }
        log.fine(CLASS_NAME, "run", "805");
    }

    private void handleRunException(MqttWireMessage message, Exception ex) {
        MqttException mex;
        log.fine(CLASS_NAME, "handleRunException", "804", null, ex);
        if (ex instanceof MqttException) {
            mex = (MqttException) ex;
        } else {
            mex = new MqttException(32109, ex);
        }
        this.running = false;
        this.clientComms.shutdownConnection(null, mex);
    }
}
