package org.jivesoftware.smackx.address.provider;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.address.packet.MultipleAddresses;
import org.jivesoftware.smackx.address.packet.MultipleAddresses.Address;
import org.xmlpull.v1.XmlPullParser;

public class MultipleAddressesProvider implements PacketExtensionProvider {
    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        boolean done = false;
        MultipleAddresses multipleAddresses = new MultipleAddresses();
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("address")) {
                    String type = parser.getAttributeValue(BuildConfig.VERSION_NAME, "type");
                    String jid = parser.getAttributeValue(BuildConfig.VERSION_NAME, "jid");
                    String node = parser.getAttributeValue(BuildConfig.VERSION_NAME, "node");
                    String desc = parser.getAttributeValue(BuildConfig.VERSION_NAME, "desc");
                    boolean delivered = "true".equals(parser.getAttributeValue(BuildConfig.VERSION_NAME, "delivered"));
                    String uri = parser.getAttributeValue(BuildConfig.VERSION_NAME, "uri");
                    Address address = new Address((byte) 0);
                    address.jid = jid;
                    address.node = node;
                    address.description = desc;
                    address.delivered = delivered;
                    address.uri = uri;
                    multipleAddresses.addresses.add(address);
                }
            } else if (eventType == 3 && parser.getName().equals("addresses")) {
                done = true;
            }
        }
        return multipleAddresses;
    }
}
