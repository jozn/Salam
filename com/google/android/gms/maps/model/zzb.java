package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzb implements Creator<CircleOptions> {
    static void zza(CircleOptions circleOptions, Parcel parcel, int i) {
        int zzG = com.google.android.gms.common.internal.safeparcel.zzb.zzG(parcel, 20293);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, circleOptions.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$377a007(parcel, 2, circleOptions.zzaQe, i);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, circleOptions.zzaQf);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, circleOptions.zzaQg);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 5, circleOptions.zzaQh);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 6, circleOptions.zzaQi);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, circleOptions.zzaQj);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, circleOptions.zzaQk);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzG);
    }

    public static CircleOptions zzfn(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int zzau = zza.zzau(parcel);
        LatLng latLng = null;
        double d = 0.0d;
        int i = 0;
        int i2 = 0;
        float f2 = 0.0f;
        int i3 = 0;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i3 = zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    latLng = (LatLng) zza.zza(parcel, readInt, LatLng.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    d = zza.zzn(parcel, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    f2 = zza.zzl(parcel, readInt);
                    break;
                case Logger.FINE /*5*/:
                    i2 = zza.zzg(parcel, readInt);
                    break;
                case Logger.FINER /*6*/:
                    i = zza.zzg(parcel, readInt);
                    break;
                case Logger.FINEST /*7*/:
                    f = zza.zzl(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    z = zza.zzc(parcel, readInt);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new CircleOptions(i3, latLng, d, f2, i2, i, f, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfn(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new CircleOptions[x0];
    }
}
