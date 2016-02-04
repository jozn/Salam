package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public class SignInAccount implements SafeParcelable {
    public static final Creator<SignInAccount> CREATOR;
    final int versionCode;
    String zzJg;
    String zzUN;
    String zzVL;
    GoogleSignInAccount zzVO;
    String zzVP;
    String zzVt;
    String zzVu;
    Uri zzVv;

    static {
        CREATOR = new zzf();
    }

    SignInAccount(int versionCode, String providerId, String idToken, String email, String displayName, Uri photoUrl, GoogleSignInAccount googleSignInAccount, String userId, String refreshToken) {
        this.versionCode = versionCode;
        this.zzVt = zzx.zzh(email, "Email cannot be empty.");
        this.zzVu = displayName;
        this.zzVv = photoUrl;
        this.zzVL = providerId;
        this.zzUN = idToken;
        this.zzVO = googleSignInAccount;
        this.zzJg = zzx.zzcG(userId);
        this.zzVP = refreshToken;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }
}
