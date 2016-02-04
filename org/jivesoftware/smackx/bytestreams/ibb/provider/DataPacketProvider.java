package org.jivesoftware.smackx.bytestreams.ibb.provider;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Data;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.xmlpull.v1.XmlPullParser;

public class DataPacketProvider implements IQProvider, PacketExtensionProvider {
    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        return new DataPacketExtension(parser.getAttributeValue(BuildConfig.VERSION_NAME, "sid"), Long.parseLong(parser.getAttributeValue(BuildConfig.VERSION_NAME, "seq")), parser.nextText());
    }

    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        return new Data((DataPacketExtension) parseExtension(parser));
    }
}
