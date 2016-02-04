package org.jivesoftware.smack.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jivesoftware.smack.packet.Packet;

public final class OrFilter implements PacketFilter {
    private final List<PacketFilter> filters;

    public OrFilter() {
        this.filters = new ArrayList();
    }

    public OrFilter(PacketFilter... filters) {
        PacketFilter[] arr$ = filters;
        for (int i$ = 0; i$ < 2; i$++) {
            if (arr$[i$] == null) {
                throw new IllegalArgumentException("Parameter must not be null.");
            }
        }
        this.filters = new ArrayList(Arrays.asList(filters));
    }

    public final void addFilter(PacketFilter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        this.filters.add(filter);
    }

    public final boolean accept(Packet packet) {
        for (PacketFilter accept : this.filters) {
            if (accept.accept(packet)) {
                return true;
            }
        }
        return false;
    }

    public final String toString() {
        return this.filters.toString();
    }
}
