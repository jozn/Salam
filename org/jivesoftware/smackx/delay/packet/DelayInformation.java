package org.jivesoftware.smackx.delay.packet;

import java.util.Date;
import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jxmpp.util.XmppDateTime;

public final class DelayInformation implements PacketExtension {
    private final String from;
    private final String reason;
    public final Date stamp;

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.attribute("stamp", XmppDateTime.formatXEP0082Date(this.stamp));
        xmlStringBuilder.optAttribute("from", this.from);
        xmlStringBuilder.rightAngleBracket();
        xmlStringBuilder.optAppend(this.reason);
        xmlStringBuilder.closeElement((NamedElement) this);
        return xmlStringBuilder;
    }

    public DelayInformation(Date stamp, String from, String reason) {
        this.stamp = stamp;
        this.from = from;
        this.reason = reason;
    }

    public final String getElementName() {
        return "delay";
    }

    public final String getNamespace() {
        return "urn:xmpp:delay";
    }

    @Deprecated
    public static DelayInformation getFrom(Packet packet) {
        return (DelayInformation) packet.getExtension("delay", "urn:xmpp:delay");
    }
}
