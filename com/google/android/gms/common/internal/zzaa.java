package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzaa implements Creator<SignInButtonConfig> {
    static void zza(SignInButtonConfig signInButtonConfig, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, signInButtonConfig.mVersionCode);
        zzb.zzc(parcel, 2, signInButtonConfig.zzakD);
        zzb.zzc(parcel, 3, signInButtonConfig.zzakE);
        zzb.zza$2d7953c6(parcel, 4, signInButtonConfig.zzaem, i);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int i = 0;
        int zzau = zza.zzau(x0);
        Scope[] scopeArr = null;
        int i2 = 0;
        int i3 = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i3 = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    i2 = zza.zzg(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    scopeArr = (Scope[]) zza.zzb(x0, readInt, Scope.CREATOR);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new SignInButtonConfig(i3, i2, i, scopeArr);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new SignInButtonConfig[x0];
    }
}
