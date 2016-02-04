package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzk implements Creator<StreetViewPanoramaLink> {
    static void zza$7cf702f(StreetViewPanoramaLink streetViewPanoramaLink, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, streetViewPanoramaLink.mVersionCode);
        zzb.zza$2cfb68bf(parcel, 2, streetViewPanoramaLink.panoId);
        zzb.zza(parcel, 3, streetViewPanoramaLink.bearing);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = zza.zzau(x0);
        int i = 0;
        String str = null;
        float f = 0.0f;
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
                    f = zza.zzl(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new StreetViewPanoramaLink(i, str, f);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new StreetViewPanoramaLink[x0];
    }
}
