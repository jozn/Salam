package org.jivesoftware.smackx.privacy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.privacy.packet.Privacy;
import org.jivesoftware.smackx.privacy.packet.PrivacyItem;

public class PrivacyListManager extends Manager {
    private static final PacketFilter PACKET_FILTER;
    private static final Map<XMPPConnection, PrivacyListManager> instances;
    private final List<Object> listeners;

    /* renamed from: org.jivesoftware.smackx.privacy.PrivacyListManager.1 */
    static class C13411 implements ConnectionCreationListener {
        C13411() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            PrivacyListManager.getInstanceFor(connection);
        }
    }

    /* renamed from: org.jivesoftware.smackx.privacy.PrivacyListManager.2 */
    class C13422 implements PacketListener {
        final /* synthetic */ XMPPConnection val$connection;

        C13422(XMPPConnection xMPPConnection) {
            this.val$connection = xMPPConnection;
        }

        public final void processPacket(Packet packet) throws NotConnectedException {
            Privacy privacy = (Privacy) packet;
            synchronized (PrivacyListManager.this.listeners) {
                Iterator it = PrivacyListManager.this.listeners.iterator();
                while (it.hasNext()) {
                    it.next();
                    for (Entry<String, List<PrivacyItem>> entry : privacy.itemLists.entrySet()) {
                        entry.getKey();
                        ((List) entry.getValue()).isEmpty();
                    }
                }
            }
            this.val$connection.sendPacket(IQ.createResultIQ(privacy));
        }
    }

    static {
        PACKET_FILTER = new AndFilter(IQTypeFilter.SET, new PacketExtensionFilter("query", "jabber:iq:privacy"));
        instances = Collections.synchronizedMap(new WeakHashMap());
        XMPPConnectionRegistry.addConnectionCreationListener(new C13411());
    }

    private PrivacyListManager(XMPPConnection connection) {
        super(connection);
        this.listeners = new ArrayList();
        instances.put(connection, this);
        connection.addPacketListener(new C13422(connection), PACKET_FILTER);
    }

    public static synchronized PrivacyListManager getInstanceFor(XMPPConnection connection) {
        PrivacyListManager plm;
        synchronized (PrivacyListManager.class) {
            plm = (PrivacyListManager) instances.get(connection);
            if (plm == null) {
                plm = new PrivacyListManager(connection);
            }
        }
        return plm;
    }
}
