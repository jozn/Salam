package com.google.android.gms.maps;

import android.graphics.Bitmap;
import android.os.RemoteException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.zzw.zza;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.internal.zzf;

public final class GoogleMap {
    public final IGoogleMapDelegate zzaOy;

    /* renamed from: com.google.android.gms.maps.GoogleMap.5 */
    class C04645 extends zza {
        final /* synthetic */ GoogleMap zzaOB;
        final /* synthetic */ SnapshotReadyCallback zzaOF;

        public C04645(GoogleMap googleMap, SnapshotReadyCallback snapshotReadyCallback) {
            this.zzaOB = googleMap;
            this.zzaOF = snapshotReadyCallback;
        }

        public final void onSnapshotReady(Bitmap snapshot) throws RemoteException {
            this.zzaOF.onSnapshotReady(snapshot);
        }

        public final void zzr(zzd com_google_android_gms_dynamic_zzd) throws RemoteException {
            this.zzaOF.onSnapshotReady((Bitmap) zze.zzp(com_google_android_gms_dynamic_zzd));
        }
    }

    public interface SnapshotReadyCallback {
        void onSnapshotReady(Bitmap bitmap);
    }

    protected GoogleMap(IGoogleMapDelegate map) {
        this.zzaOy = (IGoogleMapDelegate) zzx.zzy(map);
    }

    public final Marker addMarker(MarkerOptions options) {
        try {
            zzf addMarker = this.zzaOy.addMarker(options);
            return addMarker != null ? new Marker(addMarker) : null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera$ee6a4a2(CameraUpdate update) {
        try {
            this.zzaOy.animateCameraWithDurationAndCallback(update.zzaOw, 2000, null);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void moveCamera(CameraUpdate update) {
        try {
            this.zzaOy.moveCamera(update.zzaOw);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
