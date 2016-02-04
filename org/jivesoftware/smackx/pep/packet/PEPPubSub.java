package org.jivesoftware.smackx.pep.packet;

import org.jivesoftware.smack.packet.IQ;

public class PEPPubSub extends IQ {
    PEPItem item;

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<pubsub xmlns=\"http://jabber.org/protocol/pubsub\">");
        stringBuilder.append("<publish node=\"").append(this.item.getNode()).append("\">");
        PEPItem pEPItem = this.item;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("<item id=\"").append(pEPItem.id).append("\">");
        stringBuilder2.append(pEPItem.getItemDetailsXML());
        stringBuilder2.append("</item>");
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder.append("</publish>");
        stringBuilder.append("</pubsub>");
        return stringBuilder.toString();
    }
}
