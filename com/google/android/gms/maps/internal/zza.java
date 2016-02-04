package com.google.android.gms.maps.internal;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zza {
    public static Boolean zza(byte b) {
        switch (b) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                return Boolean.FALSE;
            case Logger.SEVERE /*1*/:
                return Boolean.TRUE;
            default:
                return null;
        }
    }

    public static byte zze(Boolean bool) {
        return bool != null ? bool.booleanValue() ? (byte) 1 : (byte) 0 : (byte) -1;
    }
}
