package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzc implements Creator<Scope> {
    static void zza$514aa83(Scope scope, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, scope.mVersionCode);
        zzb.zza$2cfb68bf(parcel, 2, scope.zzaeW);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = zza.zzau(x0);
        int i = 0;
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
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new Scope(i, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new Scope[x0];
    }
}
