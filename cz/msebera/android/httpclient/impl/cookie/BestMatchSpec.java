package cz.msebera.android.httpclient.impl.cookie;

@Deprecated
public final class BestMatchSpec extends DefaultCookieSpec {
    public BestMatchSpec(String[] datepatterns, boolean oneHeader) {
        super(datepatterns, oneHeader);
    }

    public BestMatchSpec() {
        this(null, false);
    }

    public final String toString() {
        return "best-match";
    }
}
