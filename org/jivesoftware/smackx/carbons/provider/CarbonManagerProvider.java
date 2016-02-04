package org.jivesoftware.smackx.carbons.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.carbons.packet.CarbonExtension;
import org.jivesoftware.smackx.carbons.packet.CarbonExtension.Direction;
import org.jivesoftware.smackx.forward.Forwarded;
import org.xmlpull.v1.XmlPullParser;

public class CarbonManagerProvider implements PacketExtensionProvider {
    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        Direction dir = Direction.valueOf(parser.getName());
        Forwarded fwd = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2 && parser.getName().equals("forwarded")) {
                fwd = (Forwarded) PacketParserUtils.parsePacketExtension("forwarded", "urn:xmpp:forward:0", parser);
            } else if (eventType == 3 && dir == Direction.valueOf(parser.getName())) {
                done = true;
            }
        }
        if (fwd != null) {
            return new CarbonExtension(dir, fwd);
        }
        throw new Exception("sent/received must contain exactly one <forwarded> tag");
    }
}
