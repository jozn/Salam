package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

public final class PacketTypeFilter implements PacketFilter {
    public static final PacketTypeFilter MESSAGE;
    public static final PacketTypeFilter PRESENCE;
    Class<? extends Packet> packetType;

    static {
        PRESENCE = new PacketTypeFilter(Presence.class);
        MESSAGE = new PacketTypeFilter(Message.class);
    }

    public PacketTypeFilter(Class<? extends Packet> packetType) {
        this.packetType = packetType;
    }

    public final boolean accept(Packet packet) {
        return this.packetType.isInstance(packet);
    }

    public final String toString() {
        return "PacketTypeFilter: " + this.packetType.getName();
    }
}
