package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zza implements Creator<FACLConfig> {
    static void zza$5331385a(FACLConfig fACLConfig, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, fACLConfig.version);
        zzb.zza(parcel, 2, fACLConfig.zzWH);
        zzb.zza$2cfb68bf(parcel, 3, fACLConfig.zzWI);
        zzb.zza(parcel, 4, fACLConfig.zzWJ);
        zzb.zza(parcel, 5, fACLConfig.zzWK);
        zzb.zza(parcel, 6, fACLConfig.zzWL);
        zzb.zza(parcel, 7, fACLConfig.zzWM);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        boolean z = false;
        int zzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(x0);
        String str = null;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        int i = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    z5 = com.google.android.gms.common.internal.safeparcel.zza.zzc(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzp(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    z4 = com.google.android.gms.common.internal.safeparcel.zza.zzc(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    z3 = com.google.android.gms.common.internal.safeparcel.zza.zzc(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    z2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(x0, readInt);
                    break;
                case Logger.FINEST /*7*/:
                    z = com.google.android.gms.common.internal.safeparcel.zza.zzc(x0, readInt);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new FACLConfig(i, z5, str, z4, z3, z2, z);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new FACLConfig[x0];
    }
}
