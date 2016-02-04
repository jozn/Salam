package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.common.server.converter.ConverterWrapper;
import com.google.android.gms.common.server.response.FastJsonResponse.Field;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zza implements Creator<Field> {
    static void zza(Field field, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, field.mVersionCode);
        zzb.zzc(parcel, 2, field.zzqT());
        zzb.zza(parcel, 3, field.zzqZ());
        zzb.zzc(parcel, 4, field.zzqU());
        zzb.zza(parcel, 5, field.zzra());
        zzb.zza$2cfb68bf(parcel, 6, field.zzrb());
        zzb.zzc(parcel, 7, field.zzrc());
        zzb.zza$2cfb68bf(parcel, 8, field.zzre());
        zzb.zza$377a007(parcel, 9, field.zzald == null ? null : ConverterWrapper.zza(field.zzald), i);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        ConverterWrapper converterWrapper = null;
        int i = 0;
        int zzau = com.google.android.gms.common.internal.safeparcel.zza.zzau(x0);
        String str = null;
        String str2 = null;
        boolean z = false;
        int i2 = 0;
        boolean z2 = false;
        int i3 = 0;
        int i4 = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i4 = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    i3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    z2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    z = com.google.android.gms.common.internal.safeparcel.zza.zzc(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    str2 = com.google.android.gms.common.internal.safeparcel.zza.zzp(x0, readInt);
                    break;
                case Logger.FINEST /*7*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzp(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                    converterWrapper = (ConverterWrapper) com.google.android.gms.common.internal.safeparcel.zza.zza(x0, readInt, ConverterWrapper.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new Field(i4, i3, z2, i2, z, str2, i, str, converterWrapper);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new Field[x0];
    }
}
