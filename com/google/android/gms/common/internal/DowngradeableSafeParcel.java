package com.google.android.gms.common.internal;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public abstract class DowngradeableSafeParcel implements SafeParcelable {
    private static final Object zzajw;
    private static ClassLoader zzajx;
    private static Integer zzajy;
    private boolean zzajz;

    static {
        zzajw = new Object();
        zzajx = null;
        zzajy = null;
    }

    public DowngradeableSafeParcel() {
        this.zzajz = false;
    }
}
