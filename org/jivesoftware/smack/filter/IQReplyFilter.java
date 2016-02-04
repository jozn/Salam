package org.jivesoftware.smack.filter;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jxmpp.util.XmppStringUtils;

public class IQReplyFilter implements PacketFilter {
    private static final Logger LOGGER;
    private final OrFilter fromFilter;
    private final PacketFilter iqAndIdFilter;
    private final String local;
    private final String packetId;
    private final String server;
    private final String to;

    static {
        LOGGER = Logger.getLogger(IQReplyFilter.class.getName());
    }

    public IQReplyFilter(IQ iqPacket, XMPPConnection conn) {
        String str = null;
        if (iqPacket.to != null) {
            this.to = iqPacket.to.toLowerCase(Locale.US);
        } else {
            this.to = null;
        }
        this.local = conn.getUser().toLowerCase(Locale.US);
        this.server = conn.getServiceName().toLowerCase(Locale.US);
        this.packetId = iqPacket.packetID;
        PacketFilter iqFilter = new OrFilter(IQTypeFilter.ERROR, IQTypeFilter.RESULT);
        PacketFilter idFilter = new PacketIDFilter((Packet) iqPacket);
        this.iqAndIdFilter = new AndFilter(iqFilter, idFilter);
        this.fromFilter = new OrFilter();
        this.fromFilter.addFilter(FromMatchesFilter.createFull(this.to));
        if (this.to == null) {
            OrFilter orFilter = this.fromFilter;
            String str2 = this.local;
            if (str2 != null) {
                str = XmppStringUtils.parseBareAddress(str2);
            }
            orFilter.addFilter(new FromMatchesFilter(str, true));
            this.fromFilter.addFilter(FromMatchesFilter.createFull(this.server));
        } else if (this.to.equals(XmppStringUtils.parseBareAddress(this.local))) {
            this.fromFilter.addFilter(FromMatchesFilter.createFull(null));
        }
    }

    public final boolean accept(Packet packet) {
        if (!this.iqAndIdFilter.accept(packet)) {
            return false;
        }
        if (this.fromFilter.accept(packet)) {
            return true;
        }
        LOGGER.log(Level.WARNING, String.format("Rejected potentially spoofed reply to IQ-packet. Filter settings: packetId=%s, to=%s, local=%s, server=%s. Received packet with from=%s", new Object[]{this.packetId, this.to, this.local, this.server, packet.from}), packet);
        return false;
    }
}
