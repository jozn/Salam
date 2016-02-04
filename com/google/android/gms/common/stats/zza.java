package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zza implements Creator<ConnectionEvent> {
    static void zza$151b04f0(ConnectionEvent connectionEvent, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, connectionEvent.mVersionCode);
        zzb.zza(parcel, 2, connectionEvent.zzaln);
        zzb.zza$2cfb68bf(parcel, 4, connectionEvent.zzalp);
        zzb.zza$2cfb68bf(parcel, 5, connectionEvent.zzalq);
        zzb.zza$2cfb68bf(parcel, 6, connectionEvent.zzalr);
        zzb.zza$2cfb68bf(parcel, 7, connectionEvent.zzals);
        zzb.zza$2cfb68bf(parcel, 8, connectionEvent.zzalt);
        zzb.zza(parcel, 10, connectionEvent.zzalv);
        zzb.zza(parcel, 11, connectionEvent.zzalw);
        zzb.zzc(parcel, 12, connectionEvent.zzalo);
        zzb.zza$2cfb68bf(parcel, 13, connectionEvent.zzalu);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(x0);
        int i = 0;
        long j = 0;
        int i2 = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        long j2 = 0;
        long j3 = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    j = com.google.android.gms.common.internal.safeparcel.zza.zzi(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzp(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    str2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    str3 = com.google.android.gms.common.internal.safeparcel.zza.zzp(x0, readInt);
                    break;
                case Logger.FINEST /*7*/:
                    str4 = com.google.android.gms.common.internal.safeparcel.zza.zzp(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    str5 = com.google.android.gms.common.internal.safeparcel.zza.zzp(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    j2 = com.google.android.gms.common.internal.safeparcel.zza.zzi(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    j3 = com.google.android.gms.common.internal.safeparcel.zza.zzi(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                    str6 = com.google.android.gms.common.internal.safeparcel.zza.zzp(x0, readInt);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new ConnectionEvent(i, j, i2, str, str2, str3, str4, str5, str6, j2, j3);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new ConnectionEvent[x0];
    }
}
