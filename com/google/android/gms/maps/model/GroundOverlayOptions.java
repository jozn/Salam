package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.zzd.zza;

public final class GroundOverlayOptions implements SafeParcelable {
    public static final zzc CREATOR;
    final int mVersionCode;
    float zzaQc;
    float zzaQj;
    boolean zzaQk;
    BitmapDescriptor zzaQm;
    LatLng zzaQn;
    float zzaQo;
    float zzaQp;
    LatLngBounds zzaQq;
    float zzaQr;
    float zzaQs;
    float zzaQt;

    static {
        CREATOR = new zzc();
    }

    public GroundOverlayOptions() {
        this.zzaQk = true;
        this.zzaQr = 0.0f;
        this.zzaQs = 0.5f;
        this.zzaQt = 0.5f;
        this.mVersionCode = 1;
    }

    GroundOverlayOptions(int versionCode, IBinder wrappedImage, LatLng location, float width, float height, LatLngBounds bounds, float bearing, float zIndex, boolean visible, float transparency, float anchorU, float anchorV) {
        this.zzaQk = true;
        this.zzaQr = 0.0f;
        this.zzaQs = 0.5f;
        this.zzaQt = 0.5f;
        this.mVersionCode = versionCode;
        this.zzaQm = new BitmapDescriptor(zza.zzbs(wrappedImage));
        this.zzaQn = location;
        this.zzaQo = width;
        this.zzaQp = height;
        this.zzaQq = bounds;
        this.zzaQc = bearing;
        this.zzaQj = zIndex;
        this.zzaQk = visible;
        this.zzaQr = transparency;
        this.zzaQs = anchorU;
        this.zzaQt = anchorV;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzc.zza(this, out, flags);
    }
}
