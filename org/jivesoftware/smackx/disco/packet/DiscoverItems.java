package org.jivesoftware.smackx.disco.packet;

import java.util.LinkedList;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class DiscoverItems extends IQ {
    private final List<Item> items;
    public String node;

    public static class Item {
        public String action;
        String entityID;
        public String name;
        public String node;

        public Item(String entityID) {
            this.entityID = entityID;
        }
    }

    public DiscoverItems() {
        this.items = new LinkedList();
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.halfOpenElement("query");
        xmlStringBuilder.xmlnsAttribute("http://jabber.org/protocol/disco#items");
        xmlStringBuilder.optAttribute("node", this.node);
        xmlStringBuilder.rightAngleBracket();
        for (Item item : this.items) {
            XmlStringBuilder xmlStringBuilder2 = new XmlStringBuilder();
            xmlStringBuilder2.halfOpenElement("item");
            xmlStringBuilder2.attribute("jid", item.entityID);
            xmlStringBuilder2.optAttribute("name", item.name);
            xmlStringBuilder2.optAttribute("node", item.node);
            xmlStringBuilder2.optAttribute("action", item.action);
            xmlStringBuilder2.closeEmptyElement();
            xmlStringBuilder.append(xmlStringBuilder2);
        }
        xmlStringBuilder.closeElement("query");
        return xmlStringBuilder;
    }

    public final void addItem(Item item) {
        this.items.add(item);
    }
}
