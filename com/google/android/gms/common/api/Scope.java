package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public final class Scope implements SafeParcelable {
    public static final Creator<Scope> CREATOR;
    final int mVersionCode;
    public final String zzaeW;

    static {
        CREATOR = new zzc();
    }

    Scope(int versionCode, String scopeUri) {
        zzx.zzh(scopeUri, "scopeUri must not be null or empty");
        this.mVersionCode = versionCode;
        this.zzaeW = scopeUri;
    }

    public Scope(String scopeUri) {
        this(1, scopeUri);
    }

    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return !(o instanceof Scope) ? false : this.zzaeW.equals(((Scope) o).zzaeW);
    }

    public final int hashCode() {
        return this.zzaeW.hashCode();
    }

    public final String toString() {
        return this.zzaeW;
    }

    public final void writeToParcel(Parcel dest, int flags) {
        zzc.zza$514aa83(this, dest);
    }
}
