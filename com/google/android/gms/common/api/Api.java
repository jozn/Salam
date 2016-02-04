package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.zzx;

public final class Api<O> {
    private final String mName;
    private final zzc<?> zzacX;
    private final zza<?, O> zzaep;
    private final zze<?, O> zzaeq;
    private final zzf<?> zzaer;

    public static abstract class zza<T, O> {
    }

    public static final class zzc<C> {
    }

    public interface zze<T, O> {
    }

    public static final class zzf<C> {
    }

    public <C> Api(String name, zza<C, O> clientBuilder, zzc<C> clientKey) {
        zzx.zzb((Object) clientBuilder, (Object) "Cannot construct an Api with a null ClientBuilder");
        zzx.zzb((Object) clientKey, (Object) "Cannot construct an Api with a null ClientKey");
        this.mName = name;
        this.zzaep = clientBuilder;
        this.zzaeq = null;
        this.zzacX = clientKey;
        this.zzaer = null;
    }
}
