package org.jivesoftware.smackx.iqregister.provider;

import android.support.v7.appcompat.BuildConfig;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.iqregister.packet.Registration;
import org.xmlpull.v1.XmlPullParser;

public class RegistrationProvider implements IQProvider {
    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        String instruction = null;
        Map<String, String> fields = new HashMap();
        List<PacketExtension> packetExtensions = new LinkedList();
        while (true) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getNamespace().equals("jabber:iq:register")) {
                    String name = parser.getName();
                    String value = BuildConfig.VERSION_NAME;
                    if (parser.next() == 4) {
                        value = parser.getText();
                    }
                    if (name.equals("instructions")) {
                        instruction = value;
                    } else {
                        fields.put(name, value);
                    }
                } else {
                    packetExtensions.add(PacketParserUtils.parsePacketExtension(parser.getName(), parser.getNamespace(), parser));
                }
            } else if (eventType == 3 && parser.getName().equals("query")) {
                Registration registration = new Registration(instruction, fields);
                registration.addExtensions(packetExtensions);
                return registration;
            }
        }
    }
}
