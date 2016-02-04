package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzc implements Creator<AccountChangeEventsResponse> {
    static void zza$346cd6a8(AccountChangeEventsResponse accountChangeEventsResponse, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, accountChangeEventsResponse.mVersion);
        zzb.zzc$62107c48(parcel, 2, accountChangeEventsResponse.zzpw);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = zza.zzau(x0);
        int i = 0;
        List list = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    list = zza.zzc(x0, readInt, AccountChangeEvent.CREATOR);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new AccountChangeEventsResponse(i, list);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new AccountChangeEventsResponse[x0];
    }
}
