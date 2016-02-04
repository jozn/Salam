package org.jivesoftware.smackx.muc.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.muc.packet.MUCAdmin;
import org.jivesoftware.smackx.muc.packet.MUCItem;
import org.xmlpull.v1.XmlPullParser;

public class MUCAdminProvider implements IQProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        MUCAdmin mucAdmin = new MUCAdmin();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("item")) {
                    MUCItem parseItem = MUCParserUtils.parseItem(parser);
                    synchronized (mucAdmin.items) {
                        mucAdmin.items.add(parseItem);
                    }
                } else {
                    continue;
                }
            } else if (eventType == 3 && parser.getName().equals("query")) {
                done = true;
            }
        }
        return mucAdmin;
    }
}
