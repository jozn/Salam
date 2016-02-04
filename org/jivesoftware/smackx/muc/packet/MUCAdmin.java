package org.jivesoftware.smackx.muc.packet;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class MUCAdmin extends IQ {
    public final List<MUCItem> items;

    public MUCAdmin() {
        this.items = new ArrayList();
    }

    private XmlStringBuilder getChildElementXML() {
        XmlStringBuilder xml = new XmlStringBuilder();
        xml.halfOpenElement("query");
        xml.xmlnsAttribute("http://jabber.org/protocol/muc#admin");
        xml.rightAngleBracket();
        synchronized (this.items) {
            for (MUCItem item : this.items) {
                xml.append(item.toXML());
            }
        }
        xml.append(getExtensionsXML());
        xml.closeElement("query");
        return xml;
    }
}
