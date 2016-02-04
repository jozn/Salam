package org.jivesoftware.smackx.muc.packet;

import java.util.Date;
import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jxmpp.util.XmppDateTime;

public final class MUCInitialPresence implements PacketExtension {
    private History history;
    private String password;

    public static class History implements NamedElement {
        public int maxChars;
        public int maxStanzas;
        public int seconds;
        public Date since;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            CharSequence xmlStringBuilder = new XmlStringBuilder((NamedElement) this);
            xmlStringBuilder.optIntAttribute("maxchars", this.maxChars);
            xmlStringBuilder.optIntAttribute("maxstanzas", this.maxStanzas);
            xmlStringBuilder.optIntAttribute("seconds", this.seconds);
            if (this.since != null) {
                xmlStringBuilder.attribute("since", XmppDateTime.formatXEP0082Date(this.since));
            }
            xmlStringBuilder.closeEmptyElement();
            return xmlStringBuilder;
        }

        public final String getElementName() {
            return "history";
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.rightAngleBracket();
        xmlStringBuilder.optElement("password", this.password);
        xmlStringBuilder.optElement(this.history);
        xmlStringBuilder.closeElement((NamedElement) this);
        return xmlStringBuilder;
    }

    public final String getElementName() {
        return "x";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/muc";
    }
}
