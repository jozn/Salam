package cz.msebera.android.httpclient.cookie;

import android.support.v7.appcompat.BuildConfig;
import java.io.Serializable;
import java.util.Comparator;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class CookieIdentityComparator implements Serializable, Comparator<Cookie> {
    public final /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
        String domain;
        String domain2;
        Cookie cookie = (Cookie) obj;
        Cookie cookie2 = (Cookie) obj2;
        int compareTo = cookie.getName().compareTo(cookie2.getName());
        if (compareTo == 0) {
            domain = cookie.getDomain();
            if (domain == null) {
                domain = BuildConfig.VERSION_NAME;
            } else if (domain.indexOf(46) == -1) {
                domain = domain + ".local";
            }
            domain2 = cookie2.getDomain();
            if (domain2 == null) {
                domain2 = BuildConfig.VERSION_NAME;
            } else if (domain2.indexOf(46) == -1) {
                domain2 = domain2 + ".local";
            }
            compareTo = domain.compareToIgnoreCase(domain2);
        }
        if (compareTo != 0) {
            return compareTo;
        }
        domain = cookie.getPath();
        if (domain == null) {
            domain = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        }
        domain2 = cookie2.getPath();
        if (domain2 == null) {
            domain2 = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        }
        return domain.compareTo(domain2);
    }
}
