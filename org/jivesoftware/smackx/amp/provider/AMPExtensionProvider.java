package org.jivesoftware.smackx.amp.provider;

import java.util.logging.Logger;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.amp.AMPDeliverCondition;
import org.jivesoftware.smackx.amp.AMPDeliverCondition.Value;
import org.jivesoftware.smackx.amp.AMPExpireAtCondition;
import org.jivesoftware.smackx.amp.AMPMatchResourceCondition;
import org.jivesoftware.smackx.amp.packet.AMPExtension;
import org.jivesoftware.smackx.amp.packet.AMPExtension.Action;
import org.jivesoftware.smackx.amp.packet.AMPExtension.Condition;
import org.jivesoftware.smackx.amp.packet.AMPExtension.Rule;
import org.jivesoftware.smackx.amp.packet.AMPExtension.Status;
import org.xmlpull.v1.XmlPullParser;

public class AMPExtensionProvider implements PacketExtensionProvider {
    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(AMPExtensionProvider.class.getName());
    }

    public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        String from = parser.getAttributeValue(null, "from");
        String to = parser.getAttributeValue(null, "to");
        String statusString = parser.getAttributeValue(null, NotificationCompatApi21.CATEGORY_STATUS);
        Status status = null;
        if (statusString != null) {
            try {
                status = Status.valueOf(statusString);
            } catch (IllegalArgumentException e) {
                LOGGER.severe("Found invalid amp status " + statusString);
            }
        }
        AMPExtension ampExtension = new AMPExtension(from, to, status);
        String perHopValue = parser.getAttributeValue(null, "per-hop");
        if (perHopValue != null) {
            ampExtension.setPerHop(Boolean.parseBoolean(perHopValue));
        }
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("rule")) {
                    String actionString = parser.getAttributeValue(null, "action");
                    Condition condition = createCondition(parser.getAttributeValue(null, "condition"), parser.getAttributeValue(null, "value"));
                    Action action = null;
                    if (actionString != null) {
                        try {
                            action = Action.valueOf(actionString);
                        } catch (IllegalArgumentException e2) {
                            LOGGER.severe("Found invalid rule action value " + actionString);
                        }
                    }
                    if (action == null || condition == null) {
                        LOGGER.severe("Rule is skipped because either it's action or it's condition is invalid");
                    } else {
                        Rule rule = new Rule(action, condition);
                        ampExtension.rules.add(rule);
                    }
                }
            } else if (eventType == 3 && parser.getName().equals("amp")) {
                done = true;
            }
        }
        return ampExtension;
    }

    private static Condition createCondition(String name, String value) {
        if (name == null || value == null) {
            LOGGER.severe("Can't create rule condition from null name and/or value");
            return null;
        } else if ("deliver".equals(name)) {
            try {
                return new AMPDeliverCondition(Value.valueOf(value));
            } catch (IllegalArgumentException e) {
                LOGGER.severe("Found invalid rule delivery condition value " + value);
                return null;
            }
        } else if ("expire-at".equals(name)) {
            return new AMPExpireAtCondition(value);
        } else {
            if ("match-resource".equals(name)) {
                try {
                    return new AMPMatchResourceCondition(AMPMatchResourceCondition.Value.valueOf(value));
                } catch (IllegalArgumentException e2) {
                    LOGGER.severe("Found invalid rule match-resource condition value " + value);
                    return null;
                }
            }
            LOGGER.severe("Found unknown rule condition name " + name);
            return null;
        }
    }
}
