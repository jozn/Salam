package org.jivesoftware.smack.tcp.sm.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.StreamFeatureProvider;
import org.jivesoftware.smack.tcp.sm.packet.StreamManagement.StreamManagementFeature;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class StreamManagementStreamFeatureProvider implements StreamFeatureProvider {
    public final PacketExtension parseStreamFeature(XmlPullParser parser) throws XmlPullParserException {
        return StreamManagementFeature.INSTANCE;
    }
}
