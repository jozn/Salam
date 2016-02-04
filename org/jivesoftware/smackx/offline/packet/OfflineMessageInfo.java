package org.jivesoftware.smackx.offline.packet;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public final class OfflineMessageInfo implements PacketExtension {
    String node;

    public static class Provider implements PacketExtensionProvider {
        public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            OfflineMessageInfo info = new OfflineMessageInfo();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == 2) {
                    if (parser.getName().equals("item")) {
                        info.node = parser.getAttributeValue(BuildConfig.VERSION_NAME, "node");
                    }
                } else if (eventType == 3 && parser.getName().equals("offline")) {
                    done = true;
                }
            }
            return info;
        }
    }

    public OfflineMessageInfo() {
        this.node = null;
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<offline xmlns=\"http://jabber.org/protocol/offline\">");
        if (this.node != null) {
            stringBuilder.append("<item node=\"").append(this.node).append("\"/>");
        }
        stringBuilder.append("</offline>");
        return stringBuilder.toString();
    }

    public final String getElementName() {
        return "offline";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/offline";
    }
}
