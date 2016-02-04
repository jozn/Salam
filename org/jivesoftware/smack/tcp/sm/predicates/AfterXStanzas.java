package org.jivesoftware.smack.tcp.sm.predicates;

import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

public final class AfterXStanzas implements PacketFilter {
    final int count;
    int currentCount;

    public AfterXStanzas() {
        this.count = 5;
        this.currentCount = 0;
    }

    public final synchronized boolean accept(Packet packet) {
        boolean z;
        this.currentCount++;
        if (this.currentCount == this.count) {
            resetCounter();
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public final synchronized void resetCounter() {
        this.currentCount = 0;
    }
}
