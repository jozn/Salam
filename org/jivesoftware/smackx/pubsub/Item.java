package org.jivesoftware.smackx.pubsub;

public class Item extends NodeExtension {
    String id;

    public Item() {
        super(PubSubElementType.ITEM);
    }

    public Item(String itemId, String nodeId) {
        super(PubSubElementType.ITEM_EVENT, nodeId);
        this.id = itemId;
    }

    public final String getNamespace() {
        return null;
    }

    public String toXML() {
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
        builder.append("/>");
        return builder.toString();
    }

    public String toString() {
        return getClass().getName() + " | Content [" + toXML() + "]";
    }
}
