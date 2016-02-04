package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.maps.model.CameraPosition;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zza implements Creator<GoogleMapOptions> {
    static void zza(GoogleMapOptions googleMapOptions, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, googleMapOptions.mVersionCode);
        zzb.zza(parcel, 2, com.google.android.gms.maps.internal.zza.zze(googleMapOptions.zzaOR));
        zzb.zza(parcel, 3, com.google.android.gms.maps.internal.zza.zze(googleMapOptions.zzaOS));
        zzb.zzc(parcel, 4, googleMapOptions.zzaOT);
        zzb.zza$377a007(parcel, 5, googleMapOptions.zzaOU, i);
        zzb.zza(parcel, 6, com.google.android.gms.maps.internal.zza.zze(googleMapOptions.zzaOV));
        zzb.zza(parcel, 7, com.google.android.gms.maps.internal.zza.zze(googleMapOptions.zzaOW));
        zzb.zza(parcel, 8, com.google.android.gms.maps.internal.zza.zze(googleMapOptions.zzaOX));
        zzb.zza(parcel, 9, com.google.android.gms.maps.internal.zza.zze(googleMapOptions.zzaOY));
        zzb.zza(parcel, 10, com.google.android.gms.maps.internal.zza.zze(googleMapOptions.zzaOZ));
        zzb.zza(parcel, 11, com.google.android.gms.maps.internal.zza.zze(googleMapOptions.zzaPa));
        zzb.zza(parcel, 12, com.google.android.gms.maps.internal.zza.zze(googleMapOptions.zzaPb));
        zzb.zza(parcel, 14, com.google.android.gms.maps.internal.zza.zze(googleMapOptions.zzaPc));
        zzb.zza(parcel, 15, com.google.android.gms.maps.internal.zza.zze(googleMapOptions.zzaPd));
        zzb.zzH(parcel, zzG);
    }

    public static GoogleMapOptions zzfk(Parcel parcel) {
        int zzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(parcel);
        int i = 0;
        byte b = (byte) -1;
        byte b2 = (byte) -1;
        int i2 = 0;
        CameraPosition cameraPosition = null;
        byte b3 = (byte) -1;
        byte b4 = (byte) -1;
        byte b5 = (byte) -1;
        byte b6 = (byte) -1;
        byte b7 = (byte) -1;
        byte b8 = (byte) -1;
        byte b9 = (byte) -1;
        byte b10 = (byte) -1;
        byte b11 = (byte) -1;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    b = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case Logger.INFO /*3*/:
                    b2 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, readInt);
                    break;
                case Logger.FINE /*5*/:
                    cameraPosition = (CameraPosition) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, readInt, CameraPosition.CREATOR);
                    break;
                case Logger.FINER /*6*/:
                    b3 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case Logger.FINEST /*7*/:
                    b4 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    b5 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    b6 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    b7 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    b8 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    b9 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_animationVelocity /*14*/:
                    b10 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                case C0473R.styleable.SwitchButton_radius /*15*/:
                    b11 = com.google.android.gms.common.internal.safeparcel.zza.zze(parcel, readInt);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new GoogleMapOptions(i, b, b2, i2, cameraPosition, b3, b4, b5, b6, b7, b8, b9, b10, b11);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfk(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new GoogleMapOptions[x0];
    }
}
