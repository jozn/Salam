package cz.msebera.android.httpclient.impl.client;

import cz.msebera.android.httpclient.auth.AuthScope;
import cz.msebera.android.httpclient.auth.Credentials;
import cz.msebera.android.httpclient.client.CredentialsProvider;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.LangUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BasicCredentialsProvider implements CredentialsProvider {
    private final ConcurrentHashMap<AuthScope, Credentials> credMap;

    public BasicCredentialsProvider() {
        this.credMap = new ConcurrentHashMap();
    }

    public final Credentials getCredentials(AuthScope authscope) {
        Args.notNull(authscope, "Authentication scope");
        Map map = this.credMap;
        Credentials credentials = (Credentials) map.get(authscope);
        if (credentials != null) {
            return credentials;
        }
        AuthScope authScope = null;
        int i = -1;
        for (AuthScope authScope2 : map.keySet()) {
            AuthScope authScope22;
            int i2;
            int i3 = 0;
            if (LangUtils.equals(authscope.scheme, authScope22.scheme)) {
                i3 = 1;
            } else if (!(authscope.scheme == AuthScope.ANY_SCHEME || authScope22.scheme == AuthScope.ANY_SCHEME)) {
                i3 = -1;
                if (i3 <= i) {
                    i2 = i3;
                } else {
                    authScope22 = authScope;
                    i2 = i;
                }
                i = i2;
                authScope = authScope22;
            }
            if (LangUtils.equals(authscope.realm, authScope22.realm)) {
                i3 += 2;
            } else if (!(authscope.realm == AuthScope.ANY_REALM || authScope22.realm == AuthScope.ANY_REALM)) {
                i3 = -1;
                if (i3 <= i) {
                    authScope22 = authScope;
                    i2 = i;
                } else {
                    i2 = i3;
                }
                i = i2;
                authScope = authScope22;
            }
            if (authscope.port == authScope22.port) {
                i3 += 4;
            } else if (!(authscope.port == -1 || authScope22.port == -1)) {
                i3 = -1;
                if (i3 <= i) {
                    i2 = i3;
                } else {
                    authScope22 = authScope;
                    i2 = i;
                }
                i = i2;
                authScope = authScope22;
            }
            if (LangUtils.equals(authscope.host, authScope22.host)) {
                i3 += 8;
            } else if (!(authscope.host == AuthScope.ANY_HOST || authScope22.host == AuthScope.ANY_HOST)) {
                i3 = -1;
            }
            if (i3 <= i) {
                authScope22 = authScope;
                i2 = i;
            } else {
                i2 = i3;
            }
            i = i2;
            authScope = authScope22;
        }
        return authScope != null ? (Credentials) map.get(authScope) : credentials;
    }

    public final String toString() {
        return this.credMap.toString();
    }
}
