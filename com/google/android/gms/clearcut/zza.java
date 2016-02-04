package com.google.android.gms.clearcut;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.internal.zzlt;

public final class zza {
    public static final Api<Object> API;
    public static final zzc<Object> zzTo;
    public static final com.google.android.gms.common.api.Api.zza<Object, Object> zzTp;
    public static final zzb zzadh;

    /* renamed from: com.google.android.gms.clearcut.zza.1 */
    static class C02391 extends com.google.android.gms.common.api.Api.zza<Object, Object> {
        C02391() {
        }
    }

    public interface zzb {
    }

    static {
        zzTo = new zzc();
        zzTp = new C02391();
        API = new Api("ClearcutLogger.API", zzTp, zzTo);
        zzadh = new zzlt();
    }
}
