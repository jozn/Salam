package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.EmailSignInOptions;
import com.google.android.gms.auth.api.signin.FacebookSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public final class SignInConfiguration implements SafeParcelable {
    public static final Creator<SignInConfiguration> CREATOR;
    final int versionCode;
    String zzVG;
    final String zzWh;
    EmailSignInOptions zzWi;
    GoogleSignInOptions zzWj;
    FacebookSignInOptions zzWk;
    String zzWl;

    static {
        CREATOR = new zzm();
    }

    SignInConfiguration(int versionCode, String consumerPkgName, String serverClientId, EmailSignInOptions emailConfig, GoogleSignInOptions googleConfig, FacebookSignInOptions facebookConfig, String apiKey) {
        this.versionCode = versionCode;
        this.zzWh = zzx.zzcG(consumerPkgName);
        this.zzVG = serverClientId;
        this.zzWi = emailConfig;
        this.zzWj = googleConfig;
        this.zzWk = facebookConfig;
        this.zzWl = apiKey;
    }

    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            SignInConfiguration signInConfiguration = (SignInConfiguration) obj;
            if (!this.zzWh.equals(signInConfiguration.zzWh)) {
                return false;
            }
            if (TextUtils.isEmpty(this.zzVG)) {
                if (!TextUtils.isEmpty(signInConfiguration.zzVG)) {
                    return false;
                }
            } else if (!this.zzVG.equals(signInConfiguration.zzVG)) {
                return false;
            }
            if (TextUtils.isEmpty(this.zzWl)) {
                if (!TextUtils.isEmpty(signInConfiguration.zzWl)) {
                    return false;
                }
            } else if (!this.zzWl.equals(signInConfiguration.zzWl)) {
                return false;
            }
            if (this.zzWi == null) {
                if (signInConfiguration.zzWi != null) {
                    return false;
                }
            } else if (!this.zzWi.equals(signInConfiguration.zzWi)) {
                return false;
            }
            if (this.zzWk == null) {
                if (signInConfiguration.zzWk != null) {
                    return false;
                }
            } else if (!this.zzWk.equals(signInConfiguration.zzWk)) {
                return false;
            }
            if (this.zzWj == null) {
                if (signInConfiguration.zzWj != null) {
                    return false;
                }
            } else if (!this.zzWj.equals(signInConfiguration.zzWj)) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public final int hashCode() {
        return new zze().zzo(this.zzWh).zzo(this.zzVG).zzo(this.zzWl).zzo(this.zzWi).zzo(this.zzWj).zzo(this.zzWk).zzWb;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzm.zza(this, out, flags);
    }
}
