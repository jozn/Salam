package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.zzd.zza;

public final class MarkerOptions implements SafeParcelable {
    public static final zzf CREATOR;
    float mAlpha;
    final int mVersionCode;
    public LatLng zzaPH;
    public String zzaQB;
    BitmapDescriptor zzaQC;
    boolean zzaQD;
    boolean zzaQE;
    float zzaQF;
    float zzaQG;
    float zzaQH;
    boolean zzaQk;
    float zzaQs;
    float zzaQt;
    public String zzank;

    static {
        CREATOR = new zzf();
    }

    public MarkerOptions() {
        this.zzaQs = 0.5f;
        this.zzaQt = 1.0f;
        this.zzaQk = true;
        this.zzaQE = false;
        this.zzaQF = 0.0f;
        this.zzaQG = 0.5f;
        this.zzaQH = 0.0f;
        this.mAlpha = 1.0f;
        this.mVersionCode = 1;
    }

    MarkerOptions(int versionCode, LatLng position, String title, String snippet, IBinder wrappedIcon, float anchorU, float anchorV, boolean draggable, boolean visible, boolean flat, float rotation, float infoWindowAnchorU, float infoWindowAnchorV, float alpha) {
        this.zzaQs = 0.5f;
        this.zzaQt = 1.0f;
        this.zzaQk = true;
        this.zzaQE = false;
        this.zzaQF = 0.0f;
        this.zzaQG = 0.5f;
        this.zzaQH = 0.0f;
        this.mAlpha = 1.0f;
        this.mVersionCode = versionCode;
        this.zzaPH = position;
        this.zzank = title;
        this.zzaQB = snippet;
        this.zzaQC = wrappedIcon == null ? null : new BitmapDescriptor(zza.zzbs(wrappedIcon));
        this.zzaQs = anchorU;
        this.zzaQt = anchorV;
        this.zzaQD = draggable;
        this.zzaQk = visible;
        this.zzaQE = flat;
        this.zzaQF = rotation;
        this.zzaQG = infoWindowAnchorU;
        this.zzaQH = infoWindowAnchorV;
        this.mAlpha = alpha;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzf.zza(this, out, flags);
    }
}
