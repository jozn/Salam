package org.jivesoftware.smackx.ping.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.ping.packet.Ping;
import org.xmlpull.v1.XmlPullParser;

public class PingProvider implements IQProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        return new Ping();
    }
}
