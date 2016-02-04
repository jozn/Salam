package cz.msebera.android.httpclient.auth;

import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.LangUtils;
import java.io.Serializable;
import java.security.Principal;

public final class BasicUserPrincipal implements Serializable, Principal {
    private final String username;

    public BasicUserPrincipal(String username) {
        Args.notNull(username, "User name");
        this.username = username;
    }

    public final String getName() {
        return this.username;
    }

    public final int hashCode() {
        return LangUtils.hashCode(17, this.username);
    }

    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof BasicUserPrincipal) {
            if (LangUtils.equals(this.username, ((BasicUserPrincipal) o).username)) {
                return true;
            }
        }
        return false;
    }

    public final String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[principal: ");
        buffer.append(this.username);
        buffer.append("]");
        return buffer.toString();
    }
}
