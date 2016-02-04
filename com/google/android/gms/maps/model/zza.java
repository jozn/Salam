package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zza implements Creator<CameraPosition> {
    static void zza(CameraPosition cameraPosition, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, cameraPosition.mVersionCode);
        zzb.zza$377a007(parcel, 2, cameraPosition.target, i);
        zzb.zza(parcel, 3, cameraPosition.zoom);
        zzb.zza(parcel, 4, cameraPosition.tilt);
        zzb.zza(parcel, 5, cameraPosition.bearing);
        zzb.zzH(parcel, zzG);
    }

    public static CameraPosition zzfm(Parcel parcel) {
        float f = 0.0f;
        int zzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int i = 0;
        LatLng latLng = null;
        float f2 = 0.0f;
        float f3 = 0.0f;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, readInt, LatLng.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    f3 = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    f2 = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, readInt);
                    break;
                case Logger.FINE /*5*/:
                    f = com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, readInt);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new CameraPosition(i, latLng, f3, f2, f);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfm(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new CameraPosition[x0];
    }
}
