package cz.msebera.android.httpclient.impl.client;

import cz.msebera.android.httpclient.ConnectionReuseStrategy;
import cz.msebera.android.httpclient.HttpEntityEnclosingRequest;
import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.ProtocolException;
import cz.msebera.android.httpclient.auth.AuthProtocolState;
import cz.msebera.android.httpclient.auth.AuthScheme;
import cz.msebera.android.httpclient.auth.AuthState;
import cz.msebera.android.httpclient.auth.UsernamePasswordCredentials;
import cz.msebera.android.httpclient.client.AuthenticationHandler;
import cz.msebera.android.httpclient.client.AuthenticationStrategy;
import cz.msebera.android.httpclient.client.HttpRequestRetryHandler;
import cz.msebera.android.httpclient.client.RedirectException;
import cz.msebera.android.httpclient.client.RedirectHandler;
import cz.msebera.android.httpclient.client.RedirectStrategy;
import cz.msebera.android.httpclient.client.RequestDirector;
import cz.msebera.android.httpclient.client.UserTokenHandler;
import cz.msebera.android.httpclient.client.methods.AbortableHttpRequest;
import cz.msebera.android.httpclient.client.params.HttpClientParams;
import cz.msebera.android.httpclient.client.utils.URIUtils;
import cz.msebera.android.httpclient.conn.BasicManagedEntity;
import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.conn.ClientConnectionRequest;
import cz.msebera.android.httpclient.conn.ConnectionKeepAliveStrategy;
import cz.msebera.android.httpclient.conn.ManagedClientConnection;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.conn.routing.HttpRoutePlanner;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.impl.auth.BasicScheme;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.protocol.HttpProcessor;
import cz.msebera.android.httpclient.protocol.HttpRequestExecutor;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.EntityUtils;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@Deprecated
public final class DefaultRequestDirector implements RequestDirector {
    private final HttpAuthenticator authenticator;
    protected final ClientConnectionManager connManager;
    private int execCount;
    protected final HttpProcessor httpProcessor;
    protected final ConnectionKeepAliveStrategy keepAliveStrategy;
    public HttpClientAndroidLog log;
    protected ManagedClientConnection managedConn;
    private final int maxRedirects;
    protected final HttpParams params;
    @Deprecated
    protected final AuthenticationHandler proxyAuthHandler;
    protected final AuthState proxyAuthState;
    protected final AuthenticationStrategy proxyAuthStrategy;
    private int redirectCount;
    @Deprecated
    protected final RedirectHandler redirectHandler;
    protected final RedirectStrategy redirectStrategy;
    protected final HttpRequestExecutor requestExec;
    protected final HttpRequestRetryHandler retryHandler;
    protected final ConnectionReuseStrategy reuseStrategy;
    protected final HttpRoutePlanner routePlanner;
    @Deprecated
    protected final AuthenticationHandler targetAuthHandler;
    protected final AuthState targetAuthState;
    protected final AuthenticationStrategy targetAuthStrategy;
    protected final UserTokenHandler userTokenHandler;
    private HttpHost virtualHost;

    public DefaultRequestDirector(HttpClientAndroidLog log, HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler, HttpParams params) {
        Args.notNull(log, "Log");
        Args.notNull(requestExec, "Request executor");
        Args.notNull(conman, "Client connection manager");
        Args.notNull(reustrat, "Connection reuse strategy");
        Args.notNull(kastrat, "Connection keep alive strategy");
        Args.notNull(rouplan, "Route planner");
        Args.notNull(httpProcessor, "HTTP protocol processor");
        Args.notNull(retryHandler, "HTTP request retry handler");
        Args.notNull(redirectStrategy, "Redirect strategy");
        Args.notNull(targetAuthStrategy, "Target authentication strategy");
        Args.notNull(proxyAuthStrategy, "Proxy authentication strategy");
        Args.notNull(userTokenHandler, "User token handler");
        Args.notNull(params, "HTTP parameters");
        this.log = log;
        this.authenticator = new HttpAuthenticator(log);
        this.requestExec = requestExec;
        this.connManager = conman;
        this.reuseStrategy = reustrat;
        this.keepAliveStrategy = kastrat;
        this.routePlanner = rouplan;
        this.httpProcessor = httpProcessor;
        this.retryHandler = retryHandler;
        this.redirectStrategy = redirectStrategy;
        this.targetAuthStrategy = targetAuthStrategy;
        this.proxyAuthStrategy = proxyAuthStrategy;
        this.userTokenHandler = userTokenHandler;
        this.params = params;
        if (redirectStrategy instanceof DefaultRedirectStrategyAdaptor) {
            this.redirectHandler = ((DefaultRedirectStrategyAdaptor) redirectStrategy).handler;
        } else {
            this.redirectHandler = null;
        }
        if (targetAuthStrategy instanceof AuthenticationStrategyAdaptor) {
            this.targetAuthHandler = ((AuthenticationStrategyAdaptor) targetAuthStrategy).handler;
        } else {
            this.targetAuthHandler = null;
        }
        if (proxyAuthStrategy instanceof AuthenticationStrategyAdaptor) {
            this.proxyAuthHandler = ((AuthenticationStrategyAdaptor) proxyAuthStrategy).handler;
        } else {
            this.proxyAuthHandler = null;
        }
        this.managedConn = null;
        this.execCount = 0;
        this.redirectCount = 0;
        this.targetAuthState = new AuthState();
        this.proxyAuthState = new AuthState();
        this.maxRedirects = this.params.getIntParameter("http.protocol.max-redirects", 100);
    }

