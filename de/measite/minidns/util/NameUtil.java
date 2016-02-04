package de.measite.minidns.util;

import android.support.v7.appcompat.BuildConfig;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.IDN;
import java.util.HashSet;

public final class NameUtil {
    public static byte[] toByteArray(String name) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(64);
        DataOutputStream dos = new DataOutputStream(baos);
        for (String toASCII : name.split("[.\u3002\uff0e\uff61]")) {
            byte[] buffer = IDN.toASCII(toASCII).getBytes();
            dos.writeByte(buffer.length);
            dos.write(buffer);
        }
        dos.writeByte(0);
        dos.flush();
        return baos.toByteArray();
    }

    public static String parse(DataInputStream dis, byte[] data) throws IOException {
        int c = dis.readUnsignedByte();
        if ((c & 192) == 192) {
            c = ((c & 63) << 8) + dis.readUnsignedByte();
            HashSet<Integer> jumps = new HashSet();
            jumps.add(Integer.valueOf(c));
            return parse(data, c, jumps);
        } else if (c == 0) {
            return BuildConfig.VERSION_NAME;
        } else {
            byte[] b = new byte[c];
            dis.readFully(b);
            String s = IDN.toUnicode(new String(b));
            String t = parse(dis, data);
            if (t.length() > 0) {
                return s + "." + t;
            }
            return s;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String parse(byte[] r5, int r6, java.util.HashSet<java.lang.Integer> r7) {
        /*
    L_0x0000:
        r3 = r5[r6];
        r0 = r3 & 255;
        r3 = r0 & 192;
        r4 = 192; // 0xc0 float:2.69E-43 double:9.5E-322;
        if (r3 != r4) goto L_0x0031;
    L_0x000a:
        r3 = r0 & 63;
        r3 = r3 << 8;
        r4 = r6 + 1;
        r4 = r5[r4];
        r4 = r4 & 255;
        r0 = r3 + r4;
        r3 = java.lang.Integer.valueOf(r0);
        r3 = r7.contains(r3);
        if (r3 == 0) goto L_0x0028;
    L_0x0020:
        r3 = new java.lang.IllegalStateException;
        r4 = "Cyclic offsets detected.";
        r3.<init>(r4);
        throw r3;
    L_0x0028:
        r3 = java.lang.Integer.valueOf(r0);
        r7.add(r3);
        r6 = r0;
        goto L_0x0000;
    L_0x0031:
        if (r0 != 0) goto L_0x0036;
    L_0x0033:
        r1 = "";
    L_0x0035:
        return r1;
    L_0x0036:
        r1 = new java.lang.String;
        r3 = r6 + 1;
        r1.<init>(r5, r3, r0);
        r3 = r6 + 1;
        r3 = r3 + r0;
        r2 = parse(r5, r3, r7);
        r3 = r2.length();
        if (r3 <= 0) goto L_0x0035;
    L_0x004a:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3 = r3.append(r1);
        r4 = ".";
        r3 = r3.append(r4);
        r3 = r3.append(r2);
        r1 = r3.toString();
        goto L_0x0035;
        */
        throw new UnsupportedOperationException("Method not decompiled: de.measite.minidns.util.NameUtil.parse(byte[], int, java.util.HashSet):java.lang.String");
    }
}
