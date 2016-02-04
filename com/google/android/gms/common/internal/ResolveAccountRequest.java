package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class ResolveAccountRequest implements SafeParcelable {
    public static final Creator<ResolveAccountRequest> CREATOR;
    final int mVersionCode;
    final Account zzSo;
    final GoogleSignInAccount zzakA;
    final int zzakz;

    static {
        CREATOR = new zzy();
    }

    ResolveAccountRequest(int versionCode, Account account, int sessionId, GoogleSignInAccount signInAccountHint) {
        this.mVersionCode = versionCode;
        this.zzSo = account;
        this.zzakz = sessionId;
        this.zzakA = signInAccountHint;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        zzy.zza(this, dest, flags);
    }
}
