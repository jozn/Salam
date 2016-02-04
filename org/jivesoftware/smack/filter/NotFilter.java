package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public final class NotFilter implements PacketFilter {
    private PacketFilter filter;

    public NotFilter(PacketFilter filter) {
        this.filter = filter;
    }

    public final boolean accept(Packet packet) {
        return !this.filter.accept(packet);
    }
}
