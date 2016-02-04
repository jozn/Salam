package org.jivesoftware.smackx.hoxt.provider;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.hoxt.packet.Base64BinaryChunk;
import org.xmlpull.v1.XmlPullParser;

public class Base64BinaryChunkProvider implements PacketExtensionProvider {
    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        String streamId = parser.getAttributeValue(BuildConfig.VERSION_NAME, "streamId");
        String nrString = parser.getAttributeValue(BuildConfig.VERSION_NAME, "nr");
        String lastString = parser.getAttributeValue(BuildConfig.VERSION_NAME, "last");
        boolean last = false;
        int nr = Integer.parseInt(nrString);
        if (lastString != null) {
            last = Boolean.parseBoolean(lastString);
        }
        String text = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 3) {
                if (parser.getName().equals("chunk")) {
                    done = true;
                } else {
                    throw new IllegalArgumentException("unexpected end tag of: " + parser.getName());
                }
            } else if (eventType == 4) {
                text = parser.getText();
            } else {
                throw new IllegalArgumentException("unexpected eventType: " + eventType);
            }
        }
        return new Base64BinaryChunk(text, streamId, nr, last);
    }
}
