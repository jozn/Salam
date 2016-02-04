package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zze implements Creator<DataHolder> {
    static void zza(DataHolder dataHolder, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        String[] strArr = dataHolder.zzahA;
        if (strArr != null) {
            int zzG2 = zzb.zzG(parcel, 1);
            parcel.writeStringArray(strArr);
            zzb.zzH(parcel, zzG2);
        }
        zzb.zzc(parcel, 1000, dataHolder.mVersionCode);
        zzb.zza$2d7953c6(parcel, 2, dataHolder.zzahC, i);
        zzb.zzc(parcel, 3, dataHolder.zzabx);
        zzb.zza$f7bef55(parcel, 4, dataHolder.zzahD);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int i = 0;
        Bundle bundle = null;
        int zzau = zza.zzau(x0);
        CursorWindow[] cursorWindowArr = null;
        String[] strArr = null;
        int i2 = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    strArr = zza.zzB(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    cursorWindowArr = (CursorWindow[]) zza.zzb(x0, readInt, CursorWindow.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    bundle = zza.zzr(x0, readInt);
                    break;
                case 1000:
                    i2 = zza.zzg(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() != zzau) {
            throw new zza.zza("Overread allowed size end=" + zzau, x0);
        }
        DataHolder dataHolder = new DataHolder(i2, strArr, cursorWindowArr, i, bundle);
        dataHolder.zzpL();
        return dataHolder;
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new DataHolder[x0];
    }
}
