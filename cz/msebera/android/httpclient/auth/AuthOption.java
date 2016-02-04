package cz.msebera.android.httpclient.auth;

import cz.msebera.android.httpclient.util.Args;

public final class AuthOption {
    public final AuthScheme authScheme;
    public final Credentials creds;

    public AuthOption(AuthScheme authScheme, Credentials creds) {
        Args.notNull(authScheme, "Auth scheme");
        Args.notNull(creds, "User credentials");
        this.authScheme = authScheme;
        this.creds = creds;
    }

    public final String toString() {
        return this.authScheme.toString();
    }
}
