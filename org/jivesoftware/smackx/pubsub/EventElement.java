package org.jivesoftware.smackx.pubsub;

import org.jivesoftware.smackx.pubsub.packet.PubSubNamespace;

public final class EventElement implements EmbeddedPacketExtension {
    private NodeExtension ext;
    private EventElementType type;

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        StringBuilder stringBuilder = new StringBuilder("<event xmlns='" + PubSubNamespace.EVENT.getXmlns() + "'>");
        stringBuilder.append(this.ext.toXML());
        stringBuilder.append("</event>");
        return stringBuilder.toString();
    }

    public EventElement(EventElementType eventType, NodeExtension eventExt) {
        this.type = eventType;
        this.ext = eventExt;
    }

    public final String getElementName() {
        return NotificationCompatApi21.CATEGORY_EVENT;
    }

    public final String getNamespace() {
        return PubSubNamespace.EVENT.getXmlns();
    }
}
