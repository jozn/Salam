package cz.msebera.android.httpclient.cookie;

import java.io.Serializable;
import java.util.Comparator;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class CookiePathComparator implements Serializable, Comparator<Cookie> {
    public static final CookiePathComparator INSTANCE;

    public final /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
        Cookie cookie = (Cookie) obj2;
        String normalizePath = normalizePath((Cookie) obj);
        String normalizePath2 = normalizePath(cookie);
        if (!normalizePath.equals(normalizePath2)) {
            if (normalizePath.startsWith(normalizePath2)) {
                return -1;
            }
            if (normalizePath2.startsWith(normalizePath)) {
                return 1;
            }
        }
        return 0;
    }

    static {
        INSTANCE = new CookiePathComparator();
    }

    private static String normalizePath(Cookie cookie) {
        String path = cookie.getPath();
        if (path == null) {
            path = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        }
        if (path.endsWith(MqttTopic.TOPIC_LEVEL_SEPARATOR)) {
            return path;
        }
        return path + '/';
    }
}
