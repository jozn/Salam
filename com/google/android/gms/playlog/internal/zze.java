package com.google.android.gms.playlog.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zze implements Creator<PlayLoggerContext> {
    static void zza$495264e0(PlayLoggerContext playLoggerContext, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, playLoggerContext.versionCode);
        zzb.zza$2cfb68bf(parcel, 2, playLoggerContext.packageName);
        zzb.zzc(parcel, 3, playLoggerContext.zzaYy);
        zzb.zzc(parcel, 4, playLoggerContext.zzaYz);
        zzb.zza$2cfb68bf(parcel, 5, playLoggerContext.zzaYA);
        zzb.zza$2cfb68bf(parcel, 6, playLoggerContext.zzaYB);
        zzb.zza(parcel, 7, playLoggerContext.zzaYC);
        zzb.zza$2cfb68bf(parcel, 8, playLoggerContext.zzaYD);
        zzb.zza(parcel, 9, playLoggerContext.zzaYE);
        zzb.zzc(parcel, 10, playLoggerContext.zzaYF);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        String str = null;
        int i = 0;
        int zzau = zza.zzau(x0);
        boolean z = true;
        boolean z2 = false;
        String str2 = null;
        String str3 = null;
        int i2 = 0;
        int i3 = 0;
        String str4 = null;
        int i4 = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i4 = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    str4 = zza.zzp(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    i3 = zza.zzg(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    i2 = zza.zzg(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    str3 = zza.zzp(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    str2 = zza.zzp(x0, readInt);
                    break;
                case Logger.FINEST /*7*/:
                    z = zza.zzc(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    str = zza.zzp(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    z2 = zza.zzc(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    i = zza.zzg(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new PlayLoggerContext(i4, str4, i3, i2, str3, str2, z, str, z2, i);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new PlayLoggerContext[x0];
    }
}
