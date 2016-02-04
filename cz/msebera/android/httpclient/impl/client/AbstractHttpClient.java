package cz.msebera.android.httpclient.impl.client;

import cz.msebera.android.httpclient.ConnectionReuseStrategy;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpRequestInterceptor;
import cz.msebera.android.httpclient.HttpResponseInterceptor;
import cz.msebera.android.httpclient.auth.AuthSchemeRegistry;
import cz.msebera.android.httpclient.client.AuthenticationStrategy;
import cz.msebera.android.httpclient.client.BackoffManager;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.ConnectionBackoffStrategy;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.CredentialsProvider;
import cz.msebera.android.httpclient.client.HttpRequestRetryHandler;
import cz.msebera.android.httpclient.client.RedirectStrategy;
import cz.msebera.android.httpclient.client.RequestDirector;
import cz.msebera.android.httpclient.client.UserTokenHandler;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.config.RequestConfig.Builder;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.ClientConnectionManagerFactory;
import cz.msebera.android.httpclient.conn.ConnectionKeepAliveStrategy;
import cz.msebera.android.httpclient.conn.routing.HttpRoutePlanner;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.cookie.CookieSpecRegistry;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.impl.DefaultConnectionReuseStrategy;
import cz.msebera.android.httpclient.impl.auth.BasicSchemeFactory;
import cz.msebera.android.httpclient.impl.auth.DigestSchemeFactory;
import cz.msebera.android.httpclient.impl.auth.NTLMSchemeFactory;
import cz.msebera.android.httpclient.impl.conn.BasicClientConnectionManager;
import cz.msebera.android.httpclient.impl.conn.DefaultHttpRoutePlanner;
import cz.msebera.android.httpclient.impl.conn.SchemeRegistryFactory;
import cz.msebera.android.httpclient.impl.cookie.BestMatchSpecFactory;
import cz.msebera.android.httpclient.impl.cookie.BrowserCompatSpecFactory;
import cz.msebera.android.httpclient.impl.cookie.IgnoreSpecFactory;
import cz.msebera.android.httpclient.impl.cookie.NetscapeDraftSpecFactory;
import cz.msebera.android.httpclient.impl.cookie.RFC2109SpecFactory;
import cz.msebera.android.httpclient.impl.cookie.RFC2965SpecFactory;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.BasicHttpProcessor;
import cz.msebera.android.httpclient.protocol.DefaultedHttpContext;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.protocol.HttpProcessor;
import cz.msebera.android.httpclient.protocol.HttpRequestExecutor;
import cz.msebera.android.httpclient.protocol.ImmutableHttpProcessor;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.InetAddress;
import java.util.Collection;

@Deprecated
public abstract class AbstractHttpClient extends CloseableHttpClient {
    private BackoffManager backoffManager;
    private ClientConnectionManager connManager;
    private ConnectionBackoffStrategy connectionBackoffStrategy;
    private CookieStore cookieStore;
    private CredentialsProvider credsProvider;
    private HttpParams defaultParams;
    private ConnectionKeepAliveStrategy keepAliveStrategy;
    public HttpClientAndroidLog log;
    private BasicHttpProcessor mutableProcessor;
    private ImmutableHttpProcessor protocolProcessor;
    private AuthenticationStrategy proxyAuthStrategy;
    private RedirectStrategy redirectStrategy;
    private HttpRequestExecutor requestExec;
    private HttpRequestRetryHandler retryHandler;
    private ConnectionReuseStrategy reuseStrategy;
    private HttpRoutePlanner routePlanner;
    private AuthSchemeRegistry supportedAuthSchemes;
    private CookieSpecRegistry supportedCookieSpecs;
    private AuthenticationStrategy targetAuthStrategy;
    private UserTokenHandler userTokenHandler;

    protected abstract HttpParams createHttpParams();

    protected abstract BasicHttpProcessor createHttpProcessor();

