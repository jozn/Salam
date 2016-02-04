package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzi implements Creator<PolylineOptions> {
    static void zza$37f6d9f8(PolylineOptions polylineOptions, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, polylineOptions.mVersionCode);
        zzb.zzc$62107c48(parcel, 2, polylineOptions.zzaQL);
        zzb.zza(parcel, 3, polylineOptions.zzaQo);
        zzb.zzc(parcel, 4, polylineOptions.mColor);
        zzb.zza(parcel, 5, polylineOptions.zzaQj);
        zzb.zza(parcel, 6, polylineOptions.zzaQk);
        zzb.zza(parcel, 7, polylineOptions.zzaQN);
        zzb.zzH(parcel, zzG);
    }

    public static PolylineOptions zzfu(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int zzau = zza.zzau(parcel);
        List list = null;
        boolean z2 = false;
        int i = 0;
        float f2 = 0.0f;
        int i2 = 0;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i2 = zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    list = zza.zzc(parcel, readInt, LatLng.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    f2 = zza.zzl(parcel, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    i = zza.zzg(parcel, readInt);
                    break;
                case Logger.FINE /*5*/:
                    f = zza.zzl(parcel, readInt);
                    break;
                case Logger.FINER /*6*/:
                    z2 = zza.zzc(parcel, readInt);
                    break;
                case Logger.FINEST /*7*/:
                    z = zza.zzc(parcel, readInt);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new PolylineOptions(i2, list, f2, i, f, z2, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfu(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new PolylineOptions[x0];
    }
}
