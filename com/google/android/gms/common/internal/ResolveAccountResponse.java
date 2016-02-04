package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzp.zza;

public class ResolveAccountResponse implements SafeParcelable {
    public static final Creator<ResolveAccountResponse> CREATOR;
    final int mVersionCode;
    boolean zzafR;
    IBinder zzaiS;
    ConnectionResult zzakB;
    boolean zzakC;

    static {
        CREATOR = new zzz();
    }

    ResolveAccountResponse(int versionCode, IBinder accountAccessorBinder, ConnectionResult connectionResult, boolean saveDefaultAccount, boolean isFromCrossClientAuth) {
        this.mVersionCode = versionCode;
        this.zzaiS = accountAccessorBinder;
        this.zzakB = connectionResult;
        this.zzafR = saveDefaultAccount;
        this.zzakC = isFromCrossClientAuth;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResolveAccountResponse)) {
            return false;
        }
        ResolveAccountResponse resolveAccountResponse = (ResolveAccountResponse) o;
        return this.zzakB.equals(resolveAccountResponse.zzakB) && zza.zzaP(this.zzaiS).equals(zza.zzaP(resolveAccountResponse.zzaiS));
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzz.zza(this, dest, flags);
    }
}
