package org.jivesoftware.smackx.muc.packet;

import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class Destroy implements NamedElement {
    public String jid;
    public String reason;

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((NamedElement) this);
        xmlStringBuilder.optAttribute("jid", this.jid);
        xmlStringBuilder.rightAngleBracket();
        xmlStringBuilder.optElement("reason", this.reason);
        xmlStringBuilder.closeElement((NamedElement) this);
        return xmlStringBuilder;
    }

    public final String getElementName() {
        return "destroy";
    }
}
