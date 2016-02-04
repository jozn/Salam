package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.server.converter.StringToIntConverter.Entry;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzb implements Creator<StringToIntConverter> {
    static void zza$dc69de4(StringToIntConverter stringToIntConverter, Parcel parcel) {
        int zzG = com.google.android.gms.common.internal.safeparcel.zzb.zzG(parcel, 20293);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, stringToIntConverter.mVersionCode);
        List arrayList = new ArrayList();
        for (String str : stringToIntConverter.zzakP.keySet()) {
            arrayList.add(new Entry(str, ((Integer) stringToIntConverter.zzakP.get(str)).intValue()));
        }
        com.google.android.gms.common.internal.safeparcel.zzb.zzc$62107c48(parcel, 2, arrayList);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = zza.zzau(x0);
        int i = 0;
        ArrayList arrayList = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    arrayList = zza.zzc(x0, readInt, Entry.CREATOR);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new StringToIntConverter(i, arrayList);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new StringToIntConverter[x0];
    }
}
