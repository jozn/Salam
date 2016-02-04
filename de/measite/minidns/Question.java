package de.measite.minidns;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import de.measite.minidns.Record.CLASS;
import de.measite.minidns.Record.TYPE;
import de.measite.minidns.util.NameUtil;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public final class Question {
    private byte[] byteArray;
    final CLASS clazz;
    final String name;
    final TYPE type;

    public Question(String name, TYPE type, CLASS clazz) {
        this.name = name;
        this.type = type;
        this.clazz = clazz;
    }

    public final byte[] toByteArray() {
        if (this.byteArray == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(AccessibilityNodeInfoCompat.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY);
            DataOutputStream dos = new DataOutputStream(baos);
            try {
                dos.write(NameUtil.toByteArray(this.name));
                dos.writeShort(this.type.value);
                dos.writeShort(this.clazz.value);
                dos.flush();
                this.byteArray = baos.toByteArray();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return this.byteArray;
    }

    public final int hashCode() {
        return Arrays.hashCode(toByteArray());
    }

    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Question) {
            return Arrays.equals(toByteArray(), ((Question) other).toByteArray());
        }
        return false;
    }
}
