package com.google.android.gms.auth;

import android.os.Parcel;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import java.util.Arrays;
import java.util.List;

public class TokenData implements SafeParcelable {
    public static final zzd CREATOR;
    final int mVersionCode;
    final String zzTR;
    final Long zzTS;
    final boolean zzTT;
    final boolean zzTU;
    final List<String> zzTV;

    static {
        CREATOR = new zzd();
    }

    TokenData(int versionCode, String token, Long expirationTimeSecs, boolean isCached, boolean isSnowballed, List<String> grantedScopes) {
        this.mVersionCode = versionCode;
        this.zzTR = zzx.zzcG(token);
        this.zzTS = expirationTimeSecs;
        this.zzTT = isCached;
        this.zzTU = isSnowballed;
        this.zzTV = grantedScopes;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof TokenData)) {
            return false;
        }
        TokenData tokenData = (TokenData) o;
        return TextUtils.equals(this.zzTR, tokenData.zzTR) && zzw.equal(this.zzTS, tokenData.zzTS) && this.zzTT == tokenData.zzTT && this.zzTU == tokenData.zzTU && zzw.equal(this.zzTV, tokenData.zzTV);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.zzTR, this.zzTS, Boolean.valueOf(this.zzTT), Boolean.valueOf(this.zzTU), this.zzTV});
    }

    public void writeToParcel(Parcel out, int flags) {
        zzd.zza$6d52043c(this, out);
    }
}
