package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzc implements Creator<GroundOverlayOptions> {
    static void zza(GroundOverlayOptions groundOverlayOptions, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, groundOverlayOptions.mVersionCode);
        zzb.zza$cdac282(parcel, 2, groundOverlayOptions.zzaQm.zzaOw.asBinder());
        zzb.zza$377a007(parcel, 3, groundOverlayOptions.zzaQn, i);
        zzb.zza(parcel, 4, groundOverlayOptions.zzaQo);
        zzb.zza(parcel, 5, groundOverlayOptions.zzaQp);
        zzb.zza$377a007(parcel, 6, groundOverlayOptions.zzaQq, i);
        zzb.zza(parcel, 7, groundOverlayOptions.zzaQc);
        zzb.zza(parcel, 8, groundOverlayOptions.zzaQj);
        zzb.zza(parcel, 9, groundOverlayOptions.zzaQk);
        zzb.zza(parcel, 10, groundOverlayOptions.zzaQr);
        zzb.zza(parcel, 11, groundOverlayOptions.zzaQs);
        zzb.zza(parcel, 12, groundOverlayOptions.zzaQt);
        zzb.zzH(parcel, zzG);
    }

    public static GroundOverlayOptions zzfo(Parcel parcel) {
        boolean z = false;
        LatLngBounds latLngBounds = null;
        float f = 0.0f;
        int zzau = zza.zzau(parcel);
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        LatLng latLng = null;
        IBinder iBinder = null;
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
                    latLng = (LatLng) zza.zza(parcel, readInt, LatLng.CREATOR);
                    break;
                case Logger.CONFIG /*4*/:
                    f7 = zza.zzl(parcel, readInt);
                    break;
                case Logger.FINE /*5*/:
                    f6 = zza.zzl(parcel, readInt);
                    break;
                case Logger.FINER /*6*/:
                    latLngBounds = (LatLngBounds) zza.zza(parcel, readInt, LatLngBounds.CREATOR);
                    break;
                case Logger.FINEST /*7*/:
                    f5 = zza.zzl(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    f4 = zza.zzl(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    z = zza.zzc(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    f3 = zza.zzl(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    f2 = zza.zzl(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    f = zza.zzl(parcel, readInt);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new GroundOverlayOptions(i, iBinder, latLng, f7, f6, latLngBounds, f5, f4, z, f3, f2, f);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfo(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new GroundOverlayOptions[x0];
    }
}
