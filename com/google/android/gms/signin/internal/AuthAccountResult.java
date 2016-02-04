package com.google.android.gms.signin.internal;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class AuthAccountResult implements SafeParcelable {
    public static final Creator<AuthAccountResult> CREATOR;
    final int mVersionCode;
    int zzbbS;
    Intent zzbbT;

    static {
        CREATOR = new zza();
    }

    public AuthAccountResult() {
        this((byte) 0);
    }

    private AuthAccountResult(byte b) {
        this(2, 0, null);
    }

    AuthAccountResult(int versionCode, int connectionResultCode, Intent rawAuthResultionIntent) {
        this.mVersionCode = versionCode;
        this.zzbbS = connectionResultCode;
        this.zzbbT = rawAuthResultionIntent;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zza.zza(this, dest, flags);
    }
}
