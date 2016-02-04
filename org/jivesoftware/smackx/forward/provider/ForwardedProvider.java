package org.jivesoftware.smackx.forward.provider;

import com.shamchat.activity.AddFavoriteTextActivity;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.forward.Forwarded;
import org.xmlpull.v1.XmlPullParser;

public class ForwardedProvider implements PacketExtensionProvider {
    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        DelayInformation di = null;
        Packet packet = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("delay")) {
                    di = (DelayInformation) PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser);
                } else if (parser.getName().equals(AddFavoriteTextActivity.EXTRA_MESSAGE)) {
                    packet = PacketParserUtils.parseMessage(parser);
                } else {
                    throw new Exception("Unsupported forwarded packet type: " + parser.getName());
                }
            } else if (eventType == 3 && parser.getName().equals("forwarded")) {
                done = true;
            }
        }
        if (packet != null) {
            return new Forwarded(di, packet);
        }
        throw new Exception("forwarded extension must contain a packet");
    }
}
