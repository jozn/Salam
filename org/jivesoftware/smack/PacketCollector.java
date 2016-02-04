package org.jivesoftware.smack;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

public class PacketCollector {
    private static final Logger LOGGER;
    private boolean cancelled;
    private XMPPConnection connection;
    PacketFilter packetFilter;
    ArrayBlockingQueue<Packet> resultQueue;

    static {
        LOGGER = Logger.getLogger(PacketCollector.class.getName());
    }

    protected PacketCollector(XMPPConnection connection, PacketFilter packetFilter) {
        this(connection, packetFilter, SmackConfiguration.getPacketCollectorSize());
    }

    private PacketCollector(XMPPConnection connection, PacketFilter packetFilter, int maxSize) {
        this.cancelled = false;
        this.connection = connection;
        this.packetFilter = packetFilter;
        this.resultQueue = new ArrayBlockingQueue(maxSize);
    }

    public final void cancel() {
        if (!this.cancelled) {
            this.cancelled = true;
            this.connection.removePacketCollector(this);
        }
    }

    private <P extends Packet> P nextResult(long timeout) {
        P res = null;
        long remainingWait = timeout;
        long waitStart = System.currentTimeMillis();
        while (res == null && remainingWait > 0) {
            try {
                res = (Packet) this.resultQueue.poll(remainingWait, TimeUnit.MILLISECONDS);
                remainingWait = timeout - (System.currentTimeMillis() - waitStart);
            } catch (InterruptedException e) {
                LOGGER.log(Level.FINE, "nextResult was interrupted", e);
            }
        }
        return res;
    }

    public final <P extends Packet> P nextResultOrThrow() throws NoResponseException, XMPPErrorException {
        return nextResultOrThrow(this.connection.getPacketReplyTimeout());
    }

    public final <P extends Packet> P nextResultOrThrow(long timeout) throws NoResponseException, XMPPErrorException {
        P result = nextResult(timeout);
        cancel();
        if (result == null) {
            throw new NoResponseException();
        }
        XMPPErrorException.ifHasErrorThenThrow(result);
        return result;
    }
}
