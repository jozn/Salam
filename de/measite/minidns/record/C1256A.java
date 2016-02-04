package de.measite.minidns.record;

import android.support.v4.view.MotionEventCompat;
import java.io.DataInputStream;
import java.io.IOException;

/* renamed from: de.measite.minidns.record.A */
public final class C1256A implements Data {
    private byte[] ip;

    public final void parse$4e8e5594(DataInputStream dis, byte[] data) throws IOException {
        this.ip = new byte[4];
        dis.readFully(this.ip);
    }

    public final String toString() {
        return Integer.toString(this.ip[0] & MotionEventCompat.ACTION_MASK) + "." + Integer.toString(this.ip[1] & MotionEventCompat.ACTION_MASK) + "." + Integer.toString(this.ip[2] & MotionEventCompat.ACTION_MASK) + "." + Integer.toString(this.ip[3] & MotionEventCompat.ACTION_MASK);
    }
}
