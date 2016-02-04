package org.jivesoftware.smackx.address.packet;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.PacketExtension;

public final class MultipleAddresses implements PacketExtension {
    public List<Address> addresses;

    public static class Address {
        public boolean delivered;
        public String description;
        public String jid;
        public String node;
        String type;
        public String uri;

        private Address(String type) {
            this.type = type;
        }
    }

    public MultipleAddresses() {
        this.addresses = new ArrayList();
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<addresses");
        stringBuilder.append(" xmlns=\"http://jabber.org/protocol/address\">");
        for (Address address : this.addresses) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("<address type=\"");
            stringBuilder2.append(address.type).append("\"");
            if (address.jid != null) {
                stringBuilder2.append(" jid=\"");
                stringBuilder2.append(address.jid).append("\"");
            }
            if (address.node != null) {
                stringBuilder2.append(" node=\"");
                stringBuilder2.append(address.node).append("\"");
            }
            if (address.description != null && address.description.trim().length() > 0) {
                stringBuilder2.append(" desc=\"");
                stringBuilder2.append(address.description).append("\"");
            }
            if (address.delivered) {
                stringBuilder2.append(" delivered=\"true\"");
            }
            if (address.uri != null) {
                stringBuilder2.append(" uri=\"");
                stringBuilder2.append(address.uri).append("\"");
            }
            stringBuilder2.append("/>");
            stringBuilder.append(stringBuilder2.toString());
        }
        stringBuilder.append("</addresses>");
        return stringBuilder.toString();
    }

    public final String getElementName() {
        return "addresses";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/address";
    }
}
