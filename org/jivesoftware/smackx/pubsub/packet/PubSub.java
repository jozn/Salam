package org.jivesoftware.smackx.pubsub.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class PubSub extends IQ {
    public PubSubNamespace ns;

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.halfOpenElement("pubsub").xmlnsAttribute(this.ns.getXmlns()).rightAngleBracket();
        xmlStringBuilder.append(getExtensionsXML());
        xmlStringBuilder.closeElement("pubsub");
        return xmlStringBuilder;
    }

    public PubSub() {
        this.ns = PubSubNamespace.BASIC;
    }
}
