package com.google.android.gms.common;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzb implements Creator<ConnectionResult> {
    static void zza(ConnectionResult connectionResult, Parcel parcel, int i) {
        int zzG = com.google.android.gms.common.internal.safeparcel.zzb.zzG(parcel, 20293);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, connectionResult.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, connectionResult.zzabx);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$377a007(parcel, 3, connectionResult.mPendingIntent, i);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$2cfb68bf(parcel, 4, connectionResult.zzadS);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = zza.zzau(x0);
        PendingIntent pendingIntent = null;
        int i = 0;
        int i2 = 0;
        String str = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i2 = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    pendingIntent = (PendingIntent) zza.zza(x0, readInt, PendingIntent.CREATOR);
                    break;
                case Logger.CONFIG /*4*/:
                    str = zza.zzp(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new ConnectionResult(i2, i, pendingIntent, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new ConnectionResult[x0];
    }
}
