package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public abstract class FlexiblePacketTypeFilter<P extends Packet> implements PacketFilter {
    final Class<P> packetType;

    protected abstract boolean acceptSpecific(P p);

    public FlexiblePacketTypeFilter(Class<P> packetType) {
        this.packetType = packetType;
    }

    public final boolean accept(Packet packet) {
        if (this.packetType.isInstance(packet)) {
            return acceptSpecific(packet);
        }
        return false;
    }
}
