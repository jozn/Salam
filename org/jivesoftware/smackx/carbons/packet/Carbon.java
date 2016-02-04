package org.jivesoftware.smackx.carbons.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

public final class Carbon {

    public static class Disable extends IQ {
        public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
            return "<disable xmlns='urn:xmpp:carbons:2'/>";
        }

        public Disable() {
            setType(Type.set);
        }
    }

    public static class Enable extends IQ {
        public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
            return "<enable xmlns='urn:xmpp:carbons:2'/>";
        }

        public Enable() {
            setType(Type.set);
        }
    }
}
