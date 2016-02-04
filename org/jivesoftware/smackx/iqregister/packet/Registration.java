package org.jivesoftware.smackx.iqregister.packet;

import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class Registration extends IQ {
    public final Map<String, String> attributes;
    private final String instructions;

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.halfOpenElement("query");
        xmlStringBuilder.xmlnsAttribute("jabber:iq:register");
        xmlStringBuilder.rightAngleBracket();
        xmlStringBuilder.optElement("instructions", this.instructions);
        if (this.attributes != null && this.attributes.size() > 0) {
            for (String str : this.attributes.keySet()) {
                xmlStringBuilder.element(str, (String) this.attributes.get(str));
            }
        }
        xmlStringBuilder.append(getExtensionsXML());
        xmlStringBuilder.closeElement("query");
        return xmlStringBuilder;
    }

    public Registration() {
        this(null);
    }

    public Registration(Map<String, String> attributes) {
        this(null, attributes);
    }

    public Registration(String instructions, Map<String, String> attributes) {
        this.instructions = instructions;
        this.attributes = attributes;
    }
}
