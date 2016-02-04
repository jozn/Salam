package com.shamchat.androidclient.chat.extension;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.xmlpull.v1.XmlPullParser;

public final class ChatStateExtension implements PacketExtension {
    public ChatState state;

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
        return "<" + this.state.name() + " xmlns=\"http://jabber.org/protocol/chatstates\" />";
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
