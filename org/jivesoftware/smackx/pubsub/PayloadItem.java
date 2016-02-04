package org.jivesoftware.smackx.pubsub;

import org.jivesoftware.smack.packet.PacketExtension;

public final class PayloadItem<E extends PacketExtension> extends Item {
    private E payload;

    public PayloadItem(String itemId, String nodeId, E payloadExt) {
        super(itemId, nodeId);
        if (payloadExt == null) {
            throw new IllegalArgumentException("payload cannot be 'null'");
        }
        this.payload = payloadExt;
    }

    public final String toXML() {
        StringBuilder builder = new StringBuilder("<item");
        if (this.id != null) {
            builder.append(" id='");
            builder.append(this.id);
            builder.append("'");
        }
        if (this.node != null) {
            builder.append(" node='");
            builder.append(this.node);
            builder.append("'");
        }
        builder.append(">");
        builder.append(this.payload.toXML());
        builder.append("</item>");
        return builder.toString();
    }

    public final String toString() {
        return getClass().getName() + " | Content [" + toXML() + "]";
    }
}
