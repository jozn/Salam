package org.eclipse.paho.client.mqttv3;

public class MqttMessage {
    private boolean dup;
    private boolean mutable;
    private byte[] payload;
    private int qos;
    private boolean retained;

    public static void validateQos(int qos) {
        if (qos < 0 || qos > 2) {
            throw new IllegalArgumentException();
        }
    }

    public MqttMessage() {
        this.mutable = true;
        this.qos = 1;
        this.retained = false;
        this.dup = false;
        setPayload(new byte[0]);
    }

    public MqttMessage(byte[] payload) {
        this.mutable = true;
        this.qos = 1;
        this.retained = false;
        this.dup = false;
        setPayload(payload);
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public void clearPayload() {
        checkMutable();
        this.payload = new byte[0];
    }

    public void setPayload(byte[] payload) {
        checkMutable();
        if (payload == null) {
            throw new NullPointerException();
        }
        this.payload = payload;
    }

    public boolean isRetained() {
        return this.retained;
    }

    public void setRetained(boolean retained) {
        checkMutable();
        this.retained = retained;
    }

    public int getQos() {
        return this.qos;
    }

    public void setQos(int qos) {
        checkMutable();
        validateQos(qos);
        this.qos = qos;
    }

    public String toString() {
        return new String(this.payload);
    }

    protected void setMutable(boolean mutable) {
        this.mutable = mutable;
    }

    protected void checkMutable() throws IllegalStateException {
        if (!this.mutable) {
            throw new IllegalStateException();
        }
    }

    public void setDuplicate(boolean dup) {
        this.dup = dup;
    }

    public boolean isDuplicate() {
        return this.dup;
    }
}
