package org.jivesoftware.smackx.forward;

import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.delay.packet.DelayInformation;

public final class Forwarded implements PacketExtension {
    public DelayInformation delay;
    public Packet forwardedPacket;

    public Forwarded(DelayInformation delay, Packet fwdPacket) {
        this.delay = delay;
        this.forwardedPacket = fwdPacket;
    }

    public final String getElementName() {
        return "forwarded";
    }

    public final String getNamespace() {
        return "urn:xmpp:forward:0";
    }

    public final XmlStringBuilder toXML() {
        XmlStringBuilder xml = new XmlStringBuilder((PacketExtension) this);
        xml.rightAngleBracket();
        xml.optElement(this.delay);
        xml.append(this.forwardedPacket.toXML());
        xml.closeElement((NamedElement) this);
        return xml;
    }
}
