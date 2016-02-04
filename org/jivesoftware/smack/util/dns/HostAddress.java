package org.jivesoftware.smack.util.dns;

import android.support.v4.internal.view.SupportMenu;

public class HostAddress {
    public Exception exception;
    public final String fqdn;
    public final int port;

    public HostAddress(String fqdn) {
        this(fqdn, 5222);
    }

    public HostAddress(String fqdn, int port) {
        if (fqdn == null) {
            throw new IllegalArgumentException("FQDN is null");
        } else if (port < 0 || port > SupportMenu.USER_MASK) {
            throw new IllegalArgumentException("Port must be a 16-bit unsiged integer (i.e. between 0-65535. Port was: " + port);
        } else {
            if (fqdn.charAt(fqdn.length() - 1) == '.') {
                this.fqdn = fqdn.substring(0, fqdn.length() - 1);
            } else {
                this.fqdn = fqdn;
            }
            this.port = port;
        }
    }

    public String toString() {
        return this.fqdn + ":" + this.port;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HostAddress)) {
            return false;
        }
        HostAddress address = (HostAddress) o;
        if (!this.fqdn.equals(address.fqdn)) {
            return false;
        }
        if (this.port != address.port) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((this.fqdn.hashCode() + 37) * 37) + this.port;
    }
}
