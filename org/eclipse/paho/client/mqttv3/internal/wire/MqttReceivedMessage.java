package org.eclipse.paho.client.mqttv3.internal.wire;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttReceivedMessage extends MqttMessage {
    private int messageId;

    public void setMessageId(int msgId) {
        this.messageId = msgId;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public void setDuplicate(boolean value) {
        super.setDuplicate(value);
    }
}
