package org.jivesoftware.smackx.pep.provider;

import android.support.v7.appcompat.BuildConfig;
import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

public class PEPProvider implements PacketExtensionProvider {
    Map<String, PacketExtensionProvider> nodeParsers;
    PacketExtension pepItem;

    public PEPProvider() {
        this.nodeParsers = new HashMap();
    }

    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (!parser.getName().equals(NotificationCompatApi21.CATEGORY_EVENT) && parser.getName().equals("items")) {
                    PacketExtensionProvider nodeParser = (PacketExtensionProvider) this.nodeParsers.get(parser.getAttributeValue(BuildConfig.VERSION_NAME, "node"));
                    if (nodeParser != null) {
                        this.pepItem = nodeParser.parseExtension(parser);
                    }
                }
            } else if (eventType == 3 && parser.getName().equals(NotificationCompatApi21.CATEGORY_EVENT)) {
                done = true;
            }
        }
        return this.pepItem;
    }
}
