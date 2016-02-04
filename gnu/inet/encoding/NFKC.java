package gnu.inet.encoding;

import android.support.v4.view.MotionEventCompat;

public final class NFKC {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String normalizeNFKC(java.lang.String r14) {
        /*
        r13 = 144; // 0x90 float:2.02E-43 double:7.1E-322;
        r12 = 1;
        r11 = 44032; // 0xac00 float:6.1702E-41 double:2.17547E-319;
        r1 = 0;
        r5 = -1;
        r6 = new java.lang.StringBuffer;
        r6.<init>();
        r0 = r1;
    L_0x000e:
        r2 = r14.length();
        if (r0 < r2) goto L_0x0025;
    L_0x0014:
        canonicalOrdering(r6);
        r0 = r1;
        r2 = r1;
        r3 = r1;
    L_0x001a:
        r4 = r6.length();
        if (r0 < r4) goto L_0x007c;
    L_0x0020:
        r0 = r6.toString();
        return r0;
    L_0x0025:
        r2 = r14.charAt(r0);
        if (r2 < r11) goto L_0x006a;
    L_0x002b:
        r3 = 55215; // 0xd7af float:7.7373E-41 double:2.728E-319;
        if (r2 > r3) goto L_0x006a;
    L_0x0030:
        r3 = r2 - r11;
        if (r3 < 0) goto L_0x0038;
    L_0x0034:
        r4 = 11172; // 0x2ba4 float:1.5655E-41 double:5.5197E-320;
        if (r3 < r4) goto L_0x0042;
    L_0x0038:
        r2 = java.lang.String.valueOf(r2);
    L_0x003c:
        r6.append(r2);
    L_0x003f:
        r0 = r0 + 1;
        goto L_0x000e;
    L_0x0042:
        r2 = new java.lang.StringBuffer;
        r2.<init>();
        r4 = r3 / 588;
        r4 = r4 + 4352;
        r7 = r3 % 588;
        r7 = r7 / 28;
        r7 = r7 + 4449;
        r3 = r3 % 28;
        r3 = r3 + 4519;
        r4 = (char) r4;
        r2.append(r4);
        r4 = (char) r7;
        r2.append(r4);
        r4 = 4519; // 0x11a7 float:6.332E-42 double:2.2327E-320;
        if (r3 == r4) goto L_0x0065;
    L_0x0061:
        r3 = (char) r3;
        r2.append(r3);
    L_0x0065:
        r2 = r2.toString();
        goto L_0x003c;
    L_0x006a:
        r3 = decomposeIndex(r2);
        if (r3 != r5) goto L_0x0074;
    L_0x0070:
        r6.append(r2);
        goto L_0x003f;
    L_0x0074:
        r2 = gnu.inet.encoding.DecompositionMappings.f37m;
        r2 = r2[r3];
        r6.append(r2);
        goto L_0x003f;
    L_0x007c:
        r4 = r6.charAt(r0);
        r4 = combiningClass(r4);
        if (r0 <= 0) goto L_0x0139;
    L_0x0086:
        if (r3 == 0) goto L_0x008a;
    L_0x0088:
        if (r3 == r4) goto L_0x0139;
    L_0x008a:
        r7 = r6.charAt(r2);
        r8 = r6.charAt(r0);
        r3 = r7 + -4352;
        if (r3 < 0) goto L_0x00bc;
    L_0x0096:
        r9 = 19;
        if (r3 >= r9) goto L_0x00bc;
    L_0x009a:
        r9 = r8 + -4449;
        if (r9 < 0) goto L_0x00bc;
    L_0x009e:
        r10 = 21;
        if (r9 >= r10) goto L_0x00bc;
    L_0x00a2:
        r3 = r3 * 21;
        r3 = r3 + r9;
        r3 = r3 * 28;
        r3 = r3 + r11;
    L_0x00a8:
        if (r3 == r5) goto L_0x00d4;
    L_0x00aa:
        if (r3 == r5) goto L_0x0139;
    L_0x00ac:
        r3 = (char) r3;
        r6.setCharAt(r2, r3);
        r6.deleteCharAt(r0);
        r0 = r0 + -1;
        if (r0 != r2) goto L_0x012e;
    L_0x00b7:
        r3 = r1;
    L_0x00b8:
        r0 = r0 + 1;
        goto L_0x001a;
    L_0x00bc:
        r3 = r7 - r11;
        if (r3 < 0) goto L_0x00d2;
    L_0x00c0:
        r9 = 11172; // 0x2ba4 float:1.5655E-41 double:5.5197E-320;
        if (r3 >= r9) goto L_0x00d2;
    L_0x00c4:
        r3 = r3 % 28;
        if (r3 != 0) goto L_0x00d2;
    L_0x00c8:
        r3 = r8 + -4519;
        if (r3 < 0) goto L_0x00d2;
    L_0x00cc:
        r9 = 28;
        if (r3 > r9) goto L_0x00d2;
    L_0x00d0:
        r3 = r3 + r7;
        goto L_0x00a8;
    L_0x00d2:
        r3 = r5;
        goto L_0x00a8;
    L_0x00d4:
        r3 = composeIndex(r7);
        r9 = 181; // 0xb5 float:2.54E-43 double:8.94E-322;
        if (r3 < r9) goto L_0x00f5;
    L_0x00dc:
        r9 = 391; // 0x187 float:5.48E-43 double:1.93E-321;
        if (r3 >= r9) goto L_0x00f5;
    L_0x00e0:
        r7 = gnu.inet.encoding.Composition.singleFirst;
        r9 = r3 + -181;
        r7 = r7[r9];
        r7 = r7[r1];
        if (r8 != r7) goto L_0x00f3;
    L_0x00ea:
        r7 = gnu.inet.encoding.Composition.singleFirst;
        r3 = r3 + -181;
        r3 = r7[r3];
        r3 = r3[r12];
        goto L_0x00aa;
    L_0x00f3:
        r3 = r5;
        goto L_0x00aa;
    L_0x00f5:
        r8 = composeIndex(r8);
        r9 = 391; // 0x187 float:5.48E-43 double:1.93E-321;
        if (r8 < r9) goto L_0x0112;
    L_0x00fd:
        r3 = gnu.inet.encoding.Composition.singleSecond;
        r9 = r8 + -391;
        r3 = r3[r9];
        r3 = r3[r1];
        if (r7 != r3) goto L_0x0110;
    L_0x0107:
        r3 = gnu.inet.encoding.Composition.singleSecond;
        r7 = r8 + -391;
        r3 = r3[r7];
        r3 = r3[r12];
        goto L_0x00aa;
    L_0x0110:
        r3 = r5;
        goto L_0x00aa;
    L_0x0112:
        if (r3 < 0) goto L_0x012b;
    L_0x0114:
        if (r3 >= r13) goto L_0x012b;
    L_0x0116:
        if (r8 < r13) goto L_0x012b;
    L_0x0118:
        r7 = 181; // 0xb5 float:2.54E-43 double:8.94E-322;
        if (r8 >= r7) goto L_0x012b;
    L_0x011c:
        r7 = gnu.inet.encoding.Composition.multiFirst;
        r3 = r7[r3];
        r7 = r8 + -144;
        r9 = r3.length;
        if (r7 >= r9) goto L_0x012b;
    L_0x0125:
        r7 = r8 + -144;
        r3 = r3[r7];
        if (r3 != 0) goto L_0x00aa;
    L_0x012b:
        r3 = r5;
        goto L_0x00aa;
    L_0x012e:
        r3 = r0 + -1;
        r3 = r6.charAt(r3);
        r3 = combiningClass(r3);
        goto L_0x00b8;
    L_0x0139:
        if (r4 != 0) goto L_0x013c;
    L_0x013b:
        r2 = r0;
    L_0x013c:
        r3 = r4;
        goto L_0x00b8;
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.inet.encoding.NFKC.normalizeNFKC(java.lang.String):java.lang.String");
    }

    private static int decomposeIndex(char c) {
        int length = DecompositionKeys.f36k.length / 2;
        int i = 0;
        while (true) {
            int i2 = (i + length) / 2;
            char c2 = DecompositionKeys.f36k[i2 * 2];
            if (c == c2) {
                return DecompositionKeys.f36k[(i2 * 2) + 1];
            }
            if (i2 == i) {
                return -1;
            }
            if (c > c2) {
                i = i2;
            } else {
                length = i2;
            }
        }
    }

    private static int combiningClass(char c) {
        int i = c & MotionEventCompat.ACTION_MASK;
        int i2 = CombiningClass.f35i[c >> 8];
        if (i2 >= 0) {
            return CombiningClass.f34c[i2][i];
        }
        return 0;
    }

    private static void canonicalOrdering(StringBuffer stringBuffer) {
        int i = 0;
        while (i == 0) {
            i = 1;
            int combiningClass = combiningClass(stringBuffer.charAt(0));
            for (int i2 = 0; i2 < stringBuffer.length() - 1; i2++) {
                int combiningClass2 = combiningClass(stringBuffer.charAt(i2 + 1));
                if (combiningClass2 == 0 || r2 <= combiningClass2) {
                    combiningClass = combiningClass2;
                } else {
                    int i3 = i2 + 1;
                    while (i3 > 0 && combiningClass(stringBuffer.charAt(i3 - 1)) > combiningClass2) {
                        char charAt = stringBuffer.charAt(i3);
                        stringBuffer.setCharAt(i3, stringBuffer.charAt(i3 - 1));
                        stringBuffer.setCharAt(i3 - 1, charAt);
                        i3--;
                        i = 0;
                    }
                }
            }
        }
    }

    private static int composeIndex(char c) {
        if ((c >> 8) >= Composition.composePage.length) {
            return -1;
        }
        int i = Composition.composePage[c >> 8];
        if (i != -1) {
            return Composition.composeData[i][c & MotionEventCompat.ACTION_MASK];
        }
        return -1;
    }
}
