package org.jivesoftware.smackx.rsm.provider;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.ParserUtils;
import org.jivesoftware.smackx.rsm.packet.RSMSet;
import org.xmlpull.v1.XmlPullParser;

public class RSMSetProvider implements PacketExtensionProvider {
    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        int initialDepth = parser.getDepth();
        String after = null;
        String before = null;
        int count = -1;
        int index = -1;
        String last = null;
        int max = -1;
        String firstString = null;
        int firstIndex = -1;
        while (true) {
            switch (parser.next()) {
                case Logger.WARNING /*2*/:
                    String name = parser.getName();
                    Object obj = -1;
                    switch (name.hashCode()) {
                        case -1392885889:
                            if (name.equals("before")) {
                                obj = 1;
                                break;
                            }
                            break;
                        case 107876:
                            if (name.equals("max")) {
                                obj = 6;
                                break;
                            }
                            break;
                        case 3314326:
                            if (name.equals("last")) {
                                obj = 5;
                                break;
                            }
                            break;
                        case 92734940:
                            if (name.equals("after")) {
                                obj = null;
                                break;
                            }
                            break;
                        case 94851343:
                            if (name.equals("count")) {
                                obj = 2;
                                break;
                            }
                            break;
                        case 97440432:
                            if (name.equals("first")) {
                                obj = 3;
                                break;
                            }
                            break;
                        case 100346066:
                            if (name.equals("index")) {
                                obj = 4;
                                break;
                            }
                            break;
                    }
                    switch (obj) {
                        case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                            after = parser.nextText();
                            break;
                        case Logger.SEVERE /*1*/:
                            before = parser.nextText();
                            break;
                        case Logger.WARNING /*2*/:
                            count = ParserUtils.getIntegerFromNextText(parser);
                            break;
                        case Logger.INFO /*3*/:
                            firstIndex = ParserUtils.getIntegerAttribute$24eef9d3(parser, "index");
                            firstString = parser.nextText();
                            break;
                        case Logger.CONFIG /*4*/:
                            index = ParserUtils.getIntegerFromNextText(parser);
                            break;
                        case Logger.FINE /*5*/:
                            last = parser.nextText();
                            break;
                        case Logger.FINER /*6*/:
                            max = ParserUtils.getIntegerFromNextText(parser);
                            break;
                        default:
                            break;
                    }
                case Logger.INFO /*3*/:
                    if (parser.getDepth() != initialDepth) {
                        break;
                    }
                    return new RSMSet(after, before, count, index, last, max, firstString, firstIndex);
                default:
                    break;
            }
        }
    }
}
