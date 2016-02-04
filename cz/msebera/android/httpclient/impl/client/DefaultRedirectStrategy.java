package cz.msebera.android.httpclient.impl.client;

import com.squareup.okhttp.internal.http.StatusLine;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.ProtocolException;
import cz.msebera.android.httpclient.client.CircularRedirectException;
import cz.msebera.android.httpclient.client.RedirectStrategy;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpHead;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.client.methods.RequestBuilder;
import cz.msebera.android.httpclient.client.protocol.HttpClientContext;
import cz.msebera.android.httpclient.client.utils.URIBuilder;
import cz.msebera.android.httpclient.client.utils.URIUtils;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.Asserts;
import cz.msebera.android.httpclient.util.TextUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public final class DefaultRedirectStrategy implements RedirectStrategy {
    public static final DefaultRedirectStrategy INSTANCE;
    private static final String[] REDIRECT_METHODS;
    public HttpClientAndroidLog log;

    static {
        INSTANCE = new DefaultRedirectStrategy();
        REDIRECT_METHODS = new String[]{"GET", "HEAD"};
    }

    public DefaultRedirectStrategy() {
        this.log = new HttpClientAndroidLog(getClass());
    }

    public final boolean isRedirected$4aced518(HttpRequest request, HttpResponse response) throws ProtocolException {
        Args.notNull(request, "HTTP request");
        Args.notNull(response, "HTTP response");
        int statusCode = response.getStatusLine().getStatusCode();
        String method = request.getRequestLine().getMethod();
        Header locationHeader = response.getFirstHeader("location");
        switch (statusCode) {
            case 301:
            case StatusLine.HTTP_TEMP_REDIRECT /*307*/:
                return isRedirectable(method);
            case 302:
                if (!isRedirectable(method) || locationHeader == null) {
                    return false;
                }
                return true;
            case 303:
                return true;
            default:
                return false;
        }
    }

    private URI getLocationURI(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
        Args.notNull(request, "HTTP request");
        Args.notNull(response, "HTTP response");
        Args.notNull(context, "HTTP context");
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        Header locationHeader = response.getFirstHeader("location");
        if (locationHeader == null) {
            throw new ProtocolException("Received redirect response " + response.getStatusLine() + " but no location header");
        }
        String location = locationHeader.getValue();
        if (this.log.debugEnabled) {
            this.log.debug("Redirect requested to location '" + location + "'");
        }
        RequestConfig config = clientContext.getRequestConfig();
        URI uri = createLocationURI(location);
        try {
            if (!uri.isAbsolute()) {
                if (config.relativeRedirectsAllowed) {
                    HttpHost target = clientContext.getTargetHost();
                    Asserts.notNull(target, "Target host");
                    URI rewriteURI = URIUtils.rewriteURI(new URI(request.getRequestLine().getUri()), target, false);
                    Args.notNull(rewriteURI, "Base URI");
                    Args.notNull(uri, "Reference URI");
                    String uri2 = uri.toString();
                    String uri3;
                    if (uri2.startsWith("?")) {
                        uri3 = rewriteURI.toString();
                        if (uri3.indexOf(63) >= 0) {
                            uri3 = uri3.substring(0, uri3.indexOf(63));
                        }
                        uri = URI.create(uri3 + uri.toString());
                    } else {
                        boolean isEmpty = uri2.isEmpty();
                        if (isEmpty) {
                            uri = URI.create(MqttTopic.MULTI_LEVEL_WILDCARD);
                        }
                        rewriteURI = rewriteURI.resolve(uri);
                        if (isEmpty) {
                            uri3 = rewriteURI.toString();
                            rewriteURI = URI.create(uri3.substring(0, uri3.indexOf(35)));
                        }
                        uri = URIUtils.normalizeSyntax(rewriteURI);
                    }
                } else {
                    throw new ProtocolException("Relative redirect location '" + uri + "' not allowed");
                }
            }
            RedirectLocations redirectLocations = (RedirectLocations) clientContext.getAttribute("http.protocol.redirect-locations");
            if (redirectLocations == null) {
                redirectLocations = new RedirectLocations();
                context.setAttribute("http.protocol.redirect-locations", redirectLocations);
            }
            if (config.circularRedirectsAllowed || !redirectLocations.unique.contains(uri)) {
                redirectLocations.unique.add(uri);
                redirectLocations.all.add(uri);
                return uri;
            }
            throw new CircularRedirectException("Circular redirect to '" + uri + "'");
        } catch (URISyntaxException ex) {
            throw new ProtocolException(ex.getMessage(), ex);
        }
    }

    private static URI createLocationURI(String location) throws ProtocolException {
        try {
            URIBuilder b = new URIBuilder(new URI(location).normalize());
            String host = b.host;
            if (host != null) {
                b.setHost(host.toLowerCase(Locale.ROOT));
            }
            if (TextUtils.isEmpty(b.path)) {
                b.setPath(MqttTopic.TOPIC_LEVEL_SEPARATOR);
            }
            return b.build();
        } catch (URISyntaxException ex) {
            throw new ProtocolException("Invalid redirect URI: " + location, ex);
        }
    }

    private static boolean isRedirectable(String method) {
        for (String equalsIgnoreCase : REDIRECT_METHODS) {
            if (equalsIgnoreCase.equalsIgnoreCase(method)) {
                return true;
            }
        }
        return false;
    }

    public final HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
        URI uri = getLocationURI(request, response, context);
        String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("HEAD")) {
            return new HttpHead(uri);
        }
        if (method.equalsIgnoreCase("GET")) {
            return new HttpGet(uri);
        }
        if (response.getStatusLine().getStatusCode() != StatusLine.HTTP_TEMP_REDIRECT) {
            return new HttpGet(uri);
        }
        Args.notNull(request, "HTTP request");
        RequestBuilder doCopy = new RequestBuilder((byte) 0).doCopy(request);
        doCopy.uri = uri;
        return doCopy.build();
    }
}
