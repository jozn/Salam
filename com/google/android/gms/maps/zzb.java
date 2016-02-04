package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.maps.internal.zza;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzb implements Creator<StreetViewPanoramaOptions> {
    static void zza(StreetViewPanoramaOptions streetViewPanoramaOptions, Parcel parcel, int i) {
        int zzG = com.google.android.gms.common.internal.safeparcel.zzb.zzG(parcel, 20293);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, streetViewPanoramaOptions.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$377a007(parcel, 2, streetViewPanoramaOptions.zzaPF, i);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$2cfb68bf(parcel, 3, streetViewPanoramaOptions.zzaPG);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$377a007(parcel, 4, streetViewPanoramaOptions.zzaPH, i);
        Integer num = streetViewPanoramaOptions.zzaPI;
        if (num != null) {
            com.google.android.gms.common.internal.safeparcel.zzb.zzb(parcel, 5, 4);
            parcel.writeInt(num.intValue());
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, zza.zze(streetViewPanoramaOptions.zzaPJ));
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, zza.zze(streetViewPanoramaOptions.zzaOY));
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, zza.zze(streetViewPanoramaOptions.zzaPK));
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 9, zza.zze(streetViewPanoramaOptions.zzaPL));
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 10, zza.zze(streetViewPanoramaOptions.zzaOS));
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzG);
    }

    public static StreetViewPanoramaOptions zzfl(Parcel parcel) {
        byte b = (byte) 0;
        int zzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        byte b2 = (byte) 0;
        byte b3 = (byte) 0;
        byte b4 = (byte) 0;
        byte b5 = (byte) 0;
        Integer num = null;
        LatLng latLng = null;
        String str = null;
        StreetViewPanoramaCamera streetViewPanoramaCamera = null;
        int i = 0;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    streetViewPanoramaCamera = (StreetViewPanoramaCamera) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, readInt, StreetViewPanoramaCamera.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzp(parcel, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, readInt, LatLng.CREATOR);
                    break;
                case Logger.FINE /*5*/:
                    readInt = com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, readInt);
                    if (readInt != 0) {
                        com.google.android.gms.common.internal.safeparcel.zza.zza$ae3cd4b(parcel, readInt, 4);
                        num = Integer.valueOf(parcel.readInt());
                        break;
                    }
                    num = null;
                    break;
                case Logger.FINER /*6*/:
                    b5 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case Logger.FINEST /*7*/:
                    b4 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    b3 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    b2 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    b = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new StreetViewPanoramaOptions(i, streetViewPanoramaCamera, str, latLng, num, b5, b4, b3, b2, b);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfl(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new StreetViewPanoramaOptions[x0];
    }
}
