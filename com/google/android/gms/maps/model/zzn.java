package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzn implements Creator<Tile> {
    static void zza$2bae1718(Tile tile, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, tile.mVersionCode);
        zzb.zzc(parcel, 2, tile.width);
        zzb.zzc(parcel, 3, tile.height);
        zzb.zza$52910762(parcel, 4, tile.data);
        zzb.zzH(parcel, zzG);
    }

    public static Tile zzfz(Parcel parcel) {
        int i = 0;
        int zzau = zza.zzau(parcel);
        byte[] bArr = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzau) {
            int readInt = parcel.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i3 = zza.zzg(parcel, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    i2 = zza.zzg(parcel, readInt);
                    break;
                case Logger.INFO /*3*/:
                    i = zza.zzg(parcel, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    bArr = zza.zzs(parcel, readInt);
                    break;
                default:
                    zza.zzb(parcel, readInt);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new Tile(i3, i2, i, bArr);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, parcel);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        return zzfz(x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new Tile[x0];
    }
}
