package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzo implements Creator<TileOverlayOptions> {
    static void zza$4b899d8a(TileOverlayOptions tileOverlayOptions, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, tileOverlayOptions.mVersionCode);
        zzb.zza$cdac282(parcel, 2, tileOverlayOptions.zzaQR.asBinder());
        zzb.zza(parcel, 3, tileOverlayOptions.zzaQk);
        zzb.zza(parcel, 4, tileOverlayOptions.zzaQj);
        zzb.zza(parcel, 5, tileOverlayOptions.zzaQT);
        zzb.zzH(parcel, zzG);
    }

    public static TileOverlayOptions zzfA(Parcel parcel) {
        boolean z = false;
        int zzau = zza.zzau(parcel);
        IBinder iBinder = null;
        float f = 0.0f;
        boolean z2 = true;
        int i = 0;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    iBinder = zza.zzq(parcel, readInt);
                    break;
                case Logger.INFO /*3*/:
                    z = zza.zzc(parcel, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    f = zza.zzl(parcel, readInt);
                    break;
                case Logger.FINE /*5*/:
                    z2 = zza.zzc(parcel, readInt);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new TileOverlayOptions(i, iBinder, z, f, z2);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfA(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new TileOverlayOptions[x0];
    }
}
