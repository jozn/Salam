package org.eclipse.paho.client.mqttv3.internal.wire;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.ClientState;
import org.eclipse.paho.client.mqttv3.internal.ExceptionHelper;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

public class MqttInputStream extends InputStream {
    private static final String CLASS_NAME;
    static Class class$0;
    private static final Logger log;
    private ClientState clientState;
    private DataInputStream in;

    static {
        Class cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.internal.wire.MqttInputStream");
                class$0 = cls;
            } catch (Throwable e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        CLASS_NAME = cls.getName();
        log = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, CLASS_NAME);
    }

    public MqttInputStream(ClientState clientState, InputStream in) {
        this.clientState = null;
        this.clientState = clientState;
        this.in = new DataInputStream(in);
    }

    public int read() throws IOException {
        return this.in.read();
    }

    public int available() throws IOException {
        return this.in.available();
    }

    public void close() throws IOException {
        this.in.close();
    }

    public MqttWireMessage readMqttWireMessage() throws IOException, MqttException {
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        byte first = this.in.readByte();
        this.clientState.notifyReceivedBytes(1);
        byte type = (byte) ((first >>> 4) & 15);
        if (type <= null || type > 14) {
            throw ExceptionHelper.createMqttException(32108);
        }
        long remLen = MqttWireMessage.readMBI(this.in).getValue();
        bais.write(first);
        bais.write(MqttWireMessage.encodeMBI(remLen));
        byte[] packet = new byte[((int) (((long) bais.size()) + remLen))];
        readFully(packet, bais.size(), packet.length - bais.size());
        byte[] header = bais.toByteArray();
        System.arraycopy(header, 0, packet, 0, header.length);
        log.fine(CLASS_NAME, "readMqttWireMessage", "501", new Object[]{MqttWireMessage.createWireMessage(packet)});
        return MqttWireMessage.createWireMessage(packet);
    }

    private void readFully(byte[] b, int off, int len) throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        int n = 0;
        while (n < len) {
            int count = this.in.read(b, off + n, len - n);
            this.clientState.notifyReceivedBytes(count);
            if (count < 0) {
                throw new EOFException();
            }
            n += count;
        }
    }
}
