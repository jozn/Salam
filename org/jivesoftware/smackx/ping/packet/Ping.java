package org.jivesoftware.smackx.ping.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

public class Ping extends IQ {
    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        return "<ping xmlns='urn:xmpp:ping'/>";
    }

    public Ping(String to) {
        this.to = to;
        setType(Type.get);
    }
}
