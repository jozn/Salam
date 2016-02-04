package org.jivesoftware.smackx.bytestreams.ibb;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Data;

final class DataListener implements PacketListener {
    final PacketFilter dataFilter;
    private final InBandBytestreamManager manager;

    public DataListener(InBandBytestreamManager manager) {
        this.dataFilter = new AndFilter(new PacketTypeFilter(Data.class));
        this.manager = manager;
    }

    public final void processPacket(Packet packet) throws NotConnectedException {
        Data data = (Data) packet;
        if (((InBandBytestreamSession) this.manager.sessions.get(data.dataPacketExtension.sessionID)) == null) {
            this.manager.replyItemNotFoundPacket(data);
        }
    }
}
