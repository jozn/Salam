package org.jivesoftware.smackx.iqlast;

import java.util.Map;
import java.util.WeakHashMap;
import org.eclipse.paho.client.mqttv3.logging.Logger;
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
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.iqlast.packet.LastActivity;

public class LastActivityManager extends Manager {
    private static final PacketFilter IQ_GET_LAST_FILTER;
    private static boolean enabledPerDefault;
    private static final Map<XMPPConnection, LastActivityManager> instances;
    private boolean enabled;
    private volatile long lastMessageSent;

    /* renamed from: org.jivesoftware.smackx.iqlast.LastActivityManager.1 */
    static class C13211 implements ConnectionCreationListener {
        C13211() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            LastActivityManager.getInstanceFor(connection);
        }
    }

    /* renamed from: org.jivesoftware.smackx.iqlast.LastActivityManager.2 */
    class C13222 implements PacketListener {
        C13222() {
        }

        public final void processPacket(Packet packet) {
            Mode mode = ((Presence) packet).mode;
            if (mode != null) {
                switch (C13255.$SwitchMap$org$jivesoftware$smack$packet$Presence$Mode[mode.ordinal()]) {
                    case Logger.SEVERE /*1*/:
                    case Logger.WARNING /*2*/:
                        LastActivityManager.this.lastMessageSent = System.currentTimeMillis();
                    default:
                }
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.iqlast.LastActivityManager.3 */
    class C13233 implements PacketListener {
        C13233() {
        }

        public final void processPacket(Packet packet) {
            if (((Message) packet).type != Type.error) {
                LastActivityManager.this.lastMessageSent = System.currentTimeMillis();
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.iqlast.LastActivityManager.4 */
    class C13244 implements PacketListener {
        C13244() {
        }

        public final void processPacket(Packet packet) throws NotConnectedException {
            if (LastActivityManager.this.enabled) {
                LastActivity message = new LastActivity();
                message.setType(IQ.Type.result);
                message.to = packet.from;
                message.from = packet.to;
                message.packetID = packet.packetID;
                message.lastActivity = ((System.currentTimeMillis() - LastActivityManager.this.lastMessageSent) / 1000);
                LastActivityManager.this.connection().sendPacket(message);
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.iqlast.LastActivityManager.5 */
    static /* synthetic */ class C13255 {
        static final /* synthetic */ int[] $SwitchMap$org$jivesoftware$smack$packet$Presence$Mode;

        static {
            $SwitchMap$org$jivesoftware$smack$packet$Presence$Mode = new int[Mode.values().length];
            try {
                $SwitchMap$org$jivesoftware$smack$packet$Presence$Mode[Mode.available.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$packet$Presence$Mode[Mode.chat.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    static {
        instances = new WeakHashMap();
        IQ_GET_LAST_FILTER = new AndFilter(IQTypeFilter.GET, new PacketTypeFilter(LastActivity.class));
        enabledPerDefault = true;
        XMPPConnectionRegistry.addConnectionCreationListener(new C13211());
    }

    public static synchronized LastActivityManager getInstanceFor(XMPPConnection connection) {
        LastActivityManager lastActivityManager;
        synchronized (LastActivityManager.class) {
            lastActivityManager = (LastActivityManager) instances.get(connection);
            if (lastActivityManager == null) {
                lastActivityManager = new LastActivityManager(connection);
            }
        }
        return lastActivityManager;
    }

    private LastActivityManager(XMPPConnection connection) {
        super(connection);
        this.enabled = false;
        connection.addPacketSendingListener(new C13222(), PacketTypeFilter.PRESENCE);
        connection.addPacketSendingListener(new C13233(), PacketTypeFilter.MESSAGE);
        connection.addPacketListener(new C13244(), IQ_GET_LAST_FILTER);
        if (enabledPerDefault) {
            enable();
        }
        this.lastMessageSent = System.currentTimeMillis();
        instances.put(connection, this);
    }

    private synchronized void enable() {
        ServiceDiscoveryManager.getInstanceFor(connection()).addFeature("jabber:iq:last");
        this.enabled = true;
    }
}
