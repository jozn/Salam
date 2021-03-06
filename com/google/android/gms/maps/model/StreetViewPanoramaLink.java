package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.Arrays;

public class StreetViewPanoramaLink implements SafeParcelable {
    public static final zzk CREATOR;
    public final float bearing;
    final int mVersionCode;
    public final String panoId;

    static {
        CREATOR = new zzk();
    }

    StreetViewPanoramaLink(int versionCode, String panoId, float bearing) {
        this.mVersionCode = versionCode;
        this.panoId = panoId;
        if (((double) bearing) <= 0.0d) {
            bearing = (bearing % 360.0f) + 360.0f;
        }
        this.bearing = bearing % 360.0f;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StreetViewPanoramaLink)) {
            return false;
        }
        StreetViewPanoramaLink streetViewPanoramaLink = (StreetViewPanoramaLink) o;
        return this.panoId.equals(streetViewPanoramaLink.panoId) && Float.floatToIntBits(this.bearing) == Float.floatToIntBits(streetViewPanoramaLink.bearing);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.panoId, Float.valueOf(this.bearing)});
    }

    public String toString() {
        return zzw.zzx(this).zzg("panoId", this.panoId).zzg("bearing", Float.valueOf(this.bearing)).toString();
    }

    public void writeToParcel(Parcel out, int flags) {
        zzk.zza$7cf702f(this, out);
    }
}
