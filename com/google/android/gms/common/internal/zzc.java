package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzc implements Creator<AuthAccountRequest> {
    static void zza(AuthAccountRequest authAccountRequest, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, authAccountRequest.mVersionCode);
        zzb.zza$cdac282(parcel, 2, authAccountRequest.zzaiS);
        zzb.zza$2d7953c6(parcel, 3, authAccountRequest.zzaem, i);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        Scope[] scopeArr = null;
        int zzau = zza.zzau(x0);
        int i = 0;
        IBinder iBinder = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    iBinder = zza.zzq(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    scopeArr = (Scope[]) zza.zzb(x0, readInt, Scope.CREATOR);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new AuthAccountRequest(i, iBinder, scopeArr);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new AuthAccountRequest[x0];
    }
}
