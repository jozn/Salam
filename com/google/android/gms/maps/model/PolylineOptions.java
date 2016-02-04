package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.support.v4.view.ViewCompat;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.List;

public final class PolylineOptions implements SafeParcelable {
    public static final zzi CREATOR;
    int mColor;
    final int mVersionCode;
    final List<LatLng> zzaQL;
    boolean zzaQN;
    float zzaQj;
    boolean zzaQk;
    float zzaQo;

    static {
        CREATOR = new zzi();
    }

    public PolylineOptions() {
        this.zzaQo = 10.0f;
        this.mColor = ViewCompat.MEASURED_STATE_MASK;
        this.zzaQj = 0.0f;
        this.zzaQk = true;
        this.zzaQN = false;
        this.mVersionCode = 1;
        this.zzaQL = new ArrayList();
    }

    PolylineOptions(int versionCode, List points, float width, int color, float zIndex, boolean visible, boolean geodesic) {
        this.zzaQo = 10.0f;
        this.mColor = ViewCompat.MEASURED_STATE_MASK;
        this.zzaQj = 0.0f;
        this.zzaQk = true;
        this.zzaQN = false;
        this.mVersionCode = versionCode;
        this.zzaQL = points;
        this.zzaQo = width;
        this.mColor = color;
        this.zzaQj = zIndex;
        this.zzaQk = visible;
        this.zzaQN = geodesic;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzi.zza$37f6d9f8(this, out);
    }
}
