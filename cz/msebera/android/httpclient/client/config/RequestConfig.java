package cz.msebera.android.httpclient.client.config;

import cz.msebera.android.httpclient.HttpHost;
import java.net.InetAddress;
import java.util.Collection;

public class RequestConfig implements Cloneable {
    public static final RequestConfig DEFAULT;
    private final boolean authenticationEnabled;
    public final boolean circularRedirectsAllowed;
    private final int connectTimeout;
    private final int connectionRequestTimeout;
    public final String cookieSpec;
    private final boolean decompressionEnabled;
    private final boolean expectContinueEnabled;
    private final InetAddress localAddress;
    private final int maxRedirects;
    private final HttpHost proxy;
    public final Collection<String> proxyPreferredAuthSchemes;
    private final boolean redirectsEnabled;
    public final boolean relativeRedirectsAllowed;
    private final int socketTimeout;
    private final boolean staleConnectionCheckEnabled;
    public final Collection<String> targetPreferredAuthSchemes;

    public static class Builder {
        public boolean authenticationEnabled;
        public boolean circularRedirectsAllowed;
        public int connectTimeout;
        public int connectionRequestTimeout;
        public String cookieSpec;
        private boolean decompressionEnabled;
        public boolean expectContinueEnabled;
        public InetAddress localAddress;
        public int maxRedirects;
        public HttpHost proxy;
        public Collection<String> proxyPreferredAuthSchemes;
        public boolean redirectsEnabled;
        public boolean relativeRedirectsAllowed;
        public int socketTimeout;
        public boolean staleConnectionCheckEnabled;
        public Collection<String> targetPreferredAuthSchemes;

        Builder() {
            this.staleConnectionCheckEnabled = false;
            this.redirectsEnabled = true;
            this.maxRedirects = 50;
            this.relativeRedirectsAllowed = true;
            this.authenticationEnabled = true;
            this.connectionRequestTimeout = -1;
            this.connectTimeout = -1;
            this.socketTimeout = -1;
            this.decompressionEnabled = true;
        }

        public final RequestConfig build() {
            return new RequestConfig(this.expectContinueEnabled, this.proxy, this.localAddress, this.staleConnectionCheckEnabled, this.cookieSpec, this.redirectsEnabled, this.relativeRedirectsAllowed, this.circularRedirectsAllowed, this.maxRedirects, this.authenticationEnabled, this.targetPreferredAuthSchemes, this.proxyPreferredAuthSchemes, this.connectionRequestTimeout, this.connectTimeout, this.socketTimeout, this.decompressionEnabled);
        }
    }

    protected /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
        return (RequestConfig) super.clone();
    }

    static {
        DEFAULT = new Builder().build();
    }

    RequestConfig(boolean expectContinueEnabled, HttpHost proxy, InetAddress localAddress, boolean staleConnectionCheckEnabled, String cookieSpec, boolean redirectsEnabled, boolean relativeRedirectsAllowed, boolean circularRedirectsAllowed, int maxRedirects, boolean authenticationEnabled, Collection<String> targetPreferredAuthSchemes, Collection<String> proxyPreferredAuthSchemes, int connectionRequestTimeout, int connectTimeout, int socketTimeout, boolean decompressionEnabled) {
        this.expectContinueEnabled = expectContinueEnabled;
        this.proxy = proxy;
        this.localAddress = localAddress;
        this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
        this.cookieSpec = cookieSpec;
        this.redirectsEnabled = redirectsEnabled;
        this.relativeRedirectsAllowed = relativeRedirectsAllowed;
        this.circularRedirectsAllowed = circularRedirectsAllowed;
        this.maxRedirects = maxRedirects;
        this.authenticationEnabled = authenticationEnabled;
        this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
        this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
        this.decompressionEnabled = decompressionEnabled;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append("expectContinueEnabled=").append(this.expectContinueEnabled);
        builder.append(", proxy=").append(this.proxy);
        builder.append(", localAddress=").append(this.localAddress);
        builder.append(", cookieSpec=").append(this.cookieSpec);
        builder.append(", redirectsEnabled=").append(this.redirectsEnabled);
        builder.append(", relativeRedirectsAllowed=").append(this.relativeRedirectsAllowed);
        builder.append(", maxRedirects=").append(this.maxRedirects);
        builder.append(", circularRedirectsAllowed=").append(this.circularRedirectsAllowed);
        builder.append(", authenticationEnabled=").append(this.authenticationEnabled);
        builder.append(", targetPreferredAuthSchemes=").append(this.targetPreferredAuthSchemes);
        builder.append(", proxyPreferredAuthSchemes=").append(this.proxyPreferredAuthSchemes);
        builder.append(", connectionRequestTimeout=").append(this.connectionRequestTimeout);
        builder.append(", connectTimeout=").append(this.connectTimeout);
        builder.append(", socketTimeout=").append(this.socketTimeout);
        builder.append(", decompressionEnabled=").append(this.decompressionEnabled);
        builder.append("]");
        return builder.toString();
    }

    public static Builder custom() {
        return new Builder();
    }
}
