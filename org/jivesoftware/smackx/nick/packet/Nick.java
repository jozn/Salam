package org.jivesoftware.smackx.nick.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public final class Nick implements PacketExtension {
    private String name;

    public static class Provider implements PacketExtensionProvider {
        public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            parser.next();
            String name = parser.getText();
            while (parser.getEventType() != 3) {
                parser.next();
            }
            return new Nick(name);
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<nick xmlns=\"http://jabber.org/protocol/nick\">");
        stringBuilder.append(this.name);
        stringBuilder.append("</nick>");
        return stringBuilder.toString();
    }

    public Nick(String name) {
        this.name = null;
        this.name = name;
    }

    public final String getElementName() {
        return "nick";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/nick";
    }
}
