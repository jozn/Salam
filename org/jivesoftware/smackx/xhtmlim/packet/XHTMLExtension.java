package org.jivesoftware.smackx.xhtmlim.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class XHTMLExtension implements PacketExtension {
    public List<CharSequence> bodies;

    public XHTMLExtension() {
        this.bodies = new ArrayList();
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.rightAngleBracket();
        for (CharSequence append : getBodies()) {
            xmlStringBuilder.append(append);
        }
        xmlStringBuilder.closeElement((NamedElement) this);
        return xmlStringBuilder;
    }

    public final String getElementName() {
        return "html";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/xhtml-im";
    }

    private List<CharSequence> getBodies() {
        List<CharSequence> unmodifiableList;
        synchronized (this.bodies) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(this.bodies));
        }
        return unmodifiableList;
    }
}
