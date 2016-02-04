package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.XmlStringBuilder;

public final class StartTls extends FullStreamElement {
    public final boolean required;

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.rightAngleBracket();
        xmlStringBuilder.condEmptyElement(this.required, "required");
        xmlStringBuilder.closeElement((NamedElement) this);
        return xmlStringBuilder;
    }

    public StartTls() {
        this(false);
    }

    public StartTls(boolean required) {
        this.required = required;
    }

    public final String getElementName() {
        return "starttls";
    }

    public final String getNamespace() {
        return "urn:ietf:params:xml:ns:xmpp-tls";
    }
}
