package com.google.android.gms.signin.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzg implements Creator<RecordConsentRequest> {
    static void zza(RecordConsentRequest recordConsentRequest, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, recordConsentRequest.mVersionCode);
        zzb.zza$377a007(parcel, 2, recordConsentRequest.zzSo, i);
        zzb.zza$2d7953c6(parcel, 3, recordConsentRequest.zzbbW, i);
        zzb.zza$2cfb68bf(parcel, 4, recordConsentRequest.zzVG);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = zza.zzau(x0);
        Scope[] scopeArr = null;
        Account account = null;
        int i = 0;
        String str = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    account = (Account) zza.zza(x0, readInt, Account.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    scopeArr = (Scope[]) zza.zzb(x0, readInt, Scope.CREATOR);
                    break;
                case Logger.CONFIG /*4*/:
                    str = zza.zzp(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new RecordConsentRequest(i, account, scopeArr, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new RecordConsentRequest[x0];
    }
}
