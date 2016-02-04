package cz.msebera.android.httpclient.client.params;

import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;

@Deprecated
public final class HttpClientParams {
    public static boolean isAuthenticating(HttpParams params) {
        Args.notNull(params, "HTTP parameters");
        return params.getBooleanParameter("http.protocol.handle-authentication", true);
    }
}
