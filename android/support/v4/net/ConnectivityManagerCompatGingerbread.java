package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

class ConnectivityManagerCompatGingerbread {
    ConnectivityManagerCompatGingerbread() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return true;
        }
        switch (info.getType()) {
            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
            case Logger.WARNING /*2*/:
            case Logger.INFO /*3*/:
            case Logger.CONFIG /*4*/:
            case Logger.FINE /*5*/:
            case Logger.FINER /*6*/:
                return true;
            case Logger.SEVERE /*1*/:
                return false;
            default:
                return true;
        }
    }
}
