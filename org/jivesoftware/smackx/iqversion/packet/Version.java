package org.jivesoftware.smackx.iqversion.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class Version extends IQ {
    private final String name;
    private String os;
    private final String version;

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.halfOpenElement("query").xmlnsAttribute("jabber:iq:version").rightAngleBracket();
        xmlStringBuilder.optElement("name", this.name);
        xmlStringBuilder.optElement("version", this.version);
        xmlStringBuilder.optElement("os", this.os);
        xmlStringBuilder.closeElement("query");
        return xmlStringBuilder;
    }

    public Version() {
        this.name = null;
        this.version = null;
        setType(Type.get);
    }

    public Version(String name, String version, String os) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        } else if (version == null) {
            throw new IllegalArgumentException("version must not be null");
        } else {
            setType(Type.result);
            this.name = name;
            this.version = version;
            this.os = os;
        }
    }

    private Version(Version original) {
        this(original.name, original.version, original.os);
    }

    public static Version createResultFor(Packet request, Version version) {
        Version result = new Version(version);
        result.packetID = request.packetID;
        result.to = request.from;
        return result;
    }
}
