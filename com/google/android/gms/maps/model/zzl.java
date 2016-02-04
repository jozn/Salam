package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzl implements Creator<StreetViewPanoramaLocation> {
    static void zza(StreetViewPanoramaLocation streetViewPanoramaLocation, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, streetViewPanoramaLocation.mVersionCode);
        zzb.zza$2d7953c6(parcel, 2, streetViewPanoramaLocation.links, i);
        zzb.zza$377a007(parcel, 3, streetViewPanoramaLocation.position, i);
        zzb.zza$2cfb68bf(parcel, 4, streetViewPanoramaLocation.panoId);
        zzb.zzH(parcel, zzG);
    }

    public static StreetViewPanoramaLocation zzfx(Parcel parcel) {
        int zzau = zza.zzau(parcel);
        LatLng latLng = null;
        StreetViewPanoramaLink[] streetViewPanoramaLinkArr = null;
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    streetViewPanoramaLinkArr = (StreetViewPanoramaLink[]) zza.zzb(parcel, readInt, StreetViewPanoramaLink.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    latLng = (LatLng) zza.zza(parcel, readInt, LatLng.CREATOR);
                    break;
                case Logger.CONFIG /*4*/:
                    str = zza.zzp(parcel, readInt);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new StreetViewPanoramaLocation(i, streetViewPanoramaLinkArr, latLng, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfx(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new StreetViewPanoramaLocation[x0];
    }
}
