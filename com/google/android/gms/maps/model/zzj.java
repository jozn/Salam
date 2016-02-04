package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzj implements Creator<StreetViewPanoramaCamera> {
    static void zza$31dd21c(StreetViewPanoramaCamera streetViewPanoramaCamera, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, streetViewPanoramaCamera.mVersionCode);
        zzb.zza(parcel, 2, streetViewPanoramaCamera.zoom);
        zzb.zza(parcel, 3, streetViewPanoramaCamera.tilt);
        zzb.zza(parcel, 4, streetViewPanoramaCamera.bearing);
        zzb.zzH(parcel, zzG);
    }

    public static StreetViewPanoramaCamera zzfv(Parcel parcel) {
        float f = 0.0f;
        int zzau = zza.zzau(parcel);
        float f2 = 0.0f;
        int i = 0;
        float f3 = 0.0f;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    f2 = zza.zzl(parcel, readInt);
                    break;
                case Logger.INFO /*3*/:
                    f3 = zza.zzl(parcel, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    f = zza.zzl(parcel, readInt);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new StreetViewPanoramaCamera(i, f2, f3, f);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfv(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new StreetViewPanoramaCamera[x0];
    }
}
