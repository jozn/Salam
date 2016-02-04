package com.google.android.gms.maps.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.maps.internal.zzc.zza;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class zzy {
    private static Context zzaPW;
    private static zzc zzaPX;

    private static Context getRemoteContext(Context context) {
        if (zzaPW == null) {
            zzaPW = GooglePlayServicesUtil.getRemoteContext(context);
        }
        return zzaPW;
    }

    private static <T> T zza(ClassLoader classLoader, String str) {
        try {
            return zzc(((ClassLoader) zzx.zzy(classLoader)).loadClass(str));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unable to find dynamic class " + str);
        }
    }

    public static zzc zzaP(Context context) throws GooglePlayServicesNotAvailableException {
        zzx.zzy(context);
        if (zzaPX != null) {
            return zzaPX;
        }
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        switch (isGooglePlayServicesAvailable) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                Log.i(zzy.class.getSimpleName(), "Making Creator dynamically");
                zzaPX = zza.zzcu((IBinder) zza(getRemoteContext(context).getClassLoader(), "com.google.android.gms.maps.internal.CreatorImpl"));
                try {
                    zzaPX.zzd(zze.zzB(getRemoteContext(context).getResources()), GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
                    return zzaPX;
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                }
            default:
                throw new GooglePlayServicesNotAvailableException(isGooglePlayServicesAvailable);
        }
    }

    private static <T> T zzc(Class<?> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException("Unable to instantiate the dynamic class " + cls.getName());
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Unable to call the default constructor of " + cls.getName());
        }
    }
}
