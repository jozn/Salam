package org.jivesoftware.smackx.pubsub;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.PacketExtension;

public class NodeExtension implements PacketExtension {
    final PubSubElementType element;
    final String node;

    public NodeExtension(PubSubElementType elem, String nodeId) {
        this.element = elem;
        this.node = nodeId;
    }

    public NodeExtension(PubSubElementType elem) {
        this(elem, null);
    }

    public final String getElementName() {
        return this.element.eName;
    }

    public String getNamespace() {
        return this.element.nSpace.getXmlns();
    }

    public CharSequence toXML() {
        return "<" + this.element.eName + (this.node == null ? BuildConfig.VERSION_NAME : " node='" + this.node + '\'') + "/>";
    }

    public String toString() {
        return getClass().getName() + " - content [" + toXML() + "]";
    }
}
