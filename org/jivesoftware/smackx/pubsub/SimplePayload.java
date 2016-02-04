package org.jivesoftware.smackx.pubsub;

import org.jivesoftware.smack.packet.PacketExtension;

public final class SimplePayload implements PacketExtension {
    private final String elemName;
    private final String ns;
    private final CharSequence payload;

    public SimplePayload(String elementName, String namespace, CharSequence xmlPayload) {
        this.elemName = elementName;
        this.payload = xmlPayload;
        this.ns = namespace;
    }

    public final String getElementName() {
        return this.elemName;
    }

    public final String getNamespace() {
        return this.ns;
    }

    public final CharSequence toXML() {
        return this.payload;
    }

    public final String toString() {
        return getClass().getName() + "payload [" + toXML() + "]";
    }
}
