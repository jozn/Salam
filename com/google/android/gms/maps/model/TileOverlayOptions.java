package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.model.internal.zzi;
import com.google.android.gms.maps.model.internal.zzi.zza;

public final class TileOverlayOptions implements SafeParcelable {
    public static final zzo CREATOR;
    final int mVersionCode;
    zzi zzaQR;
    private TileProvider zzaQS;
    boolean zzaQT;
    float zzaQj;
    boolean zzaQk;

    /* renamed from: com.google.android.gms.maps.model.TileOverlayOptions.1 */
    class C04711 implements TileProvider {
        private final zzi zzaQU;
        final /* synthetic */ TileOverlayOptions zzaQV;

        C04711(TileOverlayOptions tileOverlayOptions) {
            this.zzaQV = tileOverlayOptions;
            this.zzaQU = this.zzaQV.zzaQR;
        }
    }

    static {
        CREATOR = new zzo();
    }

    public TileOverlayOptions() {
        this.zzaQk = true;
        this.zzaQT = true;
        this.mVersionCode = 1;
    }

    TileOverlayOptions(int versionCode, IBinder delegate, boolean visible, float zIndex, boolean fadeIn) {
        this.zzaQk = true;
        this.zzaQT = true;
        this.mVersionCode = versionCode;
        this.zzaQR = zza.zzdh(delegate);
        this.zzaQS = this.zzaQR == null ? null : new C04711(this);
        this.zzaQk = visible;
        this.zzaQj = zIndex;
        this.zzaQT = fadeIn;
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel out, int flags) {
        zzo.zza$4b899d8a(this, out);
    }
}
