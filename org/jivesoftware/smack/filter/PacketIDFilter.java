package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public final class PacketIDFilter implements PacketFilter {
    private String packetID;

    public PacketIDFilter(Packet packet) {
        this(packet.packetID);
    }

    public PacketIDFilter(String packetID) {
        if (packetID == null) {
            throw new IllegalArgumentException("Packet ID must not be null.");
        }
        this.packetID = packetID;
    }

    public final boolean accept(Packet packet) {
        return this.packetID.equals(packet.packetID);
    }

    public final String toString() {
        return "PacketIDFilter by id: " + this.packetID;
    }
}
