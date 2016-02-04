package org.jivesoftware.smackx.muc.provider;

import android.support.v7.appcompat.BuildConfig;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.jivesoftware.smackx.muc.MUCAffiliation;
import org.jivesoftware.smackx.muc.MUCRole;
import org.jivesoftware.smackx.muc.packet.Destroy;
import org.jivesoftware.smackx.muc.packet.MUCItem;
import org.xmlpull.v1.XmlPullParser;

public final class MUCParserUtils {
    public static MUCItem parseItem(XmlPullParser parser) throws Exception {
        int initialDepth = parser.getDepth();
        MUCAffiliation affiliation = MUCAffiliation.fromString(parser.getAttributeValue(BuildConfig.VERSION_NAME, "affiliation"));
        String nick = parser.getAttributeValue(BuildConfig.VERSION_NAME, "nick");
        MUCRole role = MUCRole.fromString(parser.getAttributeValue(BuildConfig.VERSION_NAME, "role"));
        String jid = parser.getAttributeValue(BuildConfig.VERSION_NAME, "jid");
        String actor = null;
        String reason = null;
        while (true) {
            switch (parser.next()) {
                case Logger.WARNING /*2*/:
                    String name = parser.getName();
                    Object obj = -1;
                    switch (name.hashCode()) {
                        case -934964668:
                            if (name.equals("reason")) {
                                obj = 1;
                                break;
                            }
                            break;
                        case 92645877:
                            if (name.equals("actor")) {
                                obj = null;
                                break;
                            }
                            break;
                    }
                    switch (obj) {
                        case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                            actor = parser.getAttributeValue(BuildConfig.VERSION_NAME, "jid");
                            break;
                        case Logger.SEVERE /*1*/:
                            reason = parser.nextText();
                            break;
                    }
                    break;
                case Logger.INFO /*3*/:
                    break;
                default:
                    continue;
            }
            if (parser.getDepth() == initialDepth) {
                return new MUCItem(affiliation, role, actor, reason, jid, nick);
            }
        }
    }

    public static Destroy parseDestroy(XmlPullParser parser) throws Exception {
        boolean done = false;
        Destroy destroy = new Destroy();
        destroy.jid = parser.getAttributeValue(BuildConfig.VERSION_NAME, "jid");
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("reason")) {
                    destroy.reason = parser.nextText();
                }
            } else if (eventType == 3 && parser.getName().equals("destroy")) {
                done = true;
            }
        }
        return destroy;
    }
}
