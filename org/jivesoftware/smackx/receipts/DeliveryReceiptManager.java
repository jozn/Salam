package org.jivesoftware.smackx.receipts;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;

public final class DeliveryReceiptManager extends Manager implements PacketListener {
    private static Map<XMPPConnection, DeliveryReceiptManager> instances;
    public boolean auto_receipts_enabled;
    public Set<ReceiptReceivedListener> receiptReceivedListeners;

    /* renamed from: org.jivesoftware.smackx.receipts.DeliveryReceiptManager.1 */
    static class C13431 implements ConnectionCreationListener {
        C13431() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            DeliveryReceiptManager.getInstanceFor(connection);
        }
    }

    static {
        instances = new WeakHashMap();
        XMPPConnectionRegistry.addConnectionCreationListener(new C13431());
    }

    private DeliveryReceiptManager(XMPPConnection connection) {
        super(connection);
        this.auto_receipts_enabled = false;
        this.receiptReceivedListeners = Collections.synchronizedSet(new HashSet());
        ServiceDiscoveryManager.getInstanceFor(connection).addFeature("urn:xmpp:receipts");
        connection.addPacketListener(this, new PacketExtensionFilter("urn:xmpp:receipts"));
    }

    public static synchronized DeliveryReceiptManager getInstanceFor(XMPPConnection connection) {
        DeliveryReceiptManager receiptManager;
        synchronized (DeliveryReceiptManager.class) {
            receiptManager = (DeliveryReceiptManager) instances.get(connection);
            if (receiptManager == null) {
                receiptManager = new DeliveryReceiptManager(connection);
                instances.put(connection, receiptManager);
            }
        }
        return receiptManager;
    }

    public final void processPacket(Packet packet) throws NotConnectedException {
        DeliveryReceipt dr = DeliveryReceipt.getFrom(packet);
        if (dr != null) {
            for (ReceiptReceivedListener receiptReceivedListener : this.receiptReceivedListeners) {
                String str = packet.from;
                String str2 = packet.to;
                receiptReceivedListener.onReceiptReceived$14e1ec6d(str, dr.id);
            }
        }
        if (this.auto_receipts_enabled && DeliveryReceiptRequest.getFrom(packet) != null) {
            XMPPConnection connection = connection();
            Message ack = new Message(packet.from, Type.normal);
            ack.addExtension(new DeliveryReceipt(packet.packetID));
            connection.sendPacket(ack);
        }
    }
}
