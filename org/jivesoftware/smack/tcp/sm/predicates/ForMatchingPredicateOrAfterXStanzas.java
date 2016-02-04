package org.jivesoftware.smack.tcp.sm.predicates;

import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

public final class ForMatchingPredicateOrAfterXStanzas implements PacketFilter {
    private final AfterXStanzas afterXStanzas;
    private final PacketFilter predicate;

    public ForMatchingPredicateOrAfterXStanzas(PacketFilter predicate) {
        this.predicate = predicate;
        this.afterXStanzas = new AfterXStanzas();
    }

    public final boolean accept(Packet packet) {
        if (!this.predicate.accept(packet)) {
            return this.afterXStanzas.accept(packet);
        }
        this.afterXStanzas.resetCounter();
        return true;
    }
}
