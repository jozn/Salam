package cz.msebera.android.httpclient.cookie;

import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.TextUtils;
import java.util.Locale;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class CookieOrigin {
    public final String host;
    public final String path;
    public final int port;
    public final boolean secure;

    public CookieOrigin(String host, int port, String path, boolean secure) {
        Args.notBlank(host, "Host");
        Args.notNegative(port, "Port");
        Args.notNull(path, "Path");
        this.host = host.toLowerCase(Locale.ROOT);
        this.port = port;
        if (TextUtils.isBlank(path)) {
            this.path = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        } else {
            this.path = path;
        }
        this.secure = secure;
    }

    public final String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append('[');
        if (this.secure) {
            buffer.append("(secure)");
        }
        buffer.append(this.host);
        buffer.append(':');
        buffer.append(Integer.toString(this.port));
        buffer.append(this.path);
        buffer.append(']');
        return buffer.toString();
    }
}
