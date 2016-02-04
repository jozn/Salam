package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzi implements Creator<GetServiceRequest> {
    static void zza(GetServiceRequest getServiceRequest, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, getServiceRequest.version);
        zzb.zzc(parcel, 2, getServiceRequest.zzajA);
        zzb.zzc(parcel, 3, getServiceRequest.zzajB);
        zzb.zza$2cfb68bf(parcel, 4, getServiceRequest.zzajC);
        zzb.zza$cdac282(parcel, 5, getServiceRequest.zzajD);
        zzb.zza$2d7953c6(parcel, 6, getServiceRequest.zzajE, i);
        zzb.zza$f7bef55(parcel, 7, getServiceRequest.zzajF);
        zzb.zza$377a007(parcel, 8, getServiceRequest.zzajG, i);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        int i = 0;
        Account account = null;
        int zzau = zza.zzau(x0);
        Bundle bundle = null;
        Scope[] scopeArr = null;
        IBinder iBinder = null;
        String str = null;
        int i2 = 0;
        int i3 = 0;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i3 = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    i2 = zza.zzg(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    str = zza.zzp(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    iBinder = zza.zzq(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    scopeArr = (Scope[]) zza.zzb(x0, readInt, Scope.CREATOR);
                    break;
                case Logger.FINEST /*7*/:
                    bundle = zza.zzr(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    account = (Account) zza.zza(x0, readInt, Account.CREATOR);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new GetServiceRequest(i3, i2, i, str, iBinder, scopeArr, bundle, account);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new GetServiceRequest[x0];
    }
}
