package org.jivesoftware.smackx.disco.provider;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.disco.packet.DiscoverItems.Item;
import org.xmlpull.v1.XmlPullParser;

public class DiscoverItemsProvider implements IQProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        DiscoverItems discoverItems = new DiscoverItems();
        boolean done = false;
        String jid = BuildConfig.VERSION_NAME;
        String name = BuildConfig.VERSION_NAME;
        String action = BuildConfig.VERSION_NAME;
        String node = BuildConfig.VERSION_NAME;
        discoverItems.node = parser.getAttributeValue(BuildConfig.VERSION_NAME, "node");
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2 && "item".equals(parser.getName())) {
                jid = parser.getAttributeValue(BuildConfig.VERSION_NAME, "jid");
                name = parser.getAttributeValue(BuildConfig.VERSION_NAME, "name");
                node = parser.getAttributeValue(BuildConfig.VERSION_NAME, "node");
                action = parser.getAttributeValue(BuildConfig.VERSION_NAME, "action");
            } else if (eventType == 3 && "item".equals(parser.getName())) {
                Item item = new Item(jid);
                item.name = name;
                item.node = node;
                item.action = action;
                discoverItems.addItem(item);
            } else if (eventType == 3 && "query".equals(parser.getName())) {
                done = true;
            }
        }
        return discoverItems;
    }
}
