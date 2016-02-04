package org.jivesoftware.smackx.bytestreams.socks5.provider;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream.Activate;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream.Mode;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream.StreamHost;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream.StreamHostUsed;
import org.xmlpull.v1.XmlPullParser;

public class BytestreamsProvider implements IQProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        boolean done = false;
        Bytestream toReturn = new Bytestream();
        String id = parser.getAttributeValue(BuildConfig.VERSION_NAME, "sid");
        String mode = parser.getAttributeValue(BuildConfig.VERSION_NAME, "mode");
        String JID = null;
        String host = null;
        String port = null;
        while (!done) {
            int eventType = parser.next();
            String elementName = parser.getName();
            if (eventType == 2) {
                if (elementName.equals(StreamHost.ELEMENTNAME)) {
                    JID = parser.getAttributeValue(BuildConfig.VERSION_NAME, "jid");
                    host = parser.getAttributeValue(BuildConfig.VERSION_NAME, "host");
                    port = parser.getAttributeValue(BuildConfig.VERSION_NAME, "port");
                } else if (elementName.equals(StreamHostUsed.ELEMENTNAME)) {
                    toReturn.usedHost = new StreamHostUsed(parser.getAttributeValue(BuildConfig.VERSION_NAME, "jid"));
                } else if (elementName.equals(Activate.ELEMENTNAME)) {
                    toReturn.toActivate = new Activate(parser.getAttributeValue(BuildConfig.VERSION_NAME, "jid"));
                }
            } else if (eventType == 3) {
                if (elementName.equals("streamhost")) {
                    if (port == null) {
                        toReturn.addStreamHost(JID, host, 0);
                    } else {
                        toReturn.addStreamHost(JID, host, Integer.parseInt(port));
                    }
                    JID = null;
                    host = null;
                    port = null;
                } else if (elementName.equals("query")) {
                    done = true;
                }
            }
        }
        toReturn.mode = Mode.fromName(mode);
        toReturn.sessionID = id;
        return toReturn;
    }
}
