package org.jivesoftware.smackx.pubsub;

import java.util.Collections;
import java.util.List;

public final class AffiliationsExtension extends NodeExtension {
    protected List<Affiliation> items;

    public AffiliationsExtension() {
        super(PubSubElementType.AFFILIATIONS);
        this.items = Collections.emptyList();
    }

    public AffiliationsExtension(List<Affiliation> subList) {
        super(PubSubElementType.AFFILIATIONS);
        this.items = Collections.emptyList();
        this.items = subList;
    }

    public final CharSequence toXML() {
        if (this.items == null || this.items.size() == 0) {
            return super.toXML();
        }
        StringBuilder builder = new StringBuilder("<");
        builder.append(this.element.eName);
        builder.append(">");
        for (Affiliation item : this.items) {
            builder.append(item.toXML());
        }
        builder.append("</");
        builder.append(this.element.eName);
        builder.append(">");
        return builder.toString();
    }
}
