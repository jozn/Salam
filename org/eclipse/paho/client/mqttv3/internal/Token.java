package org.eclipse.paho.client.mqttv3.internal;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttAck;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnack;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttSuback;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class Token {
    private static final String CLASS_NAME;
    static Class class$0;
    private static final Logger log;
    private IMqttActionListener callback;
    private IMqttAsyncClient client;
    private volatile boolean completed;
    private MqttException exception;
    private String key;
    protected MqttMessage message;
    private int messageID;
    private boolean notified;
    private boolean pendingComplete;
    private MqttWireMessage response;
    private Object responseLock;
    private boolean sent;
    private Object sentLock;
    private String[] topics;
    private Object userContext;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.Token");
                class$0 = cls;
            } catch (Throwable e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
        log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    }

    public Token(String logContext) {
        this.completed = false;
        this.pendingComplete = false;
        this.sent = false;
        this.responseLock = new Object();
        this.sentLock = new Object();
        this.message = null;
        this.response = null;
        this.exception = null;
        this.topics = null;
        this.client = null;
        this.callback = null;
        this.userContext = null;
        this.messageID = 0;
        this.notified = false;
        log.setResourceName(logContext);
    }

    public int getMessageID() {
        return this.messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public boolean checkResult() throws MqttException {
        if (getException() == null) {
            return true;
        }
        throw getException();
    }

    public MqttException getException() {
        return this.exception;
    }

    public boolean isComplete() {
        return this.completed;
    }

    protected boolean isCompletePending() {
        return this.pendingComplete;
    }

    protected boolean isInUse() {
        return (getClient() == null || isComplete()) ? false : true;
    }

    public void setActionCallback(IMqttActionListener listener) {
        this.callback = listener;
    }

    public IMqttActionListener getActionCallback() {
        return this.callback;
    }

    public void waitForCompletion() throws MqttException {
        waitForCompletion(-1);
    }

    public void waitForCompletion(long timeout) throws MqttException {
        log.fine(CLASS_NAME, "waitForCompletion", "407", new Object[]{getKey(), new Long(timeout), this});
        if (waitForResponse(timeout) != null || this.completed) {
            checkResult();
            return;
        }
        log.fine(CLASS_NAME, "waitForCompletion", "406", new Object[]{getKey(), this});
        this.exception = new MqttException(32000);
        throw this.exception;
    }

    protected MqttWireMessage waitForResponse() throws MqttException {
        return waitForResponse(-1);
    }

    protected MqttWireMessage waitForResponse(long timeout) throws MqttException {
        synchronized (this.responseLock) {
            String str;
            Logger logger = log;
            String str2 = CLASS_NAME;
            String str3 = "waitForResponse";
            String str4 = "400";
            Object[] objArr = new Object[7];
            objArr[0] = getKey();
            objArr[1] = new Long(timeout);
            objArr[2] = new Boolean(this.sent);
            objArr[3] = new Boolean(this.completed);
            if (this.exception == null) {
                str = "false";
            } else {
                str = "true";
            }
            objArr[4] = str;
            objArr[5] = this.response;
            objArr[6] = this;
            logger.fine(str2, str3, str4, objArr, this.exception);
            while (!this.completed) {
                if (this.exception == null) {
                    try {
                        log.fine(CLASS_NAME, "waitForResponse", "408", new Object[]{getKey(), new Long(timeout)});
                        if (timeout <= 0) {
                            this.responseLock.wait();
                        } else {
                            this.responseLock.wait(timeout);
                        }
                    } catch (Throwable e) {
                        this.exception = new MqttException(e);
                    }
                }
                if (!this.completed) {
                    if (this.exception == null) {
                        if (timeout > 0) {
                            break;
                        }
                    } else {
                        log.fine(CLASS_NAME, "waitForResponse", "401", null, this.exception);
                        throw this.exception;
                    }
                }
            }
        }
        log.fine(CLASS_NAME, "waitForResponse", "402", new Object[]{getKey(), this.response});
        return this.response;
    }

    protected void markComplete(MqttWireMessage msg, MqttException ex) {
        log.fine(CLASS_NAME, "markComplete", "404", new Object[]{getKey(), msg, ex});
        synchronized (this.responseLock) {
            if (msg instanceof MqttAck) {
                this.message = null;
            }
            this.pendingComplete = true;
            this.response = msg;
            this.exception = ex;
        }
    }

    protected void notifyComplete() {
        log.fine(CLASS_NAME, "notifyComplete", "404", new Object[]{getKey(), this.response, this.exception});
        synchronized (this.responseLock) {
            if (this.exception == null && this.pendingComplete) {
                this.completed = true;
            }
            this.pendingComplete = false;
            this.responseLock.notifyAll();
        }
        synchronized (this.sentLock) {
            this.sent = true;
            this.sentLock.notifyAll();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void waitUntilSent() throws org.eclipse.paho.client.mqttv3.MqttException {
        /*
        r8 = this;
        r1 = r8.sentLock;
        monitor-enter(r1);
        r2 = r8.responseLock;	 Catch:{ all -> 0x0010 }
        monitor-enter(r2);	 Catch:{ all -> 0x0010 }
        r0 = r8.exception;	 Catch:{ all -> 0x000d }
        if (r0 == 0) goto L_0x0013;
    L_0x000a:
        r0 = r8.exception;	 Catch:{ all -> 0x000d }
        throw r0;	 Catch:{ all -> 0x000d }
    L_0x000d:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x000d }
        throw r0;	 Catch:{ all -> 0x0010 }
    L_0x0010:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0010 }
        throw r0;
    L_0x0013:
        monitor-exit(r2);	 Catch:{ all -> 0x000d }
    L_0x0014:
        r0 = r8.sent;	 Catch:{ all -> 0x0010 }
        if (r0 == 0) goto L_0x0026;
    L_0x0018:
        r0 = r8.sent;	 Catch:{ all -> 0x0010 }
        if (r0 != 0) goto L_0x0046;
    L_0x001c:
        r0 = r8.exception;	 Catch:{ all -> 0x0010 }
        if (r0 != 0) goto L_0x0043;
    L_0x0020:
        r0 = 6;
        r0 = org.eclipse.paho.client.mqttv3.internal.ExceptionHelper.createMqttException(r0);	 Catch:{ all -> 0x0010 }
        throw r0;	 Catch:{ all -> 0x0010 }
    L_0x0026:
        r0 = log;	 Catch:{ InterruptedException -> 0x0041 }
        r2 = CLASS_NAME;	 Catch:{ InterruptedException -> 0x0041 }
        r3 = "waitUntilSent";
        r4 = "409";
        r5 = 1;
        r5 = new java.lang.Object[r5];	 Catch:{ InterruptedException -> 0x0041 }
        r6 = 0;
        r7 = r8.getKey();	 Catch:{ InterruptedException -> 0x0041 }
        r5[r6] = r7;	 Catch:{ InterruptedException -> 0x0041 }
        r0.fine(r2, r3, r4, r5);	 Catch:{ InterruptedException -> 0x0041 }
        r0 = r8.sentLock;	 Catch:{ InterruptedException -> 0x0041 }
        r0.wait();	 Catch:{ InterruptedException -> 0x0041 }
        goto L_0x0014;
    L_0x0041:
        r0 = move-exception;
        goto L_0x0014;
    L_0x0043:
        r0 = r8.exception;	 Catch:{ all -> 0x0010 }
        throw r0;	 Catch:{ all -> 0x0010 }
    L_0x0046:
        monitor-exit(r1);	 Catch:{ all -> 0x0010 }
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.internal.Token.waitUntilSent():void");
    }

    protected void notifySent() {
        log.fine(CLASS_NAME, "notifySent", "403", new Object[]{getKey()});
        synchronized (this.responseLock) {
            this.response = null;
            this.completed = false;
        }
        synchronized (this.sentLock) {
            this.sent = true;
            this.sentLock.notifyAll();
        }
    }

    public IMqttAsyncClient getClient() {
        return this.client;
    }

    protected void setClient(IMqttAsyncClient client) {
        this.client = client;
    }

    public void reset() throws MqttException {
        if (isInUse()) {
            throw new MqttException(32201);
        }
        log.fine(CLASS_NAME, "reset", "410", new Object[]{getKey()});
        this.client = null;
        this.completed = false;
        this.response = null;
        this.sent = false;
        this.exception = null;
        this.userContext = null;
    }

    public MqttMessage getMessage() {
        return this.message;
    }

    public MqttWireMessage getWireMessage() {
        return this.response;
    }

    public void setMessage(MqttMessage msg) {
        this.message = msg;
    }

    public String[] getTopics() {
        return this.topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    public Object getUserContext() {
        return this.userContext;
    }

    public void setUserContext(Object userContext) {
        this.userContext = userContext;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public void setException(MqttException exception) {
        synchronized (this.responseLock) {
            this.exception = exception;
        }
    }

    public boolean isNotified() {
        return this.notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public String toString() {
        StringBuffer tok = new StringBuffer();
        tok.append("key=").append(getKey());
        tok.append(" ,topics=");
        if (getTopics() != null) {
            for (String append : getTopics()) {
                tok.append(append).append(", ");
            }
        }
        tok.append(" ,usercontext=").append(getUserContext());
        tok.append(" ,isComplete=").append(isComplete());
        tok.append(" ,isNotified=").append(isNotified());
        tok.append(" ,exception=").append(getException());
        tok.append(" ,actioncallback=").append(getActionCallback());
        return tok.toString();
    }

    public int[] getGrantedQos() {
        int[] val = new int[0];
        if (this.response instanceof MqttSuback) {
            return ((MqttSuback) this.response).getGrantedQos();
        }
        return val;
    }

    public boolean getSessionPresent() {
        if (this.response instanceof MqttConnack) {
            return ((MqttConnack) this.response).getSessionPresent();
        }
        return false;
    }

    public MqttWireMessage getResponse() {
        return this.response;
    }
}
