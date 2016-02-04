package org.jivesoftware.smackx.iqlast.packet;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class LastActivity extends IQ {
    public long lastActivity;
    public String message;

    public static class Provider implements IQProvider {
        public final IQ parseIQ(XmlPullParser parser) throws SmackException, XmlPullParserException {
            LastActivity lastActivity = new LastActivity();
            String seconds = parser.getAttributeValue(BuildConfig.VERSION_NAME, "seconds");
            if (seconds != null) {
                try {
                    lastActivity.lastActivity = Long.parseLong(seconds);
                } catch (NumberFormatException e) {
                    throw new SmackException("Could not parse last activity number", e);
                }
            }
            try {
                lastActivity.message = parser.nextText();
                return lastActivity;
            } catch (Throwable e2) {
                throw new SmackException(e2);
            }
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.halfOpenElement("query");
        xmlStringBuilder.xmlnsAttribute("jabber:iq:last");
        String str = "seconds";
        Long valueOf = Long.valueOf(this.lastActivity);
        if (valueOf.longValue() >= 0) {
            xmlStringBuilder.attribute(str, Long.toString(valueOf.longValue()));
        }
        xmlStringBuilder.closeEmptyElement();
        return xmlStringBuilder;
    }

    public LastActivity() {
        this.lastActivity = -1;
        setType(Type.get);
    }

    public LastActivity(String to) {
        this();
        this.to = to;
    }
}
