package com.squareup.okhttp.internal.spdy;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v7.appcompat.C0170R;
import java.io.IOException;
import java.io.OutputStream;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;

class Huffman {
    private static final int[] CODES;
    private static final byte[] CODE_LENGTHS;
    private static final Huffman INSTANCE;
    private final Node root;

    private static final class Node {
        private final Node[] children;
        private final int symbol;
        private final int terminalBits;

        Node() {
            this.children = new Node[AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY];
            this.symbol = 0;
            this.terminalBits = 0;
        }

        Node(int symbol, int bits) {
            this.children = null;
            this.symbol = symbol;
            int b = bits & 7;
            if (b == 0) {
                b = 8;
            }
            this.terminalBits = b;
        }
    }

    static {
        CODES = new int[]{8184, 8388568, 268435426, 268435427, 268435428, 268435429, 268435430, 268435431, 268435432, 16777194, 1073741820, 268435433, 268435434, 1073741821, 268435435, 268435436, 268435437, 268435438, 268435439, 268435440, 268435441, 268435442, 1073741822, 268435443, 268435444, 268435445, 268435446, 268435447, 268435448, 268435449, 268435450, 268435451, 20, 1016, 1017, 4090, 8185, 21, 248, 2042, 1018, 1019, 249, 2043, 250, 22, 23, 24, 0, 1, 2, 25, 26, 27, 28, 29, 30, 31, 92, 251, 32764, 32, 4091, 1020, 8186, 33, 93, 94, 95, 96, 97, 98, 99, 100, C0170R.styleable.Theme_buttonStyleSmall, C0170R.styleable.Theme_checkboxStyle, C0170R.styleable.Theme_checkedTextViewStyle, C0170R.styleable.Theme_editTextStyle, C0170R.styleable.Theme_radioButtonStyle, C0170R.styleable.Theme_ratingBarStyle, C0170R.styleable.Theme_seekBarStyle, C0170R.styleable.Theme_spinnerStyle, C0170R.styleable.Theme_switchStyle, 110, 111, 112, 113, 114, 252, 115, 253, 8187, 524272, 8188, 16380, 34, 32765, 3, 35, 4, 36, 5, 37, 38, 39, 6, 116, 117, 40, 41, 42, 7, 43, 118, 44, 8, 9, 45, 119, 120, 121, 122, 123, 32766, 2044, 16381, 8189, 268435452, 1048550, 4194258, 1048551, 1048552, 4194259, 4194260, 4194261, 8388569, 4194262, 8388570, 8388571, 8388572, 8388573, 8388574, 16777195, 8388575, 16777196, 16777197, 4194263, 8388576, 16777198, 8388577, 8388578, 8388579, 8388580, 2097116, 4194264, 8388581, 4194265, 8388582, 8388583, 16777199, 4194266, 2097117, 1048553, 4194267, 4194268, 8388584, 8388585, 2097118, 8388586, 4194269, 4194270, 16777200, 2097119, 4194271, 8388587, 8388588, 2097120, 2097121, 4194272, 2097122, 8388589, 4194273, 8388590, 8388591, 1048554, 4194274, 4194275, 4194276, 8388592, 4194277, 4194278, 8388593, 67108832, 67108833, 1048555, 524273, 4194279, 8388594, 4194280, 33554412, 67108834, 67108835, 67108836, 134217694, 134217695, 67108837, 16777201, 33554413, 524274, 2097123, 67108838, 134217696, 134217697, 67108839, 134217698, 16777202, 2097124, 2097125, 67108840, 67108841, 268435453, 134217699, 134217700, 134217701, 1048556, 16777203, 1048557, 2097126, 4194281, 2097127, 2097128, 8388595, 4194282, 4194283, 33554414, 33554415, 16777204, 16777205, 67108842, 8388596, 67108843, 134217702, 67108844, 67108845, 134217703, 134217704, 134217705, 134217706, 134217707, 268435454, 134217708, 134217709, 134217710, 134217711, 134217712, 67108846};
        CODE_LENGTHS = new byte[]{MqttWireMessage.MESSAGE_TYPE_PINGRESP, (byte) 23, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 24, (byte) 30, (byte) 28, (byte) 28, (byte) 30, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 30, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 28, (byte) 6, (byte) 10, (byte) 10, MqttWireMessage.MESSAGE_TYPE_PINGREQ, MqttWireMessage.MESSAGE_TYPE_PINGRESP, (byte) 6, (byte) 8, MqttWireMessage.MESSAGE_TYPE_UNSUBACK, (byte) 10, (byte) 10, (byte) 8, MqttWireMessage.MESSAGE_TYPE_UNSUBACK, (byte) 8, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 5, (byte) 5, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 7, (byte) 8, (byte) 15, (byte) 6, MqttWireMessage.MESSAGE_TYPE_PINGREQ, (byte) 10, MqttWireMessage.MESSAGE_TYPE_PINGRESP, (byte) 6, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 8, (byte) 7, (byte) 8, MqttWireMessage.MESSAGE_TYPE_PINGRESP, (byte) 19, MqttWireMessage.MESSAGE_TYPE_PINGRESP, MqttWireMessage.MESSAGE_TYPE_DISCONNECT, (byte) 6, (byte) 15, (byte) 5, (byte) 6, (byte) 5, (byte) 6, (byte) 5, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 7, (byte) 7, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 6, (byte) 7, (byte) 6, (byte) 5, (byte) 5, (byte) 6, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 7, (byte) 15, MqttWireMessage.MESSAGE_TYPE_UNSUBACK, MqttWireMessage.MESSAGE_TYPE_DISCONNECT, MqttWireMessage.MESSAGE_TYPE_PINGRESP, (byte) 28, (byte) 20, (byte) 22, (byte) 20, (byte) 20, (byte) 22, (byte) 22, (byte) 22, (byte) 23, (byte) 22, (byte) 23, (byte) 23, (byte) 23, (byte) 23, (byte) 23, (byte) 24, (byte) 23, (byte) 24, (byte) 24, (byte) 22, (byte) 23, (byte) 24, (byte) 23, (byte) 23, (byte) 23, (byte) 23, (byte) 21, (byte) 22, (byte) 23, (byte) 22, (byte) 23, (byte) 23, (byte) 24, (byte) 22, (byte) 21, (byte) 20, (byte) 22, (byte) 22, (byte) 23, (byte) 23, (byte) 21, (byte) 23, (byte) 22, (byte) 22, (byte) 24, (byte) 21, (byte) 22, (byte) 23, (byte) 23, (byte) 21, (byte) 21, (byte) 22, (byte) 21, (byte) 23, (byte) 22, (byte) 23, (byte) 23, (byte) 20, (byte) 22, (byte) 22, (byte) 22, (byte) 23, (byte) 22, (byte) 22, (byte) 23, (byte) 26, (byte) 26, (byte) 20, (byte) 19, (byte) 22, (byte) 23, (byte) 22, (byte) 25, (byte) 26, (byte) 26, (byte) 26, (byte) 27, (byte) 27, (byte) 26, (byte) 24, (byte) 25, (byte) 19, (byte) 21, (byte) 26, (byte) 27, (byte) 27, (byte) 26, (byte) 27, (byte) 24, (byte) 21, (byte) 21, (byte) 26, (byte) 26, (byte) 28, (byte) 27, (byte) 27, (byte) 27, (byte) 20, (byte) 24, (byte) 20, (byte) 21, (byte) 22, (byte) 21, (byte) 21, (byte) 23, (byte) 22, (byte) 22, (byte) 25, (byte) 25, (byte) 24, (byte) 24, (byte) 26, (byte) 23, (byte) 26, (byte) 27, (byte) 26, (byte) 26, (byte) 27, (byte) 27, (byte) 27, (byte) 27, (byte) 27, (byte) 28, (byte) 27, (byte) 27, (byte) 27, (byte) 27, (byte) 27, (byte) 26};
        INSTANCE = new Huffman();
    }

