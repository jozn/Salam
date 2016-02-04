package org.jivesoftware.smackx.pubsub.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.pubsub.packet.PubSub;
import org.jivesoftware.smackx.pubsub.packet.PubSubNamespace;
import org.xmlpull.v1.XmlPullParser;

public class PubSubProvider implements IQProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        PubSub pubsub = new PubSub();
        pubsub.ns = PubSubNamespace.valueOfFromXmlns(parser.getNamespace());
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                pubsub.addExtension(PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser));
            } else if (eventType == 3 && parser.getName().equals("pubsub")) {
                done = true;
            }
        }
        return pubsub;
    }
}
