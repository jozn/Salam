package cz.msebera.android.httpclient.client.utils;

import android.support.v7.appcompat.BuildConfig;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.TextUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Stack;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class URIUtils {
    public static URI rewriteURI(URI uri, HttpHost target, boolean dropFragment) throws URISyntaxException {
        Args.notNull(uri, "URI");
        if (uri.isOpaque()) {
            return uri;
        }
        URIBuilder uribuilder = new URIBuilder(uri);
        if (target != null) {
            uribuilder.scheme = target.getSchemeName();
            uribuilder.setHost(target.getHostName());
            uribuilder.setPort(target.getPort());
        } else {
            uribuilder.scheme = null;
            uribuilder.setHost(null);
            uribuilder.setPort(-1);
        }
        if (dropFragment) {
            uribuilder.setFragment$2c8be85b();
        }
        if (TextUtils.isEmpty(uribuilder.path)) {
            uribuilder.setPath(MqttTopic.TOPIC_LEVEL_SEPARATOR);
        }
        return uribuilder.build();
    }

    public static URI rewriteURI(URI uri) throws URISyntaxException {
        Args.notNull(uri, "URI");
        if (uri.isOpaque()) {
            return uri;
        }
        URIBuilder uribuilder = new URIBuilder(uri);
        if (uribuilder.userInfo != null) {
            uribuilder.userInfo = null;
            uribuilder.encodedSchemeSpecificPart = null;
            uribuilder.encodedAuthority = null;
            uribuilder.encodedUserInfo = null;
        }
        if (TextUtils.isEmpty(uribuilder.path)) {
            uribuilder.setPath(MqttTopic.TOPIC_LEVEL_SEPARATOR);
        }
        if (uribuilder.host != null) {
            uribuilder.setHost(uribuilder.host.toLowerCase(Locale.ROOT));
        }
        uribuilder.setFragment$2c8be85b();
        return uribuilder.build();
    }

    public static URI normalizeSyntax(URI uri) {
        if (uri.isOpaque() || uri.getAuthority() == null) {
            return uri;
        }
        Args.check(uri.isAbsolute(), "Base URI must be absolute");
        String path = uri.getPath() == null ? BuildConfig.VERSION_NAME : uri.getPath();
        String[] inputSegments = path.split(MqttTopic.TOPIC_LEVEL_SEPARATOR);
        Stack<String> outputSegments = new Stack();
        for (String inputSegment : inputSegments) {
            if (!(inputSegment.isEmpty() || ".".equals(inputSegment))) {
                if (!"..".equals(inputSegment)) {
                    outputSegments.push(inputSegment);
                } else if (!outputSegments.isEmpty()) {
                    outputSegments.pop();
                }
            }
        }
        StringBuilder outputBuffer = new StringBuilder();
        Iterator it = outputSegments.iterator();
        while (it.hasNext()) {
            outputBuffer.append('/').append((String) it.next());
        }
        if (path.lastIndexOf(47) == path.length() - 1) {
            outputBuffer.append('/');
        }
        try {
            URI ref = new URI(uri.getScheme().toLowerCase(Locale.ROOT), uri.getAuthority().toLowerCase(Locale.ROOT), outputBuffer.toString(), null, null);
            if (uri.getQuery() == null && uri.getFragment() == null) {
                return ref;
            }
            StringBuilder normalized = new StringBuilder(ref.toASCIIString());
            if (uri.getQuery() != null) {
                normalized.append('?').append(uri.getRawQuery());
            }
            if (uri.getFragment() != null) {
                normalized.append('#').append(uri.getRawFragment());
            }
            return URI.create(normalized.toString());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static HttpHost extractHost(URI uri) {
        if (uri == null) {
            return null;
        }
        if (!uri.isAbsolute()) {
            return null;
        }
        int port = uri.getPort();
        String host = uri.getHost();
        if (host == null) {
            host = uri.getAuthority();
            if (host != null) {
                int at = host.indexOf(64);
                if (at >= 0) {
                    if (host.length() > at + 1) {
                        host = host.substring(at + 1);
                    } else {
                        host = null;
                    }
                }
                if (host != null) {
                    int colon = host.indexOf(58);
                    if (colon >= 0) {
                        int pos = colon + 1;
                        int len = 0;
                        int i = pos;
                        while (i < host.length() && Character.isDigit(host.charAt(i))) {
                            len++;
                            i++;
                        }
                        if (len > 0) {
                            try {
                                port = Integer.parseInt(host.substring(pos, pos + len));
                            } catch (NumberFormatException e) {
                            }
                        }
                        host = host.substring(0, colon);
                    }
                }
            }
        }
        String scheme = uri.getScheme();
        if (TextUtils.isBlank(host)) {
            return null;
        }
        try {
            return new HttpHost(host, port, scheme);
        } catch (IllegalArgumentException e2) {
            return null;
        }
    }
}
