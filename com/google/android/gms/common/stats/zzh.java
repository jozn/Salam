package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.kyleduo.switchbutton.C0473R;
import java.util.List;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzh implements Creator<WakeLockEvent> {
    static void zza$7ab08521(WakeLockEvent wakeLockEvent, Parcel parcel) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, wakeLockEvent.mVersionCode);
        zzb.zza(parcel, 2, wakeLockEvent.zzaln);
        zzb.zza$2cfb68bf(parcel, 4, wakeLockEvent.zzalZ);
        zzb.zzc(parcel, 5, wakeLockEvent.zzama);
        zzb.zzb$62107c48(parcel, 6, wakeLockEvent.zzamb);
        zzb.zza(parcel, 8, wakeLockEvent.zzalv);
        zzb.zza$2cfb68bf(parcel, 10, wakeLockEvent.zzame);
        zzb.zzc(parcel, 11, wakeLockEvent.zzalo);
        zzb.zza$2cfb68bf(parcel, 12, wakeLockEvent.zzamc);
        zzb.zza$2cfb68bf(parcel, 13, wakeLockEvent.zzamf);
        zzb.zzc(parcel, 14, wakeLockEvent.zzamd);
        zzb.zza(parcel, 15, wakeLockEvent.zzamg);
        zzb.zza(parcel, 16, wakeLockEvent.mTimeout);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int zzau = zza.zzau(x0);
        int i = 0;
        long j = 0;
        int i2 = 0;
        String str = null;
        int i3 = 0;
        List list = null;
        String str2 = null;
        long j2 = 0;
        int i4 = 0;
        String str3 = null;
        String str4 = null;
        float f = 0.0f;
        long j3 = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    j = zza.zzi(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    str = zza.zzp(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    i3 = zza.zzg(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    list = zza.zzD(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    j2 = zza.zzi(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_onColor /*10*/:
                    str3 = zza.zzp(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_offColor /*11*/:
                    i2 = zza.zzg(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumbColor /*12*/:
                    str2 = zza.zzp(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumbPressedColor /*13*/:
                    str4 = zza.zzp(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_animationVelocity /*14*/:
                    i4 = zza.zzg(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_radius /*15*/:
                    f = zza.zzl(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_measureFactor /*16*/:
                    j3 = zza.zzi(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new WakeLockEvent(i, j, i2, str, i3, list, str2, j2, i4, str3, str4, f, j3);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new WakeLockEvent[x0];
    }
}
