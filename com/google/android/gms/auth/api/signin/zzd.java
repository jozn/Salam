package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzd implements Creator<GoogleSignInOptions> {
    static void zza(GoogleSignInOptions googleSignInOptions, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, googleSignInOptions.versionCode);
        zzb.zzc$62107c48(parcel, 2, googleSignInOptions.zzmu());
        zzb.zza$377a007(parcel, 3, googleSignInOptions.zzSo, i);
        zzb.zza(parcel, 4, googleSignInOptions.zzVD);
        zzb.zza(parcel, 5, googleSignInOptions.zzVE);
        zzb.zza(parcel, 6, googleSignInOptions.zzVF);
        zzb.zza$2cfb68bf(parcel, 7, googleSignInOptions.zzVG);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        String str = null;
        boolean z = false;
        int zzau = zza.zzau(x0);
        boolean z2 = false;
        boolean z3 = false;
        Account account = null;
        ArrayList arrayList = null;
        int i = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    arrayList = zza.zzc(x0, readInt, Scope.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    account = (Account) zza.zza(x0, readInt, Account.CREATOR);
                    break;
                case Logger.CONFIG /*4*/:
                    z3 = zza.zzc(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    z2 = zza.zzc(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    z = zza.zzc(x0, readInt);
                    break;
                case Logger.FINEST /*7*/:
                    str = zza.zzp(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new GoogleSignInOptions(i, arrayList, account, z3, z2, z, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new GoogleSignInOptions[x0];
    }
}
