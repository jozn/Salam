package org.jivesoftware.smackx.iqversion.provider;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smackx.iqversion.packet.Version;
import org.xmlpull.v1.XmlPullParser;

public class VersionProvider implements IQProvider {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !VersionProvider.class.desiredAssertionStatus();
    }

    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        if ($assertionsDisabled || parser.getEventType() == 2) {
            int initalDepth = parser.getDepth();
            String name = null;
            String version = null;
            String os = null;
            while (true) {
                switch (parser.next()) {
                    case Logger.WARNING /*2*/:
                        String name2 = parser.getName();
                        Object obj = -1;
                        switch (name2.hashCode()) {
                            case 3556:
                                if (name2.equals("os")) {
                                    int i = 2;
                                    break;
                                }
                                break;
                            case 3373707:
                                if (name2.equals("name")) {
                                    obj = null;
                                    break;
                                }
                                break;
                            case 351608024:
                                if (name2.equals("version")) {
                                    obj = 1;
                                    break;
                                }
                                break;
                        }
                        switch (obj) {
                            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                                name = parser.nextText();
                                break;
                            case Logger.SEVERE /*1*/:
                                version = parser.nextText();
                                break;
                            case Logger.WARNING /*2*/:
                                os = parser.nextText();
                                break;
                            default:
                                break;
                        }
                    case Logger.INFO /*3*/:
                        if (parser.getDepth() == initalDepth && parser.getName().equals("query")) {
                            if (name == null && version == null && os == null) {
                                return new Version();
                            }
                            return new Version(name, version, os);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        throw new AssertionError();
    }
}
