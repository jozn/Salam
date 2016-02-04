package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.kyleduo.switchbutton.C0473R;
import java.util.List;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzc implements Creator<GoogleSignInAccount> {
    static void zza(GoogleSignInAccount googleSignInAccount, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, googleSignInAccount.versionCode);
        zzb.zza$2cfb68bf(parcel, 2, googleSignInAccount.zzxX);
        zzb.zza$2cfb68bf(parcel, 3, googleSignInAccount.zzUN);
        zzb.zza$2cfb68bf(parcel, 4, googleSignInAccount.zzVt);
        zzb.zza$2cfb68bf(parcel, 5, googleSignInAccount.zzVu);
        zzb.zza$377a007(parcel, 6, googleSignInAccount.zzVv, i);
        zzb.zza$2cfb68bf(parcel, 7, googleSignInAccount.zzVw);
        zzb.zza(parcel, 8, googleSignInAccount.zzVx);
        zzb.zza$2cfb68bf(parcel, 9, googleSignInAccount.zzVy);
        zzb.zzc$62107c48(parcel, 10, googleSignInAccount.zzTV);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        List list = null;
        int zzau = zza.zzau(x0);
        int i = 0;
        long j = 0;
        String str = null;
        String str2 = null;
        Uri uri = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    str6 = zza.zzp(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    str5 = zza.zzp(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    str4 = zza.zzp(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    str3 = zza.zzp(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    uri = (Uri) zza.zza(x0, readInt, Uri.CREATOR);
                    break;
                case Logger.FINEST /*7*/:
                    str2 = zza.zzp(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    j = zza.zzi(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    str = zza.zzp(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    list = zza.zzc(x0, readInt, Scope.CREATOR);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new GoogleSignInAccount(i, str6, str5, str4, str3, uri, str2, j, str, list);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new GoogleSignInAccount[x0];
    }
}
