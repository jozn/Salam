package com.google.android.gms.internal;

import java.io.IOException;

public abstract class zzte<M extends zzte<M>> extends zztk {
    protected zztg zzbpQ;

    private M zzHz() throws CloneNotSupportedException {
        zzte com_google_android_gms_internal_zzte = (zzte) super.clone();
        zzti.zza(this, com_google_android_gms_internal_zzte);
        return com_google_android_gms_internal_zzte;
    }

    public final /* synthetic */ zztk clone() throws CloneNotSupportedException {
        return zzHz();
    }

    public /* synthetic */ Object m47clone() throws CloneNotSupportedException {
        return zzHz();
    }

    public void writeTo(zztd output) throws IOException {
        if (this.zzbpQ != null) {
            for (int i = 0; i < this.zzbpQ.size(); i++) {
                this.zzbpQ.zzmD(i).writeTo(output);
            }
        }
    }

    protected int zzz() {
        int i = 0;
        if (this.zzbpQ == null) {
            return 0;
        }
        int i2 = 0;
        while (i < this.zzbpQ.size()) {
            i2 += this.zzbpQ.zzmD(i).zzz();
            i++;
        }
        return i2;
    }
}
