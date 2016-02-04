package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class Bind extends IQ {
    public final String jid;
    private final String resource;

    public static class Feature implements PacketExtension {
        public static final Feature INSTANCE;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            return "<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/>";
        }

        static {
            INSTANCE = new Feature();
        }

        private Feature() {
        }

        public final String getElementName() {
            return "bind";
        }

        public final String getNamespace() {
            return "urn:ietf:params:xml:ns:xmpp-bind";
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.halfOpenElement("bind").xmlnsAttribute("urn:ietf:params:xml:ns:xmpp-bind").rightAngleBracket();
        xmlStringBuilder.optElement("resource", this.resource);
        xmlStringBuilder.optElement("jid", this.jid);
        xmlStringBuilder.closeElement("bind");
        return xmlStringBuilder;
    }

    private Bind(String resource, String jid) {
        this.resource = resource;
        this.jid = jid;
    }

    public static Bind newSet(String resource) {
        Bind bind = new Bind(resource, null);
        bind.setType(Type.set);
        return bind;
    }

    public static Bind newResult(String jid) {
        return new Bind(null, jid);
    }
}
