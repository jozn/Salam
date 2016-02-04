package org.jivesoftware.smackx.caps.provider;

import java.io.IOException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.StreamFeatureProvider;
import org.jivesoftware.smackx.caps.packet.CapsExtension;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class CapsExtensionProvider implements PacketExtensionProvider, StreamFeatureProvider {
    public final PacketExtension parseExtension(XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
        if (parser.getEventType() == 2 && parser.getName().equalsIgnoreCase("c")) {
            String hash = parser.getAttributeValue(null, "hash");
            String version = parser.getAttributeValue(null, "ver");
            String node = parser.getAttributeValue(null, "node");
            parser.next();
            if (parser.getEventType() != 3 || !parser.getName().equalsIgnoreCase("c")) {
                throw new SmackException("Malformed nested Caps element");
            } else if (hash != null && version != null && node != null) {
                return new CapsExtension(node, version, hash);
            } else {
                throw new SmackException("Caps elment with missing attributes");
            }
        }
        throw new SmackException("Malformed Caps element");
    }

    public final PacketExtension parseStreamFeature(XmlPullParser parser) throws XmlPullParserException, IOException, SmackException {
        return parseExtension(parser);
    }
}
