package cz.msebera.android.httpclient.extras;

public class Base64 {
    static final /* synthetic */ boolean $assertionsDisabled;

    static abstract class Coder {
        public int op;
        public byte[] output;

        Coder() {
        }
    }

    static class Decoder extends Coder {
        private static final int[] DECODE;
        private static final int[] DECODE_WEBSAFE;
        final int[] alphabet;
        int state;
        int value;

        static {
            DECODE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
            DECODE_WEBSAFE = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        }

        public Decoder(byte[] output) {
            this.output = output;
            this.alphabet = DECODE;
            this.state = 0;
            this.value = 0;
        }
    }

    static class Encoder extends Coder {
        static final /* synthetic */ boolean $assertionsDisabled;
        private static final byte[] ENCODE;
        private static final byte[] ENCODE_WEBSAFE;
        final byte[] alphabet;
        int count;
        public final boolean do_cr;
        public final boolean do_newline;
        public final boolean do_padding;
        final byte[] tail;
        int tailLen;

        static {
            boolean z;
            if (Base64.class.desiredAssertionStatus()) {
                z = false;
            } else {
                z = true;
            }
            $assertionsDisabled = z;
            ENCODE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
            ENCODE_WEBSAFE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
        }

        public Encoder() {
            this.output = null;
            this.do_padding = true;
            this.do_newline = false;
            this.do_cr = false;
            this.alphabet = ENCODE;
            this.tail = new byte[2];
            this.tailLen = 0;
            this.count = this.do_newline ? 19 : -1;
        }
    }

