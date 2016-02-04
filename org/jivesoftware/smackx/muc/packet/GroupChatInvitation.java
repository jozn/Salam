package org.jivesoftware.smackx.muc.packet;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.xmlpull.v1.XmlPullParser;

public final class GroupChatInvitation implements PacketExtension {
    private final String roomAddress;

    public static class Provider implements PacketExtensionProvider {
        public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            String roomAddress = parser.getAttributeValue(BuildConfig.VERSION_NAME, "jid");
            parser.next();
            return new GroupChatInvitation(roomAddress);
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.attribute("jid", this.roomAddress);
        xmlStringBuilder.closeEmptyElement();
        return xmlStringBuilder;
    }

    public GroupChatInvitation(String roomAddress) {
        this.roomAddress = roomAddress;
    }

    public final String getElementName() {
        return "x";
    }

    public final String getNamespace() {
        return "jabber:x:conference";
    }
}
