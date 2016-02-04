package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zza implements Creator<EmailSignInOptions> {
    static void zza(EmailSignInOptions emailSignInOptions, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, emailSignInOptions.versionCode);
        zzb.zza$377a007(parcel, 2, emailSignInOptions.zzVo, i);
        zzb.zza$2cfb68bf(parcel, 3, emailSignInOptions.zzVp);
        zzb.zza$377a007(parcel, 4, emailSignInOptions.zzVq, i);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(x0);
        String str = null;
        Uri uri = null;
        int i = 0;
        Uri uri2 = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    uri = (Uri) com.google.android.gms.common.internal.safeparcel.zza.zza(x0, readInt, Uri.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzp(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    uri2 = (Uri) com.google.android.gms.common.internal.safeparcel.zza.zza(x0, readInt, Uri.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new EmailSignInOptions(i, uri, str, uri2);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new EmailSignInOptions[x0];
    }
}