    static {
        $assertionsDisabled = !Base64.class.desiredAssertionStatus();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] decode$1cf9d9ca(byte[] r14) {
        /*
        r13 = -2;
        r12 = -1;
        r11 = 6;
        r3 = 0;
        r0 = r14.length;
        r6 = new cz.msebera.android.httpclient.extras.Base64$Decoder;
        r1 = r0 * 3;
        r1 = r1 / 4;
        r1 = new byte[r1];
        r6.<init>(r1);
        r1 = r6.state;
        if (r1 != r11) goto L_0x001f;
    L_0x0014:
        r0 = r3;
    L_0x0015:
        if (r0 != 0) goto L_0x013c;
    L_0x0017:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "bad base-64";
        r0.<init>(r1);
        throw r0;
    L_0x001f:
        r7 = r0 + 0;
        r0 = r6.state;
        r1 = r6.value;
        r8 = r6.output;
        r9 = r6.alphabet;
        r4 = r0;
        r2 = r3;
        r0 = r3;
    L_0x002c:
        if (r2 >= r7) goto L_0x010f;
    L_0x002e:
        if (r4 != 0) goto L_0x0075;
    L_0x0030:
        r5 = r2 + 4;
        if (r5 > r7) goto L_0x0073;
    L_0x0034:
        r1 = r14[r2];
        r1 = r1 & 255;
        r1 = r9[r1];
        r1 = r1 << 18;
        r5 = r2 + 1;
        r5 = r14[r5];
        r5 = r5 & 255;
        r5 = r9[r5];
        r5 = r5 << 12;
        r1 = r1 | r5;
        r5 = r2 + 2;
        r5 = r14[r5];
        r5 = r5 & 255;
        r5 = r9[r5];
        r5 = r5 << 6;
        r1 = r1 | r5;
        r5 = r2 + 3;
        r5 = r14[r5];
        r5 = r5 & 255;
        r5 = r9[r5];
        r1 = r1 | r5;
        if (r1 < 0) goto L_0x0073;
    L_0x005d:
        r5 = r0 + 2;
        r10 = (byte) r1;
        r8[r5] = r10;
        r5 = r0 + 1;
        r10 = r1 >> 8;
        r10 = (byte) r10;
        r8[r5] = r10;
        r5 = r1 >> 16;
        r5 = (byte) r5;
        r8[r0] = r5;
        r0 = r0 + 3;
        r2 = r2 + 4;
        goto L_0x0030;
    L_0x0073:
        if (r2 >= r7) goto L_0x010f;
    L_0x0075:
        r5 = r2 + 1;
        r2 = r14[r2];
        r2 = r2 & 255;
        r2 = r9[r2];
        switch(r4) {
            case 0: goto L_0x0082;
            case 1: goto L_0x0090;
            case 2: goto L_0x00a1;
            case 3: goto L_0x00c1;
            case 4: goto L_0x00f9;
            case 5: goto L_0x0108;
            default: goto L_0x0080;
        };
    L_0x0080:
        r2 = r5;
        goto L_0x002c;
    L_0x0082:
        if (r2 < 0) goto L_0x008a;
    L_0x0084:
        r1 = r4 + 1;
        r4 = r1;
        r1 = r2;
        r2 = r5;
        goto L_0x002c;
    L_0x008a:
        if (r2 == r12) goto L_0x0080;
    L_0x008c:
        r6.state = r11;
        r0 = r3;
        goto L_0x0015;
    L_0x0090:
        if (r2 < 0) goto L_0x009a;
    L_0x0092:
        r1 = r1 << 6;
        r1 = r1 | r2;
        r2 = r4 + 1;
        r4 = r2;
        r2 = r5;
        goto L_0x002c;
    L_0x009a:
        if (r2 == r12) goto L_0x0080;
    L_0x009c:
        r6.state = r11;
        r0 = r3;
        goto L_0x0015;
    L_0x00a1:
        if (r2 < 0) goto L_0x00ab;
    L_0x00a3:
        r1 = r1 << 6;
        r1 = r1 | r2;
        r2 = r4 + 1;
        r4 = r2;
        r2 = r5;
        goto L_0x002c;
    L_0x00ab:
        if (r2 != r13) goto L_0x00ba;
    L_0x00ad:
        r2 = r0 + 1;
        r4 = r1 >> 4;
        r4 = (byte) r4;
        r8[r0] = r4;
        r0 = 4;
        r4 = r0;
        r0 = r2;
        r2 = r5;
        goto L_0x002c;
    L_0x00ba:
        if (r2 == r12) goto L_0x0080;
    L_0x00bc:
        r6.state = r11;
        r0 = r3;
        goto L_0x0015;
    L_0x00c1:
        if (r2 < 0) goto L_0x00dd;
    L_0x00c3:
        r1 = r1 << 6;
        r1 = r1 | r2;
        r2 = r0 + 2;
        r4 = (byte) r1;
        r8[r2] = r4;
        r2 = r0 + 1;
        r4 = r1 >> 8;
        r4 = (byte) r4;
        r8[r2] = r4;
        r2 = r1 >> 16;
        r2 = (byte) r2;
        r8[r0] = r2;
        r0 = r0 + 3;
        r4 = r3;
        r2 = r5;
        goto L_0x002c;
    L_0x00dd:
        if (r2 != r13) goto L_0x00f2;
    L_0x00df:
        r2 = r0 + 1;
        r4 = r1 >> 2;
        r4 = (byte) r4;
        r8[r2] = r4;
        r2 = r1 >> 10;
        r2 = (byte) r2;
        r8[r0] = r2;
        r0 = r0 + 2;
        r2 = 5;
        r4 = r2;
        r2 = r5;
        goto L_0x002c;
    L_0x00f2:
        if (r2 == r12) goto L_0x0080;
    L_0x00f4:
        r6.state = r11;
        r0 = r3;
        goto L_0x0015;
    L_0x00f9:
        if (r2 != r13) goto L_0x0101;
    L_0x00fb:
        r2 = r4 + 1;
        r4 = r2;
        r2 = r5;
        goto L_0x002c;
    L_0x0101:
        if (r2 == r12) goto L_0x0080;
    L_0x0103:
        r6.state = r11;
        r0 = r3;
        goto L_0x0015;
    L_0x0108:
        if (r2 == r12) goto L_0x0080;
    L_0x010a:
        r6.state = r11;
        r0 = r3;
        goto L_0x0015;
    L_0x010f:
        r2 = r1;
        switch(r4) {
            case 0: goto L_0x0113;
            case 1: goto L_0x011a;
            case 2: goto L_0x011f;
            case 3: goto L_0x0128;
            case 4: goto L_0x0137;
            default: goto L_0x0113;
        };
    L_0x0113:
        r6.state = r4;
        r6.op = r0;
        r0 = 1;
        goto L_0x0015;
    L_0x011a:
        r6.state = r11;
        r0 = r3;
        goto L_0x0015;
    L_0x011f:
        r1 = r0 + 1;
        r2 = r2 >> 4;
        r2 = (byte) r2;
        r8[r0] = r2;
        r0 = r1;
        goto L_0x0113;
    L_0x0128:
        r1 = r0 + 1;
        r5 = r2 >> 10;
        r5 = (byte) r5;
        r8[r0] = r5;
        r0 = r1 + 1;
        r2 = r2 >> 2;
        r2 = (byte) r2;
        r8[r1] = r2;
        goto L_0x0113;
    L_0x0137:
        r6.state = r11;
        r0 = r3;
        goto L_0x0015;
    L_0x013c:
        r0 = r6.op;
        r1 = r6.output;
        r1 = r1.length;
        if (r0 != r1) goto L_0x0146;
    L_0x0143:
        r0 = r6.output;
    L_0x0145:
        return r0;
    L_0x0146:
        r0 = r6.op;
        r0 = new byte[r0];
        r1 = r6.output;
        r2 = r6.op;
        java.lang.System.arraycopy(r1, r3, r0, r3, r2);
        goto L_0x0145;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.extras.Base64.decode$1cf9d9ca(byte[]):byte[]");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] encode$1cf9d9ca(byte[] r12) {
        /*
        r4 = r12.length;
        r7 = new cz.msebera.android.httpclient.extras.Base64$Encoder;
        r7.<init>();
        r0 = r4 / 3;
        r0 = r0 * 4;
        r1 = r7.do_padding;
        if (r1 == 0) goto L_0x00d3;
    L_0x000e:
        r1 = r4 % 3;
        if (r1 <= 0) goto L_0x0014;
    L_0x0012:
        r0 = r0 + 4;
    L_0x0014:
        r1 = r7.do_newline;
        if (r1 == 0) goto L_0x0027;
    L_0x0018:
        if (r4 <= 0) goto L_0x0027;
    L_0x001a:
        r1 = r4 + -1;
        r1 = r1 / 57;
        r2 = r1 + 1;
        r1 = r7.do_cr;
        if (r1 == 0) goto L_0x00e2;
    L_0x0024:
        r1 = 2;
    L_0x0025:
        r1 = r1 * r2;
        r0 = r0 + r1;
    L_0x0027:
        r1 = new byte[r0];
        r7.output = r1;
        r8 = r7.alphabet;
        r9 = r7.output;
        r5 = 0;
        r2 = r7.count;
        r3 = 0;
        r10 = r4 + 0;
        r1 = -1;
        r4 = r7.tailLen;
        switch(r4) {
            case 0: goto L_0x00e5;
            case 1: goto L_0x00e8;
            case 2: goto L_0x0109;
            default: goto L_0x003b;
        };
    L_0x003b:
        r4 = r1;
    L_0x003c:
        r1 = -1;
        if (r4 == r1) goto L_0x0257;
    L_0x003f:
        r1 = 0;
        r5 = r4 >> 18;
        r5 = r5 & 63;
        r5 = r8[r5];
        r9[r1] = r5;
        r1 = 1;
        r5 = r4 >> 12;
        r5 = r5 & 63;
        r5 = r8[r5];
        r9[r1] = r5;
        r1 = 2;
        r5 = r4 >> 6;
        r5 = r5 & 63;
        r5 = r8[r5];
        r9[r1] = r5;
        r5 = 3;
        r1 = 4;
        r4 = r4 & 63;
        r4 = r8[r4];
        r9[r5] = r4;
        r2 = r2 + -1;
        if (r2 != 0) goto L_0x0253;
    L_0x0066:
        r2 = r7.do_cr;
        if (r2 == 0) goto L_0x0070;
    L_0x006a:
        r2 = 4;
        r1 = 5;
        r4 = 13;
        r9[r2] = r4;
    L_0x0070:
        r5 = r1 + 1;
        r2 = 10;
        r9[r1] = r2;
        r1 = 19;
        r6 = r1;
    L_0x0079:
        r1 = r3 + 3;
        if (r1 > r10) goto L_0x012a;
    L_0x007d:
        r1 = r12[r3];
        r1 = r1 & 255;
        r1 = r1 << 16;
        r2 = r3 + 1;
        r2 = r12[r2];
        r2 = r2 & 255;
        r2 = r2 << 8;
        r1 = r1 | r2;
        r2 = r3 + 2;
        r2 = r12[r2];
        r2 = r2 & 255;
        r1 = r1 | r2;
        r2 = r1 >> 18;
        r2 = r2 & 63;
        r2 = r8[r2];
        r9[r5] = r2;
        r2 = r5 + 1;
        r4 = r1 >> 12;
        r4 = r4 & 63;
        r4 = r8[r4];
        r9[r2] = r4;
        r2 = r5 + 2;
        r4 = r1 >> 6;
        r4 = r4 & 63;
        r4 = r8[r4];
        r9[r2] = r4;
        r2 = r5 + 3;
        r1 = r1 & 63;
        r1 = r8[r1];
        r9[r2] = r1;
        r3 = r3 + 3;
        r2 = r5 + 4;
        r1 = r6 + -1;
        if (r1 != 0) goto L_0x024f;
    L_0x00bf:
        r1 = r7.do_cr;
        if (r1 == 0) goto L_0x024c;
    L_0x00c3:
        r1 = r2 + 1;
        r4 = 13;
        r9[r2] = r4;
    L_0x00c9:
        r5 = r1 + 1;
        r2 = 10;
        r9[r1] = r2;
        r1 = 19;
        r6 = r1;
        goto L_0x0079;
    L_0x00d3:
        r1 = r4 % 3;
        switch(r1) {
            case 0: goto L_0x0014;
            case 1: goto L_0x00da;
            case 2: goto L_0x00de;
            default: goto L_0x00d8;
        };
    L_0x00d8:
        goto L_0x0014;
    L_0x00da:
        r0 = r0 + 2;
        goto L_0x0014;
    L_0x00de:
        r0 = r0 + 3;
        goto L_0x0014;
    L_0x00e2:
        r1 = 1;
        goto L_0x0025;
    L_0x00e5:
        r4 = r1;
        goto L_0x003c;
    L_0x00e8:
        r4 = 2;
        if (r4 > r10) goto L_0x003b;
    L_0x00eb:
        r1 = r7.tail;
        r3 = 0;
        r1 = r1[r3];
        r1 = r1 & 255;
        r1 = r1 << 16;
        r3 = 0;
        r3 = r12[r3];
        r3 = r3 & 255;
        r3 = r3 << 8;
        r1 = r1 | r3;
        r4 = 1;
        r3 = 2;
        r4 = r12[r4];
        r4 = r4 & 255;
        r1 = r1 | r4;
        r4 = 0;
        r7.tailLen = r4;
        r4 = r1;
        goto L_0x003c;
    L_0x0109:
        if (r10 <= 0) goto L_0x003b;
    L_0x010b:
        r1 = r7.tail;
        r3 = 0;
        r1 = r1[r3];
        r1 = r1 & 255;
        r1 = r1 << 16;
        r3 = r7.tail;
        r4 = 1;
        r3 = r3[r4];
        r3 = r3 & 255;
        r3 = r3 << 8;
        r1 = r1 | r3;
        r4 = 0;
        r3 = 1;
        r4 = r12[r4];
        r4 = r4 & 255;
        r1 = r1 | r4;
        r4 = 0;
        r7.tailLen = r4;
        goto L_0x003b;
    L_0x012a:
        r1 = r7.tailLen;
        r1 = r3 - r1;
        r2 = r10 + -1;
        if (r1 != r2) goto L_0x0193;
    L_0x0132:
        r2 = 0;
        r1 = r7.tailLen;
        if (r1 <= 0) goto L_0x018d;
    L_0x0137:
        r1 = r7.tail;
        r4 = 0;
        r2 = 1;
        r1 = r1[r4];
    L_0x013d:
        r1 = r1 & 255;
        r4 = r1 << 4;
        r1 = r7.tailLen;
        r1 = r1 - r2;
        r7.tailLen = r1;
        r2 = r5 + 1;
        r1 = r4 >> 6;
        r1 = r1 & 63;
        r1 = r8[r1];
        r9[r5] = r1;
        r1 = r2 + 1;
        r4 = r4 & 63;
        r4 = r8[r4];
        r9[r2] = r4;
        r2 = r7.do_padding;
        if (r2 == 0) goto L_0x0168;
    L_0x015c:
        r2 = r1 + 1;
        r4 = 61;
        r9[r1] = r4;
        r1 = r2 + 1;
        r4 = 61;
        r9[r2] = r4;
    L_0x0168:
        r2 = r7.do_newline;
        if (r2 == 0) goto L_0x017e;
    L_0x016c:
        r2 = r7.do_cr;
        if (r2 == 0) goto L_0x0177;
    L_0x0170:
        r2 = r1 + 1;
        r4 = 13;
        r9[r1] = r4;
        r1 = r2;
    L_0x0177:
        r2 = r1 + 1;
        r4 = 10;
        r9[r1] = r4;
        r1 = r2;
    L_0x017e:
        r5 = r1;
    L_0x017f:
        r1 = cz.msebera.android.httpclient.extras.Base64.Encoder.$assertionsDisabled;
        if (r1 != 0) goto L_0x0227;
    L_0x0183:
        r1 = r7.tailLen;
        if (r1 == 0) goto L_0x0227;
    L_0x0187:
        r0 = new java.lang.AssertionError;
        r0.<init>();
        throw r0;
    L_0x018d:
        r4 = r3 + 1;
        r1 = r12[r3];
        r3 = r4;
        goto L_0x013d;
    L_0x0193:
        r1 = r7.tailLen;
        r1 = r3 - r1;
        r2 = r10 + -2;
        if (r1 != r2) goto L_0x020b;
    L_0x019b:
        r2 = 0;
        r1 = r7.tailLen;
        r4 = 1;
        if (r1 <= r4) goto L_0x01ff;
    L_0x01a1:
        r1 = r7.tail;
        r4 = 0;
        r2 = 1;
        r1 = r1[r4];
    L_0x01a7:
        r1 = r1 & 255;
        r11 = r1 << 10;
        r1 = r7.tailLen;
        if (r1 <= 0) goto L_0x0205;
    L_0x01af:
        r1 = r7.tail;
        r4 = r2 + 1;
        r1 = r1[r2];
        r2 = r4;
    L_0x01b6:
        r1 = r1 & 255;
        r1 = r1 << 2;
        r1 = r1 | r11;
        r4 = r7.tailLen;
        r2 = r4 - r2;
        r7.tailLen = r2;
        r2 = r5 + 1;
        r4 = r1 >> 12;
        r4 = r4 & 63;
        r4 = r8[r4];
        r9[r5] = r4;
        r4 = r2 + 1;
        r5 = r1 >> 6;
        r5 = r5 & 63;
        r5 = r8[r5];
        r9[r2] = r5;
        r2 = r4 + 1;
        r1 = r1 & 63;
        r1 = r8[r1];
        r9[r4] = r1;
        r1 = r7.do_padding;
        if (r1 == 0) goto L_0x024a;
    L_0x01e1:
        r1 = r2 + 1;
        r4 = 61;
        r9[r2] = r4;
    L_0x01e7:
        r2 = r7.do_newline;
        if (r2 == 0) goto L_0x01fd;
    L_0x01eb:
        r2 = r7.do_cr;
        if (r2 == 0) goto L_0x01f6;
    L_0x01ef:
        r2 = r1 + 1;
        r4 = 13;
        r9[r1] = r4;
        r1 = r2;
    L_0x01f6:
        r2 = r1 + 1;
        r4 = 10;
        r9[r1] = r4;
        r1 = r2;
    L_0x01fd:
        r5 = r1;
        goto L_0x017f;
    L_0x01ff:
        r4 = r3 + 1;
        r1 = r12[r3];
        r3 = r4;
        goto L_0x01a7;
    L_0x0205:
        r4 = r3 + 1;
        r1 = r12[r3];
        r3 = r4;
        goto L_0x01b6;
    L_0x020b:
        r1 = r7.do_newline;
        if (r1 == 0) goto L_0x017f;
    L_0x020f:
        if (r5 <= 0) goto L_0x017f;
    L_0x0211:
        r1 = 19;
        if (r6 == r1) goto L_0x017f;
    L_0x0215:
        r1 = r7.do_cr;
        if (r1 == 0) goto L_0x0248;
    L_0x0219:
        r1 = r5 + 1;
        r2 = 13;
        r9[r5] = r2;
    L_0x021f:
        r5 = r1 + 1;
        r2 = 10;
        r9[r1] = r2;
        goto L_0x017f;
    L_0x0227:
        r1 = cz.msebera.android.httpclient.extras.Base64.Encoder.$assertionsDisabled;
        if (r1 != 0) goto L_0x0233;
    L_0x022b:
        if (r3 == r10) goto L_0x0233;
    L_0x022d:
        r0 = new java.lang.AssertionError;
        r0.<init>();
        throw r0;
    L_0x0233:
        r7.op = r5;
        r7.count = r6;
        r1 = $assertionsDisabled;
        if (r1 != 0) goto L_0x0245;
    L_0x023b:
        r1 = r7.op;
        if (r1 == r0) goto L_0x0245;
    L_0x023f:
        r0 = new java.lang.AssertionError;
        r0.<init>();
        throw r0;
    L_0x0245:
        r0 = r7.output;
        return r0;
    L_0x0248:
        r1 = r5;
        goto L_0x021f;
    L_0x024a:
        r1 = r2;
        goto L_0x01e7;
    L_0x024c:
        r1 = r2;
        goto L_0x00c9;
    L_0x024f:
        r6 = r1;
        r5 = r2;
        goto L_0x0079;
    L_0x0253:
        r6 = r2;
        r5 = r1;
        goto L_0x0079;
    L_0x0257:
        r6 = r2;
        goto L_0x0079;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.extras.Base64.encode$1cf9d9ca(byte[]):byte[]");
    }

    private Base64() {
    }
}
