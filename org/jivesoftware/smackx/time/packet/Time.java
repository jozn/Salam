package org.jivesoftware.smackx.time.packet;

import java.util.Calendar;
import java.util.logging.Logger;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jxmpp.util.XmppDateTime;

public class Time extends IQ {
    private static final Logger LOGGER;
    private String tzo;
    private String utc;

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<time xmlns='urn:xmpp:time'>");
        if (this.utc != null) {
            stringBuilder.append("<utc>").append(this.utc).append("</utc>");
            stringBuilder.append("<tzo>").append(this.tzo).append("</tzo>");
        }
        stringBuilder.append("</time>");
        return stringBuilder.toString();
    }

    static {
        LOGGER = Logger.getLogger(Time.class.getName());
    }

    public Time() {
        setType(Type.get);
    }

    private Time(Calendar cal) {
        this.tzo = XmppDateTime.asString(cal.getTimeZone());
        this.utc = XmppDateTime.formatXEP0082Date(cal.getTime());
    }

    public static Time createResponse(Packet request) {
        Time time = new Time(Calendar.getInstance());
        time.setType(Type.result);
        time.to = request.from;
        return time;
    }
}
