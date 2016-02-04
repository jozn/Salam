package com.google.android.gms.internal;

import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class zzlt implements com.google.android.gms.clearcut.zzb {
    private static final Object zzadD;
    private static final zze zzadE;
    private static final long zzadF;
    private GoogleApiClient zzYC;
    private final zza zzadG;
    private final Object zzadH;
    private long zzadI;
    private final long zzadJ;
    private ScheduledFuture<?> zzadK;
    private final Runnable zzadL;
    private final zznl zzqD;

    /* renamed from: com.google.android.gms.internal.zzlt.1 */
    class C04621 implements Runnable {
        final /* synthetic */ zzlt zzadM;

        C04621(zzlt com_google_android_gms_internal_zzlt) {
            this.zzadM = com_google_android_gms_internal_zzlt;
        }

        public final void run() {
            synchronized (this.zzadM.zzadH) {
                if (this.zzadM.zzadI <= this.zzadM.zzqD.elapsedRealtime() && this.zzadM.zzYC != null) {
                    Log.i("ClearcutLoggerApiImpl", "disconnect managed GoogleApiClient");
                    this.zzadM.zzYC.disconnect();
                    this.zzadM.zzYC = null;
                }
            }
        }
    }

    public interface zza {
    }

    public static class zzb implements zza {
    }

    private static final class zze {
        private int mSize;

        private zze() {
            this.mSize = 0;
        }
    }

    static {
        zzadD = new Object();
        zzadE = new zze();
        zzadF = TimeUnit.MILLISECONDS.convert(2, TimeUnit.MINUTES);
    }

    public zzlt() {
        this(new zzno(), zzadF, new zzb());
    }

    private zzlt(zznl com_google_android_gms_internal_zznl, long j, zza com_google_android_gms_internal_zzlt_zza) {
        this.zzadH = new Object();
        this.zzadI = 0;
        this.zzadK = null;
        this.zzYC = null;
        this.zzadL = new C04621(this);
        this.zzqD = com_google_android_gms_internal_zznl;
        this.zzadJ = j;
        this.zzadG = com_google_android_gms_internal_zzlt_zza;
    }
}
