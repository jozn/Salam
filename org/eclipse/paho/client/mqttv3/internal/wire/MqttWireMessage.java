package org.eclipse.paho.client.mqttv3.internal.wire;

import android.support.v4.media.TransportMediator;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistable;
import org.eclipse.paho.client.mqttv3.internal.ExceptionHelper;

public abstract class MqttWireMessage {
    public static final byte MESSAGE_TYPE_CONNACK = (byte) 2;
    public static final byte MESSAGE_TYPE_CONNECT = (byte) 1;
    public static final byte MESSAGE_TYPE_DISCONNECT = (byte) 14;
    public static final byte MESSAGE_TYPE_PINGREQ = (byte) 12;
    public static final byte MESSAGE_TYPE_PINGRESP = (byte) 13;
    public static final byte MESSAGE_TYPE_PUBACK = (byte) 4;
    public static final byte MESSAGE_TYPE_PUBCOMP = (byte) 7;
    public static final byte MESSAGE_TYPE_PUBLISH = (byte) 3;
    public static final byte MESSAGE_TYPE_PUBREC = (byte) 5;
    public static final byte MESSAGE_TYPE_PUBREL = (byte) 6;
    public static final byte MESSAGE_TYPE_SUBACK = (byte) 9;
    public static final byte MESSAGE_TYPE_SUBSCRIBE = (byte) 8;
    public static final byte MESSAGE_TYPE_UNSUBACK = (byte) 11;
    public static final byte MESSAGE_TYPE_UNSUBSCRIBE = (byte) 10;
    private static final String[] PACKET_NAMES;
    protected static final String STRING_ENCODING = "UTF-8";
    protected boolean duplicate;
    protected int msgId;
    private byte type;

    protected abstract byte getMessageInfo();

    protected abstract byte[] getVariableHeader() throws MqttException;

    static {
        PACKET_NAMES = new String[]{"reserved", "CONNECT", "CONNACK", "PUBLISH", "PUBACK", "PUBREC", "PUBREL", "PUBCOMP", "SUBSCRIBE", "SUBACK", "UNSUBSCRIBE", "UNSUBACK", "PINGREQ", "PINGRESP", "DISCONNECT"};
    }

    public MqttWireMessage(byte type) {
        this.duplicate = false;
        this.type = type;
        this.msgId = 0;
    }

    public byte[] getPayload() throws MqttException {
        return new byte[0];
    }

    public byte getType() {
        return this.type;
    }

    public int getMessageId() {
        return this.msgId;
    }

    public void setMessageId(int msgId) {
        this.msgId = msgId;
    }

    public String getKey() {
        return new Integer(getMessageId()).toString();
    }

    public byte[] getHeader() throws MqttException {
        try {
            int first = ((getType() & 15) << 4) ^ (getMessageInfo() & 15);
            byte[] varHeader = getVariableHeader();
            int remLen = varHeader.length + getPayload().length;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(first);
            dos.write(encodeMBI((long) remLen));
            dos.write(varHeader);
            dos.flush();
            return baos.toByteArray();
        } catch (Throwable ioe) {
            throw new MqttException(ioe);
        }
    }

    public boolean isMessageIdRequired() {
        return true;
    }

    public static MqttWireMessage createWireMessage(MqttPersistable data) throws MqttException {
        byte[] payload = data.getPayloadBytes();
        if (payload == null) {
            payload = new byte[0];
        }
        return createWireMessage(new MultiByteArrayInputStream(data.getHeaderBytes(), data.getHeaderOffset(), data.getHeaderLength(), payload, data.getPayloadOffset(), data.getPayloadLength()));
    }

    public static MqttWireMessage createWireMessage(byte[] bytes) throws MqttException {
        return createWireMessage(new ByteArrayInputStream(bytes));
    }

    private static MqttWireMessage createWireMessage(InputStream inputStream) throws MqttException {
        try {
            CountingInputStream counter = new CountingInputStream(inputStream);
            DataInputStream in = new DataInputStream(counter);
            int first = in.readUnsignedByte();
            byte type = (byte) (first >> 4);
            byte info = (byte) (first & 15);
            long remLen = readMBI(in).getValue();
            long remainder = (((long) counter.getCounter()) + remLen) - ((long) counter.getCounter());
            byte[] data = new byte[0];
            if (remainder > 0) {
                data = new byte[((int) remainder)];
                in.readFully(data, 0, data.length);
            }
            if (type == 1) {
                return new MqttConnect(info, data);
            }
            if (type == 3) {
                return new MqttPublish(info, data);
            }
            if (type == 4) {
                return new MqttPubAck(info, data);
            }
            if (type == 7) {
                return new MqttPubComp(info, data);
            }
            if (type == 2) {
                return new MqttConnack(info, data);
            }
            if (type == 12) {
                return new MqttPingReq(info, data);
            }
            if (type == 13) {
                return new MqttPingResp(info, data);
            }
            if (type == 8) {
                return new MqttSubscribe(info, data);
            }
            if (type == 9) {
                return new MqttSuback(info, data);
            }
            if (type == 10) {
                return new MqttUnsubscribe(info, data);
            }
            if (type == 11) {
                return new MqttUnsubAck(info, data);
            }
            if (type == 6) {
                return new MqttPubRel(info, data);
            }
            if (type == 5) {
                return new MqttPubRec(info, data);
            }
            if (type == 14) {
                return new MqttDisconnect(info, data);
            }
            throw ExceptionHelper.createMqttException(6);
        } catch (Throwable io) {
            throw new MqttException(io);
        }
    }

    protected static byte[] encodeMBI(long number) {
        int numBytes = 0;
        long no = number;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        do {
            byte digit = (byte) ((int) (no % 128));
            no /= 128;
            if (no > 0) {
                digit = (byte) (digit | AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            }
            bos.write(digit);
            numBytes++;
            if (no <= 0) {
                break;
            }
        } while (numBytes < 4);
        return bos.toByteArray();
    }

    protected static MultiByteInteger readMBI(DataInputStream in) throws IOException {
        long msgLength = 0;
        int multiplier = 1;
        int count = 0;
        byte digit;
        do {
            digit = in.readByte();
            count++;
            msgLength += (long) ((digit & TransportMediator.KEYCODE_MEDIA_PAUSE) * multiplier);
            multiplier *= AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
        } while ((digit & AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS) != 0);
        return new MultiByteInteger(msgLength, count);
    }

    protected byte[] encodeMessageId() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeShort(this.msgId);
            dos.flush();
            return baos.toByteArray();
        } catch (Throwable ex) {
            throw new MqttException(ex);
        }
    }

    public boolean isRetryable() {
        return false;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    protected void encodeUTF8(DataOutputStream dos, String stringToEncode) throws MqttException {
        try {
            byte[] encodedString = stringToEncode.getBytes(STRING_ENCODING);
            byte byte2 = (byte) ((encodedString.length >>> 0) & MotionEventCompat.ACTION_MASK);
            dos.write((byte) ((encodedString.length >>> 8) & MotionEventCompat.ACTION_MASK));
            dos.write(byte2);
            dos.write(encodedString);
        } catch (Throwable ex) {
            throw new MqttException(ex);
        } catch (Throwable ex2) {
            throw new MqttException(ex2);
        }
    }

    protected String decodeUTF8(DataInputStream input) throws MqttException {
        try {
            byte[] encodedString = new byte[input.readUnsignedShort()];
            input.readFully(encodedString);
            return new String(encodedString, STRING_ENCODING);
        } catch (Throwable ex) {
            throw new MqttException(ex);
        }
    }

    public String toString() {
        return PACKET_NAMES[this.type];
    }
}
