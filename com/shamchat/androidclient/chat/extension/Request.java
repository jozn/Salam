package com.shamchat.androidclient.chat.extension;

import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

public final class Request extends PacketExtension {
    public final String getElementName() {
        return "request";
    }

    public final String getNamespace() {
        return "urn:xmpp:receipts";
    }

    public final void serializeContent(XmlSerializer serializer) throws IOException {
    }
}
