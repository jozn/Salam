package cz.msebera.android.httpclient.impl.auth;

import cz.msebera.android.httpclient.auth.AuthScheme;
import cz.msebera.android.httpclient.auth.AuthSchemeFactory;
import cz.msebera.android.httpclient.auth.AuthSchemeProvider;
import cz.msebera.android.httpclient.protocol.HttpContext;
import java.nio.charset.Charset;

public final class DigestSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {
    private final Charset charset;

    private DigestSchemeFactory() {
        this.charset = null;
    }

    public DigestSchemeFactory(byte b) {
        this();
    }

    public final AuthScheme newInstance$5096e802() {
        return new DigestScheme();
    }

    public final AuthScheme create(HttpContext context) {
        return new DigestScheme(this.charset);
    }
}
