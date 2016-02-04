package cz.msebera.android.httpclient.client;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.auth.AuthScheme;
import cz.msebera.android.httpclient.auth.AuthenticationException;
import cz.msebera.android.httpclient.auth.MalformedChallengeException;
import java.util.Map;

@Deprecated
public interface AuthenticationHandler {
    Map<String, Header> getChallenges$eb31523() throws MalformedChallengeException;

    boolean isAuthenticationRequested$22649b72();

    AuthScheme selectScheme$238ab12c() throws AuthenticationException;
}
