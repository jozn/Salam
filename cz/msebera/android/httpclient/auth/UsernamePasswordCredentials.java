package cz.msebera.android.httpclient.auth;

import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.LangUtils;
import java.io.Serializable;
import java.security.Principal;

public final class UsernamePasswordCredentials implements Credentials, Serializable {
    private final String password;
    private final BasicUserPrincipal principal;

    public UsernamePasswordCredentials(String usernamePassword) {
        Args.notNull(usernamePassword, "Username:password string");
        int atColon = usernamePassword.indexOf(58);
        if (atColon >= 0) {
            this.principal = new BasicUserPrincipal(usernamePassword.substring(0, atColon));
            this.password = usernamePassword.substring(atColon + 1);
            return;
        }
        this.principal = new BasicUserPrincipal(usernamePassword);
        this.password = null;
    }

    public final Principal getUserPrincipal() {
        return this.principal;
    }

    public final String getPassword() {
        return this.password;
    }

    public final int hashCode() {
        return this.principal.hashCode();
    }

    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UsernamePasswordCredentials) {
            if (LangUtils.equals(this.principal, ((UsernamePasswordCredentials) o).principal)) {
                return true;
            }
        }
        return false;
    }

    public final String toString() {
        return this.principal.toString();
    }
}
