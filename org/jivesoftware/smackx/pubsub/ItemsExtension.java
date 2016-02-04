package org.jivesoftware.smackx.pubsub;

import java.util.List;
import org.jivesoftware.smack.packet.PacketExtension;

public final class ItemsExtension extends NodeExtension implements EmbeddedPacketExtension {
    protected List<? extends PacketExtension> items;
    protected Boolean notify;
    protected ItemsElementType type;

    public enum ItemsElementType {
        items(PubSubElementType.ITEMS, "max_items"),
        retract(PubSubElementType.RETRACT, "notify");
        
        String att;
        PubSubElementType elem;

        private ItemsElementType(PubSubElementType nodeElement, String attribute) {
            this.elem = nodeElement;
            this.att = attribute;
        }
    }

    public ItemsExtension(ItemsElementType itemsType, String nodeId, List<? extends PacketExtension> items) {
        super(itemsType.elem, nodeId);
        this.type = itemsType;
        this.items = items;
    }

    public final CharSequence toXML() {
        if (this.items == null || this.items.size() == 0) {
            return super.toXML();
        }
        StringBuilder builder = new StringBuilder("<");
        builder.append(this.element.eName);
        builder.append(" node='");
        builder.append(this.node);
        if (this.notify != null) {
            builder.append("' ");
            builder.append(this.type.att);
            builder.append("='");
            builder.append(this.notify.equals(Boolean.TRUE) ? 1 : 0);
            builder.append("'>");
        } else {
            builder.append("'>");
            for (PacketExtension item : this.items) {
                builder.append(item.toXML());
            }
        }
        builder.append("</");
        builder.append(this.element.eName);
        builder.append(">");
        return builder.toString();
    }

    public final String toString() {
        return getClass().getName() + "Content [" + toXML() + "]";
    }
}
