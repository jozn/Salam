package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzae implements Creator<ValidateAccountRequest> {
    static void zza(ValidateAccountRequest validateAccountRequest, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, validateAccountRequest.mVersionCode);
        zzb.zzc(parcel, 2, validateAccountRequest.zzakH);
        zzb.zza$cdac282(parcel, 3, validateAccountRequest.zzaiS);
        zzb.zza$2d7953c6(parcel, 4, validateAccountRequest.zzaem, i);
        zzb.zza$f7bef55(parcel, 5, validateAccountRequest.zzakI);
        zzb.zza$2cfb68bf(parcel, 6, validateAccountRequest.zzUr);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int i = 0;
        String str = null;
        int zzau = zza.zzau(x0);
        Bundle bundle = null;
        Scope[] scopeArr = null;
        IBinder iBinder = null;
        int i2 = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i2 = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    iBinder = zza.zzq(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    scopeArr = (Scope[]) zza.zzb(x0, readInt, Scope.CREATOR);
                    break;
                case Logger.FINE /*5*/:
                    bundle = zza.zzr(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    str = zza.zzp(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new ValidateAccountRequest(i2, i, iBinder, scopeArr, bundle, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new ValidateAccountRequest[x0];
    }
}
