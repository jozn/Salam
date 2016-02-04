package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zza implements Creator<AccountChangeEvent> {
    static void zza$119e69c0(AccountChangeEvent accountChangeEvent, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, accountChangeEvent.mVersion);
        zzb.zza(parcel, 2, accountChangeEvent.zzTC);
        zzb.zza$2cfb68bf(parcel, 3, accountChangeEvent.zzTD);
        zzb.zzc(parcel, 4, accountChangeEvent.zzTE);
        zzb.zzc(parcel, 5, accountChangeEvent.zzTF);
        zzb.zza$2cfb68bf(parcel, 6, accountChangeEvent.zzTG);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        String str = null;
        int i = 0;
        int zzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(x0);
        long j = 0;
        int i2 = 0;
        String str2 = null;
        int i3 = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    j = com.google.android.gms.common.internal.safeparcel.zza.zzi(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    str2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzp(x0, readInt);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new AccountChangeEvent(i3, j, str2, i2, i, str);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new AccountChangeEvent[x0];
    }
}
