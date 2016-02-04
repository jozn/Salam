package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Packet;

public final class PacketExtensionFilter implements PacketFilter {
    private final String elementName;
    private final String namespace;

    public PacketExtensionFilter(String elementName, String namespace) {
        this.elementName = elementName;
        this.namespace = namespace;
    }

    public PacketExtensionFilter(String namespace) {
        this(null, namespace);
    }

    public final boolean accept(Packet packet) {
        return packet.getExtension(this.elementName, this.namespace) != null;
    }
}
