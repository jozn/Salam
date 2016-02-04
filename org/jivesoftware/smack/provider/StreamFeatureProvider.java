package org.jivesoftware.smack.provider;

import java.io.IOException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.PacketExtension;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public interface StreamFeatureProvider {
    PacketExtension parseStreamFeature(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, SmackException;
}
