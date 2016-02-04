package org.jivesoftware.smack.tcp.sm.predicates;

import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public final class ForEveryMessage implements PacketFilter {
    public static final ForEveryMessage INSTANCE;

    static {
        INSTANCE = new ForEveryMessage();
    }

    private ForEveryMessage() {
    }

    public final boolean accept(Packet packet) {
        if (packet instanceof Message) {
            return true;
        }
        return false;
    }
}
