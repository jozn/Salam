package org.jivesoftware.smack.packet;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class Mechanisms implements PacketExtension {
    public final List<String> mechanisms;

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.rightAngleBracket();
        for (String element : this.mechanisms) {
            xmlStringBuilder.element("mechanism", element);
        }
        xmlStringBuilder.closeElement((NamedElement) this);
        return xmlStringBuilder;
    }

    public Mechanisms(Collection<String> mechanisms) {
        this.mechanisms = new LinkedList();
        this.mechanisms.addAll(mechanisms);
    }

    public final String getElementName() {
        return "mechanisms";
    }

    public final String getNamespace() {
        return "urn:ietf:params:xml:ns:xmpp-sasl";
    }
}
