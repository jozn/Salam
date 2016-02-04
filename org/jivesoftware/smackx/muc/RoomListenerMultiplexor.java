package org.jivesoftware.smackx.muc;

import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jxmpp.util.XmppStringUtils;

final class RoomListenerMultiplexor extends Manager {
    private static final Map<XMPPConnection, RoomListenerMultiplexor> monitors;
    final RoomMultiplexFilter filter;
    final RoomMultiplexListener listener;

    private static class RoomMultiplexFilter implements PacketFilter {
        Map<String, String> roomAddressTable;

        private RoomMultiplexFilter() {
            this.roomAddressTable = new ConcurrentHashMap();
        }

        public final boolean accept(Packet p) {
            String from = p.from;
            if (from == null) {
                return false;
            }
            return this.roomAddressTable.containsKey(XmppStringUtils.parseBareAddress(from).toLowerCase(Locale.US));
        }
    }

    private static class RoomMultiplexListener implements PacketListener {
        Map<String, PacketMultiplexListener> roomListenersByAddress;

        private RoomMultiplexListener() {
            this.roomListenersByAddress = new ConcurrentHashMap();
        }

        public final void processPacket(Packet p) throws NotConnectedException {
            String from = p.from;
            if (from != null) {
                PacketMultiplexListener listener = (PacketMultiplexListener) this.roomListenersByAddress.get(XmppStringUtils.parseBareAddress(from).toLowerCase(Locale.US));
                if (listener != null) {
                    listener.processPacket(p);
                }
            }
        }
    }

    static {
        monitors = new WeakHashMap();
    }

    public static synchronized RoomListenerMultiplexor getRoomMultiplexor(XMPPConnection conn) {
        RoomListenerMultiplexor rlm;
        synchronized (RoomListenerMultiplexor.class) {
            rlm = (RoomListenerMultiplexor) monitors.get(conn);
            if (rlm == null) {
                rlm = new RoomListenerMultiplexor(conn, new RoomMultiplexFilter(), new RoomMultiplexListener());
            }
        }
        return rlm;
    }

    private RoomListenerMultiplexor(XMPPConnection connection, RoomMultiplexFilter filter, RoomMultiplexListener listener) {
        super(connection);
        connection.addPacketListener(listener, filter);
        this.filter = filter;
        this.listener = listener;
        monitors.put(connection, this);
    }
}
