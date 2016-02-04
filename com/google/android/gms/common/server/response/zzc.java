package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.server.response.FieldMappingDictionary.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzc implements Creator<FieldMappingDictionary> {
    static void zza$51a5452c(FieldMappingDictionary fieldMappingDictionary, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, fieldMappingDictionary.mVersionCode);
        List arrayList = new ArrayList();
        for (String str : fieldMappingDictionary.zzale.keySet()) {
            arrayList.add(new Entry(str, (Map) fieldMappingDictionary.zzale.get(str)));
        }
        zzb.zzc$62107c48(parcel, 2, arrayList);
        zzb.zza$2cfb68bf(parcel, 3, fieldMappingDictionary.zzalg);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        String str = null;
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
                case Logger.INFO /*3*/:
                    str = zza.zzp(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new FieldMappingDictionary(i, arrayList, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new FieldMappingDictionary[x0];
    }
}
