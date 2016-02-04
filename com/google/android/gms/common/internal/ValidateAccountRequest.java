package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ValidateAccountRequest implements SafeParcelable {
    public static final Creator<ValidateAccountRequest> CREATOR;
    final int mVersionCode;
    final String zzUr;
    final Scope[] zzaem;
    final IBinder zzaiS;
    final int zzakH;
    final Bundle zzakI;

    static {
        CREATOR = new zzae();
    }

    ValidateAccountRequest(int versionCode, int clientVersion, IBinder accountAccessorBinder, Scope[] scopes, Bundle extraArgs, String callingPackage) {
        this.mVersionCode = versionCode;
        this.zzakH = clientVersion;
        this.zzaiS = accountAccessorBinder;
        this.zzaem = scopes;
        this.zzakI = extraArgs;
        this.zzUr = callingPackage;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzae.zza(this, dest, flags);
    }
}
