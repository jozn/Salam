package org.eclipse.paho.client.mqttv3.internal.wire;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttConnect extends MqttWireMessage {
    public static final String KEY = "Con";
    private int MqttVersion;
    private boolean cleanSession;
    private String clientId;
    private int keepAliveInterval;
    private char[] password;
    private String userName;
    private String willDestination;
    private MqttMessage willMessage;

    public MqttConnect(byte info, byte[] data) throws IOException, MqttException {
        super((byte) 1);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        decodeUTF8(dis);
        dis.readByte();
        dis.readByte();
        this.keepAliveInterval = dis.readUnsignedShort();
        this.clientId = decodeUTF8(dis);
        dis.close();
    }

    public MqttConnect(String clientId, int MqttVersion, boolean cleanSession, int keepAliveInterval, String userName, char[] password, MqttMessage willMessage, String willDestination) {
        super((byte) 1);
        this.clientId = clientId;
        this.cleanSession = cleanSession;
        this.keepAliveInterval = keepAliveInterval;
        this.userName = userName;
        this.password = password;
        this.willMessage = willMessage;
        this.willDestination = willDestination;
        this.MqttVersion = MqttVersion;
    }

    public String toString() {
        return new StringBuffer(String.valueOf(super.toString())).append(" clientId ").append(this.clientId).append(" keepAliveInterval ").append(this.keepAliveInterval).toString();
    }

    protected byte getMessageInfo() {
        return (byte) 0;
    }

    public boolean isCleanSession() {
        return this.cleanSession;
    }

    protected byte[] getVariableHeader() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            if (this.MqttVersion == 3) {
                encodeUTF8(dos, "MQIsdp");
            } else if (this.MqttVersion == 4) {
                encodeUTF8(dos, "MQTT");
            }
            dos.write(this.MqttVersion);
            byte connectFlags = (byte) 0;
            if (this.cleanSession) {
                connectFlags = (byte) 2;
            }
            if (this.willMessage != null) {
                connectFlags = (byte) (((byte) (connectFlags | 4)) | (this.willMessage.getQos() << 3));
                if (this.willMessage.isRetained()) {
                    connectFlags = (byte) (connectFlags | 32);
                }
            }
            if (this.userName != null) {
                connectFlags = (byte) (connectFlags | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
                if (this.password != null) {
                    connectFlags = (byte) (connectFlags | 64);
                }
            }
            dos.write(connectFlags);
            dos.writeShort(this.keepAliveInterval);
            dos.flush();
            return baos.toByteArray();
        } catch (Throwable ioe) {
            throw new MqttException(ioe);
        }
    }

    public byte[] getPayload() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            encodeUTF8(dos, this.clientId);
            if (this.willMessage != null) {
                encodeUTF8(dos, this.willDestination);
                dos.writeShort(this.willMessage.getPayload().length);
                dos.write(this.willMessage.getPayload());
            }
            if (this.userName != null) {
                encodeUTF8(dos, this.userName);
                if (this.password != null) {
                    encodeUTF8(dos, new String(this.password));
                }
            }
            dos.flush();
            return baos.toByteArray();
        } catch (Throwable ex) {
            throw new MqttException(ex);
        }
    }

    public boolean isMessageIdRequired() {
        return false;
    }

    public String getKey() {
        return KEY;
    }
}
