package org.jivesoftware.smackx.carbons;

import java.util.Map;
import java.util.WeakHashMap;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;

public final class CarbonManager extends Manager {
    private static Map<XMPPConnection, CarbonManager> INSTANCES;
    private volatile boolean enabled_state;

    /* renamed from: org.jivesoftware.smackx.carbons.CarbonManager.1 */
    static class C13121 implements ConnectionCreationListener {
        C13121() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            CarbonManager.getInstanceFor(connection);
        }
    }

    /* renamed from: org.jivesoftware.smackx.carbons.CarbonManager.2 */
    class C13132 implements PacketListener {
        final /* synthetic */ boolean val$new_state;

        public C13132(boolean z) {
            this.val$new_state = z;
        }

        public final void processPacket(Packet packet) {
            CarbonManager.this.enabled_state = this.val$new_state;
        }
    }

    static {
        INSTANCES = new WeakHashMap();
        XMPPConnectionRegistry.addConnectionCreationListener(new C13121());
    }

    private CarbonManager(XMPPConnection connection) {
        super(connection);
        this.enabled_state = false;
        ServiceDiscoveryManager.getInstanceFor(connection).addFeature("urn:xmpp:carbons:2");
    }

    public static synchronized CarbonManager getInstanceFor(XMPPConnection connection) {
        CarbonManager carbonManager;
        synchronized (CarbonManager.class) {
            carbonManager = (CarbonManager) INSTANCES.get(connection);
            if (carbonManager == null) {
                carbonManager = new CarbonManager(connection);
                INSTANCES.put(connection, carbonManager);
            }
        }
        return carbonManager;
    }
}
