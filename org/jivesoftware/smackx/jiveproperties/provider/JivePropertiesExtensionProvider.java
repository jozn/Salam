package org.jivesoftware.smackx.jiveproperties.provider;

import android.support.v7.appcompat.BuildConfig;
import com.shamchat.activity.MessageDetailsActivity;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.stringencoder.Base64;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jivesoftware.smackx.jiveproperties.packet.JivePropertiesExtension;
import org.xmlpull.v1.XmlPullParser;

public class JivePropertiesExtensionProvider implements PacketExtensionProvider {
    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(JivePropertiesExtensionProvider.class.getName());
    }

    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        Map<String, Object> properties = new HashMap();
        while (true) {
            int eventType = parser.next();
            if (eventType == 2 && parser.getName().equals("property")) {
                boolean done = false;
                String name = null;
                String type = null;
                String valueText = null;
                Object obj = null;
                while (!done) {
                    eventType = parser.next();
                    if (eventType == 2) {
                        String elementName = parser.getName();
                        if (elementName.equals("name")) {
                            name = parser.nextText();
                        } else if (elementName.equals("value")) {
                            type = parser.getAttributeValue(BuildConfig.VERSION_NAME, "type");
                            valueText = parser.nextText();
                        }
                    } else if (eventType == 3 && parser.getName().equals("property")) {
                        if ("integer".equals(type)) {
                            obj = Integer.valueOf(valueText);
                        } else if ("long".equals(type)) {
                            obj = Long.valueOf(valueText);
                        } else if ("float".equals(type)) {
                            obj = Float.valueOf(valueText);
                        } else if ("double".equals(type)) {
                            obj = Double.valueOf(valueText);
                        } else if (MessageDetailsActivity.EXTRA_BOOLEAN.equals(type)) {
                            obj = Boolean.valueOf(valueText);
                        } else if ("string".equals(type)) {
                            String value = valueText;
                        } else if ("java-object".equals(type)) {
                            if (JivePropertiesManager.isJavaObjectEnabled()) {
                                try {
                                    obj = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(valueText))).readObject();
                                } catch (Exception e) {
                                    LOGGER.log(Level.SEVERE, "Error parsing java object", e);
                                }
                            } else {
                                LOGGER.severe("JavaObject is not enabled. Enable with JivePropertiesManager.setJavaObjectEnabled(true)");
                            }
                        }
                        if (!(name == null || obj == null)) {
                            properties.put(name, obj);
                        }
                        done = true;
                    }
                }
            } else if (eventType == 3 && parser.getName().equals("properties")) {
                return new JivePropertiesExtension(properties);
            }
        }
    }
}
