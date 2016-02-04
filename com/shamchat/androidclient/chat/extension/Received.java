package com.shamchat.androidclient.chat.extension;

import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public final class Received extends PacketExtension {
    private final String id;

    public Received(String id) {
        this.id = id;
    }

    public final String getElementName() {
        return "received";
    }

    public final String getNamespace() {
        return "urn:xmpp:receipts";
    }

    public final void serializeContent(XmlSerializer serializer) throws IOException {
        SerializerUtils.setTextAttribute(serializer, "id", this.id);
    }
}
