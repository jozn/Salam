package org.jivesoftware.smackx.muc.provider;

import android.support.v7.appcompat.BuildConfig;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.muc.packet.MUCUser;
import org.jivesoftware.smackx.muc.packet.MUCUser.Decline;
import org.jivesoftware.smackx.muc.packet.MUCUser.Invite;
import org.jivesoftware.smackx.muc.packet.MUCUser.Status;
import org.xmlpull.v1.XmlPullParser;

public class MUCUserProvider implements PacketExtensionProvider {
    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        int initialDepth = parser.getDepth();
        MUCUser mucUser = new MUCUser();
        while (true) {
            switch (parser.next()) {
                case Logger.WARNING /*2*/:
                    String name = parser.getName();
                    Object obj = -1;
                    int i;
                    switch (name.hashCode()) {
                        case -1183699191:
                            if (name.equals("invite")) {
                                obj = null;
                                break;
                            }
                            break;
                        case -892481550:
                            if (name.equals(NotificationCompatApi21.CATEGORY_STATUS)) {
                                i = 3;
                                break;
                            }
                            break;
                        case 3242771:
                            if (name.equals("item")) {
                                i = 1;
                                break;
                            }
                            break;
                        case 1216985755:
                            if (name.equals("password")) {
                                i = 2;
                                break;
                            }
                            break;
                        case 1542349558:
                            if (name.equals("decline")) {
                                obj = 4;
                                break;
                            }
                            break;
                        case 1557372922:
                            if (name.equals("destroy")) {
                                obj = 5;
                                break;
                            }
                            break;
                    }
                    int next;
                    switch (obj) {
                        case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                            Invite invite = new Invite();
                            invite.from = parser.getAttributeValue(BuildConfig.VERSION_NAME, "from");
                            invite.to = parser.getAttributeValue(BuildConfig.VERSION_NAME, "to");
                            obj = null;
                            while (obj == null) {
                                next = parser.next();
                                if (next == 2) {
                                    if (parser.getName().equals("reason")) {
                                        invite.reason = parser.nextText();
                                    }
                                } else if (next == 3 && parser.getName().equals("invite")) {
                                    obj = 1;
                                }
                            }
                            mucUser.invite = invite;
                            break;
                        case Logger.SEVERE /*1*/:
                            mucUser.item = MUCParserUtils.parseItem(parser);
                            break;
                        case Logger.WARNING /*2*/:
                            mucUser.password = parser.nextText();
                            break;
                        case Logger.INFO /*3*/:
                            mucUser.statusCodes.add(Status.create(parser.getAttributeValue(BuildConfig.VERSION_NAME, "code")));
                            break;
                        case Logger.CONFIG /*4*/:
                            Decline decline = new Decline();
                            decline.from = parser.getAttributeValue(BuildConfig.VERSION_NAME, "from");
                            decline.to = parser.getAttributeValue(BuildConfig.VERSION_NAME, "to");
                            obj = null;
                            while (obj == null) {
                                next = parser.next();
                                if (next == 2) {
                                    if (parser.getName().equals("reason")) {
                                        decline.reason = parser.nextText();
                                    }
                                } else if (next == 3 && parser.getName().equals("decline")) {
                                    obj = 1;
                                }
                            }
                            mucUser.decline = decline;
                            break;
                        case Logger.FINE /*5*/:
                            mucUser.destroy = MUCParserUtils.parseDestroy(parser);
                            break;
                        default:
                            break;
                    }
                case Logger.INFO /*3*/:
                    if (parser.getDepth() != initialDepth) {
                        break;
                    }
                    return mucUser;
                default:
                    break;
            }
        }
    }
}
