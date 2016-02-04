package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzb implements Creator<FACLData> {
    static void zza(FACLData fACLData, Parcel parcel, int i) {
        int zzG = com.google.android.gms.common.internal.safeparcel.zzb.zzG(parcel, 20293);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, fACLData.version);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$377a007(parcel, 2, fACLData.zzWN, i);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$2cfb68bf(parcel, 3, fACLData.zzWO);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, fACLData.zzWP);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$2cfb68bf(parcel, 5, fACLData.zzWQ);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        boolean z = false;
        String str = null;
        int zzau = zza.zzau(x0);
        String str2 = null;
        FACLConfig fACLConfig = null;
        int i = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    fACLConfig = (FACLConfig) zza.zza(x0, readInt, FACLConfig.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    str2 = zza.zzp(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    z = zza.zzc(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    str = zza.zzp(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new FACLData(i, fACLConfig, str2, z, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new FACLData[x0];
    }
}
