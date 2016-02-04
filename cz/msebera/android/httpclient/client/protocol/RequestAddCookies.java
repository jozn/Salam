package cz.msebera.android.httpclient.client.protocol;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpRequestInterceptor;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.config.Lookup;
import cz.msebera.android.httpclient.conn.routing.RouteInfo;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.cookie.CookieOrigin;
import cz.msebera.android.httpclient.cookie.CookieSpec;
import cz.msebera.android.httpclient.cookie.CookieSpecProvider;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.TextUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class RequestAddCookies implements HttpRequestInterceptor {
    public HttpClientAndroidLog log;

    public RequestAddCookies() {
        this.log = new HttpClientAndroidLog(getClass());
    }

    public final void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(context, "HTTP context");
        if (!request.getRequestLine().getMethod().equalsIgnoreCase("CONNECT")) {
            HttpClientContext clientContext = HttpClientContext.adapt(context);
            CookieStore cookieStore = clientContext.getCookieStore();
            if (cookieStore == null) {
                this.log.debug("Cookie store not specified in HTTP context");
                return;
            }
            Lookup<CookieSpecProvider> registry = clientContext.getLookup$4e562c4e("http.cookiespec-registry");
            if (registry == null) {
                this.log.debug("CookieSpec registry not specified in HTTP context");
                return;
            }
            HttpHost targetHost = clientContext.getTargetHost();
            if (targetHost == null) {
                this.log.debug("Target host not set in the context");
                return;
            }
            RouteInfo route = clientContext.getHttpRoute();
            if (route == null) {
                this.log.debug("Connection route not set in the context");
                return;
            }
            Header header;
            String policy = clientContext.getRequestConfig().cookieSpec;
            if (policy == null) {
                policy = "default";
            }
            if (this.log.debugEnabled) {
                this.log.debug("CookieSpec selected: " + policy);
            }
            URI requestURI = null;
            if (request instanceof HttpUriRequest) {
                requestURI = ((HttpUriRequest) request).getURI();
            } else {
                try {
                    requestURI = new URI(request.getRequestLine().getUri());
                } catch (URISyntaxException e) {
                }
            }
            String path = requestURI != null ? requestURI.getPath() : null;
            String hostName = targetHost.getHostName();
            int port = targetHost.getPort();
            if (port < 0) {
                port = route.getTargetHost().getPort();
            }
            if (port < 0) {
                port = 0;
            }
            if (TextUtils.isEmpty(path)) {
                path = MqttTopic.TOPIC_LEVEL_SEPARATOR;
            }
            CookieOrigin cookieOrigin = new CookieOrigin(hostName, port, path, route.isSecure());
            CookieSpec cookieSpec = ((CookieSpecProvider) registry.lookup(policy)).create(clientContext);
            List<Cookie> cookies = cookieStore.getCookies();
            List<Cookie> matchedCookies = new ArrayList();
            Date now = new Date();
            boolean expired = false;
            for (Cookie cookie : cookies) {
                if (cookie.isExpired(now)) {
                    if (this.log.debugEnabled) {
                        this.log.debug("Cookie " + cookie + " expired");
                    }
                    expired = true;
                } else if (cookieSpec.match(cookie, cookieOrigin)) {
                    if (this.log.debugEnabled) {
                        this.log.debug("Cookie " + cookie + " match " + cookieOrigin);
                    }
                    matchedCookies.add(cookie);
                }
            }
            if (expired) {
                cookieStore.clearExpired(now);
            }
            if (!matchedCookies.isEmpty()) {
                for (Header header2 : cookieSpec.formatCookies(matchedCookies)) {
                    request.addHeader(header2);
                }
            }
            if (cookieSpec.getVersion() > 0) {
                header2 = cookieSpec.getVersionHeader();
                if (header2 != null) {
                    request.addHeader(header2);
                }
            }
            context.setAttribute("http.cookie-spec", cookieSpec);
            context.setAttribute("http.cookie-origin", cookieOrigin);
        }
    }
}