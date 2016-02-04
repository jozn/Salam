package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzg implements Creator<PointOfInterest> {
    static void zza(PointOfInterest pointOfInterest, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, pointOfInterest.mVersionCode);
        zzb.zza$377a007(parcel, 2, pointOfInterest.zzaQI, i);
        zzb.zza$2cfb68bf(parcel, 3, pointOfInterest.zzaQJ);
        zzb.zza$2cfb68bf(parcel, 4, pointOfInterest.name);
        zzb.zzH(parcel, zzG);
    }

    public static PointOfInterest zzfs(Parcel parcel) {
        int zzau = zza.zzau(parcel);
        String str = null;
        LatLng latLng = null;
        int i = 0;
        String str2 = null;
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
                    str = zza.zzp(parcel, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    str2 = zza.zzp(parcel, readInt);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new PointOfInterest(i, latLng, str, str2);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfs(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new PointOfInterest[x0];
    }
}
