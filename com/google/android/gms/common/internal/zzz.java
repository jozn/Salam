package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzz implements Creator<ResolveAccountResponse> {
    static void zza(ResolveAccountResponse resolveAccountResponse, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, resolveAccountResponse.mVersionCode);
        zzb.zza$cdac282(parcel, 2, resolveAccountResponse.zzaiS);
        zzb.zza$377a007(parcel, 3, resolveAccountResponse.zzakB, i);
        zzb.zza(parcel, 4, resolveAccountResponse.zzafR);
        zzb.zza(parcel, 5, resolveAccountResponse.zzakC);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        ConnectionResult connectionResult = null;
        boolean z = false;
        int zzau = zza.zzau(x0);
        boolean z2 = false;
        IBinder iBinder = null;
        int i = 0;
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
                    connectionResult = (ConnectionResult) zza.zza(x0, readInt, ConnectionResult.CREATOR);
                    break;
                case Logger.CONFIG /*4*/:
                    z2 = zza.zzc(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    z = zza.zzc(x0, readInt);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new ResolveAccountResponse(i, iBinder, connectionResult, z2, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new ResolveAccountResponse[x0];
    }
}
