package org.jivesoftware.smackx.bytestreams.ibb.packet;

import android.support.v7.appcompat.BuildConfig;
import java.util.Locale;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager.StanzaType;

public class Open extends IQ {
    public final int blockSize;
    public final String sessionID;
    private final StanzaType stanza;

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.halfOpenElement("open");
        xmlStringBuilder.xmlnsAttribute("http://jabber.org/protocol/ibb");
        xmlStringBuilder.attribute("block-size", Integer.toString(this.blockSize));
        xmlStringBuilder.attribute("sid", this.sessionID);
        xmlStringBuilder.attribute("stanza", this.stanza.toString().toLowerCase(Locale.US));
        xmlStringBuilder.closeEmptyElement();
        return xmlStringBuilder;
    }

    public Open(String sessionID, int blockSize, StanzaType stanza) {
        if (sessionID == null || BuildConfig.VERSION_NAME.equals(sessionID)) {
            throw new IllegalArgumentException("Session ID must not be null or empty");
        } else if (blockSize <= 0) {
            throw new IllegalArgumentException("Block size must be greater than zero");
        } else {
            this.sessionID = sessionID;
            this.blockSize = blockSize;
            this.stanza = stanza;
            setType(Type.set);
        }
    }
}
