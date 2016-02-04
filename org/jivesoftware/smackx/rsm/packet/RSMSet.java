package org.jivesoftware.smackx.rsm.packet;

import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class RSMSet implements PacketExtension {
    private final String after;
    private final String before;
    private final int count;
    private final int firstIndex;
    private final String firstString;
    private final int index;
    private final String last;
    private final int max;

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.rightAngleBracket();
        xmlStringBuilder.optElement("after", this.after);
        xmlStringBuilder.optElement("before", this.before);
        xmlStringBuilder.optIntElement("count", this.count);
        if (this.firstString != null) {
            xmlStringBuilder.halfOpenElement("first");
            xmlStringBuilder.optIntAttribute("index", this.firstIndex);
            xmlStringBuilder.rightAngleBracket();
            xmlStringBuilder.append(this.firstString);
            xmlStringBuilder.closeElement("first");
        }
        xmlStringBuilder.optIntElement("index", this.index);
        xmlStringBuilder.optElement("last", this.last);
        xmlStringBuilder.optIntElement("max", this.max);
        xmlStringBuilder.closeElement((NamedElement) this);
        return xmlStringBuilder;
    }

    public RSMSet(String after, String before, int count, int index, String last, int max, String firstString, int firstIndex) {
        this.after = after;
        this.before = before;
        this.count = count;
        this.index = index;
        this.last = last;
        this.max = max;
        this.firstString = firstString;
        this.firstIndex = firstIndex;
    }

    public final String getElementName() {
        return "set";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/rsm";
    }
}
