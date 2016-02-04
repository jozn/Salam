package com.shamchat.androidclient.chat.extension;

import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public final class SeenReceived extends PacketExtension {
    public final String id;

    public SeenReceived(String id) {
        this.id = id;
    }

    public final String getElementName() {
        return "seen_received";
    }

    public final String getNamespace() {
        return "urn:xmpp:receipts";
    }

    public final void serializeContent(XmlSerializer serializer) throws IOException {
        SerializerUtils.setTextAttribute(serializer, "seen", this.id);
    }
}
