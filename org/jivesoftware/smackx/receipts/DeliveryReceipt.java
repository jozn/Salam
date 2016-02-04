package org.jivesoftware.smackx.receipts;

import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.EmbeddedExtensionProvider;

public final class DeliveryReceipt implements PacketExtension {
    public String id;

    public static class Provider extends EmbeddedExtensionProvider {
        protected final PacketExtension createReturnExtension(String currentElement, String currentNamespace, Map<String, String> attributeMap, List<? extends PacketExtension> list) {
            return new DeliveryReceipt((String) attributeMap.get("id"));
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        return "<received xmlns='urn:xmpp:receipts' id='" + this.id + "'/>";
    }

    public DeliveryReceipt(String id) {
        this.id = id;
    }

    public final String getElementName() {
        return "received";
    }

    public final String getNamespace() {
        return "urn:xmpp:receipts";
    }

    @Deprecated
    public static DeliveryReceipt getFrom(Packet p) {
        return (DeliveryReceipt) p.getExtension("received", "urn:xmpp:receipts");
    }
}
