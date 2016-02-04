package org.jivesoftware.smackx.pubsub;

import java.util.Collections;
import java.util.List;

public final class SubscriptionsExtension extends NodeExtension {
    protected List<Subscription> items;

    public SubscriptionsExtension(String nodeId, List<Subscription> subList) {
        super(PubSubElementType.SUBSCRIPTIONS, nodeId);
        this.items = Collections.emptyList();
        if (subList != null) {
            this.items = subList;
        }
    }

    public final CharSequence toXML() {
        if (this.items == null || this.items.size() == 0) {
            return super.toXML();
        }
        StringBuilder builder = new StringBuilder("<");
        builder.append(this.element.eName);
        if (this.node != null) {
            builder.append(" node='");
            builder.append(this.node);
            builder.append("'");
        }
        builder.append(">");
        for (Subscription item : this.items) {
            builder.append(item.toXML());
        }
        builder.append("</");
        builder.append(this.element.eName);
        builder.append(">");
        return builder.toString();
    }
}
