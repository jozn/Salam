package cz.msebera.android.httpclient.conn.ssl;

import javax.net.ssl.SSLException;

@Deprecated
public final class StrictHostnameVerifier extends AbstractVerifier {
    public static final StrictHostnameVerifier INSTANCE;

    static {
        INSTANCE = new StrictHostnameVerifier();
    }

    public final void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
        AbstractVerifier.verify(host, cns, subjectAlts, true);
    }

    public final String toString() {
        return "STRICT";
    }
}
