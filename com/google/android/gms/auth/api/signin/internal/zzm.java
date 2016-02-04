package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.auth.api.signin.EmailSignInOptions;
import com.google.android.gms.auth.api.signin.FacebookSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzm implements Creator<SignInConfiguration> {
    static void zza(SignInConfiguration signInConfiguration, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, signInConfiguration.versionCode);
        zzb.zza$2cfb68bf(parcel, 2, signInConfiguration.zzWh);
        zzb.zza$2cfb68bf(parcel, 3, signInConfiguration.zzVG);
        zzb.zza$377a007(parcel, 4, signInConfiguration.zzWi, i);
        zzb.zza$377a007(parcel, 5, signInConfiguration.zzWj, i);
        zzb.zza$377a007(parcel, 6, signInConfiguration.zzWk, i);
        zzb.zza$2cfb68bf(parcel, 7, signInConfiguration.zzWl);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        String str = null;
        int zzau = zza.zzau(x0);
        int i = 0;
        FacebookSignInOptions facebookSignInOptions = null;
        GoogleSignInOptions googleSignInOptions = null;
        EmailSignInOptions emailSignInOptions = null;
        String str2 = null;
        String str3 = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    str3 = zza.zzp(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    str2 = zza.zzp(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    emailSignInOptions = (EmailSignInOptions) zza.zza(x0, readInt, EmailSignInOptions.CREATOR);
                    break;
                case Logger.FINE /*5*/:
                    googleSignInOptions = (GoogleSignInOptions) zza.zza(x0, readInt, GoogleSignInOptions.CREATOR);
                    break;
                case Logger.FINER /*6*/:
                    facebookSignInOptions = (FacebookSignInOptions) zza.zza(x0, readInt, FacebookSignInOptions.CREATOR);
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
            return new SignInConfiguration(i, str3, str2, emailSignInOptions, googleSignInOptions, facebookSignInOptions, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new SignInConfiguration[x0];
    }
}
