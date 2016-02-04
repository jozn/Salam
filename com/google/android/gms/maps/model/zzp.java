package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzp implements Creator<VisibleRegion> {
    static void zza(VisibleRegion visibleRegion, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, visibleRegion.mVersionCode);
        zzb.zza$377a007(parcel, 2, visibleRegion.nearLeft, i);
        zzb.zza$377a007(parcel, 3, visibleRegion.nearRight, i);
        zzb.zza$377a007(parcel, 4, visibleRegion.farLeft, i);
        zzb.zza$377a007(parcel, 5, visibleRegion.farRight, i);
        zzb.zza$377a007(parcel, 6, visibleRegion.latLngBounds, i);
        zzb.zzH(parcel, zzG);
    }

    public static VisibleRegion zzfB(Parcel parcel) {
        LatLngBounds latLngBounds = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        LatLng latLng = null;
        LatLng latLng2 = null;
        LatLng latLng3 = null;
        LatLng latLng4 = null;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    latLng4 = (LatLng) zza.zza(parcel, readInt, LatLng.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    latLng3 = (LatLng) zza.zza(parcel, readInt, LatLng.CREATOR);
                    break;
                case Logger.CONFIG /*4*/:
                    latLng2 = (LatLng) zza.zza(parcel, readInt, LatLng.CREATOR);
                    break;
                case Logger.FINE /*5*/:
                    latLng = (LatLng) zza.zza(parcel, readInt, LatLng.CREATOR);
                    break;
                case Logger.FINER /*6*/:
                    latLngBounds = (LatLngBounds) zza.zza(parcel, readInt, LatLngBounds.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new VisibleRegion(i, latLng4, latLng3, latLng2, latLng, latLngBounds);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfB(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new VisibleRegion[x0];
    }
}
