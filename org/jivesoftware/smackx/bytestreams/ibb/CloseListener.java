package org.jivesoftware.smackx.bytestreams.ibb;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Close;

final class CloseListener implements PacketListener {
    final PacketFilter closeFilter;
    private final InBandBytestreamManager manager;

    protected CloseListener(InBandBytestreamManager manager) {
        this.closeFilter = new AndFilter(new PacketTypeFilter(Close.class), IQTypeFilter.SET);
        this.manager = manager;
    }

    public final void processPacket(Packet packet) throws NotConnectedException {
        Close closeRequest = (Close) packet;
        InBandBytestreamSession ibbSession = (InBandBytestreamSession) this.manager.sessions.get(closeRequest.sessionID);
        if (ibbSession == null) {
            this.manager.replyItemNotFoundPacket(closeRequest);
            return;
        }
        ibbSession.closeByPeer(closeRequest);
        this.manager.sessions.remove(closeRequest.sessionID);
    }
}
