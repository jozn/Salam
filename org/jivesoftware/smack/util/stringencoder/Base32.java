package org.jivesoftware.smack.util.stringencoder;

import android.support.v4.view.MotionEventCompat;
import java.io.ByteArrayOutputStream;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class Base32 {
    private static final StringEncoder base32Stringencoder;

    /* renamed from: org.jivesoftware.smack.util.stringencoder.Base32.1 */
    static class C12961 implements StringEncoder {
        C12961() {
        }

        public final String encode(String string) {
            return Base32.encode(string);
        }
    }

    static {
        base32Stringencoder = new C12961();
    }

    public static StringEncoder getStringEncoder() {
        return base32Stringencoder;
    }

    public static String encode(String str) {
        byte[] b = str.getBytes();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (int i = 0; i < (b.length + 4) / 5; i++) {
            int j;
            int padlen;
            short[] s = new short[5];
            int[] t = new int[8];
            int blocklen = 5;
            for (j = 0; j < 5; j++) {
                if ((i * 5) + j < b.length) {
                    s[j] = (short) (b[(i * 5) + j] & MotionEventCompat.ACTION_MASK);
                } else {
                    s[j] = (short) 0;
                    blocklen--;
                }
            }
            switch (blocklen) {
                case Logger.SEVERE /*1*/:
                    padlen = 6;
                    break;
                case Logger.WARNING /*2*/:
                    padlen = 4;
                    break;
                case Logger.INFO /*3*/:
                    padlen = 3;
                    break;
                case Logger.CONFIG /*4*/:
                    padlen = 1;
                    break;
                case Logger.FINE /*5*/:
                    padlen = 0;
                    break;
                default:
                    padlen = -1;
                    break;
            }
            t[0] = (byte) ((s[0] >> 3) & 31);
            t[1] = (byte) (((s[0] & 7) << 2) | ((s[1] >> 6) & 3));
            t[2] = (byte) ((s[1] >> 1) & 31);
            t[3] = (byte) (((s[1] & 1) << 4) | ((s[2] >> 4) & 15));
            t[4] = (byte) (((s[2] & 15) << 1) | ((s[3] >> 7) & 1));
            t[5] = (byte) ((s[3] >> 2) & 31);
            t[6] = (byte) (((s[3] & 3) << 3) | ((s[4] >> 5) & 7));
            t[7] = (byte) (s[4] & 31);
            for (j = 0; j < 8 - padlen; j++) {
                os.write("ABCDEFGHIJKLMNOPQRSTUVWXYZ2345678".charAt(t[j]));
            }
        }
        return new String(os.toByteArray());
    }
}
