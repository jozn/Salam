package org.jivesoftware.smackx.muc.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.muc.packet.MUCItem;
import org.jivesoftware.smackx.muc.packet.MUCOwner;
import org.xmlpull.v1.XmlPullParser;

public class MUCOwnerProvider implements IQProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        MUCOwner mucOwner = new MUCOwner();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("item")) {
                    MUCItem parseItem = MUCParserUtils.parseItem(parser);
                    synchronized (mucOwner.items) {
                        mucOwner.items.add(parseItem);
                    }
                } else if (parser.getName().equals("destroy")) {
                    mucOwner.destroy = MUCParserUtils.parseDestroy(parser);
                } else {
                    mucOwner.addExtension(PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser));
                }
            } else if (eventType == 3 && parser.getName().equals("query")) {
                done = true;
            }
        }
        return mucOwner;
    }
}
