package com.google.android.gms.common.api;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzd implements Creator<Status> {
    static void zza(Status status, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, status.zzabx);
        zzb.zzc(parcel, 1000, status.mVersionCode);
        zzb.zza$2cfb68bf(parcel, 2, status.zzadS);
        zzb.zza$377a007(parcel, 3, status.mPendingIntent, i);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        PendingIntent pendingIntent = null;
        int zzau = zza.zzau(x0);
        int i = 0;
        int i2 = 0;
        String str = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    str = zza.zzp(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    pendingIntent = (PendingIntent) zza.zza(x0, readInt, PendingIntent.CREATOR);
                    break;
                case 1000:
                    i2 = zza.zzg(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new Status(i2, i, str, pendingIntent);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new Status[x0];
    }
}