    private static RequestWrapper wrapRequest(HttpRequest request) throws ProtocolException {
        if (request instanceof HttpEntityEnclosingRequest) {
            return new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest) request);
        }
        return new RequestWrapper(request);
    }

    public final HttpResponse execute(HttpHost targetHost, HttpRequest request, HttpContext context) throws HttpException, IOException {
        HttpHost httpHost;
        context.setAttribute("http.auth.target-scope", this.targetAuthState);
        context.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
        HttpHost target = targetHost;
        HttpRequest orig = request;
        RequestWrapper origWrapper = wrapRequest(request);
        origWrapper.setParams(this.params);
        HttpRoute origRoute = determineRoute$1e70857f(target, origWrapper);
        this.virtualHost = (HttpHost) origWrapper.getParams().getParameter("http.virtual-host");
        if (this.virtualHost != null && this.virtualHost.getPort() == -1) {
            if (target != null) {
                httpHost = target;
            } else {
                httpHost = origRoute.targetHost;
            }
            int port = httpHost.getPort();
            if (port != -1) {
                this.virtualHost = new HttpHost(this.virtualHost.getHostName(), port, this.virtualHost.getSchemeName());
            }
        }
        RoutedRequest routedRequest = new RoutedRequest(origWrapper, origRoute);
        boolean reuse = false;
        boolean done = false;
        HttpResponse response = null;
        while (!done) {
            RequestWrapper wrapper;
            try {
                RoutedRequest roureq;
                wrapper = roureq.getRequest();
                HttpRoute route = roureq.getRoute();
                Object userToken = context.getAttribute("http.user-token");
                if (this.managedConn == null) {
                    ClientConnectionRequest connRequest = this.connManager.requestConnection(route, userToken);
                    if (orig instanceof AbortableHttpRequest) {
                        ((AbortableHttpRequest) orig).setConnectionRequest(connRequest);
                    }
                    HttpParams httpParams = this.params;
                    Args.notNull(httpParams, "HTTP parameters");
                    Long l = (Long) httpParams.getParameter("http.conn-manager.timeout");
                    this.managedConn = connRequest.getConnection(l != null ? l.longValue() : (long) HttpConnectionParams.getConnectionTimeout(httpParams), TimeUnit.MILLISECONDS);
                    HttpParams httpParams2 = this.params;
                    Args.notNull(httpParams2, "HTTP parameters");
                    if (httpParams2.getBooleanParameter("http.connection.stalecheck", true) && this.managedConn.isOpen()) {
                        this.log.debug("Stale connection check");
                        if (this.managedConn.isStale()) {
                            this.log.debug("Stale connection detected");
                            this.managedConn.close();
                        }
                    }
                }
                if (orig instanceof AbortableHttpRequest) {
                    ((AbortableHttpRequest) orig).setReleaseTrigger(this.managedConn);
                }
                try {
                    tryConnect(roureq, context);
                    String userinfo = wrapper.uri.getUserInfo();
                    if (userinfo != null) {
                        this.targetAuthState.update(new BasicScheme(), new UsernamePasswordCredentials(userinfo));
                    }
                    if (this.virtualHost != null) {
                        target = this.virtualHost;
                    } else {
                        URI requestURI = wrapper.uri;
                        if (requestURI.isAbsolute()) {
                            target = URIUtils.extractHost(requestURI);
                        }
                    }
                    if (target == null) {
                        target = route.targetHost;
                    }
                    wrapper.headergroup.clear();
                    wrapper.setHeaders(wrapper.original.getAllHeaders());
                    URI uri = wrapper.uri;
                    uri = (route.getProxyHost() == null || route.isTunnelled()) ? uri.isAbsolute() ? URIUtils.rewriteURI(uri, null, true) : URIUtils.rewriteURI(uri) : !uri.isAbsolute() ? URIUtils.rewriteURI(uri, route.targetHost, true) : URIUtils.rewriteURI(uri);
                    wrapper.uri = uri;
                    context.setAttribute("http.target_host", target);
                    context.setAttribute("http.route", route);
                    context.setAttribute("http.connection", this.managedConn);
                    HttpRequestExecutor.preProcess(wrapper, this.httpProcessor, context);
                    response = tryExecute(roureq, context);
                    if (response != null) {
                        RoutedRequest followup;
                        response.setParams(this.params);
                        HttpRequestExecutor.postProcess(response, this.httpProcessor, context);
                        reuse = this.reuseStrategy.keepAlive(response, context);
                        if (reuse) {
                            long duration = this.keepAliveStrategy.getKeepAliveDuration$22649b62(response);
                            if (this.log.debugEnabled) {
                                String s;
                                if (duration > 0) {
                                    s = "for " + duration + " " + TimeUnit.MILLISECONDS;
                                } else {
                                    s = "indefinitely";
                                }
                                this.log.debug("Connection can be kept alive " + s);
                            }
                            this.managedConn.setIdleDuration(duration, TimeUnit.MILLISECONDS);
                        }
                        HttpRoute route2 = roureq.getRoute();
                        RequestWrapper request2 = roureq.getRequest();
                        HttpParams params = request2.getParams();
                        if (HttpClientParams.isAuthenticating(params)) {
                            HttpHost httpHost2;
                            httpHost = (HttpHost) context.getAttribute("http.target_host");
                            if (httpHost == null) {
                                httpHost = route2.targetHost;
                            }
                            if (httpHost.getPort() < 0) {
                                SchemeRegistry schemeRegistry = this.connManager.getSchemeRegistry();
                                Args.notNull(httpHost, "Host");
                                httpHost2 = new HttpHost(httpHost.getHostName(), schemeRegistry.getScheme(httpHost.getSchemeName()).defaultPort, httpHost.getSchemeName());
                            } else {
                                httpHost2 = httpHost;
                            }
                            boolean isAuthenticationRequested = this.authenticator.isAuthenticationRequested(httpHost2, response, this.targetAuthStrategy, this.targetAuthState, context);
                            HttpHost proxyHost = route2.getProxyHost();
                            if (proxyHost == null) {
                                proxyHost = route2.targetHost;
                            }
                            boolean isAuthenticationRequested2 = this.authenticator.isAuthenticationRequested(proxyHost, response, this.proxyAuthStrategy, this.proxyAuthState, context);
                            if (isAuthenticationRequested) {
                                if (this.authenticator.authenticate(httpHost2, response, this.targetAuthStrategy, this.targetAuthState, context)) {
                                    followup = roureq;
                                    if (followup != null) {
                                        done = true;
                                    } else {
                                        if (reuse) {
                                            this.managedConn.close();
                                            if (this.proxyAuthState.state.compareTo(AuthProtocolState.CHALLENGED) > 0 && this.proxyAuthState.authScheme != null && this.proxyAuthState.authScheme.isConnectionBased()) {
                                                this.log.debug("Resetting proxy auth state");
                                                this.proxyAuthState.reset();
                                            }
                                            if (this.targetAuthState.state.compareTo(AuthProtocolState.CHALLENGED) > 0 && this.targetAuthState.authScheme != null && this.targetAuthState.authScheme.isConnectionBased()) {
                                                this.log.debug("Resetting target auth state");
                                                this.targetAuthState.reset();
                                            }
                                        } else {
                                            EntityUtils.consume(response.getEntity());
                                            this.managedConn.markReusable();
                                        }
                                        if (!followup.getRoute().equals(roureq.getRoute())) {
                                            releaseConnection();
                                        }
                                        roureq = followup;
                                    }
                                    if (this.managedConn == null) {
                                        if (userToken == null) {
                                            userToken = this.userTokenHandler.getUserToken(context);
                                            context.setAttribute("http.user-token", userToken);
                                        }
                                        if (userToken == null) {
                                            this.managedConn.setState(userToken);
                                        }
                                    }
                                }
                            }
                            if (isAuthenticationRequested2) {
                                if (this.authenticator.authenticate(proxyHost, response, this.proxyAuthStrategy, this.proxyAuthState, context)) {
                                    followup = roureq;
                                    if (followup != null) {
                                        if (reuse) {
                                            this.managedConn.close();
                                            this.log.debug("Resetting proxy auth state");
                                            this.proxyAuthState.reset();
                                            this.log.debug("Resetting target auth state");
                                            this.targetAuthState.reset();
                                        } else {
                                            EntityUtils.consume(response.getEntity());
                                            this.managedConn.markReusable();
                                        }
                                        if (followup.getRoute().equals(roureq.getRoute())) {
                                            releaseConnection();
                                        }
                                        roureq = followup;
                                    } else {
                                        done = true;
                                    }
                                    if (this.managedConn == null) {
                                        if (userToken == null) {
                                            userToken = this.userTokenHandler.getUserToken(context);
                                            context.setAttribute("http.user-token", userToken);
                                        }
                                        if (userToken == null) {
                                            this.managedConn.setState(userToken);
                                        }
                                    }
                                }
                            }
                        }
                        Args.notNull(params, "HTTP parameters");
                        if (!params.getBooleanParameter("http.protocol.handle-redirects", true) || !this.redirectStrategy.isRedirected$4aced518(request2, response)) {
                            followup = null;
                            if (followup != null) {
                                done = true;
                            } else {
                                if (reuse) {
                                    EntityUtils.consume(response.getEntity());
                                    this.managedConn.markReusable();
                                } else {
                                    this.managedConn.close();
                                    this.log.debug("Resetting proxy auth state");
                                    this.proxyAuthState.reset();
                                    this.log.debug("Resetting target auth state");
                                    this.targetAuthState.reset();
                                }
                                if (followup.getRoute().equals(roureq.getRoute())) {
                                    releaseConnection();
                                }
                                roureq = followup;
                            }
                            if (this.managedConn == null) {
                                if (userToken == null) {
                                    userToken = this.userTokenHandler.getUserToken(context);
                                    context.setAttribute("http.user-token", userToken);
                                }
                                if (userToken == null) {
                                    this.managedConn.setState(userToken);
                                }
                            }
                        } else if (this.redirectCount >= this.maxRedirects) {
                            throw new RedirectException("Maximum redirects (" + this.maxRedirects + ") exceeded");
                        } else {
                            this.redirectCount++;
                            this.virtualHost = null;
                            HttpRequest redirect = this.redirectStrategy.getRedirect(request2, response, context);
                            redirect.setHeaders(request2.original.getAllHeaders());
                            URI uri2 = redirect.getURI();
                            HttpHost extractHost = URIUtils.extractHost(uri2);
                            if (extractHost == null) {
                                throw new ProtocolException("Redirect URI does not specify a valid host name: " + uri2);
                            }
                            if (!route2.targetHost.equals(extractHost)) {
                                this.log.debug("Resetting target auth state");
                                this.targetAuthState.reset();
                                AuthScheme authScheme = this.proxyAuthState.authScheme;
                                if (authScheme != null && authScheme.isConnectionBased()) {
                                    this.log.debug("Resetting proxy auth state");
                                    this.proxyAuthState.reset();
                                }
                            }
                            Object wrapRequest = wrapRequest(redirect);
                            wrapRequest.setParams(params);
                            HttpRoute determineRoute$1e70857f = determineRoute$1e70857f(extractHost, wrapRequest);
                            routedRequest = new RoutedRequest(wrapRequest, determineRoute$1e70857f);
                            if (this.log.debugEnabled) {
                                this.log.debug("Redirecting to '" + uri2 + "' via " + determineRoute$1e70857f);
                            }
                            if (followup != null) {
                                if (reuse) {
                                    this.managedConn.close();
                                    this.log.debug("Resetting proxy auth state");
                                    this.proxyAuthState.reset();
                                    this.log.debug("Resetting target auth state");
                                    this.targetAuthState.reset();
                                } else {
                                    EntityUtils.consume(response.getEntity());
                                    this.managedConn.markReusable();
                                }
                                if (followup.getRoute().equals(roureq.getRoute())) {
                                    releaseConnection();
                                }
                                roureq = followup;
                            } else {
                                done = true;
                            }
                            if (this.managedConn == null) {
                                if (userToken == null) {
                                    userToken = this.userTokenHandler.getUserToken(context);
                                    context.setAttribute("http.user-token", userToken);
                                }
                                if (userToken == null) {
                                    this.managedConn.setState(userToken);
                                }
                            }
                        }
                    }
                } catch (TunnelRefusedException ex) {
                    if (this.log.debugEnabled) {
                        this.log.debug(ex.getMessage());
                    }
                    response = ex.response;
                }
            } catch (Throwable e) {
                throw new ProtocolException("Invalid URI: " + wrapper.getRequestLine().getUri(), e);
            } catch (InterruptedException e2) {
                Thread.currentThread().interrupt();
                throw new InterruptedIOException();
            } catch (Throwable ex2) {
                InterruptedIOException interruptedIOException = new InterruptedIOException("Connection has been shut down");
                interruptedIOException.initCause(ex2);
                throw interruptedIOException;
            } catch (HttpException ex3) {
                abortConnection();
                throw ex3;
            } catch (IOException ex4) {
                abortConnection();
                throw ex4;
            } catch (RuntimeException ex5) {
                abortConnection();
                throw ex5;
            }
        }
        if (response == null || response.getEntity() == null || !response.getEntity().isStreaming()) {
            if (reuse) {
                this.managedConn.markReusable();
            }
            releaseConnection();
        } else {
            response.setEntity(new BasicManagedEntity(response.getEntity(), this.managedConn, reuse));
        }
        return response;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void tryConnect(cz.msebera.android.httpclient.impl.client.RoutedRequest r14, cz.msebera.android.httpclient.protocol.HttpContext r15) throws cz.msebera.android.httpclient.HttpException, java.io.IOException {
        /*
        r13 = this;
        r8 = r14.getRoute();
        r9 = r14.getRequest();
        r6 = 0;
    L_0x0009:
        r0 = "http.request";
        r15.setAttribute(r0, r9);
        r6 = r6 + 1;
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r0 = r0.isOpen();	 Catch:{ IOException -> 0x004c }
        if (r0 != 0) goto L_0x00b9;
    L_0x0018:
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r1 = r13.params;	 Catch:{ IOException -> 0x004c }
        r0.open(r8, r15, r1);	 Catch:{ IOException -> 0x004c }
    L_0x001f:
        r10 = new cz.msebera.android.httpclient.conn.routing.BasicRouteDirector;	 Catch:{ IOException -> 0x004c }
        r10.<init>();	 Catch:{ IOException -> 0x004c }
    L_0x0024:
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r0 = r0.getRoute();	 Catch:{ IOException -> 0x004c }
        r11 = r10.nextStep(r8, r0);	 Catch:{ IOException -> 0x004c }
        switch(r11) {
            case -1: goto L_0x022a;
            case 0: goto L_0x00cd;
            case 1: goto L_0x00c6;
            case 2: goto L_0x00c6;
            case 3: goto L_0x00d0;
            case 4: goto L_0x0216;
            case 5: goto L_0x0221;
            default: goto L_0x0031;
        };	 Catch:{ IOException -> 0x004c }
    L_0x0031:
        r0 = new java.lang.IllegalStateException;	 Catch:{ IOException -> 0x004c }
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x004c }
        r2 = "Unknown step indicator ";
        r1.<init>(r2);	 Catch:{ IOException -> 0x004c }
        r1 = r1.append(r11);	 Catch:{ IOException -> 0x004c }
        r2 = " from RouteDirector.";
        r1 = r1.append(r2);	 Catch:{ IOException -> 0x004c }
        r1 = r1.toString();	 Catch:{ IOException -> 0x004c }
        r0.<init>(r1);	 Catch:{ IOException -> 0x004c }
        throw r0;	 Catch:{ IOException -> 0x004c }
    L_0x004c:
        r7 = move-exception;
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x024a }
        r0.close();	 Catch:{ IOException -> 0x024a }
    L_0x0052:
        r0 = r13.retryHandler;
        r0 = r0.retryRequest(r7, r6, r15);
        if (r0 == 0) goto L_0x0249;
    L_0x005a:
        r0 = r13.log;
        r0 = r0.infoEnabled;
        if (r0 == 0) goto L_0x0009;
    L_0x0060:
        r0 = r13.log;
        r1 = new java.lang.StringBuilder;
        r2 = "I/O exception (";
        r1.<init>(r2);
        r2 = r7.getClass();
        r2 = r2.getName();
        r1 = r1.append(r2);
        r2 = ") caught when connecting to ";
        r1 = r1.append(r2);
        r1 = r1.append(r8);
        r2 = ": ";
        r1 = r1.append(r2);
        r2 = r7.getMessage();
        r1 = r1.append(r2);
        r1 = r1.toString();
        r0.info(r1);
        r0 = r13.log;
        r0 = r0.debugEnabled;
        if (r0 == 0) goto L_0x00a3;
    L_0x009a:
        r0 = r13.log;
        r1 = r7.getMessage();
        r0.debug(r1, r7);
    L_0x00a3:
        r0 = r13.log;
        r1 = new java.lang.StringBuilder;
        r2 = "Retrying connect to ";
        r1.<init>(r2);
        r1 = r1.append(r8);
        r1 = r1.toString();
        r0.info(r1);
        goto L_0x0009;
    L_0x00b9:
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r1 = r13.params;	 Catch:{ IOException -> 0x004c }
        r1 = cz.msebera.android.httpclient.params.HttpConnectionParams.getSoTimeout(r1);	 Catch:{ IOException -> 0x004c }
        r0.setSocketTimeout(r1);	 Catch:{ IOException -> 0x004c }
        goto L_0x001f;
    L_0x00c6:
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r1 = r13.params;	 Catch:{ IOException -> 0x004c }
        r0.open(r8, r15, r1);	 Catch:{ IOException -> 0x004c }
    L_0x00cd:
        if (r11 > 0) goto L_0x0024;
    L_0x00cf:
        return;
    L_0x00d0:
        r1 = r8.getProxyHost();	 Catch:{ IOException -> 0x004c }
        r12 = r8.targetHost;	 Catch:{ IOException -> 0x004c }
    L_0x00d6:
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r0 = r0.isOpen();	 Catch:{ IOException -> 0x004c }
        if (r0 != 0) goto L_0x00e5;
    L_0x00de:
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r2 = r13.params;	 Catch:{ IOException -> 0x004c }
        r0.open(r8, r15, r2);	 Catch:{ IOException -> 0x004c }
    L_0x00e5:
        r2 = r8.targetHost;	 Catch:{ IOException -> 0x004c }
        r3 = r2.getHostName();	 Catch:{ IOException -> 0x004c }
        r0 = r2.getPort();	 Catch:{ IOException -> 0x004c }
        if (r0 >= 0) goto L_0x0101;
    L_0x00f1:
        r0 = r13.connManager;	 Catch:{ IOException -> 0x004c }
        r0 = r0.getSchemeRegistry();	 Catch:{ IOException -> 0x004c }
        r2 = r2.getSchemeName();	 Catch:{ IOException -> 0x004c }
        r0 = r0.getScheme(r2);	 Catch:{ IOException -> 0x004c }
        r0 = r0.defaultPort;	 Catch:{ IOException -> 0x004c }
    L_0x0101:
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x004c }
        r4 = r3.length();	 Catch:{ IOException -> 0x004c }
        r4 = r4 + 6;
        r2.<init>(r4);	 Catch:{ IOException -> 0x004c }
        r2.append(r3);	 Catch:{ IOException -> 0x004c }
        r3 = 58;
        r2.append(r3);	 Catch:{ IOException -> 0x004c }
        r0 = java.lang.Integer.toString(r0);	 Catch:{ IOException -> 0x004c }
        r2.append(r0);	 Catch:{ IOException -> 0x004c }
        r0 = r2.toString();	 Catch:{ IOException -> 0x004c }
        r2 = r13.params;	 Catch:{ IOException -> 0x004c }
        r2 = cz.msebera.android.httpclient.params.HttpProtocolParams.getVersion(r2);	 Catch:{ IOException -> 0x004c }
        r3 = new cz.msebera.android.httpclient.message.BasicHttpRequest;	 Catch:{ IOException -> 0x004c }
        r4 = "CONNECT";
        r3.<init>(r4, r0, r2);	 Catch:{ IOException -> 0x004c }
        r0 = r13.params;	 Catch:{ IOException -> 0x004c }
        r3.setParams(r0);	 Catch:{ IOException -> 0x004c }
        r0 = "http.target_host";
        r15.setAttribute(r0, r12);	 Catch:{ IOException -> 0x004c }
        r0 = "http.route";
        r15.setAttribute(r0, r8);	 Catch:{ IOException -> 0x004c }
        r0 = "http.proxy_host";
        r15.setAttribute(r0, r1);	 Catch:{ IOException -> 0x004c }
        r0 = "http.connection";
        r2 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r15.setAttribute(r0, r2);	 Catch:{ IOException -> 0x004c }
        r0 = "http.request";
        r15.setAttribute(r0, r3);	 Catch:{ IOException -> 0x004c }
        r0 = r13.httpProcessor;	 Catch:{ IOException -> 0x004c }
        cz.msebera.android.httpclient.protocol.HttpRequestExecutor.preProcess(r3, r0, r15);	 Catch:{ IOException -> 0x004c }
        r0 = r13.requestExec;	 Catch:{ IOException -> 0x004c }
        r2 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r2 = r0.execute(r3, r2, r15);	 Catch:{ IOException -> 0x004c }
        r0 = r13.params;	 Catch:{ IOException -> 0x004c }
        r2.setParams(r0);	 Catch:{ IOException -> 0x004c }
        r0 = r13.httpProcessor;	 Catch:{ IOException -> 0x004c }
        cz.msebera.android.httpclient.protocol.HttpRequestExecutor.postProcess(r2, r0, r15);	 Catch:{ IOException -> 0x004c }
        r0 = r2.getStatusLine();	 Catch:{ IOException -> 0x004c }
        r0 = r0.getStatusCode();	 Catch:{ IOException -> 0x004c }
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r0 >= r3) goto L_0x0188;
    L_0x016f:
        r0 = new cz.msebera.android.httpclient.HttpException;	 Catch:{ IOException -> 0x004c }
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x004c }
        r3 = "Unexpected response to CONNECT request: ";
        r1.<init>(r3);	 Catch:{ IOException -> 0x004c }
        r2 = r2.getStatusLine();	 Catch:{ IOException -> 0x004c }
        r1 = r1.append(r2);	 Catch:{ IOException -> 0x004c }
        r1 = r1.toString();	 Catch:{ IOException -> 0x004c }
        r0.<init>(r1);	 Catch:{ IOException -> 0x004c }
        throw r0;	 Catch:{ IOException -> 0x004c }
    L_0x0188:
        r0 = r13.params;	 Catch:{ IOException -> 0x004c }
        r0 = cz.msebera.android.httpclient.client.params.HttpClientParams.isAuthenticating(r0);	 Catch:{ IOException -> 0x004c }
        if (r0 == 0) goto L_0x00d6;
    L_0x0190:
        r0 = r13.authenticator;	 Catch:{ IOException -> 0x004c }
        r3 = r13.proxyAuthStrategy;	 Catch:{ IOException -> 0x004c }
        r4 = r13.proxyAuthState;	 Catch:{ IOException -> 0x004c }
        r5 = r15;
        r0 = r0.isAuthenticationRequested(r1, r2, r3, r4, r5);	 Catch:{ IOException -> 0x004c }
        if (r0 == 0) goto L_0x01c9;
    L_0x019d:
        r0 = r13.authenticator;	 Catch:{ IOException -> 0x004c }
        r3 = r13.proxyAuthStrategy;	 Catch:{ IOException -> 0x004c }
        r4 = r13.proxyAuthState;	 Catch:{ IOException -> 0x004c }
        r5 = r15;
        r0 = r0.authenticate(r1, r2, r3, r4, r5);	 Catch:{ IOException -> 0x004c }
        if (r0 == 0) goto L_0x01c9;
    L_0x01aa:
        r0 = r13.reuseStrategy;	 Catch:{ IOException -> 0x004c }
        r0 = r0.keepAlive(r2, r15);	 Catch:{ IOException -> 0x004c }
        if (r0 == 0) goto L_0x01c2;
    L_0x01b2:
        r0 = r13.log;	 Catch:{ IOException -> 0x004c }
        r3 = "Connection kept alive";
        r0.debug(r3);	 Catch:{ IOException -> 0x004c }
        r0 = r2.getEntity();	 Catch:{ IOException -> 0x004c }
        cz.msebera.android.httpclient.util.EntityUtils.consume(r0);	 Catch:{ IOException -> 0x004c }
        goto L_0x00d6;
    L_0x01c2:
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r0.close();	 Catch:{ IOException -> 0x004c }
        goto L_0x00d6;
    L_0x01c9:
        r0 = r2.getStatusLine();	 Catch:{ IOException -> 0x004c }
        r0 = r0.getStatusCode();	 Catch:{ IOException -> 0x004c }
        r1 = 299; // 0x12b float:4.19E-43 double:1.477E-321;
        if (r0 <= r1) goto L_0x0201;
    L_0x01d5:
        r0 = r2.getEntity();	 Catch:{ IOException -> 0x004c }
        if (r0 == 0) goto L_0x01e3;
    L_0x01db:
        r1 = new cz.msebera.android.httpclient.entity.BufferedHttpEntity;	 Catch:{ IOException -> 0x004c }
        r1.<init>(r0);	 Catch:{ IOException -> 0x004c }
        r2.setEntity(r1);	 Catch:{ IOException -> 0x004c }
    L_0x01e3:
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r0.close();	 Catch:{ IOException -> 0x004c }
        r0 = new cz.msebera.android.httpclient.impl.client.TunnelRefusedException;	 Catch:{ IOException -> 0x004c }
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x004c }
        r3 = "CONNECT refused by proxy: ";
        r1.<init>(r3);	 Catch:{ IOException -> 0x004c }
        r3 = r2.getStatusLine();	 Catch:{ IOException -> 0x004c }
        r1 = r1.append(r3);	 Catch:{ IOException -> 0x004c }
        r1 = r1.toString();	 Catch:{ IOException -> 0x004c }
        r0.<init>(r1, r2);	 Catch:{ IOException -> 0x004c }
        throw r0;	 Catch:{ IOException -> 0x004c }
    L_0x0201:
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r0.markReusable();	 Catch:{ IOException -> 0x004c }
        r0 = r13.log;	 Catch:{ IOException -> 0x004c }
        r1 = "Tunnel to target created.";
        r0.debug(r1);	 Catch:{ IOException -> 0x004c }
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r1 = r13.params;	 Catch:{ IOException -> 0x004c }
        r0.tunnelTarget$83338f(r1);	 Catch:{ IOException -> 0x004c }
        goto L_0x00cd;
    L_0x0216:
        r0.getHopCount();	 Catch:{ IOException -> 0x004c }
        r0 = new cz.msebera.android.httpclient.HttpException;	 Catch:{ IOException -> 0x004c }
        r1 = "Proxy chains are not supported.";
        r0.<init>(r1);	 Catch:{ IOException -> 0x004c }
        throw r0;	 Catch:{ IOException -> 0x004c }
    L_0x0221:
        r0 = r13.managedConn;	 Catch:{ IOException -> 0x004c }
        r1 = r13.params;	 Catch:{ IOException -> 0x004c }
        r0.layerProtocol(r15, r1);	 Catch:{ IOException -> 0x004c }
        goto L_0x00cd;
    L_0x022a:
        r1 = new cz.msebera.android.httpclient.HttpException;	 Catch:{ IOException -> 0x004c }
        r2 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x004c }
        r3 = "Unable to establish route: planned = ";
        r2.<init>(r3);	 Catch:{ IOException -> 0x004c }
        r2 = r2.append(r8);	 Catch:{ IOException -> 0x004c }
        r3 = "; current = ";
        r2 = r2.append(r3);	 Catch:{ IOException -> 0x004c }
        r0 = r2.append(r0);	 Catch:{ IOException -> 0x004c }
        r0 = r0.toString();	 Catch:{ IOException -> 0x004c }
        r1.<init>(r0);	 Catch:{ IOException -> 0x004c }
        throw r1;	 Catch:{ IOException -> 0x004c }
    L_0x0249:
        throw r7;
    L_0x024a:
        r0 = move-exception;
        goto L_0x0052;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.impl.client.DefaultRequestDirector.tryConnect(cz.msebera.android.httpclient.impl.client.RoutedRequest, cz.msebera.android.httpclient.protocol.HttpContext):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private cz.msebera.android.httpclient.HttpResponse tryExecute(cz.msebera.android.httpclient.impl.client.RoutedRequest r10, cz.msebera.android.httpclient.protocol.HttpContext r11) throws cz.msebera.android.httpclient.HttpException, java.io.IOException {
        /*
        r9 = this;
        r5 = r10.getRequest();
        r3 = r10.getRoute();
        r1 = 0;
        r2 = 0;
    L_0x000a:
        r6 = r9.execCount;
        r6 = r6 + 1;
        r9.execCount = r6;
        r6 = r5.execCount;
        r6 = r6 + 1;
        r5.execCount = r6;
        r6 = r5.isRepeatable();
        if (r6 != 0) goto L_0x0035;
    L_0x001c:
        r6 = r9.log;
        r7 = "Cannot retry non-repeatable request";
        r6.debug(r7);
        if (r2 == 0) goto L_0x002d;
    L_0x0025:
        r6 = new cz.msebera.android.httpclient.client.NonRepeatableRequestException;
        r7 = "Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed.";
        r6.<init>(r7, r2);
        throw r6;
    L_0x002d:
        r6 = new cz.msebera.android.httpclient.client.NonRepeatableRequestException;
        r7 = "Cannot retry request with a non-repeatable request entity.";
        r6.<init>(r7);
        throw r6;
    L_0x0035:
        r6 = r9.managedConn;	 Catch:{ IOException -> 0x0084 }
        r6 = r6.isOpen();	 Catch:{ IOException -> 0x0084 }
        if (r6 != 0) goto L_0x0051;
    L_0x003d:
        r6 = r3.isTunnelled();	 Catch:{ IOException -> 0x0084 }
        if (r6 != 0) goto L_0x007c;
    L_0x0043:
        r6 = r9.log;	 Catch:{ IOException -> 0x0084 }
        r7 = "Reopening the direct connection.";
        r6.debug(r7);	 Catch:{ IOException -> 0x0084 }
        r6 = r9.managedConn;	 Catch:{ IOException -> 0x0084 }
        r7 = r9.params;	 Catch:{ IOException -> 0x0084 }
        r6.open(r3, r11, r7);	 Catch:{ IOException -> 0x0084 }
    L_0x0051:
        r6 = r9.log;	 Catch:{ IOException -> 0x0084 }
        r6 = r6.debugEnabled;	 Catch:{ IOException -> 0x0084 }
        if (r6 == 0) goto L_0x0073;
    L_0x0057:
        r6 = r9.log;	 Catch:{ IOException -> 0x0084 }
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0084 }
        r8 = "Attempt ";
        r7.<init>(r8);	 Catch:{ IOException -> 0x0084 }
        r8 = r9.execCount;	 Catch:{ IOException -> 0x0084 }
        r7 = r7.append(r8);	 Catch:{ IOException -> 0x0084 }
        r8 = " to execute request";
        r7 = r7.append(r8);	 Catch:{ IOException -> 0x0084 }
        r7 = r7.toString();	 Catch:{ IOException -> 0x0084 }
        r6.debug(r7);	 Catch:{ IOException -> 0x0084 }
    L_0x0073:
        r6 = r9.requestExec;	 Catch:{ IOException -> 0x0084 }
        r7 = r9.managedConn;	 Catch:{ IOException -> 0x0084 }
        r1 = r6.execute(r5, r7, r11);	 Catch:{ IOException -> 0x0084 }
    L_0x007b:
        return r1;
    L_0x007c:
        r6 = r9.log;	 Catch:{ IOException -> 0x0084 }
        r7 = "Proxied connection. Need to start over.";
        r6.debug(r7);	 Catch:{ IOException -> 0x0084 }
        goto L_0x007b;
    L_0x0084:
        r0 = move-exception;
        r6 = r9.log;
        r7 = "Closing the connection.";
        r6.debug(r7);
        r6 = r9.managedConn;	 Catch:{ IOException -> 0x012c }
        r6.close();	 Catch:{ IOException -> 0x012c }
    L_0x0091:
        r6 = r9.retryHandler;
        r7 = r5.execCount;
        r6 = r6.retryRequest(r0, r7, r11);
        if (r6 == 0) goto L_0x0101;
    L_0x009b:
        r6 = r9.log;
        r6 = r6.infoEnabled;
        if (r6 == 0) goto L_0x00d5;
    L_0x00a1:
        r6 = r9.log;
        r7 = new java.lang.StringBuilder;
        r8 = "I/O exception (";
        r7.<init>(r8);
        r8 = r0.getClass();
        r8 = r8.getName();
        r7 = r7.append(r8);
        r8 = ") caught when processing request to ";
        r7 = r7.append(r8);
        r7 = r7.append(r3);
        r8 = ": ";
        r7 = r7.append(r8);
        r8 = r0.getMessage();
        r7 = r7.append(r8);
        r7 = r7.toString();
        r6.info(r7);
    L_0x00d5:
        r6 = r9.log;
        r6 = r6.debugEnabled;
        if (r6 == 0) goto L_0x00e4;
    L_0x00db:
        r6 = r9.log;
        r7 = r0.getMessage();
        r6.debug(r7, r0);
    L_0x00e4:
        r6 = r9.log;
        r6 = r6.infoEnabled;
        if (r6 == 0) goto L_0x00fe;
    L_0x00ea:
        r6 = r9.log;
        r7 = new java.lang.StringBuilder;
        r8 = "Retrying request to ";
        r7.<init>(r8);
        r7 = r7.append(r3);
        r7 = r7.toString();
        r6.info(r7);
    L_0x00fe:
        r2 = r0;
        goto L_0x000a;
    L_0x0101:
        r6 = r0 instanceof cz.msebera.android.httpclient.NoHttpResponseException;
        if (r6 == 0) goto L_0x012b;
    L_0x0105:
        r4 = new cz.msebera.android.httpclient.NoHttpResponseException;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = r3.targetHost;
        r7 = r7.toHostString();
        r6 = r6.append(r7);
        r7 = " failed to respond";
        r6 = r6.append(r7);
        r6 = r6.toString();
        r4.<init>(r6);
        r6 = r0.getStackTrace();
        r4.setStackTrace(r6);
        throw r4;
    L_0x012b:
        throw r0;
    L_0x012c:
        r6 = move-exception;
        goto L_0x0091;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.impl.client.DefaultRequestDirector.tryExecute(cz.msebera.android.httpclient.impl.client.RoutedRequest, cz.msebera.android.httpclient.protocol.HttpContext):cz.msebera.android.httpclient.HttpResponse");
    }

    private void releaseConnection() {
        try {
            this.managedConn.releaseConnection();
        } catch (IOException ignored) {
            this.log.debug("IOException releasing connection", ignored);
        }
        this.managedConn = null;
    }

    private HttpRoute determineRoute$1e70857f(HttpHost targetHost, HttpRequest request) throws HttpException {
        HttpRoutePlanner httpRoutePlanner = this.routePlanner;
        if (targetHost == null) {
            targetHost = (HttpHost) request.getParams().getParameter("http.default-host");
        }
        return httpRoutePlanner.determineRoute$1e70857f(targetHost, request);
    }

    private void abortConnection() {
        ManagedClientConnection mcc = this.managedConn;
        if (mcc != null) {
            this.managedConn = null;
            try {
                mcc.abortConnection();
            } catch (IOException ex) {
                if (this.log.debugEnabled) {
                    this.log.debug(ex.getMessage(), ex);
                }
            }
            try {
                mcc.releaseConnection();
            } catch (IOException ignored) {
                this.log.debug("Error releasing connection", ignored);
            }
        }
    }
}
