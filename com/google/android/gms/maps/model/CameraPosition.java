package com.google.android.gms.maps.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.C0237R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.common.internal.zzx;
import java.util.Arrays;

public final class CameraPosition implements SafeParcelable {
    public static final zza CREATOR;
    public final float bearing;
    final int mVersionCode;
    public final LatLng target;
    public final float tilt;
    public final float zoom;

    public static final class Builder {
        LatLng zzaPZ;
        float zzaQa;
        float zzaQb;
        float zzaQc;
    }

    static {
        CREATOR = new zza();
    }

    CameraPosition(int versionCode, LatLng target, float zoom, float tilt, float bearing) {
        zzx.zzb((Object) target, (Object) "null camera target");
        boolean z = 0.0f <= tilt && tilt <= 90.0f;
        zzx.zzb(z, "Tilt needs to be between 0 and 90 inclusive: %s", Float.valueOf(tilt));
        this.mVersionCode = versionCode;
        this.target = target;
        this.zoom = zoom;
        this.tilt = tilt + 0.0f;
        if (((double) bearing) <= 0.0d) {
            bearing = (bearing % 360.0f) + 360.0f;
        }
        this.bearing = bearing % 360.0f;
    }

    private CameraPosition(LatLng target, float zoom, float tilt, float bearing) {
        this(1, target, zoom, tilt, bearing);
    }

    public static CameraPosition createFromAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return null;
        }
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attrs, C0237R.styleable.MapAttrs);
        LatLng latLng = new LatLng((double) (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_cameraTargetLat) ? obtainAttributes.getFloat(C0237R.styleable.MapAttrs_cameraTargetLat, 0.0f) : 0.0f), (double) (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_cameraTargetLng) ? obtainAttributes.getFloat(C0237R.styleable.MapAttrs_cameraTargetLng, 0.0f) : 0.0f));
        Builder builder = new Builder();
        builder.zzaPZ = latLng;
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_cameraZoom)) {
            builder.zzaQa = obtainAttributes.getFloat(C0237R.styleable.MapAttrs_cameraZoom, 0.0f);
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_cameraBearing)) {
            builder.zzaQc = obtainAttributes.getFloat(C0237R.styleable.MapAttrs_cameraBearing, 0.0f);
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_cameraTilt)) {
            builder.zzaQb = obtainAttributes.getFloat(C0237R.styleable.MapAttrs_cameraTilt, 0.0f);
        }
        return new CameraPosition(builder.zzaPZ, builder.zzaQa, builder.zzaQb, builder.zzaQc);
    }

    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CameraPosition)) {
            return false;
        }
        CameraPosition cameraPosition = (CameraPosition) o;
        return this.target.equals(cameraPosition.target) && Float.floatToIntBits(this.zoom) == Float.floatToIntBits(cameraPosition.zoom) && Float.floatToIntBits(this.tilt) == Float.floatToIntBits(cameraPosition.tilt) && Float.floatToIntBits(this.bearing) == Float.floatToIntBits(cameraPosition.bearing);
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.target, Float.valueOf(this.zoom), Float.valueOf(this.tilt), Float.valueOf(this.bearing)});
    }

    public final String toString() {
        return zzw.zzx(this).zzg("target", this.target).zzg("zoom", Float.valueOf(this.zoom)).zzg("tilt", Float.valueOf(this.tilt)).zzg("bearing", Float.valueOf(this.bearing)).toString();
    }

    public final void writeToParcel(Parcel out, int flags) {
        zza.zza(this, out, flags);
    }
}
