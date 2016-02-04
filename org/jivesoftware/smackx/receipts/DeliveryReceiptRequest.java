package org.jivesoftware.smackx.receipts;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public final class DeliveryReceiptRequest implements PacketExtension {

    public static class Provider implements PacketExtensionProvider {
        public final PacketExtension parseExtension(XmlPullParser parser) {
            return new DeliveryReceiptRequest();
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        return "<request xmlns='urn:xmpp:receipts'/>";
    }

    public final String getElementName() {
        return "request";
    }

    public final String getNamespace() {
        return "urn:xmpp:receipts";
    }

    @Deprecated
    public static DeliveryReceiptRequest getFrom(Packet p) {
        return (DeliveryReceiptRequest) p.getExtension("request", "urn:xmpp:receipts");
    }
}
