package org.jivesoftware.smackx.caps.packet;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class CapsExtension implements PacketExtension {
    public final String hash;
    public final String node;
    public final String ver;

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.attribute("hash", this.hash).attribute("node", this.node).attribute("ver", this.ver);
        xmlStringBuilder.closeEmptyElement();
        return xmlStringBuilder;
    }

    public CapsExtension(String node, String version, String hash) {
        this.node = node;
        this.ver = version;
        this.hash = hash;
    }

    public final String getElementName() {
        return "c";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/caps";
    }

    public static CapsExtension from(Packet stanza) {
        return (CapsExtension) stanza.getExtension("c", "http://jabber.org/protocol/caps");
    }
}
