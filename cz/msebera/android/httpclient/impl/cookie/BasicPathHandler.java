package cz.msebera.android.httpclient.impl.cookie;

import cz.msebera.android.httpclient.cookie.CommonCookieAttributeHandler;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.cookie.CookieOrigin;
import cz.msebera.android.httpclient.cookie.CookieRestrictionViolationException;
import cz.msebera.android.httpclient.cookie.MalformedCookieException;
import cz.msebera.android.httpclient.cookie.SetCookie;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.TextUtils;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class BasicPathHandler implements CommonCookieAttributeHandler {
    public final void parse(SetCookie cookie, String value) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (TextUtils.isBlank(value)) {
            value = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        }
        cookie.setPath(value);
    }

    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        if (!match(cookie, origin)) {
            throw new CookieRestrictionViolationException("Illegal 'path' attribute \"" + cookie.getPath() + "\". Path of origin: \"" + origin.path + "\"");
        }
    }

    public final boolean match(Cookie cookie, CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        String str = origin.path;
        String path = cookie.getPath();
        if (path == null) {
            path = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        }
        if (path.length() > 1 && path.endsWith(MqttTopic.TOPIC_LEVEL_SEPARATOR)) {
            path = path.substring(0, path.length() - 1);
        }
        if (str.startsWith(path)) {
            if (path.equals(MqttTopic.TOPIC_LEVEL_SEPARATOR)) {
                return true;
            }
            if (str.length() == path.length()) {
                return true;
            }
            if (str.charAt(path.length()) == '/') {
                return true;
            }
        }
        return false;
    }

    public final String getAttributeName() {
        return "path";
    }
}
