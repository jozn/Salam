package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzy implements Creator<ResolveAccountRequest> {
    static void zza(ResolveAccountRequest resolveAccountRequest, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, resolveAccountRequest.mVersionCode);
        zzb.zza$377a007(parcel, 2, resolveAccountRequest.zzSo, i);
        zzb.zzc(parcel, 3, resolveAccountRequest.zzakz);
        zzb.zza$377a007(parcel, 4, resolveAccountRequest.zzakA, i);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = zza.zzau(x0);
        int i = 0;
        Account account = null;
        int i2 = 0;
        GoogleSignInAccount googleSignInAccount = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i2 = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    account = (Account) zza.zza(x0, readInt, Account.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    googleSignInAccount = (GoogleSignInAccount) zza.zza(x0, readInt, GoogleSignInAccount.CREATOR);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new ResolveAccountRequest(i2, account, i, googleSignInAccount);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new ResolveAccountRequest[x0];
    }
}
