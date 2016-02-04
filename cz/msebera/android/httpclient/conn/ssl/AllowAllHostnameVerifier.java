package cz.msebera.android.httpclient.conn.ssl;

@Deprecated
public final class AllowAllHostnameVerifier extends AbstractVerifier {
    public static final AllowAllHostnameVerifier INSTANCE;

    static {
        INSTANCE = new AllowAllHostnameVerifier();
    }

    public final void verify(String host, String[] cns, String[] subjectAlts) {
    }

    public final String toString() {
        return "ALLOW_ALL";
    }
}
