package org.jivesoftware.smackx.bytestreams.ibb.provider;

import android.support.v7.appcompat.BuildConfig;
import java.util.Locale;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager.StanzaType;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Open;
import org.xmlpull.v1.XmlPullParser;

public class OpenIQProvider implements IQProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        StanzaType stanza;
        String sessionID = parser.getAttributeValue(BuildConfig.VERSION_NAME, "sid");
        int blockSize = Integer.parseInt(parser.getAttributeValue(BuildConfig.VERSION_NAME, "block-size"));
        String stanzaValue = parser.getAttributeValue(BuildConfig.VERSION_NAME, "stanza");
        if (stanzaValue == null) {
            stanza = StanzaType.IQ;
        } else {
            stanza = StanzaType.valueOf(stanzaValue.toUpperCase(Locale.US));
        }
        return new Open(sessionID, blockSize, stanza);
    }
}
