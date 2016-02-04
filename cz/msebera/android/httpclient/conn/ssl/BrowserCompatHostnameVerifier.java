package cz.msebera.android.httpclient.conn.ssl;

import javax.net.ssl.SSLException;

@Deprecated
public final class BrowserCompatHostnameVerifier extends AbstractVerifier {
    public static final BrowserCompatHostnameVerifier INSTANCE;

    static {
        INSTANCE = new BrowserCompatHostnameVerifier();
    }

    public final void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
        AbstractVerifier.verify(host, cns, subjectAlts, false);
    }

    public final String toString() {
        return "BROWSER_COMPATIBLE";
    }
}
