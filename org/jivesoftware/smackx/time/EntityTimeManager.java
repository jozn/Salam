package org.jivesoftware.smackx.time;

import java.util.Map;
import java.util.WeakHashMap;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.time.packet.Time;

public class EntityTimeManager extends Manager {
    private static final Map<XMPPConnection, EntityTimeManager> INSTANCES;
    private static final PacketFilter TIME_PACKET_FILTER;
    private static boolean autoEnable;
    private boolean enabled;

    /* renamed from: org.jivesoftware.smackx.time.EntityTimeManager.1 */
    static class C13441 implements ConnectionCreationListener {
        C13441() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            EntityTimeManager.getInstanceFor(connection);
        }
    }

    /* renamed from: org.jivesoftware.smackx.time.EntityTimeManager.2 */
    class C13452 implements PacketListener {
        C13452() {
        }

        public final void processPacket(Packet packet) throws NotConnectedException {
            if (EntityTimeManager.this.enabled) {
                EntityTimeManager.this.connection().sendPacket(Time.createResponse(packet));
            }
        }
    }

    static {
        INSTANCES = new WeakHashMap();
        TIME_PACKET_FILTER = new AndFilter(new PacketTypeFilter(Time.class), IQTypeFilter.GET);
        autoEnable = true;
        XMPPConnectionRegistry.addConnectionCreationListener(new C13441());
    }

    public static synchronized EntityTimeManager getInstanceFor(XMPPConnection connection) {
        EntityTimeManager entityTimeManager;
        synchronized (EntityTimeManager.class) {
            entityTimeManager = (EntityTimeManager) INSTANCES.get(connection);
            if (entityTimeManager == null) {
                entityTimeManager = new EntityTimeManager(connection);
                INSTANCES.put(connection, entityTimeManager);
            }
        }
        return entityTimeManager;
    }

    private EntityTimeManager(XMPPConnection connection) {
        super(connection);
        this.enabled = false;
        if (autoEnable) {
            enable();
        }
        connection.addPacketListener(new C13452(), TIME_PACKET_FILTER);
    }

    private synchronized void enable() {
        if (!this.enabled) {
            ServiceDiscoveryManager.getInstanceFor(connection()).addFeature("urn:xmpp:time");
            this.enabled = true;
        }
    }
}
