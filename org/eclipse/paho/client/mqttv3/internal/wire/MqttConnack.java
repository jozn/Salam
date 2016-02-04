package org.eclipse.paho.client.mqttv3.internal.wire;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttConnack extends MqttAck {
    public static final String KEY = "Con";
    private int returnCode;
    private boolean sessionPresent;

    public MqttConnack(byte info, byte[] variableHeader) throws IOException {
        boolean z = true;
        super((byte) 2);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(variableHeader));
        if ((dis.readUnsignedByte() & 1) != 1) {
            z = false;
        }
        this.sessionPresent = z;
        this.returnCode = dis.readUnsignedByte();
        dis.close();
    }

    public int getReturnCode() {
        return this.returnCode;
    }

    protected byte[] getVariableHeader() throws MqttException {
        return new byte[0];
    }

    public boolean isMessageIdRequired() {
        return false;
    }

    public String getKey() {
        return KEY;
    }

    public String toString() {
        return new StringBuffer(String.valueOf(super.toString())).append(" session present:").append(this.sessionPresent).append(" return code: ").append(this.returnCode).toString();
    }

    public boolean getSessionPresent() {
        return this.sessionPresent;
    }
}
