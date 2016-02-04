package com.google.android.gms.maps;

import android.os.RemoteException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public final class CameraUpdateFactory {
    static ICameraUpdateFactoryDelegate zzaOx;

    public static CameraUpdate newLatLngZoom$6c32fdd3(LatLng latLng) {
        try {
            return new CameraUpdate(zzyT().newLatLngZoom(latLng, 16.0f));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public static CameraUpdate zoomTo$44fa1c82() {
        try {
            return new CameraUpdate(zzyT().zoomTo(16.0f));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    private static ICameraUpdateFactoryDelegate zzyT() {
        return (ICameraUpdateFactoryDelegate) zzx.zzb(zzaOx, (Object) "CameraUpdateFactory is not initialized");
    }
}
