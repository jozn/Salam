package org.jivesoftware.smackx.muc;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

final class PacketMultiplexListener implements PacketListener {
    private static final PacketFilter DECLINES_FILTER;
    private static final PacketFilter PRESENCE_FILTER;
    private static final PacketFilter SUBJECT_FILTER;
    private PacketListener declinesListener;
    private ConnectionDetachedPacketCollector messageCollector;
    private PacketListener presenceListener;
    private PacketListener subjectListener;

    /* renamed from: org.jivesoftware.smackx.muc.PacketMultiplexListener.1 */
    static class C13361 implements PacketFilter {
        C13361() {
        }

        public final boolean accept(Packet packet) {
            return ((Message) packet).getSubject(null) != null;
        }
    }

    static {
        PRESENCE_FILTER = new PacketTypeFilter(Presence.class);
        SUBJECT_FILTER = new C13361();
        DECLINES_FILTER = new PacketExtensionFilter("x", "http://jabber.org/protocol/muc#user");
    }

    public PacketMultiplexListener(ConnectionDetachedPacketCollector messageCollector, PacketListener presenceListener, PacketListener subjectListener, PacketListener declinesListener) {
        if (messageCollector == null) {
            throw new IllegalArgumentException("MessageCollector is null");
        }
        this.messageCollector = messageCollector;
        this.presenceListener = presenceListener;
        this.subjectListener = subjectListener;
        this.declinesListener = declinesListener;
    }

    public final void processPacket(Packet p) throws NotConnectedException {
        if (PRESENCE_FILTER.accept(p)) {
            this.presenceListener.processPacket(p);
        } else if (MessageTypeFilter.GROUPCHAT.accept(p)) {
            ConnectionDetachedPacketCollector connectionDetachedPacketCollector = this.messageCollector;
            if (p != null) {
                while (!connectionDetachedPacketCollector.resultQueue.offer(p)) {
                    connectionDetachedPacketCollector.resultQueue.poll();
                }
            }
            if (SUBJECT_FILTER.accept(p)) {
                this.subjectListener.processPacket(p);
            }
        } else if (DECLINES_FILTER.accept(p)) {
            this.declinesListener.processPacket(p);
        }
    }
}
