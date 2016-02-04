package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.kyleduo.switchbutton.C0473R;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;

class ConnectivityManagerCompatHoneycombMR2 {
    ConnectivityManagerCompatHoneycombMR2() {
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
            case Logger.FINEST /*7*/:
            case C0473R.styleable.SwitchButton_thumb_height /*9*/:
                return false;
            default:
                return true;
        }
    }
}
