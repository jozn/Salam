package org.eclipse.paho.client.mqttv3.internal.wire;

import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttPingReq extends MqttWireMessage {
    public static final String KEY = "Ping";

    public MqttPingReq() {
        super(MqttWireMessage.MESSAGE_TYPE_PINGREQ);
    }

    public MqttPingReq(byte info, byte[] variableHeader) throws IOException {
        super(MqttWireMessage.MESSAGE_TYPE_PINGREQ);
    }

    public boolean isMessageIdRequired() {
        return false;
    }

    protected byte[] getVariableHeader() throws MqttException {
        return new byte[0];
    }

    protected byte getMessageInfo() {
        return (byte) 0;
    }

    public String getKey() {
        return KEY;
    }
}
