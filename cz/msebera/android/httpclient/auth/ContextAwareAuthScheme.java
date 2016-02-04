package cz.msebera.android.httpclient.auth;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpRequest;

public interface ContextAwareAuthScheme extends AuthScheme {
    Header authenticate$f1343fe(Credentials credentials, HttpRequest httpRequest) throws AuthenticationException;
}
