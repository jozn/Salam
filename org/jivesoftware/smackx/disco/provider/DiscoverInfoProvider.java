package org.jivesoftware.smackx.disco.provider;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo.Identity;
import org.xmlpull.v1.XmlPullParser;

public class DiscoverInfoProvider implements IQProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        DiscoverInfo discoverInfo = new DiscoverInfo();
        boolean done = false;
        String category = BuildConfig.VERSION_NAME;
        String name = BuildConfig.VERSION_NAME;
        String type = BuildConfig.VERSION_NAME;
        String variable = BuildConfig.VERSION_NAME;
        String lang = BuildConfig.VERSION_NAME;
        discoverInfo.node = parser.getAttributeValue(BuildConfig.VERSION_NAME, "node");
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("identity")) {
                    category = parser.getAttributeValue(BuildConfig.VERSION_NAME, "category");
                    name = parser.getAttributeValue(BuildConfig.VERSION_NAME, "name");
                    type = parser.getAttributeValue(BuildConfig.VERSION_NAME, "type");
                    lang = parser.getAttributeValue(parser.getNamespace("xml"), "lang");
                } else if (parser.getName().equals("feature")) {
                    variable = parser.getAttributeValue(BuildConfig.VERSION_NAME, "var");
                } else {
                    discoverInfo.addExtension(PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser));
                }
            } else if (eventType == 3) {
                if (parser.getName().equals("identity")) {
                    Identity identity = new Identity(category, name, type);
                    if (lang != null) {
                        identity.lang = lang;
                    }
                    discoverInfo.addIdentity(identity);
                }
                if (parser.getName().equals("feature")) {
                    discoverInfo.addFeature(variable);
                }
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return discoverInfo;
    }
}
