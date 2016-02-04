package com.google.android.gms.common.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import com.google.android.gms.common.api.Scope;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public final class zzac extends Button {
    public zzac(Context context) {
        this(context, null);
    }

    public zzac(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 16842824);
    }

    public static boolean zza(Scope[] scopeArr) {
        if (scopeArr == null) {
            return false;
        }
        for (Scope scope : scopeArr) {
            String str = scope.zzaeW;
            if (str.contains("/plus.") && !str.equals("https://www.googleapis.com/auth/plus.me")) {
                return true;
            }
            if (str.equals("https://www.googleapis.com/auth/games")) {
                return true;
            }
        }
        return false;
    }

    public static int zzd(int i, int i2, int i3) {
        switch (i) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
            case Logger.SEVERE /*1*/:
                return i3;
            case Logger.WARNING /*2*/:
                return i2;
            default:
                throw new IllegalStateException("Unknown button size: " + i);
        }
    }

    public static int zzf(int i, int i2, int i3, int i4) {
        switch (i) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                return i2;
            case Logger.SEVERE /*1*/:
                return i3;
            case Logger.WARNING /*2*/:
                return i4;
            default:
                throw new IllegalStateException("Unknown color scheme: " + i);
        }
    }
}
