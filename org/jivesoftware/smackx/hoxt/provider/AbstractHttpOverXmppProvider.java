package org.jivesoftware.smackx.hoxt.provider;

import android.support.v7.appcompat.BuildConfig;
import com.shamchat.activity.AddFavoriteTextActivity;
import java.util.Collection;
import java.util.HashSet;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.hoxt.packet.AbstractHttpOverXmpp.AbstractBody;
import org.jivesoftware.smackx.hoxt.packet.AbstractHttpOverXmpp.Base64;
import org.jivesoftware.smackx.hoxt.packet.AbstractHttpOverXmpp.ChunkedBase64;
import org.jivesoftware.smackx.hoxt.packet.AbstractHttpOverXmpp.Data;
import org.jivesoftware.smackx.hoxt.packet.AbstractHttpOverXmpp.DataChild;
import org.jivesoftware.smackx.hoxt.packet.AbstractHttpOverXmpp.Ibb;
import org.jivesoftware.smackx.hoxt.packet.AbstractHttpOverXmpp.Text;
import org.jivesoftware.smackx.hoxt.packet.AbstractHttpOverXmpp.Xml;
import org.jivesoftware.smackx.shim.packet.Header;
import org.jivesoftware.smackx.shim.packet.HeadersExtension;
import org.jivesoftware.smackx.shim.provider.HeaderProvider;
import org.xmlpull.v1.XmlPullParser;

public abstract class AbstractHttpOverXmppProvider implements IQProvider {
    protected static void parseHeadersAndData(XmlPullParser parser, String elementName, AbstractBody body) throws Exception {
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals("headers")) {
                    HeaderProvider headerProvider = new HeaderProvider();
                    Collection hashSet = new HashSet();
                    Object obj = null;
                    while (obj == null) {
                        Object obj2;
                        int next = parser.next();
                        if (next == 2) {
                            if (parser.getName().equals("header")) {
                                hashSet.add((Header) headerProvider.parseExtension(parser));
                            }
                            obj2 = obj;
                        } else {
                            if (next == 3 && parser.getName().equals("headers")) {
                                obj2 = 1;
                            }
                            obj2 = obj;
                        }
                        obj = obj2;
                    }
                    body.headers = new HeadersExtension(hashSet);
                } else if (parser.getName().endsWith("data")) {
                    body.data = parseData(parser);
                } else {
                    throw new IllegalArgumentException("unexpected tag:" + parser.getName() + "'");
                }
            } else if (eventType == 3 && parser.getName().equals(elementName)) {
                done = true;
            }
        }
    }

    private static Data parseData(XmlPullParser parser) throws Exception {
        DataChild child = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 2) {
                if (parser.getName().equals(AddFavoriteTextActivity.EXTRA_RESULT_TEXT)) {
                    String str = null;
                    Object obj = null;
                    while (obj == null) {
                        int next = parser.next();
                        if (next == 3) {
                            if (parser.getName().equals(AddFavoriteTextActivity.EXTRA_RESULT_TEXT)) {
                                obj = 1;
                            } else {
                                throw new IllegalArgumentException("unexpected end tag of: " + parser.getName());
                            }
                        } else if (next == 4) {
                            str = parser.getText();
                        } else {
                            throw new IllegalArgumentException("unexpected eventType: " + next);
                        }
                    }
                    child = new Text(str);
                } else if (parser.getName().equals("base64")) {
                    child = parseBase64(parser);
                } else if (parser.getName().equals("chunkedBase64")) {
                    child = parseChunkedBase64(parser);
                } else if (parser.getName().equals("xml")) {
                    child = parseXml(parser);
                } else if (parser.getName().equals("ibb")) {
                    child = parseIbb(parser);
                } else if (parser.getName().equals("sipub")) {
                    throw new UnsupportedOperationException("sipub is not supported yet");
                } else if (parser.getName().equals("jingle")) {
                    throw new UnsupportedOperationException("jingle is not supported yet");
                } else {
                    throw new IllegalArgumentException("unsupported child tag: " + parser.getName());
                }
            } else if (eventType == 3 && parser.getName().equals("data")) {
                done = true;
            }
        }
        return new Data(child);
    }

    private static Xml parseXml(XmlPullParser parser) throws Exception {
        StringBuilder builder = new StringBuilder();
        boolean done = false;
        boolean startClosed = true;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 3 && parser.getName().equals("xml")) {
                done = true;
            } else if (eventType == 2) {
                if (!startClosed) {
                    builder.append('>');
                }
                builder.append('<');
                builder.append(parser.getName());
                appendXmlAttributes(parser, builder);
                startClosed = false;
            } else if (eventType == 3) {
                if (startClosed) {
                    builder.append("</");
                    builder.append(parser.getName());
                    builder.append('>');
                } else {
                    builder.append("/>");
                    startClosed = true;
                }
            } else if (eventType == 4) {
                if (!startClosed) {
                    builder.append('>');
                    startClosed = true;
                }
                builder.append(StringUtils.escapeForXML(parser.getText()));
            } else {
                throw new IllegalArgumentException("unexpected eventType: " + eventType);
            }
        }
        return new Xml(builder.toString());
    }

    private static void appendXmlAttributes(XmlPullParser parser, StringBuilder builder) throws Exception {
        int count = parser.getAttributeCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                builder.append(' ');
                builder.append(parser.getAttributeName(i));
                builder.append("=\"");
                builder.append(StringUtils.escapeForXML(parser.getAttributeValue(i)));
                builder.append('\"');
            }
        }
    }

    private static Base64 parseBase64(XmlPullParser parser) throws Exception {
        String text = null;
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == 3) {
                if (parser.getName().equals("base64")) {
                    done = true;
                } else {
                    throw new IllegalArgumentException("unexpected end tag of: " + parser.getName());
                }
            } else if (eventType == 4) {
                text = parser.getText();
            } else {
                throw new IllegalArgumentException("unexpected eventType: " + eventType);
            }
        }
        return new Base64(text);
    }

    private static ChunkedBase64 parseChunkedBase64(XmlPullParser parser) throws Exception {
        ChunkedBase64 child = new ChunkedBase64(parser.getAttributeValue(BuildConfig.VERSION_NAME, "streamId"));
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType != 3) {
                throw new IllegalArgumentException("unexpected event type: " + eventType);
            } else if (parser.getName().equals("chunkedBase64")) {
                done = true;
            } else {
                throw new IllegalArgumentException("unexpected end tag: " + parser.getName());
            }
        }
        return child;
    }

    private static Ibb parseIbb(XmlPullParser parser) throws Exception {
        Ibb child = new Ibb(parser.getAttributeValue(BuildConfig.VERSION_NAME, "sid"));
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType != 3) {
                throw new IllegalArgumentException("unexpected event type: " + eventType);
            } else if (parser.getName().equals("ibb")) {
                done = true;
            } else {
                throw new IllegalArgumentException("unexpected end tag: " + parser.getName());
            }
        }
        return child;
    }
}