    protected AbstractHttpClient(ClientConnectionManager conman, HttpParams params) {
        this.log = new HttpClientAndroidLog(getClass());
        this.defaultParams = params;
        this.connManager = conman;
    }

    private ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = SchemeRegistryFactory.createDefault();
        ClientConnectionManagerFactory factory = null;
        String className = (String) getParams().getParameter("http.connection-manager.factory-class-name");
        if (className != null) {
            try {
                factory = (ClientConnectionManagerFactory) Class.forName(className).newInstance();
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Invalid class name: " + className);
            } catch (IllegalAccessException ex) {
                throw new IllegalAccessError(ex.getMessage());
            } catch (InstantiationException ex2) {
                throw new InstantiationError(ex2.getMessage());
            }
        }
        if (factory != null) {
            return factory.newInstance$2561a043();
        }
        return new BasicClientConnectionManager(registry);
    }

    public final synchronized HttpParams getParams() {
        if (this.defaultParams == null) {
            this.defaultParams = createHttpParams();
        }
        return this.defaultParams;
    }

    private synchronized ClientConnectionManager getConnectionManager() {
        if (this.connManager == null) {
            this.connManager = createClientConnectionManager();
        }
        return this.connManager;
    }

    private synchronized HttpRequestExecutor getRequestExecutor() {
        if (this.requestExec == null) {
            this.requestExec = new HttpRequestExecutor((byte) 0);
        }
        return this.requestExec;
    }

    private synchronized AuthSchemeRegistry getAuthSchemes() {
        if (this.supportedAuthSchemes == null) {
            AuthSchemeRegistry authSchemeRegistry = new AuthSchemeRegistry();
            authSchemeRegistry.register("Basic", new BasicSchemeFactory((byte) 0));
            authSchemeRegistry.register("Digest", new DigestSchemeFactory((byte) 0));
            authSchemeRegistry.register("NTLM", new NTLMSchemeFactory());
            this.supportedAuthSchemes = authSchemeRegistry;
        }
        return this.supportedAuthSchemes;
    }

    private synchronized ConnectionBackoffStrategy getConnectionBackoffStrategy() {
        return this.connectionBackoffStrategy;
    }

    private synchronized CookieSpecRegistry getCookieSpecs() {
        if (this.supportedCookieSpecs == null) {
            CookieSpecRegistry cookieSpecRegistry = new CookieSpecRegistry();
            cookieSpecRegistry.register("default", new BestMatchSpecFactory((byte) 0));
            cookieSpecRegistry.register("best-match", new BestMatchSpecFactory((byte) 0));
            cookieSpecRegistry.register("compatibility", new BrowserCompatSpecFactory());
            cookieSpecRegistry.register("netscape", new NetscapeDraftSpecFactory((byte) 0));
            cookieSpecRegistry.register("rfc2109", new RFC2109SpecFactory((byte) 0));
            cookieSpecRegistry.register("rfc2965", new RFC2965SpecFactory((byte) 0));
            cookieSpecRegistry.register("ignoreCookies", new IgnoreSpecFactory());
            this.supportedCookieSpecs = cookieSpecRegistry;
        }
        return this.supportedCookieSpecs;
    }

    private synchronized BackoffManager getBackoffManager() {
        return this.backoffManager;
    }

    private synchronized ConnectionReuseStrategy getConnectionReuseStrategy() {
        if (this.reuseStrategy == null) {
            this.reuseStrategy = new DefaultConnectionReuseStrategy();
        }
        return this.reuseStrategy;
    }

    private synchronized ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
        if (this.keepAliveStrategy == null) {
            this.keepAliveStrategy = new DefaultConnectionKeepAliveStrategy();
        }
        return this.keepAliveStrategy;
    }

    public final synchronized HttpRequestRetryHandler getHttpRequestRetryHandler() {
        if (this.retryHandler == null) {
            this.retryHandler = new DefaultHttpRequestRetryHandler((byte) 0);
        }
        return this.retryHandler;
    }

    public final synchronized void setHttpRequestRetryHandler(HttpRequestRetryHandler handler) {
        this.retryHandler = handler;
    }

    private synchronized RedirectStrategy getRedirectStrategy() {
        if (this.redirectStrategy == null) {
            this.redirectStrategy = new DefaultRedirectStrategy();
        }
        return this.redirectStrategy;
    }

    private synchronized AuthenticationStrategy getTargetAuthenticationStrategy() {
        if (this.targetAuthStrategy == null) {
            this.targetAuthStrategy = new TargetAuthenticationStrategy();
        }
        return this.targetAuthStrategy;
    }

    private synchronized AuthenticationStrategy getProxyAuthenticationStrategy() {
        if (this.proxyAuthStrategy == null) {
            this.proxyAuthStrategy = new ProxyAuthenticationStrategy();
        }
        return this.proxyAuthStrategy;
    }

    private synchronized CookieStore getCookieStore() {
        if (this.cookieStore == null) {
            this.cookieStore = new BasicCookieStore();
        }
        return this.cookieStore;
    }

    private synchronized CredentialsProvider getCredentialsProvider() {
        if (this.credsProvider == null) {
            this.credsProvider = new BasicCredentialsProvider();
        }
        return this.credsProvider;
    }

    private synchronized HttpRoutePlanner getRoutePlanner() {
        if (this.routePlanner == null) {
            this.routePlanner = new DefaultHttpRoutePlanner(getConnectionManager().getSchemeRegistry());
        }
        return this.routePlanner;
    }

    private synchronized UserTokenHandler getUserTokenHandler() {
        if (this.userTokenHandler == null) {
            this.userTokenHandler = new DefaultUserTokenHandler();
        }
        return this.userTokenHandler;
    }

    private synchronized BasicHttpProcessor getHttpProcessor() {
        if (this.mutableProcessor == null) {
            this.mutableProcessor = createHttpProcessor();
        }
        return this.mutableProcessor;
    }

    private synchronized HttpProcessor getProtocolProcessor() {
        if (this.protocolProcessor == null) {
            int i;
            BasicHttpProcessor proc = getHttpProcessor();
            int reqc = proc.getRequestInterceptorCount();
            HttpRequestInterceptor[] reqinterceptors = new HttpRequestInterceptor[reqc];
            for (i = 0; i < reqc; i++) {
                reqinterceptors[i] = proc.getRequestInterceptor(i);
            }
            int resc = proc.getResponseInterceptorCount();
            HttpResponseInterceptor[] resinterceptors = new HttpResponseInterceptor[resc];
            for (i = 0; i < resc; i++) {
                resinterceptors[i] = proc.getResponseInterceptor(i);
            }
            this.protocolProcessor = new ImmutableHttpProcessor(reqinterceptors, resinterceptors);
        }
        return this.protocolProcessor;
    }

    public final synchronized void addResponseInterceptor(HttpResponseInterceptor itcp) {
        getHttpProcessor().addInterceptor(itcp);
        this.protocolProcessor = null;
    }

    public final synchronized void addRequestInterceptor(HttpRequestInterceptor itcp) {
        getHttpProcessor().addInterceptor(itcp);
        this.protocolProcessor = null;
    }

    public final synchronized void addRequestInterceptor$62d44063(HttpRequestInterceptor itcp) {
        getHttpProcessor().addRequestInterceptor$62d44063(itcp);
        this.protocolProcessor = null;
    }

    protected final CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
        CloseableHttpResponse newProxy;
        Args.notNull(request, "HTTP request");
        synchronized (this) {
            HttpContext execContext;
            HttpContext defaultContext = new BasicHttpContext();
            defaultContext.setAttribute("http.scheme-registry", getConnectionManager().getSchemeRegistry());
            defaultContext.setAttribute("http.authscheme-registry", getAuthSchemes());
            defaultContext.setAttribute("http.cookiespec-registry", getCookieSpecs());
            defaultContext.setAttribute("http.cookie-store", getCookieStore());
            defaultContext.setAttribute("http.auth.credentials-provider", getCredentialsProvider());
            if (context == null) {
                execContext = defaultContext;
            } else {
                HttpContext defaultedHttpContext = new DefaultedHttpContext(context, defaultContext);
            }
            HttpParams params = determineParams(request);
            Builder custom = RequestConfig.custom();
            custom.socketTimeout = params.getIntParameter("http.socket.timeout", 0);
            custom.staleConnectionCheckEnabled = params.getBooleanParameter("http.connection.stalecheck", true);
            custom.connectTimeout = params.getIntParameter("http.connection.timeout", 0);
            custom.expectContinueEnabled = params.getBooleanParameter("http.protocol.expect-continue", false);
            custom.proxy = (HttpHost) params.getParameter("http.route.default-proxy");
            custom.localAddress = (InetAddress) params.getParameter("http.route.local-address");
            custom.proxyPreferredAuthSchemes = (Collection) params.getParameter("http.auth.proxy-scheme-pref");
            custom.targetPreferredAuthSchemes = (Collection) params.getParameter("http.auth.target-scheme-pref");
            custom.authenticationEnabled = params.getBooleanParameter("http.protocol.handle-authentication", true);
            custom.circularRedirectsAllowed = params.getBooleanParameter("http.protocol.allow-circular-redirects", false);
            custom.connectionRequestTimeout = (int) params.getLongParameter$505cfb67("http.conn-manager.timeout");
            custom.cookieSpec = (String) params.getParameter("http.protocol.cookie-policy");
            custom.maxRedirects = params.getIntParameter("http.protocol.max-redirects", 50);
            custom.redirectsEnabled = params.getBooleanParameter("http.protocol.handle-redirects", true);
            custom.relativeRedirectsAllowed = !params.getBooleanParameter("http.protocol.reject-relative-redirect", false);
            execContext.setAttribute("http.request-config", custom.build());
            RequestDirector director = new DefaultRequestDirector(this.log, getRequestExecutor(), getConnectionManager(), getConnectionReuseStrategy(), getConnectionKeepAliveStrategy(), getRoutePlanner(), getProtocolProcessor(), getHttpRequestRetryHandler(), getRedirectStrategy(), getTargetAuthenticationStrategy(), getProxyAuthenticationStrategy(), getUserTokenHandler(), params);
            HttpRoutePlanner routePlanner = getRoutePlanner();
            ConnectionBackoffStrategy connectionBackoffStrategy = getConnectionBackoffStrategy();
            BackoffManager backoffManager = getBackoffManager();
        }
        if (connectionBackoffStrategy == null || backoffManager == null) {
            newProxy = CloseableHttpResponseProxy.newProxy(director.execute(target, request, execContext));
        } else {
            HttpHost targetForRoute;
            if (target != null) {
                targetForRoute = target;
            } else {
                targetForRoute = (HttpHost) determineParams(request).getParameter("http.default-host");
            }
            try {
                routePlanner.determineRoute$1e70857f(targetForRoute, request);
                newProxy = CloseableHttpResponseProxy.newProxy(director.execute(target, request, execContext));
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable e2) {
                if (e2 instanceof HttpException) {
                    throw ((HttpException) e2);
                } else if (e2 instanceof IOException) {
                    throw ((IOException) e2);
                } else {
                    throw new UndeclaredThrowableException(e2);
                }
            } catch (Throwable httpException) {
                throw new ClientProtocolException(httpException);
            }
        }
        return newProxy;
    }

    private HttpParams determineParams(HttpRequest req) {
        return new ClientParamsStack(getParams(), req.getParams());
    }

    public void close() {
        getConnectionManager().shutdown();
    }
}
