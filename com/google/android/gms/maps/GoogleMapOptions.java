package com.google.android.gms.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.C0237R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.zza;
import com.google.android.gms.maps.model.CameraPosition;

public final class GoogleMapOptions implements SafeParcelable {
    public static final zza CREATOR;
    final int mVersionCode;
    Boolean zzaOR;
    Boolean zzaOS;
    int zzaOT;
    CameraPosition zzaOU;
    Boolean zzaOV;
    Boolean zzaOW;
    Boolean zzaOX;
    Boolean zzaOY;
    Boolean zzaOZ;
    Boolean zzaPa;
    Boolean zzaPb;
    Boolean zzaPc;
    Boolean zzaPd;

    static {
        CREATOR = new zza();
    }

    public GoogleMapOptions() {
        this.zzaOT = -1;
        this.mVersionCode = 1;
    }

    GoogleMapOptions(int versionCode, byte zOrderOnTop, byte useViewLifecycleInFragment, int mapType, CameraPosition camera, byte zoomControlsEnabled, byte compassEnabled, byte scrollGesturesEnabled, byte zoomGesturesEnabled, byte tiltGesturesEnabled, byte rotateGesturesEnabled, byte liteMode, byte mapToolbarEnabled, byte ambientEnabled) {
        this.zzaOT = -1;
        this.mVersionCode = versionCode;
        this.zzaOR = zza.zza(zOrderOnTop);
        this.zzaOS = zza.zza(useViewLifecycleInFragment);
        this.zzaOT = mapType;
        this.zzaOU = camera;
        this.zzaOV = zza.zza(zoomControlsEnabled);
        this.zzaOW = zza.zza(compassEnabled);
        this.zzaOX = zza.zza(scrollGesturesEnabled);
        this.zzaOY = zza.zza(zoomGesturesEnabled);
        this.zzaOZ = zza.zza(tiltGesturesEnabled);
        this.zzaPa = zza.zza(rotateGesturesEnabled);
        this.zzaPb = zza.zza(liteMode);
        this.zzaPc = zza.zza(mapToolbarEnabled);
        this.zzaPd = zza.zza(ambientEnabled);
    }

    public static GoogleMapOptions createFromAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return null;
        }
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attrs, C0237R.styleable.MapAttrs);
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_mapType)) {
            googleMapOptions.zzaOT = obtainAttributes.getInt(C0237R.styleable.MapAttrs_mapType, -1);
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_zOrderOnTop)) {
            googleMapOptions.zzaOR = Boolean.valueOf(obtainAttributes.getBoolean(C0237R.styleable.MapAttrs_zOrderOnTop, false));
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_useViewLifecycle)) {
            googleMapOptions.zzaOS = Boolean.valueOf(obtainAttributes.getBoolean(C0237R.styleable.MapAttrs_useViewLifecycle, false));
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_uiCompass)) {
            googleMapOptions.zzaOW = Boolean.valueOf(obtainAttributes.getBoolean(C0237R.styleable.MapAttrs_uiCompass, true));
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_uiRotateGestures)) {
            googleMapOptions.zzaPa = Boolean.valueOf(obtainAttributes.getBoolean(C0237R.styleable.MapAttrs_uiRotateGestures, true));
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_uiScrollGestures)) {
            googleMapOptions.zzaOX = Boolean.valueOf(obtainAttributes.getBoolean(C0237R.styleable.MapAttrs_uiScrollGestures, true));
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_uiTiltGestures)) {
            googleMapOptions.zzaOZ = Boolean.valueOf(obtainAttributes.getBoolean(C0237R.styleable.MapAttrs_uiTiltGestures, true));
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_uiZoomGestures)) {
            googleMapOptions.zzaOY = Boolean.valueOf(obtainAttributes.getBoolean(C0237R.styleable.MapAttrs_uiZoomGestures, true));
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_uiZoomControls)) {
            googleMapOptions.zzaOV = Boolean.valueOf(obtainAttributes.getBoolean(C0237R.styleable.MapAttrs_uiZoomControls, true));
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_liteMode)) {
            googleMapOptions.zzaPb = Boolean.valueOf(obtainAttributes.getBoolean(C0237R.styleable.MapAttrs_liteMode, false));
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_uiMapToolbar)) {
            googleMapOptions.zzaPc = Boolean.valueOf(obtainAttributes.getBoolean(C0237R.styleable.MapAttrs_uiMapToolbar, true));
        }
        if (obtainAttributes.hasValue(C0237R.styleable.MapAttrs_ambientEnabled)) {
            googleMapOptions.zzaPd = Boolean.valueOf(obtainAttributes.getBoolean(C0237R.styleable.MapAttrs_ambientEnabled, false));
        }
        googleMapOptions.zzaOU = CameraPosition.createFromAttributes(context, attrs);
        obtainAttributes.recycle();
        return googleMapOptions;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zza.zza(this, out, flags);
    }
}
