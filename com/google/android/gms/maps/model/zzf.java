package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzf implements Creator<MarkerOptions> {
    static void zza(MarkerOptions markerOptions, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, markerOptions.mVersionCode);
        zzb.zza$377a007(parcel, 2, markerOptions.zzaPH, i);
        zzb.zza$2cfb68bf(parcel, 3, markerOptions.zzank);
        zzb.zza$2cfb68bf(parcel, 4, markerOptions.zzaQB);
        zzb.zza$cdac282(parcel, 5, markerOptions.zzaQC == null ? null : markerOptions.zzaQC.zzaOw.asBinder());
        zzb.zza(parcel, 6, markerOptions.zzaQs);
        zzb.zza(parcel, 7, markerOptions.zzaQt);
        zzb.zza(parcel, 8, markerOptions.zzaQD);
        zzb.zza(parcel, 9, markerOptions.zzaQk);
        zzb.zza(parcel, 10, markerOptions.zzaQE);
        zzb.zza(parcel, 11, markerOptions.zzaQF);
        zzb.zza(parcel, 12, markerOptions.zzaQG);
        zzb.zza(parcel, 13, markerOptions.zzaQH);
        zzb.zza(parcel, 14, markerOptions.mAlpha);
        zzb.zzH(parcel, zzG);
    }

    public static MarkerOptions zzfr(Parcel parcel) {
        int zzau = zza.zzau(parcel);
        int i = 0;
        LatLng latLng = null;
        String str = null;
        String str2 = null;
        IBinder iBinder = null;
        float f = 0.0f;
        float f2 = 0.0f;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        float f3 = 0.0f;
        float f4 = 0.5f;
        float f5 = 0.0f;
        float f6 = 1.0f;
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
                case Logger.FINE /*5*/:
                    iBinder = zza.zzq(parcel, readInt);
                    break;
                case Logger.FINER /*6*/:
                    f = zza.zzl(parcel, readInt);
                    break;
                case Logger.FINEST /*7*/:
                    f2 = zza.zzl(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    z = zza.zzc(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    z2 = zza.zzc(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    z3 = zza.zzc(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    f3 = zza.zzl(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    f4 = zza.zzl(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                    f5 = zza.zzl(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_animationVelocity /*14*/:
                    f6 = zza.zzl(parcel, readInt);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new MarkerOptions(i, latLng, str, str2, iBinder, f, f2, z, z2, z3, f3, f4, f5, f6);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfr(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new MarkerOptions[x0];
    }
}
