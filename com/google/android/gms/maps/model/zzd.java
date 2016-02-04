package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzd implements Creator<LatLngBounds> {
    static void zza(LatLngBounds latLngBounds, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, latLngBounds.mVersionCode);
        zzb.zza$377a007(parcel, 2, latLngBounds.southwest, i);
        zzb.zza$377a007(parcel, 3, latLngBounds.northeast, i);
        zzb.zzH(parcel, zzG);
    }

    public static LatLngBounds zzfp(Parcel parcel) {
        int zzau = zza.zzau(parcel);
        LatLng latLng = null;
        int i = 0;
        LatLng latLng2 = null;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    latLng = (LatLng) zza.zza(parcel, readInt, LatLng.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    latLng2 = (LatLng) zza.zza(parcel, readInt, LatLng.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new LatLngBounds(i, latLng, latLng2);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfp(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new LatLngBounds[x0];
    }
}
