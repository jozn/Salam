package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.XmlStringBuilder;

public final class RosterVer implements PacketExtension {
    public static final RosterVer INSTANCE;

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.closeEmptyElement();
        return xmlStringBuilder;
    }

    static {
        INSTANCE = new RosterVer();
    }

    private RosterVer() {
    }

    public final String getElementName() {
        return "ver";
    }

    public final String getNamespace() {
        return "urn:xmpp:features:rosterver";
    }
}
