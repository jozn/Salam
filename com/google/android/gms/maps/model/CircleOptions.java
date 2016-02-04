package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.support.v4.view.ViewCompat;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class CircleOptions implements SafeParcelable {
    public static final zzb CREATOR;
    final int mVersionCode;
    LatLng zzaQe;
    double zzaQf;
    float zzaQg;
    int zzaQh;
    int zzaQi;
    float zzaQj;
    boolean zzaQk;

    static {
        CREATOR = new zzb();
    }

    public CircleOptions() {
        this.zzaQe = null;
        this.zzaQf = 0.0d;
        this.zzaQg = 10.0f;
        this.zzaQh = ViewCompat.MEASURED_STATE_MASK;
        this.zzaQi = 0;
        this.zzaQj = 0.0f;
        this.zzaQk = true;
        this.mVersionCode = 1;
    }

    CircleOptions(int versionCode, LatLng center, double radius, float strokeWidth, int strokeColor, int fillColor, float zIndex, boolean visible) {
        this.zzaQe = null;
        this.zzaQf = 0.0d;
        this.zzaQg = 10.0f;
        this.zzaQh = ViewCompat.MEASURED_STATE_MASK;
        this.zzaQi = 0;
        this.zzaQj = 0.0f;
        this.zzaQk = true;
        this.mVersionCode = versionCode;
        this.zzaQe = center;
        this.zzaQf = radius;
        this.zzaQg = strokeWidth;
        this.zzaQh = strokeColor;
        this.zzaQi = fillColor;
        this.zzaQj = zIndex;
        this.zzaQk = visible;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }
}
