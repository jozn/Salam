package org.jivesoftware.smackx.muc.packet;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class MUCOwner extends IQ {
    public Destroy destroy;
    public final List<MUCItem> items;

    public MUCOwner() {
        this.items = new ArrayList();
    }

    private XmlStringBuilder getChildElementXML() {
        XmlStringBuilder xml = new XmlStringBuilder();
        xml.halfOpenElement("query");
        xml.xmlnsAttribute("http://jabber.org/protocol/muc#owner");
        xml.rightAngleBracket();
        synchronized (this.items) {
            for (MUCItem item : this.items) {
                xml.append(item.toXML());
            }
        }
        xml.optElement(this.destroy);
        xml.append(getExtensionsXML());
        xml.closeElement("query");
        return xml;
    }
}
