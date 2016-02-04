package org.jivesoftware.smackx.commands.provider;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.commands.AdHocCommand.Action;
import org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition;
import org.jivesoftware.smackx.commands.AdHocCommand.Status;
import org.jivesoftware.smackx.commands.AdHocCommandNote;
import org.jivesoftware.smackx.commands.AdHocCommandNote.Type;
import org.jivesoftware.smackx.commands.packet.AdHocCommandData;
import org.jivesoftware.smackx.commands.packet.AdHocCommandData.SpecificError;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jivesoftware.smackx.xdata.provider.DataFormProvider;
import org.xmlpull.v1.XmlPullParser;

public class AdHocCommandDataProvider implements IQProvider {

    public static class BadActionError implements PacketExtensionProvider {
        public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            return new SpecificError(SpecificErrorCondition.badAction);
        }
    }

    public static class BadLocaleError implements PacketExtensionProvider {
        public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            return new SpecificError(SpecificErrorCondition.badLocale);
        }
    }

    public static class BadPayloadError implements PacketExtensionProvider {
        public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            return new SpecificError(SpecificErrorCondition.badPayload);
        }
    }

    public static class BadSessionIDError implements PacketExtensionProvider {
        public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            return new SpecificError(SpecificErrorCondition.badSessionid);
        }
    }

    public static class MalformedActionError implements PacketExtensionProvider {
        public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            return new SpecificError(SpecificErrorCondition.malformedAction);
        }
    }

    public static class SessionExpiredError implements PacketExtensionProvider {
        public final PacketExtension parseExtension(XmlPullParser parser) throws Exception {
            return new SpecificError(SpecificErrorCondition.sessionExpired);
        }
    }

    public final IQ parseIQ(XmlPullParser parser) throws Exception {
        boolean done = false;
        AdHocCommandData adHocCommandData = new AdHocCommandData();
        DataFormProvider dataFormProvider = new DataFormProvider();
        adHocCommandData.setSessionID(parser.getAttributeValue(BuildConfig.VERSION_NAME, "sessionid"));
        adHocCommandData.node = parser.getAttributeValue(BuildConfig.VERSION_NAME, "node");
        String status = parser.getAttributeValue(BuildConfig.VERSION_NAME, NotificationCompatApi21.CATEGORY_STATUS);
        if (Status.executing.toString().equalsIgnoreCase(status)) {
            adHocCommandData.status = Status.executing;
        } else if (Status.completed.toString().equalsIgnoreCase(status)) {
            adHocCommandData.status = Status.completed;
        } else if (Status.canceled.toString().equalsIgnoreCase(status)) {
            adHocCommandData.status = Status.canceled;
        }
        String action = parser.getAttributeValue(BuildConfig.VERSION_NAME, "action");
        if (action != null) {
            Action realAction = Action.valueOf(action);
            if (realAction == null || realAction.equals(Action.unknown)) {
                adHocCommandData.action = Action.unknown;
            } else {
                adHocCommandData.action = realAction;
            }
        }
        while (!done) {
            int eventType = parser.next();
            String elementName = parser.getName();
            String namespace = parser.getNamespace();
            if (eventType == 2) {
                if (parser.getName().equals("actions")) {
                    String execute = parser.getAttributeValue(BuildConfig.VERSION_NAME, "execute");
                    if (execute != null) {
                        adHocCommandData.executeAction = Action.valueOf(execute);
                    }
                } else if (parser.getName().equals("next")) {
                    adHocCommandData.addAction(Action.next);
                } else if (parser.getName().equals("complete")) {
                    adHocCommandData.addAction(Action.complete);
                } else if (parser.getName().equals("prev")) {
                    adHocCommandData.addAction(Action.prev);
                } else if (elementName.equals("x") && namespace.equals("jabber:x:data")) {
                    adHocCommandData.form = (DataForm) dataFormProvider.parseExtension(parser);
                } else if (parser.getName().equals("note")) {
                    adHocCommandData.notes.add(new AdHocCommandNote(Type.valueOf(parser.getAttributeValue(BuildConfig.VERSION_NAME, "type")), parser.nextText()));
                } else if (parser.getName().equals(MqttServiceConstants.TRACE_ERROR)) {
                    adHocCommandData.error = PacketParserUtils.parseError(parser);
                }
            } else if (eventType == 3 && parser.getName().equals("command")) {
                done = true;
            }
        }
        return adHocCommandData;
    }
}
