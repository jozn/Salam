package org.jivesoftware.smack.compress.packet;

import java.util.List;
import org.jivesoftware.smack.packet.FullStreamElement;
import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class Compress extends FullStreamElement {
    public final String method;

    public static class Feature implements PacketExtension {
        public final List<String> methods;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
            xmlStringBuilder.rightAngleBracket();
            for (String element : this.methods) {
                xmlStringBuilder.element("method", element);
            }
            xmlStringBuilder.closeElement((NamedElement) this);
            return xmlStringBuilder;
        }

        public Feature(List<String> methods) {
            this.methods = methods;
        }

        public final String getNamespace() {
            return "http://jabber.org/protocol/compress";
        }

        public final String getElementName() {
            return "compression";
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.rightAngleBracket();
        xmlStringBuilder.element("method", this.method);
        xmlStringBuilder.closeElement((NamedElement) this);
        return xmlStringBuilder;
    }

    public Compress(String method) {
        this.method = method;
    }

    public final String getElementName() {
        return "compress";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/compress";
    }
}
