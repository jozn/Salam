package com.google.android.gms.maps;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.internal.zzc;
import com.google.android.gms.maps.internal.zzy;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.internal.zza;

public final class MapsInitializer {
    private static boolean zznK;

    static {
        zznK = false;
    }

    public static synchronized int initialize(Context context) {
        int i;
        synchronized (MapsInitializer.class) {
            zzx.zzb((Object) context, (Object) "Context is null");
            if (zznK) {
                i = 0;
            } else {
                try {
                    zzc zzaP = zzy.zzaP(context);
                    CameraUpdateFactory.zzaOx = (ICameraUpdateFactoryDelegate) zzx.zzy(zzaP.zzzp());
                    zza zzzq = zzaP.zzzq();
                    if (BitmapDescriptorFactory.zzaPY == null) {
                        BitmapDescriptorFactory.zzaPY = (zza) zzx.zzy(zzzq);
                    }
                    zznK = true;
                    i = 0;
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                    i = e2.errorCode;
                }
            }
        }
        return i;
    }
}
