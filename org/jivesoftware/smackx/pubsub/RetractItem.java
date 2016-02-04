package org.jivesoftware.smackx.pubsub;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.pubsub.packet.PubSubNamespace;

public final class RetractItem implements PacketExtension {
    private String id;

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        return "<retract id='" + this.id + "'/>";
    }

    public RetractItem(String itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("itemId must not be 'null'");
        }
        this.id = itemId;
    }

    public final String getElementName() {
        return "retract";
    }

    public final String getNamespace() {
        return PubSubNamespace.EVENT.getXmlns();
    }
}
