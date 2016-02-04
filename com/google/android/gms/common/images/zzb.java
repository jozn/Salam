package com.google.android.gms.common.images;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzb implements Creator<WebImage> {
    static void zza(WebImage webImage, Parcel parcel, int i) {
        int zzG = com.google.android.gms.common.internal.safeparcel.zzb.zzG(parcel, 20293);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, webImage.mVersionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$377a007(parcel, 2, webImage.zzair, i);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 3, webImage.zzov);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 4, webImage.zzow);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = zza.zzau(x0);
        int i = 0;
        Uri uri = null;
        int i2 = 0;
        int i3 = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i2 = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    uri = (Uri) zza.zza(x0, readInt, Uri.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    i3 = zza.zzg(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new WebImage(i2, uri, i, i3);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new WebImage[x0];
    }
}
