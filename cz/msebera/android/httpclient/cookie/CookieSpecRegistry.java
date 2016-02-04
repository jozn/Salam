package cz.msebera.android.httpclient.cookie;

import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.config.Lookup;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.HttpContext;
import cz.msebera.android.httpclient.util.Args;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public final class CookieSpecRegistry implements Lookup<CookieSpecProvider> {
    final ConcurrentHashMap<String, CookieSpecFactory> registeredSpecs;

    /* renamed from: cz.msebera.android.httpclient.cookie.CookieSpecRegistry.1 */
    class C12461 implements CookieSpecProvider {
        final /* synthetic */ String val$name;

        C12461(String str) {
            this.val$name = str;
        }

        public final CookieSpec create(HttpContext context) {
            HttpRequest request = (HttpRequest) context.getAttribute("http.request");
            CookieSpecRegistry cookieSpecRegistry = CookieSpecRegistry.this;
            String str = this.val$name;
            HttpParams params = request.getParams();
            Args.notNull(str, "Name");
            CookieSpecFactory cookieSpecFactory = (CookieSpecFactory) cookieSpecRegistry.registeredSpecs.get(str.toLowerCase(Locale.ENGLISH));
            if (cookieSpecFactory != null) {
                return cookieSpecFactory.newInstance(params);
            }
            throw new IllegalStateException("Unsupported cookie spec: " + str);
        }
    }

    public final /* bridge */ /* synthetic */ Object lookup(String str) {
        return new C12461(str);
    }

    public CookieSpecRegistry() {
        this.registeredSpecs = new ConcurrentHashMap();
    }

    public final void register(String name, CookieSpecFactory factory) {
        Args.notNull(name, "Name");
        Args.notNull(factory, "Cookie spec factory");
        this.registeredSpecs.put(name.toLowerCase(Locale.ENGLISH), factory);
    }
}
