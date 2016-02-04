package com.google.android.gms.auth.firstparty.shared;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.kyleduo.switchbutton.C0473R;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzc implements Creator<ScopeDetail> {
    static void zza(ScopeDetail scopeDetail, Parcel parcel, int i) {
        int zzG = zzb.zzG(parcel, 20293);
        zzb.zzc(parcel, 1, scopeDetail.version);
        zzb.zza$2cfb68bf(parcel, 2, scopeDetail.description);
        zzb.zza$2cfb68bf(parcel, 3, scopeDetail.zzWR);
        zzb.zza$2cfb68bf(parcel, 4, scopeDetail.zzWS);
        zzb.zza$2cfb68bf(parcel, 5, scopeDetail.zzWT);
        zzb.zza$2cfb68bf(parcel, 6, scopeDetail.zzWU);
        zzb.zzb$62107c48(parcel, 7, scopeDetail.zzWV);
        zzb.zza$377a007(parcel, 8, scopeDetail.zzWW, i);
        zzb.zzH(parcel, zzG);
    }

    public final /* synthetic */ Object createFromParcel(Parcel x0) {
        FACLData fACLData = null;
        int zzau = zza.zzau(x0);
        int i = 0;
        List arrayList = new ArrayList();
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        while (x0.dataPosition() < zzau) {
            int readInt = x0.readInt();
            switch (SupportMenu.USER_MASK & readInt) {
                case Logger.SEVERE /*1*/:
                    i = zza.zzg(x0, readInt);
                    break;
                case Logger.WARNING /*2*/:
                    str5 = zza.zzp(x0, readInt);
                    break;
                case Logger.INFO /*3*/:
                    str4 = zza.zzp(x0, readInt);
                    break;
                case Logger.CONFIG /*4*/:
                    str3 = zza.zzp(x0, readInt);
                    break;
                case Logger.FINE /*5*/:
                    str2 = zza.zzp(x0, readInt);
                    break;
                case Logger.FINER /*6*/:
                    str = zza.zzp(x0, readInt);
                    break;
                case Logger.FINEST /*7*/:
                    arrayList = zza.zzD(x0, readInt);
                    break;
                case C0473R.styleable.SwitchButton_thumb_width /*8*/:
                    fACLData = (FACLData) zza.zza(x0, readInt, FACLData.CREATOR);
                    break;
                default:
                    zza.zzb(x0, readInt);
                    break;
            }
        }
        if (x0.dataPosition() == zzau) {
            return new ScopeDetail(i, str5, str4, str3, str2, str, arrayList, fACLData);
        }
        throw new zza.zza("Overread allowed size end=" + zzau, x0);
    }

    public final /* synthetic */ Object[] newArray(int x0) {
        return new ScopeDetail[x0];
    }
}
