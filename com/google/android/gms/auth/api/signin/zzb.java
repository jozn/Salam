package com.google.android.gms.auth.api.signin;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import java.util.ArrayList;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzb implements Creator<FacebookSignInOptions> {
    static void zza(FacebookSignInOptions facebookSignInOptions, Parcel parcel, int i) {
        int zzG = com.google.android.gms.common.internal.safeparcel.zzb.zzG(parcel, 20293);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, facebookSignInOptions.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza$377a007(parcel, 2, facebookSignInOptions.mIntent, i);
        com.google.android.gms.common.internal.safeparcel.zzb.zzb$62107c48(parcel, 3, facebookSignInOptions.zzmu());
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = zza.zzau(x0);
        Intent intent = null;
        int i = 0;
        ArrayList arrayList = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    intent = (Intent) zza.zza(x0, readInt, Intent.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    arrayList = zza.zzD(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new FacebookSignInOptions(i, intent, arrayList);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new FacebookSignInOptions[x0];
    }
}
