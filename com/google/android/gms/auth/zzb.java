package com.google.android.gms.auth;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzb implements Creator<AccountChangeEventsRequest> {
    static void zza(AccountChangeEventsRequest accountChangeEventsRequest, Parcel parcel, int i) {
        int zzG = com.google.android.gms.common.internal.safeparcel.zzb.zzG(parcel, 20293);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, accountChangeEventsRequest.mVersion);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 2, accountChangeEventsRequest.zzTF);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$2cfb68bf(parcel, 3, accountChangeEventsRequest.zzTD);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$377a007(parcel, 4, accountChangeEventsRequest.zzSo, i);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        Account account = null;
        int zzau = zza.zzau(x0);
        int i = 0;
        int i2 = 0;
        String str = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i2 = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    str = zza.zzp(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    account = (Account) zza.zza(x0, readInt, Account.CREATOR);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new AccountChangeEventsRequest(i2, i, str, account);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new AccountChangeEventsRequest[x0];
    }
}
