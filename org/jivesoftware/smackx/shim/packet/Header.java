package org.jivesoftware.smackx.shim.packet;

import org.jivesoftware.smack.packet.PacketExtension;

public final class Header implements PacketExtension {
    private String name;
    private String value;

    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public final String getElementName() {
        return "header";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/shim";
    }

    public final String toXML() {
        return "<header name='" + this.name + "'>" + this.value + "</header>";
    }
}
