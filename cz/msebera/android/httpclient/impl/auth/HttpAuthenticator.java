package cz.msebera.android.httpclient.impl.auth;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpHost;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.auth.AuthOption;
import cz.msebera.android.httpclient.auth.AuthProtocolState;
import cz.msebera.android.httpclient.auth.AuthScheme;
import cz.msebera.android.httpclient.auth.AuthState;
import cz.msebera.android.httpclient.auth.MalformedChallengeException;
import cz.msebera.android.httpclient.client.AuthenticationStrategy;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import org.eclipse.paho.client.mqttv3.logging.Logger;

public class HttpAuthenticator {
    public HttpClientAndroidLog log;

    /* renamed from: cz.msebera.android.httpclient.impl.auth.HttpAuthenticator.1 */
    static /* synthetic */ class C12471 {
        static final /* synthetic */ int[] $SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState;

        static {
            $SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState = new int[AuthProtocolState.values().length];
            try {
                $SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState[AuthProtocolState.CHALLENGED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState[AuthProtocolState.HANDSHAKE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState[AuthProtocolState.SUCCESS.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState[AuthProtocolState.FAILURE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState[AuthProtocolState.UNCHALLENGED.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public HttpAuthenticator(HttpClientAndroidLog log) {
        if (log == null) {
            log = new HttpClientAndroidLog(getClass());
        }
        this.log = log;
    }

    public HttpAuthenticator() {
        this(null);
    }

    public final boolean isAuthenticationRequested(HttpHost host, HttpResponse response, AuthenticationStrategy authStrategy, AuthState authState, HttpContext context) {
        if (authStrategy.isAuthenticationRequested(host, response, context)) {
            this.log.debug("Authentication required");
            if (authState.state == AuthProtocolState.SUCCESS) {
                authStrategy.authFailed(host, authState.authScheme, context);
            }
            return true;
        }
        switch (C12471.$SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState[authState.state.ordinal()]) {
            case Logger.SEVERE /*1*/:
            case Logger.WARNING /*2*/:
                this.log.debug("Authentication succeeded");
                authState.setState(AuthProtocolState.SUCCESS);
                authStrategy.authSucceeded(host, authState.authScheme, context);
                break;
            case Logger.INFO /*3*/:
                break;
            default:
                authState.setState(AuthProtocolState.UNCHALLENGED);
                break;
        }
        return false;
    }

    public final boolean handleAuthChallenge(HttpHost host, HttpResponse response, AuthenticationStrategy authStrategy, AuthState authState, HttpContext context) {
        try {
            if (this.log.debugEnabled) {
                this.log.debug(host.toHostString() + " requested authentication");
            }
            Map<String, Header> challenges = authStrategy.getChallenges(host, response, context);
            if (challenges.isEmpty()) {
                this.log.debug("Response contains no authentication challenges");
                return false;
            }
            AuthScheme authScheme = authState.authScheme;
            switch (C12471.$SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState[authState.state.ordinal()]) {
                case Logger.SEVERE /*1*/:
                case Logger.WARNING /*2*/:
                    if (authScheme == null) {
                        this.log.debug("Auth scheme is null");
                        authStrategy.authFailed(host, null, context);
                        authState.reset();
                        authState.setState(AuthProtocolState.FAILURE);
                        return false;
                    }
                    break;
                case Logger.INFO /*3*/:
                    authState.reset();
                    break;
                case Logger.CONFIG /*4*/:
                    return false;
                case Logger.FINE /*5*/:
                    break;
            }
            if (authScheme != null) {
                Header challenge = (Header) challenges.get(authScheme.getSchemeName().toLowerCase(Locale.ROOT));
                if (challenge != null) {
                    this.log.debug("Authorization challenge processed");
                    authScheme.processChallenge(challenge);
                    if (authScheme.isComplete()) {
                        this.log.debug("Authentication failed");
                        authStrategy.authFailed(host, authState.authScheme, context);
                        authState.reset();
                        authState.setState(AuthProtocolState.FAILURE);
                        return false;
                    }
                    authState.setState(AuthProtocolState.HANDSHAKE);
                    return true;
                }
                authState.reset();
            }
            Queue<AuthOption> authOptions = authStrategy.select(challenges, host, response, context);
            if (authOptions == null || authOptions.isEmpty()) {
                return false;
            }
            if (this.log.debugEnabled) {
                this.log.debug("Selected authentication options: " + authOptions);
            }
            authState.setState(AuthProtocolState.CHALLENGED);
            Args.notEmpty(authOptions, "Queue of auth options");
            authState.authOptions = authOptions;
            authState.authScheme = null;
            authState.credentials = null;
            return true;
        } catch (MalformedChallengeException ex) {
            if (this.log.warnEnabled) {
                this.log.warn("Malformed challenge: " + ex.getMessage());
            }
            authState.reset();
            return false;
        }
    }
}
