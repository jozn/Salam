package de.measite.minidns.record;

import android.support.v4.view.MotionEventCompat;
import java.io.DataInputStream;
import java.io.IOException;

public final class AAAA implements Data {
    private byte[] ip;

    public final void parse$4e8e5594(DataInputStream dis, byte[] data) throws IOException {
        this.ip = new byte[16];
        dis.readFully(this.ip);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.ip.length; i += 2) {
            if (i != 0) {
                sb.append(':');
            }
            sb.append(Integer.toHexString(((this.ip[i] & MotionEventCompat.ACTION_MASK) << 8) + (this.ip[i + 1] & MotionEventCompat.ACTION_MASK)));
        }
        return sb.toString();
    }
}
