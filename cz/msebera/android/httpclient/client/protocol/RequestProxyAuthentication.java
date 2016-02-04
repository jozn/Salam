package cz.msebera.android.httpclient.client.protocol;

import cz.msebera.android.httpclient.HttpException;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.auth.AuthState;
import cz.msebera.android.httpclient.conn.HttpRoutedConnection;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;

@Deprecated
public final class RequestProxyAuthentication extends RequestAuthenticationBase {
    public final void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        Args.notNull(context, "HTTP context");
        if (!request.containsHeader("Proxy-Authorization")) {
            HttpRoutedConnection conn = (HttpRoutedConnection) context.getAttribute("http.connection");
            if (conn == null) {
                this.log.debug("HTTP connection not set in the context");
            } else if (!conn.getRoute().isTunnelled()) {
                AuthState authState = (AuthState) context.getAttribute("http.auth.proxy-scope");
                if (authState == null) {
                    this.log.debug("Proxy auth state not set in the context");
                    return;
                }
                if (this.log.debugEnabled) {
                    this.log.debug("Proxy auth state: " + authState.state);
                }
                process$218650dd(authState, request);
            }
        }
    }
}
