package com.google.android.gms.playlog.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzc implements Creator<LogEvent> {
    static void zza$298c9f3b(LogEvent logEvent, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, logEvent.versionCode);
        zzb.zza(parcel, 2, logEvent.zzaYn);
        zzb.zza$2cfb68bf(parcel, 3, logEvent.tag);
        zzb.zza$52910762(parcel, 4, logEvent.zzaYp);
        zzb.zza$f7bef55(parcel, 5, logEvent.zzaYq);
        zzb.zza(parcel, 6, logEvent.zzaYo);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        long j = 0;
        Bundle bundle = null;
        int zzau = zza.zzau(x0);
        int i = 0;
        byte[] bArr = null;
        String str = null;
        long j2 = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    j2 = zza.zzi(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    str = zza.zzp(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    bArr = zza.zzs(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    bundle = zza.zzr(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    j = zza.zzi(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new LogEvent(i, j2, j, str, bArr, bundle);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new LogEvent[x0];
    }
}
