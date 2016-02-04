package com.google.android.gms.maps;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.zza;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;

public final class StreetViewPanoramaOptions implements SafeParcelable {
    public static final zzb CREATOR;
    final int mVersionCode;
    Boolean zzaOS;
    Boolean zzaOY;
    StreetViewPanoramaCamera zzaPF;
    String zzaPG;
    LatLng zzaPH;
    Integer zzaPI;
    Boolean zzaPJ;
    Boolean zzaPK;
    Boolean zzaPL;

    static {
        CREATOR = new zzb();
    }

    public StreetViewPanoramaOptions() {
        this.zzaPJ = Boolean.valueOf(true);
        this.zzaOY = Boolean.valueOf(true);
        this.zzaPK = Boolean.valueOf(true);
        this.zzaPL = Boolean.valueOf(true);
        this.mVersionCode = 1;
    }

    StreetViewPanoramaOptions(int versionCode, StreetViewPanoramaCamera camera, String panoId, LatLng position, Integer radius, byte userNavigationEnabled, byte zoomGesturesEnabled, byte panningGesturesEnabled, byte streetNamesEnabled, byte useViewLifecycleInFragment) {
        this.zzaPJ = Boolean.valueOf(true);
        this.zzaOY = Boolean.valueOf(true);
        this.zzaPK = Boolean.valueOf(true);
        this.zzaPL = Boolean.valueOf(true);
        this.mVersionCode = versionCode;
        this.zzaPF = camera;
        this.zzaPH = position;
        this.zzaPI = radius;
        this.zzaPG = panoId;
        this.zzaPJ = zza.zza(userNavigationEnabled);
        this.zzaOY = zza.zza(zoomGesturesEnabled);
        this.zzaPK = zza.zza(panningGesturesEnabled);
        this.zzaPL = zza.zza(streetNamesEnabled);
        this.zzaOS = zza.zza(useViewLifecycleInFragment);
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }
}
