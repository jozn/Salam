package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.packet.IQ.Type;

public class Session extends IQ {

    public static class Feature implements PacketExtension {
        public static final Feature INSTANCE;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            return "<session xmlns='urn:ietf:params:xml:ns:xmpp-session'/>";
        }

        static {
            INSTANCE = new Feature();
        }

        private Feature() {
        }

        public final String getElementName() {
            return "session";
        }

        public final String getNamespace() {
            return "urn:ietf:params:xml:ns:xmpp-session";
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        return "<session xmlns='urn:ietf:params:xml:ns:xmpp-session'/>";
    }

    public Session() {
        setType(Type.set);
    }
}
