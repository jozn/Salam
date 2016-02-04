package cz.msebera.android.httpclient.auth;

import cz.msebera.android.httpclient.util.LangUtils;
import java.io.Serializable;
import java.security.Principal;

public final class NTUserPrincipal implements Serializable, Principal {
    public final String domain;
    private final String ntname;
    public final String username;

    public final String getName() {
        return this.ntname;
    }

    public final int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode(17, this.username), this.domain);
    }

    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof NTUserPrincipal) {
            NTUserPrincipal that = (NTUserPrincipal) o;
            if (LangUtils.equals(this.username, that.username) && LangUtils.equals(this.domain, that.domain)) {
                return true;
            }
        }
        return false;
    }

    public final String toString() {
        return this.ntname;
    }
}
