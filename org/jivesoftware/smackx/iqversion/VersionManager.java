package org.jivesoftware.smackx.iqversion;

import java.util.Map;
import java.util.WeakHashMap;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.iqversion.packet.Version;

public final class VersionManager extends Manager {
    private static final Map<XMPPConnection, VersionManager> INSTANCES;
    private static final PacketFilter PACKET_FILTER;
    private static boolean autoAppendSmackVersion;
    private static Version defaultVersion;
    private Version ourVersion;

    /* renamed from: org.jivesoftware.smackx.iqversion.VersionManager.1 */
    static class C13281 implements ConnectionCreationListener {
        C13281() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            VersionManager.getInstanceFor(connection);
        }
    }

    /* renamed from: org.jivesoftware.smackx.iqversion.VersionManager.2 */
    class C13292 implements PacketListener {
        C13292() {
        }

        public final void processPacket(Packet packet) throws NotConnectedException {
            if (VersionManager.this.ourVersion != null) {
                VersionManager.this.connection().sendPacket(Version.createResultFor(packet, VersionManager.this.ourVersion));
            }
        }
    }

    static {
        INSTANCES = new WeakHashMap();
        PACKET_FILTER = new AndFilter(new PacketTypeFilter(Version.class), IQTypeFilter.GET);
        autoAppendSmackVersion = true;
        XMPPConnectionRegistry.addConnectionCreationListener(new C13281());
    }

    public static void setDefaultVersion(String name, String version, String os) {
        if (autoAppendSmackVersion) {
            name = name + " (Smack " + SmackConfiguration.getVersion() + ')';
        }
        defaultVersion = new Version(name, version, os);
    }

    private VersionManager(XMPPConnection connection) {
        super(connection);
        this.ourVersion = defaultVersion;
        ServiceDiscoveryManager.getInstanceFor(connection).addFeature("jabber:iq:version");
        connection.addPacketListener(new C13292(), PACKET_FILTER);
    }

    public static synchronized VersionManager getInstanceFor(XMPPConnection connection) {
        VersionManager versionManager;
        synchronized (VersionManager.class) {
            versionManager = (VersionManager) INSTANCES.get(connection);
            if (versionManager == null) {
                versionManager = new VersionManager(connection);
                INSTANCES.put(connection, versionManager);
            }
        }
        return versionManager;
    }
}
