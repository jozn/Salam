package com.google.android.gms.common.data;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zza implements Creator<BitmapTeleporter> {
    static void zza(BitmapTeleporter bitmapTeleporter, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, bitmapTeleporter.mVersionCode);
        zzb.zza$377a007(parcel, 2, bitmapTeleporter.zzHf, i);
        zzb.zzc(parcel, 3, bitmapTeleporter.zzZU);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(x0);
        ParcelFileDescriptor parcelFileDescriptor = null;
        int i = 0;
        int i2 = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    parcelFileDescriptor = (ParcelFileDescriptor) com.google.android.gms.common.internal.safeparcel.zza.zza(x0, readInt, ParcelFileDescriptor.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new BitmapTeleporter(i, parcelFileDescriptor, i2);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new BitmapTeleporter[x0];
    }
}
