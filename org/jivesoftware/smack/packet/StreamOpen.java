package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.util.XmlStringBuilder;

public final class StreamOpen extends FullStreamElement {
    private final String service;

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.attribute("to", this.service);
        xmlStringBuilder.attribute("xmlns:stream", "http://etherx.jabber.org/streams");
        xmlStringBuilder.attribute("version", "1.0");
        xmlStringBuilder.rightAngleBracket();
        return xmlStringBuilder;
    }

    public StreamOpen(String service) {
        this.service = service;
    }

    public final String getNamespace() {
        return "jabber:client";
    }

    public final String getElementName() {
        return "stream:stream";
    }
}
