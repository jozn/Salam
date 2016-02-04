package org.jivesoftware.smackx.carbons.packet;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.forward.Forwarded;

public final class CarbonExtension implements PacketExtension {
    public final Direction dir;
    public final Forwarded fwd;

    public enum Direction {
        received,
        sent
    }

    public CarbonExtension(Direction dir, Forwarded fwd) {
        this.dir = dir;
        this.fwd = fwd;
    }

    public final String getElementName() {
        return this.dir.name();
    }

    public final String getNamespace() {
        return "urn:xmpp:carbons:2";
    }

    public final XmlStringBuilder toXML() {
        XmlStringBuilder xml = new XmlStringBuilder((PacketExtension) this);
        xml.rightAngleBracket();
        xml.append(this.fwd.toXML());
        xml.closeElement((NamedElement) this);
        return xml;
    }

    @Deprecated
    public static CarbonExtension getFrom(Message msg) {
        CarbonExtension carbonExtension = (CarbonExtension) msg.getExtension(Direction.received.name(), "urn:xmpp:carbons:2");
        return carbonExtension == null ? (CarbonExtension) msg.getExtension(Direction.sent.name(), "urn:xmpp:carbons:2") : carbonExtension;
    }
}
