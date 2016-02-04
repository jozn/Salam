package org.jivesoftware.smackx.chatstates.packet;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.xmlpull.v1.XmlPullParser;

public final class ChatStateExtension implements PacketExtension {
    private final ChatState state;

    public static class Provider implements PacketExtensionProvider {
        public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            ChatState state;
            try {
                state = ChatState.valueOf(parser.getName());
            } catch (Exception e) {
                state = ChatState.active;
            }
            return new ChatStateExtension(state);
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.closeEmptyElement();
        return xmlStringBuilder;
    }

    public ChatStateExtension(ChatState state) {
        this.state = state;
    }

    public final String getElementName() {
        return this.state.name();
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/chatstates";
    }
}
