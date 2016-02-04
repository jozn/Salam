package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzp.zza;

public class GetServiceRequest implements SafeParcelable {
    public static final Creator<GetServiceRequest> CREATOR;
    final int version;
    final int zzajA;
    int zzajB;
    String zzajC;
    IBinder zzajD;
    Scope[] zzajE;
    Bundle zzajF;
    Account zzajG;

    static {
        CREATOR = new zzi();
    }

    GetServiceRequest(int version, int serviceId, int clientVersion, String callingPackage, IBinder accountAccessorBinder, Scope[] scopes, Bundle extraArgs, Account clientRequestedAccount) {
        this.version = version;
        this.zzajA = serviceId;
        this.zzajB = clientVersion;
        this.zzajC = callingPackage;
        if (version < 2) {
            Account account = null;
            if (accountAccessorBinder != null) {
                account = zza.zzb(zza.zzaP(accountAccessorBinder));
            }
            this.zzajG = account;
        } else {
            this.zzajD = accountAccessorBinder;
            this.zzajG = clientRequestedAccount;
        }
        this.zzajE = scopes;
        this.zzajF = extraArgs;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzi.zza(this, dest, flags);
    }
}
