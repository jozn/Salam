package org.jivesoftware.smackx.pubsub.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.pubsub.Subscription;
import org.jivesoftware.smackx.pubsub.Subscription.State;
import org.xmlpull.v1.XmlPullParser;

public class SubscriptionProvider implements PacketExtensionProvider {
    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        State state = null;
        String jid = parser.getAttributeValue(null, "jid");
        String nodeId = parser.getAttributeValue(null, "node");
        String subId = parser.getAttributeValue(null, "subid");
        String state2 = parser.getAttributeValue(null, "subscription");
        boolean isRequired = false;
        if (parser.next() == 2 && parser.getName().equals("subscribe-options")) {
            int tag = parser.next();
            if (tag == 2 && parser.getName().equals("required")) {
                isRequired = true;
            }
            while (tag != 3 && !parser.getName().equals("subscribe-options")) {
                tag = parser.next();
            }
        }
        while (parser.getEventType() != 3) {
            parser.next();
        }
        if (state2 != null) {
            state = State.valueOf(state2);
        }
        return new Subscription(jid, nodeId, subId, state, isRequired);
    }
}
