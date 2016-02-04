package org.jivesoftware.smackx.bytestreams.ibb.packet;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class Close extends IQ {
    public final String sessionID;

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.halfOpenElement("close");
        xmlStringBuilder.xmlnsAttribute("http://jabber.org/protocol/ibb");
        xmlStringBuilder.attribute("sid", this.sessionID);
        xmlStringBuilder.closeEmptyElement();
        return xmlStringBuilder;
    }

    public Close(String sessionID) {
        if (sessionID == null || BuildConfig.VERSION_NAME.equals(sessionID)) {
            throw new IllegalArgumentException("Session ID must not be null or empty");
        }
        this.sessionID = sessionID;
        setType(Type.set);
    }
}
