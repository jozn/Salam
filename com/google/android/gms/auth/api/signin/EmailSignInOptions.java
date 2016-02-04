package com.google.android.gms.auth.api.signin;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Patterns;
import com.google.android.gms.auth.api.signin.internal.zze;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public class EmailSignInOptions implements SafeParcelable {
    public static final Creator<EmailSignInOptions> CREATOR;
    final int versionCode;
    final Uri zzVo;
    String zzVp;
    Uri zzVq;

    static {
        CREATOR = new zza();
    }

    EmailSignInOptions(int versionCode, Uri serverWidgetUrl, String modeQueryName, Uri tosUrl) {
        zzx.zzb((Object) serverWidgetUrl, (Object) "Server widget url cannot be null in order to use email/password sign in.");
        zzx.zzh(serverWidgetUrl.toString(), "Server widget url cannot be null in order to use email/password sign in.");
        zzx.zzb(Patterns.WEB_URL.matcher(serverWidgetUrl.toString()).matches(), (Object) "Invalid server widget url");
        this.versionCode = versionCode;
        this.zzVo = serverWidgetUrl;
        this.zzVp = modeQueryName;
        this.zzVq = tosUrl;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            EmailSignInOptions emailSignInOptions = (EmailSignInOptions) obj;
            if (!this.zzVo.equals(emailSignInOptions.zzVo)) {
                return false;
            }
            if (this.zzVq == null) {
                if (emailSignInOptions.zzVq != null) {
                    return false;
                }
            } else if (!this.zzVq.equals(emailSignInOptions.zzVq)) {
                return false;
            }
            if (TextUtils.isEmpty(this.zzVp)) {
                if (!TextUtils.isEmpty(emailSignInOptions.zzVp)) {
                    return false;
                }
            } else if (!this.zzVp.equals(emailSignInOptions.zzVp)) {
                return false;
            }
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public int hashCode() {
        return new zze().zzo(this.zzVo).zzo(this.zzVq).zzo(this.zzVp).zzWb;
    }

    public void writeToParcel(Parcel out, int flags) {
        zza.zza(this, out, flags);
    }
}
