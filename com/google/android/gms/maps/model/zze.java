package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zze implements Creator<LatLng> {
    static void zza$7afccc40(LatLng latLng, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, latLng.mVersionCode);
        zzb.zza(parcel, 2, latLng.latitude);
        zzb.zza(parcel, 3, latLng.longitude);
        zzb.zzH(parcel, zzG);
    }

    public static LatLng zzfq(Parcel parcel) {
        double d = 0.0d;
        int zzau = zza.zzau(parcel);
        int i = 0;
        double d2 = 0.0d;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    d2 = zza.zzn(parcel, readInt);
                    break;
                case Logger.INFO /*3*/:
                    d = zza.zzn(parcel, readInt);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new LatLng(i, d2, d);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfq(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new LatLng[x0];
    }
}
