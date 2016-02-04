package org.jivesoftware.smack.util;

import android.support.v7.appcompat.BuildConfig;
import com.shamchat.activity.AddFavoriteTextActivity;
import com.shamchat.activity.MessageDetailsActivity;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Bind;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.packet.RosterPacket.ItemStatus;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.packet.StartTls;
import org.jivesoftware.smack.packet.StreamError;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.sasl.packet.SaslStreamElements.SASLFailure;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class PacketParserUtils {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final Logger LOGGER;

    /* renamed from: org.jivesoftware.smack.util.PacketParserUtils.1 */
    static class C12931 extends IQ {
        C12931() {
        }

        public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
            return null;
        }
    }

    /* renamed from: org.jivesoftware.smack.util.PacketParserUtils.2 */
    static class C12942 extends IQ {
        C12942() {
        }

        public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
            return null;
        }
    }

    public static class UnparsedResultIQ extends IQ {
        private final CharSequence content;

        public UnparsedResultIQ(CharSequence content) {
            this.content = content;
        }

        public final CharSequence getChildElementXML() {
            return this.content;
        }
    }

    static {
        $assertionsDisabled = !PacketParserUtils.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(PacketParserUtils.class.getName());
    }

    public static Packet parseStanza(XmlPullParser parser, XMPPConnection connection) throws Exception {
        if ($assertionsDisabled || parser.getEventType() == 2) {
            String name = parser.getName();
            Object obj = -1;
            switch (name.hashCode()) {
                case -1276666629:
                    if (name.equals("presence")) {
                        int i = 2;
                        break;
                    }
                    break;
                case 3368:
                    if (name.equals("iq")) {
                        obj = 1;
                        break;
                    }
                    break;
                case 954925063:
                    if (name.equals(AddFavoriteTextActivity.EXTRA_MESSAGE)) {
                        obj = null;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                    return parseMessage(parser);
                case org.eclipse.paho.client.mqttv3.logging.Logger.SEVERE /*1*/:
                    return parseIQ(parser, connection);
                case org.eclipse.paho.client.mqttv3.logging.Logger.WARNING /*2*/:
                    return parsePresence(parser);
                default:
                    throw new IllegalArgumentException("Can only parse message, iq or presence, not " + name);
            }
        }
        throw new AssertionError();
    }

    public static XmlPullParser newXmppParser(Reader reader) throws XmlPullParserException {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
        parser.setInput(reader);
        return parser;
    }

    public static Message parseMessage(XmlPullParser parser) throws Exception {
        String defaultLanguage;
        Message message = new Message();
        message.packetID = parser.getAttributeValue(BuildConfig.VERSION_NAME, "id");
        message.to = parser.getAttributeValue(BuildConfig.VERSION_NAME, "to");
        message.from = parser.getAttributeValue(BuildConfig.VERSION_NAME, "from");
        String typeString = parser.getAttributeValue(BuildConfig.VERSION_NAME, "type");
        if (typeString != null) {
            message.setType(Type.fromString(typeString));
        }
        String language = getLanguageAttribute(parser);
        if (language == null || BuildConfig.VERSION_NAME.equals(language.trim())) {
            defaultLanguage = Packet.getDefaultLanguage();
        } else {
            message.setLanguage(language);
            defaultLanguage = language;
        }
        boolean done = false;
        String thread = null;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                String elementName = parser.getName();
                String namespace = parser.getNamespace();
                String xmlLang;
                if (elementName.equals("subject")) {
                    xmlLang = getLanguageAttribute(parser);
                    if (xmlLang == null) {
                        xmlLang = defaultLanguage;
                    }
                    String subject = parseElementText(parser);
                    if (message.getSubject(xmlLang) == null) {
                        message.addSubject(xmlLang, subject);
                    }
                } else if (elementName.equals("body")) {
                    xmlLang = getLanguageAttribute(parser);
                    if (xmlLang == null) {
                        xmlLang = defaultLanguage;
                    }
                    String body = parseElementText(parser);
                    if (message.getBody(xmlLang) == null) {
                        message.addBody(xmlLang, body);
                    }
                } else if (elementName.equals("thread")) {
                    if (thread == null) {
                        thread = parser.nextText();
                    }
                } else if (elementName.equals(MqttServiceConstants.TRACE_ERROR)) {
                    message.error = parseError(parser);
                } else {
                    message.addExtension(parsePacketExtension(elementName, namespace, parser));
                }
            } else if (eventType == 3 && parser.getName().equals(AddFavoriteTextActivity.EXTRA_MESSAGE)) {
                done = true;
            }
        }
        message.thread = thread;
        return message;
    }

    private static String parseElementText(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (!$assertionsDisabled && parser.getEventType() != 2) {
            throw new AssertionError();
        } else if (parser.isEmptyElementTag()) {
            return BuildConfig.VERSION_NAME;
        } else {
            int event = parser.next();
            if (event == 4) {
                String res = parser.getText();
                if (parser.next() == 3) {
                    return res;
                }
                throw new XmlPullParserException("Non-empty element tag contains child-elements, while Mixed Content (XML 3.2.2) is disallowed");
            } else if (event == 3) {
                return BuildConfig.VERSION_NAME;
            } else {
                throw new XmlPullParserException("Non-empty element tag not followed by text, while Mixed Content (XML 3.2.2) is disallowed");
            }
        }
    }

    public static CharSequence parseElement(XmlPullParser parser) throws XmlPullParserException, IOException {
        return parseElement(parser, false);
    }

    public static CharSequence parseElement(XmlPullParser parser, boolean fullNamespaces) throws XmlPullParserException, IOException {
        if ($assertionsDisabled || parser.getEventType() == 2) {
            return parseContentDepth(parser, parser.getDepth(), fullNamespaces);
        }
        throw new AssertionError();
    }

    public static CharSequence parseContentDepth(XmlPullParser parser, int depth) throws XmlPullParserException, IOException {
        return parseContentDepth(parser, depth, false);
    }

    private static CharSequence parseContentDepth(XmlPullParser parser, int depth, boolean fullNamespaces) throws XmlPullParserException, IOException {
        XmlStringBuilder xml = new XmlStringBuilder();
        int event = parser.getEventType();
        boolean isEmptyElement = false;
        String namespaceElement = null;
        while (true) {
            if (event == 2) {
                xml.halfOpenElement(parser.getName());
                if (namespaceElement == null || fullNamespaces) {
                    String namespace = parser.getNamespace();
                    if (StringUtils.isNotEmpty(namespace)) {
                        xml.attribute("xmlns", namespace);
                        namespaceElement = parser.getName();
                    }
                }
                for (int i = 0; i < parser.getAttributeCount(); i++) {
                    xml.attribute(parser.getAttributeName(i), parser.getAttributeValue(i));
                }
                if (parser.isEmptyElementTag()) {
                    xml.closeEmptyElement();
                    isEmptyElement = true;
                } else {
                    xml.rightAngleBracket();
                }
            } else if (event == 3) {
                if (isEmptyElement) {
                    isEmptyElement = false;
                } else {
                    xml.closeElement(parser.getName());
                }
                if (namespaceElement != null && namespaceElement.equals(parser.getName())) {
                    namespaceElement = null;
                }
                if (parser.getDepth() <= depth) {
                    return xml;
                }
            } else if (event == 4) {
                xml.append(parser.getText());
            }
            event = parser.next();
        }
    }

    private static Presence parsePresence(XmlPullParser parser) throws Exception {
        Presence.Type type = Presence.Type.available;
        String typeString = parser.getAttributeValue(BuildConfig.VERSION_NAME, "type");
        if (!(typeString == null || typeString.equals(BuildConfig.VERSION_NAME))) {
            type = Presence.Type.fromString(typeString);
        }
        Presence presence = new Presence(type);
        presence.to = parser.getAttributeValue(BuildConfig.VERSION_NAME, "to");
        presence.from = parser.getAttributeValue(BuildConfig.VERSION_NAME, "from");
        presence.packetID = parser.getAttributeValue(BuildConfig.VERSION_NAME, "id");
        String language = getLanguageAttribute(parser);
        if (!(language == null || BuildConfig.VERSION_NAME.equals(language.trim()))) {
            presence.setLanguage(language);
        }
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                String elementName = parser.getName();
                String namespace = parser.getNamespace();
                if (elementName.equals(NotificationCompatApi21.CATEGORY_STATUS)) {
                    presence.status = parser.nextText();
                } else if (elementName.equals("priority")) {
                    presence.setPriority(Integer.parseInt(parser.nextText()));
                } else if (elementName.equals("show")) {
                    String modeText = parser.nextText();
                    if (StringUtils.isNotEmpty(modeText)) {
                        presence.mode = Mode.fromString(modeText);
                    } else {
                        LOGGER.warning("Empty or null mode text in presence show element form " + presence.from + " with id '" + presence.packetID + "' which is invalid according to RFC6121 4.7.2.1");
                    }
                } else if (elementName.equals(MqttServiceConstants.TRACE_ERROR)) {
                    presence.error = parseError(parser);
                } else {
                    try {
                        presence.addExtension(parsePacketExtension(elementName, namespace, parser));
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Failed to parse extension packet in Presence packet.", e);
                    }
                }
            } else if (eventType == 3 && parser.getName().equals("presence")) {
                done = true;
            }
        }
        return presence;
    }

    private static IQ parseIQ(XmlPullParser parser, XMPPConnection connection) throws Exception {
        IQ iqPacket = null;
        XMPPError error = null;
        String id = parser.getAttributeValue(BuildConfig.VERSION_NAME, "id");
        String to = parser.getAttributeValue(BuildConfig.VERSION_NAME, "to");
        String from = parser.getAttributeValue(BuildConfig.VERSION_NAME, "from");
        IQ.Type type = IQ.Type.fromString(parser.getAttributeValue(BuildConfig.VERSION_NAME, "type"));
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                String elementName = parser.getName();
                String namespace = parser.getNamespace();
                if (elementName.equals(MqttServiceConstants.TRACE_ERROR)) {
                    error = parseError(parser);
                } else if (elementName.equals("query") && namespace.equals("jabber:iq:roster")) {
                    iqPacket = parseRoster(parser);
                } else if (!elementName.equals("bind") || !namespace.equals("urn:ietf:params:xml:ns:xmpp-bind")) {
                    IQProvider provider = ProviderManager.getIQProvider(elementName, namespace);
                    if (provider != null) {
                        iqPacket = provider.parseIQ(parser);
                    } else {
                        Class<?> introspectionProvider = ProviderManager.getIQIntrospectionProvider(elementName, namespace);
                        if (introspectionProvider != null) {
                            iqPacket = (IQ) parseWithIntrospection(elementName, introspectionProvider, parser);
                        } else if (IQ.Type.result != type) {
                            continue;
                        } else if ($assertionsDisabled || parser.getEventType() == 2) {
                            CharSequence charSequence;
                            if (parser.isEmptyElementTag()) {
                                charSequence = BuildConfig.VERSION_NAME;
                            } else {
                                parser.next();
                                charSequence = parseContentDepth(parser, parser.getDepth(), false);
                            }
                            iqPacket = new UnparsedResultIQ(charSequence);
                        } else {
                            throw new AssertionError();
                        }
                    }
                } else if ($assertionsDisabled || parser.getEventType() == 2) {
                    int depth = parser.getDepth();
                    iqPacket = null;
                    while (true) {
                        switch (parser.next()) {
                            case org.eclipse.paho.client.mqttv3.logging.Logger.WARNING /*2*/:
                                String name = parser.getName();
                                Object obj = -1;
                                switch (name.hashCode()) {
                                    case -341064690:
                                        if (name.equals("resource")) {
                                            obj = null;
                                            break;
                                        }
                                        break;
                                    case 105221:
                                        if (name.equals("jid")) {
                                            obj = 1;
                                            break;
                                        }
                                        break;
                                }
                                switch (obj) {
                                    case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                                        iqPacket = Bind.newSet(parser.nextText());
                                        break;
                                    case org.eclipse.paho.client.mqttv3.logging.Logger.SEVERE /*1*/:
                                        iqPacket = Bind.newResult(parser.nextText());
                                        break;
                                    default:
                                        break;
                                }
                            case org.eclipse.paho.client.mqttv3.logging.Logger.INFO /*3*/:
                                if (parser.getName().equals("bind") && parser.getDepth() == depth) {
                                    if ($assertionsDisabled || parser.getEventType() == 3) {
                                        continue;
                                    } else {
                                        throw new AssertionError();
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    throw new AssertionError();
                }
            } else if (eventType == 3 && parser.getName().equals("iq")) {
                done = true;
            }
        }
        if (iqPacket == null) {
            if (connection == null || !(IQ.Type.get == type || IQ.Type.set == type)) {
                iqPacket = new C12942();
            } else {
                iqPacket = new C12931();
                iqPacket.packetID = id;
                iqPacket.to = from;
                iqPacket.from = to;
                iqPacket.setType(IQ.Type.error);
                iqPacket.error = new XMPPError(Condition.feature_not_implemented);
                connection.sendPacket(iqPacket);
                return null;
            }
        }
        iqPacket.packetID = id;
        iqPacket.to = to;
        iqPacket.from = from;
        iqPacket.setType(type);
        iqPacket.error = error;
        return iqPacket;
    }

    public static RosterPacket parseRoster(XmlPullParser parser) throws Exception {
        RosterPacket roster = new RosterPacket();
        boolean done = false;
        Item item = null;
        roster.rosterVersion = parser.getAttributeValue(BuildConfig.VERSION_NAME, "ver");
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("item")) {
                    item = new Item(parser.getAttributeValue(BuildConfig.VERSION_NAME, "jid"), parser.getAttributeValue(BuildConfig.VERSION_NAME, "name"));
                    item.itemStatus = ItemStatus.fromString(parser.getAttributeValue(BuildConfig.VERSION_NAME, "ask"));
                    String subscription = parser.getAttributeValue(BuildConfig.VERSION_NAME, "subscription");
                    if (subscription == null) {
                        subscription = "none";
                    }
                    item.itemType = ItemType.valueOf(subscription);
                } else if (parser.getName().equals("group") && item != null) {
                    String groupName = parser.nextText();
                    if (groupName != null && groupName.trim().length() > 0) {
                        item.addGroupName(groupName);
                    }
                }
            } else if (eventType == 3) {
                if (parser.getName().equals("item")) {
                    roster.addRosterItem(item);
                }
                if (parser.getName().equals("query")) {
                    done = true;
                }
            }
        }
        return roster;
    }

    public static Collection<String> parseMechanisms(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<String> mechanisms = new ArrayList();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("mechanism")) {
                    mechanisms.add(parser.nextText());
                }
            } else if (eventType == 3 && parser.getName().equals("mechanisms")) {
                done = true;
            }
        }
        return mechanisms;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.jivesoftware.smack.compress.packet.Compress.Feature parseCompressionFeature(org.xmlpull.v1.XmlPullParser r6) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
        r3 = 0;
        r4 = -1;
        r2 = $assertionsDisabled;
        if (r2 != 0) goto L_0x0013;
    L_0x0006:
        r2 = r6.getEventType();
        r5 = 2;
        if (r2 == r5) goto L_0x0013;
    L_0x000d:
        r2 = new java.lang.AssertionError;
        r2.<init>();
        throw r2;
    L_0x0013:
        r0 = r6.getDepth();
        r1 = new java.util.LinkedList;
        r1.<init>();
    L_0x001c:
        r2 = r6.next();
        switch(r2) {
            case 2: goto L_0x0024;
            case 3: goto L_0x0046;
            default: goto L_0x0023;
        };
    L_0x0023:
        goto L_0x001c;
    L_0x0024:
        r2 = r6.getName();
        r5 = r2.hashCode();
        switch(r5) {
            case -1077554975: goto L_0x003c;
            default: goto L_0x002f;
        };
    L_0x002f:
        r2 = r4;
    L_0x0030:
        switch(r2) {
            case 0: goto L_0x0034;
            default: goto L_0x0033;
        };
    L_0x0033:
        goto L_0x001c;
    L_0x0034:
        r2 = r6.nextText();
        r1.add(r2);
        goto L_0x001c;
    L_0x003c:
        r5 = "method";
        r2 = r2.equals(r5);
        if (r2 == 0) goto L_0x002f;
    L_0x0044:
        r2 = r3;
        goto L_0x0030;
    L_0x0046:
        r2 = r6.getName();
        r5 = r2.hashCode();
        switch(r5) {
            case 1431984486: goto L_0x006d;
            default: goto L_0x0051;
        };
    L_0x0051:
        r2 = r4;
    L_0x0052:
        switch(r2) {
            case 0: goto L_0x0056;
            default: goto L_0x0055;
        };
    L_0x0055:
        goto L_0x001c;
    L_0x0056:
        r2 = r6.getDepth();
        if (r2 != r0) goto L_0x001c;
    L_0x005c:
        r2 = $assertionsDisabled;
        if (r2 != 0) goto L_0x0077;
    L_0x0060:
        r2 = r6.getEventType();
        r3 = 3;
        if (r2 == r3) goto L_0x0077;
    L_0x0067:
        r2 = new java.lang.AssertionError;
        r2.<init>();
        throw r2;
    L_0x006d:
        r5 = "compression";
        r2 = r2.equals(r5);
        if (r2 == 0) goto L_0x0051;
    L_0x0075:
        r2 = r3;
        goto L_0x0052;
    L_0x0077:
        r2 = $assertionsDisabled;
        if (r2 != 0) goto L_0x0087;
    L_0x007b:
        r2 = r6.getDepth();
        if (r2 == r0) goto L_0x0087;
    L_0x0081:
        r2 = new java.lang.AssertionError;
        r2.<init>();
        throw r2;
    L_0x0087:
        r2 = new org.jivesoftware.smack.compress.packet.Compress$Feature;
        r2.<init>(r1);
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.util.PacketParserUtils.parseCompressionFeature(org.xmlpull.v1.XmlPullParser):org.jivesoftware.smack.compress.packet.Compress$Feature");
    }

    public static SASLFailure parseSASLFailure(XmlPullParser parser) throws Exception {
        String condition = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (!parser.getName().equals("failure")) {
                    condition = parser.getName();
                }
            } else if (eventType == 3 && parser.getName().equals("failure")) {
                done = true;
            }
        }
        return new SASLFailure(condition);
    }

    public static StreamError parseStreamError(XmlPullParser parser) throws IOException, XmlPullParserException {
        int depth = parser.getDepth();
        boolean done = false;
        String code = null;
        String text = null;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if ("urn:ietf:params:xml:ns:xmpp-streams".equals(parser.getNamespace())) {
                    String name = parser.getName();
                    if (!name.equals(AddFavoriteTextActivity.EXTRA_RESULT_TEXT) || parser.isEmptyElementTag()) {
                        code = name;
                    } else {
                        parser.next();
                        text = parser.getText();
                    }
                }
            } else if (eventType == 3 && depth == parser.getDepth()) {
                done = true;
            }
        }
        return new StreamError(code, text);
    }

    public static XMPPError parseError(XmlPullParser parser) throws Exception {
        String message = null;
        String condition = null;
        List<PacketExtension> extensions = new ArrayList();
        String type = parser.getAttributeValue(BuildConfig.VERSION_NAME, "type");
        while (true) {
            int eventType = parser.next();
            if (eventType != 2) {
                if (eventType == 3 && parser.getName().equals(MqttServiceConstants.TRACE_ERROR)) {
                    break;
                }
            } else if (parser.getName().equals(AddFavoriteTextActivity.EXTRA_RESULT_TEXT)) {
                message = parser.nextText();
            } else {
                String elementName = parser.getName();
                String namespace = parser.getNamespace();
                if (namespace.equals("urn:ietf:params:xml:ns:xmpp-stanzas")) {
                    condition = elementName;
                } else {
                    extensions.add(parsePacketExtension(elementName, namespace, parser));
                }
            }
        }
        XMPPError.Type errorType = XMPPError.Type.CANCEL;
        if (type != null) {
            try {
                errorType = XMPPError.Type.valueOf(type.toUpperCase(Locale.US));
            } catch (IllegalArgumentException iae) {
                LOGGER.log(Level.SEVERE, "Could not find error type for " + type.toUpperCase(Locale.US), iae);
            }
        }
        return new XMPPError(errorType, condition, message, extensions);
    }

    public static PacketExtension parsePacketExtension(String elementName, String namespace, XmlPullParser parser) throws Exception {
        PacketExtensionProvider provider = ProviderManager.getExtensionProvider(elementName, namespace);
        if (provider != null) {
            return provider.parseExtension(parser);
        }
        Class<?> introspectionProvider = ProviderManager.getExtensionIntrospectionProvider(elementName, namespace);
        if (introspectionProvider != null) {
            return (PacketExtension) parseWithIntrospection(elementName, introspectionProvider, parser);
        }
        DefaultPacketExtension extension = new DefaultPacketExtension(elementName, namespace);
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                String name = parser.getName();
                if (parser.isEmptyElementTag()) {
                    extension.setValue(name, BuildConfig.VERSION_NAME);
                } else if (parser.next() == 4) {
                    extension.setValue(name, parser.getText());
                }
            } else if (eventType == 3 && parser.getName().equals(elementName)) {
                done = true;
            }
        }
        return extension;
    }

    public static StartTls parseStartTlsFeature(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (!$assertionsDisabled && parser.getEventType() != 2) {
            throw new AssertionError();
        } else if ($assertionsDisabled || parser.getNamespace().equals("urn:ietf:params:xml:ns:xmpp-tls")) {
            int initalDepth = parser.getDepth();
            boolean required = false;
            while (true) {
                switch (parser.next()) {
                    case org.eclipse.paho.client.mqttv3.logging.Logger.WARNING /*2*/:
                        String name = parser.getName();
                        Object obj = -1;
                        switch (name.hashCode()) {
                            case -393139297:
                                if (name.equals("required")) {
                                    obj = null;
                                    break;
                                }
                                break;
                        }
                        switch (obj) {
                            case MqttConnectOptions.MQTT_VERSION_DEFAULT /*0*/:
                                required = true;
                                break;
                            default:
                                break;
                        }
                    case org.eclipse.paho.client.mqttv3.logging.Logger.INFO /*3*/:
                        if (parser.getDepth() != initalDepth) {
                            break;
                        } else if ($assertionsDisabled || parser.getEventType() == 3) {
                            return new StartTls(required);
                        } else {
                            throw new AssertionError();
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            throw new AssertionError();
        }
    }

    private static String getLanguageAttribute(XmlPullParser parser) {
        int i = 0;
        while (i < parser.getAttributeCount()) {
            String attributeName = parser.getAttributeName(i);
            if ("xml:lang".equals(attributeName) || ("lang".equals(attributeName) && "xml".equals(parser.getAttributePrefix(i)))) {
                return parser.getAttributeValue(i);
            }
            i++;
        }
        return null;
    }

    private static Object parseWithIntrospection(String elementName, Class<?> objectClass, XmlPullParser parser) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, XmlPullParserException, IOException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
        boolean done = false;
        Object object = objectClass.newInstance();
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                String name = parser.getName();
                String stringValue = parser.nextText();
                Class<?> propertyType = object.getClass().getMethod("get" + Character.toUpperCase(name.charAt(0)) + name.substring(1), new Class[0]).getReturnType();
                Object value = propertyType.getName().equals("java.lang.String") ? stringValue : propertyType.getName().equals(MessageDetailsActivity.EXTRA_BOOLEAN) ? Boolean.valueOf(stringValue) : propertyType.getName().equals("int") ? Integer.valueOf(stringValue) : propertyType.getName().equals("long") ? Long.valueOf(stringValue) : propertyType.getName().equals("float") ? Float.valueOf(stringValue) : propertyType.getName().equals("double") ? Double.valueOf(stringValue) : propertyType.getName().equals("java.lang.Class") ? Class.forName(stringValue) : null;
                object.getClass().getMethod("set" + Character.toUpperCase(name.charAt(0)) + name.substring(1), new Class[]{propertyType}).invoke(object, new Object[]{value});
            } else if (eventType == 3 && parser.getName().equals(elementName)) {
                done = true;
            }
        }
        return object;
    }
}
