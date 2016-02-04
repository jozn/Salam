package org.jivesoftware.smack.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jivesoftware.smack.packet.Packet;

public final class AndFilter implements PacketFilter {
    private final List<PacketFilter> filters;

    public AndFilter() {
        this.filters = new ArrayList();
    }

    public AndFilter(PacketFilter... filters) {
        PacketFilter[] arr$ = filters;
        int len$ = filters.length;
        for (int i$ = 0; i$ < len$; i$++) {
            if (arr$[i$] == null) {
                throw new IllegalArgumentException("Parameter must not be null.");
            }
        }
        this.filters = new ArrayList(Arrays.asList(filters));
    }

    public final boolean accept(Packet packet) {
        for (PacketFilter accept : this.filters) {
            if (!accept.accept(packet)) {
                return false;
            }
        }
        return true;
    }

    public final String toString() {
        return this.filters.toString();
    }
}
