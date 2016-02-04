package com.arellomobile.android.push.preference;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public enum VibrateType {
    DEFAULT_MODE(0),
    NO_VIBRATE(1),
    ALWAYS(2);
    
    private final int value;

    private VibrateType(int i) {
        this.value = i;
    }

    public static VibrateType fromInt(int i) {
        switch (i) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                return DEFAULT_MODE;
            case Logger.SEVERE /*1*/:
                return NO_VIBRATE;
            case Logger.WARNING /*2*/:
                return ALWAYS;
            default:
                return DEFAULT_MODE;
        }
    }
}
