package cz.msebera.android.httpclient.auth;

import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.config.Lookup;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public final class AuthSchemeRegistry implements Lookup<AuthSchemeProvider> {
    final ConcurrentHashMap<String, AuthSchemeFactory> registeredSchemes;

    /* renamed from: cz.msebera.android.httpclient.auth.AuthSchemeRegistry.1 */
    class C12401 implements AuthSchemeProvider {
        final /* synthetic */ String val$name;

        C12401(String str) {
            this.val$name = str;
        }

        public final AuthScheme create(HttpContext context) {
            HttpRequest request = (HttpRequest) context.getAttribute("http.request");
            AuthSchemeRegistry authSchemeRegistry = AuthSchemeRegistry.this;
            String str = this.val$name;
            request.getParams();
            Args.notNull(str, "Name");
            AuthSchemeFactory authSchemeFactory = (AuthSchemeFactory) authSchemeRegistry.registeredSchemes.get(str.toLowerCase(Locale.ENGLISH));
            if (authSchemeFactory != null) {
                return authSchemeFactory.newInstance$5096e802();
            }
            throw new IllegalStateException("Unsupported authentication scheme: " + str);
        }
    }

    public final /* bridge */ /* synthetic */ Object lookup(String str) {
        return new C12401(str);
    }

    public AuthSchemeRegistry() {
        this.registeredSchemes = new ConcurrentHashMap();
    }

    public final void register(String name, AuthSchemeFactory factory) {
        Args.notNull(name, "Name");
        Args.notNull(factory, "Authentication scheme factory");
        this.registeredSchemes.put(name.toLowerCase(Locale.ENGLISH), factory);
    }
}
