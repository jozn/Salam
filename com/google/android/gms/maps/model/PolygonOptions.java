package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.support.v4.view.ViewCompat;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.List;

public final class PolygonOptions implements SafeParcelable {
    public static final zzh CREATOR;
    final int mVersionCode;
    final List<LatLng> zzaQL;
    final List<List<LatLng>> zzaQM;
    boolean zzaQN;
    float zzaQg;
    int zzaQh;
    int zzaQi;
    float zzaQj;
    boolean zzaQk;

    static {
        CREATOR = new zzh();
    }

    public PolygonOptions() {
        this.zzaQg = 10.0f;
        this.zzaQh = ViewCompat.MEASURED_STATE_MASK;
        this.zzaQi = 0;
        this.zzaQj = 0.0f;
        this.zzaQk = true;
        this.zzaQN = false;
        this.mVersionCode = 1;
        this.zzaQL = new ArrayList();
        this.zzaQM = new ArrayList();
    }

    PolygonOptions(int versionCode, List<LatLng> points, List holes, float strokeWidth, int strokeColor, int fillColor, float zIndex, boolean visible, boolean geodesic) {
        this.zzaQg = 10.0f;
        this.zzaQh = ViewCompat.MEASURED_STATE_MASK;
        this.zzaQi = 0;
        this.zzaQj = 0.0f;
        this.zzaQk = true;
        this.zzaQN = false;
        this.mVersionCode = versionCode;
        this.zzaQL = points;
        this.zzaQM = holes;
        this.zzaQg = strokeWidth;
        this.zzaQh = strokeColor;
        this.zzaQi = fillColor;
        this.zzaQj = zIndex;
        this.zzaQk = visible;
        this.zzaQN = geodesic;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzh.zza$4c96f4fe(this, out);
    }
}
