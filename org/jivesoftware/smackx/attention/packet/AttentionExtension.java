package org.jivesoftware.smackx.attention.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public final class AttentionExtension implements PacketExtension {

    public static class Provider implements PacketExtensionProvider {
        public final PacketExtension parseExtension(XmlPullParser arg0) throws Exception {
            return new AttentionExtension();
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<attention xmlns=\"urn:xmpp:attention:0\"/>");
        return stringBuilder.toString();
    }

    public final String getElementName() {
        return "attention";
    }

    public final String getNamespace() {
        return "urn:xmpp:attention:0";
    }
}
