package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zza implements Creator<ConverterWrapper> {
    static void zza(ConverterWrapper converterWrapper, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, converterWrapper.mVersionCode);
        zzb.zza$377a007(parcel, 2, converterWrapper.zzakO, i);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(x0);
        int i = 0;
        StringToIntConverter stringToIntConverter = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    stringToIntConverter = (StringToIntConverter) com.google.android.gms.common.internal.safeparcel.zza.zza(x0, readInt, StringToIntConverter.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new ConverterWrapper(i, stringToIntConverter);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new ConverterWrapper[x0];
    }
}
