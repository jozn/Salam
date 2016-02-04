package cz.msebera.android.httpclient.impl.client;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.auth.AuthOption;
import cz.msebera.android.httpclient.auth.AuthScheme;
import cz.msebera.android.httpclient.auth.AuthScope;
import cz.msebera.android.httpclient.auth.AuthenticationException;
import cz.msebera.android.httpclient.auth.Credentials;
import cz.msebera.android.httpclient.auth.MalformedChallengeException;
import cz.msebera.android.httpclient.client.AuthCache;
import cz.msebera.android.httpclient.client.AuthenticationHandler;
import cz.msebera.android.httpclient.client.AuthenticationStrategy;
import cz.msebera.android.httpclient.client.CredentialsProvider;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

@Deprecated
final class AuthenticationStrategyAdaptor implements AuthenticationStrategy {
    final AuthenticationHandler handler;
    public HttpClientAndroidLog log;

    public final boolean isAuthenticationRequested(HttpHost authhost, HttpResponse response, HttpContext context) {
        return this.handler.isAuthenticationRequested$22649b72();
    }

    public final Map<String, Header> getChallenges(HttpHost authhost, HttpResponse response, HttpContext context) throws MalformedChallengeException {
        return this.handler.getChallenges$eb31523();
    }

    public final Queue<AuthOption> select(Map<String, Header> challenges, HttpHost authhost, HttpResponse response, HttpContext context) throws MalformedChallengeException {
        Args.notNull(challenges, "Map of auth challenges");
        Args.notNull(authhost, "Host");
        Args.notNull(response, "HTTP response");
        Args.notNull(context, "HTTP context");
        Queue<AuthOption> options = new LinkedList();
        CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute("http.auth.credentials-provider");
        if (credsProvider == null) {
            this.log.debug("Credentials provider not set in the context");
        } else {
            try {
                AuthScheme authScheme = this.handler.selectScheme$238ab12c();
                authScheme.processChallenge((Header) challenges.get(authScheme.getSchemeName().toLowerCase(Locale.ROOT)));
                Credentials credentials = credsProvider.getCredentials(new AuthScope(authhost.getHostName(), authhost.getPort(), authScheme.getRealm(), authScheme.getSchemeName()));
                if (credentials != null) {
                    options.add(new AuthOption(authScheme, credentials));
                }
            } catch (AuthenticationException ex) {
                if (this.log.warnEnabled) {
                    this.log.warn(ex.getMessage(), ex);
                }
            }
        }
        return options;
    }

    public final void authSucceeded(HttpHost authhost, AuthScheme authScheme, HttpContext context) {
        Object obj;
        AuthCache authCache = (AuthCache) context.getAttribute("http.auth.auth-cache");
        if (authScheme == null || !authScheme.isComplete()) {
            obj = (byte) 0;
        } else {
            String schemeName = authScheme.getSchemeName();
            obj = (schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest")) ? 1 : (byte) 0;
        }
        if (obj != null) {
            if (authCache == null) {
                authCache = new BasicAuthCache((byte) 0);
                context.setAttribute("http.auth.auth-cache", authCache);
            }
            if (this.log.debugEnabled) {
                this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + authhost);
            }
            authCache.put(authhost, authScheme);
        }
    }

    public final void authFailed(HttpHost authhost, AuthScheme authScheme, HttpContext context) {
        AuthCache authCache = (AuthCache) context.getAttribute("http.auth.auth-cache");
        if (authCache != null) {
            if (this.log.debugEnabled) {
                this.log.debug("Removing from cache '" + authScheme.getSchemeName() + "' auth scheme for " + authhost);
            }
            authCache.remove(authhost);
        }
    }
}
