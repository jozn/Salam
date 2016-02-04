package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzd implements Creator<TokenData> {
    static void zza$6d52043c(TokenData tokenData, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, tokenData.mVersionCode);
        zzb.zza$2cfb68bf(parcel, 2, tokenData.zzTR);
        Long l = tokenData.zzTS;
        if (l != null) {
            zzb.zzb(parcel, 3, 8);
            parcel.writeLong(l.longValue());
        }
        zzb.zza(parcel, 4, tokenData.zzTT);
        zzb.zza(parcel, 5, tokenData.zzTU);
        zzb.zzb$62107c48(parcel, 6, tokenData.zzTV);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        boolean z = false;
        int zzau = zza.zzau(x0);
        List list = null;
        boolean z2 = false;
        Long l = null;
        String str = null;
        int i = 0;
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
                    int zza = zza.zza(x0, readInt);
                    if (zza != 0) {
                        zza.zza$ae3cd4b(x0, zza, 8);
                        l = Long.valueOf(x0.readLong());
                        break;
                    }
                    l = null;
                    break;
                case Logger.CONFIG /*4*/:
                    z2 = zza.zzc(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    z = zza.zzc(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    list = zza.zzD(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new TokenData(i, str, l, z2, z, list);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new TokenData[x0];
    }
}
