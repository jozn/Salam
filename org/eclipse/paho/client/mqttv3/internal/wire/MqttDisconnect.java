package org.eclipse.paho.client.mqttv3.internal.wire;

import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttDisconnect extends MqttWireMessage {
    public static final String KEY = "Disc";

    public MqttDisconnect() {
        super(MqttWireMessage.MESSAGE_TYPE_DISCONNECT);
    }

    public MqttDisconnect(byte info, byte[] variableHeader) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_DISCONNECT);
    }

    protected byte getMessageInfo() {
        return (byte) 0;
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
}
