package org.jivesoftware.smackx.bytestreams.ibb.provider;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Close;
import org.xmlpull.v1.XmlPullParser;

public class CloseIQProvider implements IQProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        return new Close(parser.getAttributeValue(BuildConfig.VERSION_NAME, "sid"));
    }
}
