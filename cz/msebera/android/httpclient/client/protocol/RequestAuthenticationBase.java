package cz.msebera.android.httpclient.client.protocol;

import android.util.Log;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpRequestInterceptor;
import cz.msebera.android.httpclient.auth.AuthOption;
import cz.msebera.android.httpclient.auth.AuthProtocolState;
import cz.msebera.android.httpclient.auth.AuthScheme;
import cz.msebera.android.httpclient.auth.AuthState;
import cz.msebera.android.httpclient.auth.AuthenticationException;
import cz.msebera.android.httpclient.auth.ContextAwareAuthScheme;
import cz.msebera.android.httpclient.auth.Credentials;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.util.Asserts;
import java.util.Queue;
import org.eclipse.paho.client.mqttv3.logging.Logger;

@Deprecated
abstract class RequestAuthenticationBase implements HttpRequestInterceptor {
    final HttpClientAndroidLog log;

    /* renamed from: cz.msebera.android.httpclient.client.protocol.RequestAuthenticationBase.1 */
    static /* synthetic */ class C12431 {
        static final /* synthetic */ int[] $SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState;

        static {
            $SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState = new int[AuthProtocolState.values().length];
            try {
                $SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState[AuthProtocolState.FAILURE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState[AuthProtocolState.SUCCESS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState[AuthProtocolState.CHALLENGED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    public RequestAuthenticationBase() {
        this.log = new HttpClientAndroidLog(getClass());
    }

    final void process$218650dd(AuthState authState, HttpRequest request) {
        AuthScheme authScheme = authState.authScheme;
        Credentials creds = authState.credentials;
        switch (C12431.$SwitchMap$cz$msebera$android$httpclient$auth$AuthProtocolState[authState.state.ordinal()]) {
            case Logger.SEVERE /*1*/:
                return;
            case Logger.WARNING /*2*/:
                Asserts.notNull(authScheme, "Auth scheme");
                if (authScheme.isConnectionBased()) {
                    return;
                }
                break;
            case Logger.INFO /*3*/:
                Queue<AuthOption> authOptions = authState.authOptions;
                if (authOptions == null) {
                    Asserts.notNull(authScheme, "Auth scheme");
                    break;
                }
                while (!authOptions.isEmpty()) {
                    AuthOption authOption = (AuthOption) authOptions.remove();
                    authScheme = authOption.authScheme;
                    creds = authOption.creds;
                    authState.update(authScheme, creds);
                    if (this.log.debugEnabled) {
                        this.log.debug("Generating response to an authentication challenge using " + authScheme.getSchemeName() + " scheme");
                    }
                    try {
                        request.addHeader(authenticate$62a6cd4b(authScheme, creds, request));
                        return;
                    } catch (AuthenticationException ex) {
                        if (this.log.warnEnabled) {
                            this.log.warn(authScheme + " authentication error: " + ex.getMessage());
                        }
                    }
                }
                return;
        }
        if (authScheme != null) {
            try {
                request.addHeader(authenticate$62a6cd4b(authScheme, creds, request));
            } catch (AuthenticationException ex2) {
                if (this.log.errorEnabled) {
                    HttpClientAndroidLog httpClientAndroidLog = this.log;
                    String str = authScheme + " authentication error: " + ex2.getMessage();
                    if (httpClientAndroidLog.errorEnabled) {
                        Log.e(httpClientAndroidLog.logTag, str.toString());
                    }
                }
            }
        }
    }

    private static Header authenticate$62a6cd4b(AuthScheme authScheme, Credentials creds, HttpRequest request) throws AuthenticationException {
        Asserts.notNull(authScheme, "Auth scheme");
        if (authScheme instanceof ContextAwareAuthScheme) {
            return ((ContextAwareAuthScheme) authScheme).authenticate$f1343fe(creds, request);
        }
        return authScheme.authenticate(creds, request);
    }
}
