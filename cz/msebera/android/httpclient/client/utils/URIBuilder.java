package cz.msebera.android.httpclient.client.utils;

import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.conn.util.InetAddressUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class URIBuilder {
    public Charset charset;
    String encodedAuthority;
    private String encodedFragment;
    private String encodedPath;
    public String encodedQuery;
    public String encodedSchemeSpecificPart;
    String encodedUserInfo;
    private String fragment;
    public String host;
    public String path;
    private int port;
    public String query;
    public List<NameValuePair> queryParams;
    String scheme;
    String userInfo;

    public URIBuilder() {
        this.port = -1;
    }

    public URIBuilder(URI uri) {
        this.scheme = uri.getScheme();
        this.encodedSchemeSpecificPart = uri.getRawSchemeSpecificPart();
        this.encodedAuthority = uri.getRawAuthority();
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.encodedUserInfo = uri.getRawUserInfo();
        this.userInfo = uri.getUserInfo();
        this.encodedPath = uri.getRawPath();
        this.path = uri.getPath();
        this.encodedQuery = uri.getRawQuery();
        String rawQuery = uri.getRawQuery();
        List parse = (rawQuery == null || rawQuery.isEmpty()) ? null : URLEncodedUtils.parse(rawQuery, this.charset != null ? this.charset : Consts.UTF_8);
        this.queryParams = parse;
        this.encodedFragment = uri.getRawFragment();
        this.fragment = uri.getFragment();
    }

    public final URI build() throws URISyntaxException {
        return new URI(buildString());
    }

    private String buildString() {
        StringBuilder sb = new StringBuilder();
        if (this.scheme != null) {
            sb.append(this.scheme).append(':');
        }
        if (this.encodedSchemeSpecificPart != null) {
            sb.append(this.encodedSchemeSpecificPart);
        } else {
            if (this.encodedAuthority != null) {
                sb.append("//").append(this.encodedAuthority);
            } else if (this.host != null) {
                sb.append("//");
                if (this.encodedUserInfo != null) {
                    sb.append(this.encodedUserInfo).append("@");
                } else if (this.userInfo != null) {
                    sb.append(URLEncodedUtils.encUserInfo(this.userInfo, this.charset != null ? this.charset : Consts.UTF_8)).append("@");
                }
                if (InetAddressUtils.isIPv6Address(this.host)) {
                    sb.append("[").append(this.host).append("]");
                } else {
                    sb.append(this.host);
                }
                if (this.port >= 0) {
                    sb.append(":").append(this.port);
                }
            }
            if (this.encodedPath != null) {
                sb.append(normalizePath(this.encodedPath));
            } else if (this.path != null) {
                sb.append(URLEncodedUtils.encPath(normalizePath(this.path), this.charset != null ? this.charset : Consts.UTF_8));
            }
            if (this.encodedQuery != null) {
                sb.append("?").append(this.encodedQuery);
            } else if (this.queryParams != null) {
                sb.append("?").append(URLEncodedUtils.format(this.queryParams, this.charset != null ? this.charset : Consts.UTF_8));
            } else if (this.query != null) {
                sb.append("?").append(encodeUric(this.query));
            }
        }
        if (this.encodedFragment != null) {
            sb.append(MqttTopic.MULTI_LEVEL_WILDCARD).append(this.encodedFragment);
        } else if (this.fragment != null) {
            sb.append(MqttTopic.MULTI_LEVEL_WILDCARD).append(encodeUric(this.fragment));
        }
        return sb.toString();
    }

    private String encodeUric(String fragment) {
        return URLEncodedUtils.encUric(fragment, this.charset != null ? this.charset : Consts.UTF_8);
    }

    public final URIBuilder setHost(String host) {
        this.host = host;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        return this;
    }

    public final URIBuilder setPort(int port) {
        if (port < 0) {
            port = -1;
        }
        this.port = port;
        this.encodedSchemeSpecificPart = null;
        this.encodedAuthority = null;
        return this;
    }

    public final URIBuilder setPath(String path) {
        this.path = path;
        this.encodedSchemeSpecificPart = null;
        this.encodedPath = null;
        return this;
    }

    public final URIBuilder setFragment$2c8be85b() {
        this.fragment = null;
        this.encodedFragment = null;
        return this;
    }

    public final String toString() {
        return buildString();
    }

    private static String normalizePath(String path) {
        String s = path;
        if (path == null) {
            return null;
        }
        int n = 0;
        while (n < s.length() && s.charAt(n) == '/') {
            n++;
        }
        if (n > 1) {
            s = s.substring(n - 1);
        }
        return s;
    }
}
