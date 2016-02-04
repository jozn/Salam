package com.google.android.gms.clearcut;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.playlog.internal.PlayLoggerContext;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzc implements Creator<LogEventParcelable> {
    static void zza(LogEventParcelable logEventParcelable, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, logEventParcelable.versionCode);
        zzb.zza$377a007(parcel, 2, logEventParcelable.zzadx, i);
        zzb.zza$52910762(parcel, 3, logEventParcelable.zzady);
        int[] iArr = logEventParcelable.zzadz;
        if (iArr != null) {
            int zzG2 = zzb.zzG(parcel, 4);
            parcel.writeIntArray(iArr);
            zzb.zzH(parcel, zzG2);
        }
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = zza.zzau(x0);
        byte[] bArr = null;
        PlayLoggerContext playLoggerContext = null;
        int i = 0;
        int[] iArr = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    playLoggerContext = (PlayLoggerContext) zza.zza(x0, readInt, PlayLoggerContext.CREATOR);
                    break;
                case Logger.INFO /*3*/:
                    bArr = zza.zzs(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    iArr = zza.zzv(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new LogEventParcelable(i, playLoggerContext, bArr, iArr);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new LogEventParcelable[x0];
    }
}
