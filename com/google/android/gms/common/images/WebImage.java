package com.google.android.gms.common.images;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.Arrays;

public final class WebImage implements SafeParcelable {
    public static final Creator<WebImage> CREATOR;
    final int mVersionCode;
    final Uri zzair;
    final int zzov;
    final int zzow;

    static {
        CREATOR = new zzb();
    }

    WebImage(int versionCode, Uri url, int width, int height) {
        this.mVersionCode = versionCode;
        this.zzair = url;
        this.zzov = width;
        this.zzow = height;
    }

    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(other instanceof WebImage)) {
            return false;
        }
        WebImage webImage = (WebImage) other;
        return zzw.equal(this.zzair, webImage.zzair) && this.zzov == webImage.zzov && this.zzow == webImage.zzow;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.zzair, Integer.valueOf(this.zzov), Integer.valueOf(this.zzow)});
    }

    public final String toString() {
        return String.format("Image %dx%d %s", new Object[]{Integer.valueOf(this.zzov), Integer.valueOf(this.zzow), this.zzair.toString()});
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }
}
