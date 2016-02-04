package cz.msebera.android.httpclient.impl.auth;

import cz.msebera.android.httpclient.auth.AuthScheme;
import cz.msebera.android.httpclient.auth.AuthSchemeFactory;
import cz.msebera.android.httpclient.auth.AuthSchemeProvider;
import cz.msebera.android.httpclient.protocol.HttpContext;

public final class NTLMSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {
    public final AuthScheme newInstance$5096e802() {
        return new NTLMScheme();
    }

    public final AuthScheme create(HttpContext context) {
        return new NTLMScheme();
    }
}
