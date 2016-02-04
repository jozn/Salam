package cz.msebera.android.httpclient.auth;

import cz.msebera.android.httpclient.util.Args;
import java.util.Queue;

public class AuthState {
    public Queue<AuthOption> authOptions;
    public AuthScheme authScheme;
    private AuthScope authScope;
    public Credentials credentials;
    public AuthProtocolState state;

    public AuthState() {
        this.state = AuthProtocolState.UNCHALLENGED;
    }

    public final void reset() {
        this.state = AuthProtocolState.UNCHALLENGED;
        this.authOptions = null;
        this.authScheme = null;
        this.authScope = null;
        this.credentials = null;
    }

    public final void setState(AuthProtocolState state) {
        if (state == null) {
            state = AuthProtocolState.UNCHALLENGED;
        }
        this.state = state;
    }

    public final void update(AuthScheme authScheme, Credentials credentials) {
        Args.notNull(authScheme, "Auth scheme");
        Args.notNull(credentials, "Credentials");
        this.authScheme = authScheme;
        this.credentials = credentials;
        this.authOptions = null;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("state:").append(this.state).append(";");
        if (this.authScheme != null) {
            buffer.append("auth scheme:").append(this.authScheme.getSchemeName()).append(";");
        }
        if (this.credentials != null) {
            buffer.append("credentials present");
        }
        return buffer.toString();
    }
}
