package org.eclipse.paho.client.mqttv3.internal.wire;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.IOException;
import java.io.InputStream;

public class MultiByteArrayInputStream extends InputStream {
    private byte[] bytesA;
    private byte[] bytesB;
    private int lengthA;
    private int lengthB;
    private int offsetA;
    private int offsetB;
    private int pos;

    public MultiByteArrayInputStream(byte[] bytesA, int offsetA, int lengthA, byte[] bytesB, int offsetB, int lengthB) {
        this.pos = 0;
        this.bytesA = bytesA;
        this.bytesB = bytesB;
        this.offsetA = offsetA;
        this.offsetB = offsetB;
        this.lengthA = lengthA;
        this.lengthB = lengthB;
    }

    public int read() throws IOException {
        int result;
        if (this.pos < this.lengthA) {
            result = this.bytesA[this.offsetA + this.pos];
        } else if (this.pos >= this.lengthA + this.lengthB) {
            return -1;
        } else {
            result = this.bytesB[(this.offsetB + this.pos) - this.lengthA];
        }
        if (result < 0) {
            result += AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
        }
        this.pos++;
        return result;
    }
}