    public static Huffman get() {
        return INSTANCE;
    }

    private Huffman() {
        this.root = new Node();
        buildTree();
    }

    void encode(byte[] data, OutputStream out) throws IOException {
        long current = 0;
        int n = 0;
        for (byte b : data) {
            int b2 = b & MotionEventCompat.ACTION_MASK;
            int code = CODES[b2];
            int nbits = CODE_LENGTHS[b2];
            current = (current << nbits) | ((long) code);
            n += nbits;
            while (n >= 8) {
                n -= 8;
                out.write((int) (current >> n));
            }
        }
        if (n > 0) {
            out.write((int) ((current << (8 - n)) | ((long) (MotionEventCompat.ACTION_MASK >>> n))));
        }
    }

    int encodedLength(byte[] bytes) {
        long len = 0;
        for (byte b : bytes) {
            len += (long) CODE_LENGTHS[b & MotionEventCompat.ACTION_MASK];
        }
        return (int) ((7 + len) >> 3);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    byte[] decode(byte[] r9) throws java.io.IOException {
        /*
        r8 = this;
        r1 = new java.io.ByteArrayOutputStream;
        r1.<init>();
        r6 = r8.root;
        r3 = 0;
        r5 = 0;
        r4 = 0;
    L_0x000a:
        r7 = r9.length;
        if (r4 >= r7) goto L_0x0042;
    L_0x000d:
        r7 = r9[r4];
        r0 = r7 & 255;
        r7 = r3 << 8;
        r3 = r7 | r0;
        r5 = r5 + 8;
    L_0x0017:
        r7 = 8;
        if (r5 < r7) goto L_0x003f;
    L_0x001b:
        r7 = r5 + -8;
        r7 = r3 >>> r7;
        r2 = r7 & 255;
        r7 = r6.children;
        r6 = r7[r2];
        r7 = r6.children;
        if (r7 != 0) goto L_0x003c;
    L_0x002d:
        r7 = r6.symbol;
        r1.write(r7);
        r7 = r6.terminalBits;
        r5 = r5 - r7;
        r6 = r8.root;
        goto L_0x0017;
    L_0x003c:
        r5 = r5 + -8;
        goto L_0x0017;
    L_0x003f:
        r4 = r4 + 1;
        goto L_0x000a;
    L_0x0042:
        if (r5 <= 0) goto L_0x006b;
    L_0x0044:
        r7 = 8 - r5;
        r7 = r3 << r7;
        r2 = r7 & 255;
        r7 = r6.children;
        r6 = r7[r2];
        r7 = r6.children;
        if (r7 != 0) goto L_0x006b;
    L_0x0056:
        r7 = r6.terminalBits;
        if (r7 > r5) goto L_0x006b;
    L_0x005c:
        r7 = r6.symbol;
        r1.write(r7);
        r7 = r6.terminalBits;
        r5 = r5 - r7;
        r6 = r8.root;
        goto L_0x0042;
    L_0x006b:
        r7 = r1.toByteArray();
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.spdy.Huffman.decode(byte[]):byte[]");
    }

    private void buildTree() {
        for (int i = 0; i < CODE_LENGTHS.length; i++) {
            addCode(i, CODES[i], CODE_LENGTHS[i]);
        }
    }

    private void addCode(int sym, int code, byte len) {
        Node terminal = new Node(sym, len);
        Node current = this.root;
        while (len > 8) {
            len = (byte) (len - 8);
            int i = (code >>> len) & MotionEventCompat.ACTION_MASK;
            if (current.children == null) {
                throw new IllegalStateException("invalid dictionary: prefix not unique");
            }
            if (current.children[i] == null) {
                current.children[i] = new Node();
            }
            current = current.children[i];
        }
        int shift = 8 - len;
        int start = (code << shift) & MotionEventCompat.ACTION_MASK;
        int end = 1 << shift;
        for (i = start; i < start + end; i++) {
            current.children[i] = terminal;
        }
    }
}
