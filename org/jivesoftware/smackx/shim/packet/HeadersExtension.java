package org.jivesoftware.smackx.shim.packet;

import java.util.Collection;
import java.util.Collections;
import org.jivesoftware.smack.packet.PacketExtension;

public final class HeadersExtension implements PacketExtension {
    private Collection<Header> headers;

    public HeadersExtension(Collection<Header> headerList) {
        this.headers = Collections.emptyList();
        if (headerList != null) {
            this.headers = headerList;
        }
    }

    public final String getElementName() {
        return "headers";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/shim";
    }

    public final String toXML() {
        StringBuilder builder = new StringBuilder(new StringBuilder("<headers xmlns='http://jabber.org/protocol/shim'>").toString());
        for (Header header : this.headers) {
            builder.append(header.toXML());
        }
        builder.append("</headers>");
        return builder.toString();
    }
}
