package org.jivesoftware.smackx.bytestreams.ibb.packet;

import android.support.v7.appcompat.BuildConfig;
import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class DataPacketExtension implements PacketExtension {
    public final String data;
    public final long seq;
    public final String sessionID;

    public DataPacketExtension(String sessionID, long seq, String data) {
        if (sessionID == null || BuildConfig.VERSION_NAME.equals(sessionID)) {
            throw new IllegalArgumentException("Session ID must not be null or empty");
        } else if (seq < 0 || seq > 65535) {
            throw new IllegalArgumentException("Sequence must not be between 0 and 65535");
        } else if (data == null) {
            throw new IllegalArgumentException("Data must not be null");
        } else {
            this.sessionID = sessionID;
            this.seq = seq;
            this.data = data;
        }
    }

    public final String getElementName() {
        return "data";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/ibb";
    }

    public final XmlStringBuilder toXML() {
        XmlStringBuilder xml = new XmlStringBuilder((PacketExtension) this);
        xml.attribute("seq", Long.toString(this.seq));
        xml.attribute("sid", this.sessionID);
        xml.rightAngleBracket();
        xml.append(this.data);
        xml.closeElement((NamedElement) this);
        return xml;
    }
}
