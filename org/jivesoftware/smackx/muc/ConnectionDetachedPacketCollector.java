package org.jivesoftware.smackx.muc;

import java.util.concurrent.ArrayBlockingQueue;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.packet.Packet;

final class ConnectionDetachedPacketCollector {
    ArrayBlockingQueue<Packet> resultQueue;

    public ConnectionDetachedPacketCollector() {
        this(SmackConfiguration.getPacketCollectorSize());
    }

    private ConnectionDetachedPacketCollector(int maxSize) {
        this.resultQueue = new ArrayBlockingQueue(maxSize);
    }
}
