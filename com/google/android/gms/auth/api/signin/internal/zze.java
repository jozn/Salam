package com.google.android.gms.auth.api.signin.internal;

public final class zze {
    static int zzWa;
    public int zzWb;

    static {
        zzWa = 31;
    }

    public zze() {
        this.zzWb = 1;
    }

    public final zze zzP(boolean z) {
        this.zzWb = (z ? 1 : 0) + (this.zzWb * zzWa);
        return this;
    }

    public final zze zzo(Object obj) {
        this.zzWb = (obj == null ? 0 : obj.hashCode()) + (this.zzWb * zzWa);
        return this;
    }
}
