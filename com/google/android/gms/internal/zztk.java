package com.google.android.gms.internal;

import java.io.IOException;

public abstract class zztk {
    protected volatile int zzbqb;

    public zztk() {
        this.zzbqb = -1;
    }

    public zztk clone() throws CloneNotSupportedException {
        return (zztk) super.clone();
    }

    public final int getCachedSize() {
        if (this.zzbqb < 0) {
            getSerializedSize();
        }
        return this.zzbqb;
    }

    public final int getSerializedSize() {
        int zzz = zzz();
        this.zzbqb = zzz;
        return zzz;
    }

    public String toString() {
        return zztl.zzf(this);
    }

    public void writeTo(zztd output) throws IOException {
    }

    protected int zzz() {
        return 0;
    }
}
