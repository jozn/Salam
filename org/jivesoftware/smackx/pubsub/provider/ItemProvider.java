package org.jivesoftware.smackx.pubsub.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.SimplePayload;
import org.xmlpull.v1.XmlPullParser;

public class ItemProvider implements PacketExtensionProvider {
    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        String id = parser.getAttributeValue(null, "id");
        String node = parser.getAttributeValue(null, "node");
        if (parser.next() == 3) {
            return new Item(id, node);
        }
        String payloadElemName = parser.getName();
        String payloadNS = parser.getNamespace();
        if (ProviderManager.getExtensionProvider(payloadElemName, payloadNS) == null) {
            return new PayloadItem(id, node, new SimplePayload(payloadElemName, payloadNS, PacketParserUtils.parseElement(parser, true)));
        }
        return new PayloadItem(id, node, PacketParserUtils.parsePacketExtension(payloadElemName, payloadNS, parser));
    }
}
