package org.jivesoftware.smack.packet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class DefaultPacketExtension implements PacketExtension {
    private String elementName;
    private Map<String, String> map;
    private String namespace;

    public DefaultPacketExtension(String elementName, String namespace) {
        this.elementName = elementName;
        this.namespace = namespace;
    }

    public final String getElementName() {
        return this.elementName;
    }

    public final String getNamespace() {
        return this.namespace;
    }

    public final CharSequence toXML() {
        XmlStringBuilder buf = new XmlStringBuilder();
        buf.halfOpenElement(this.elementName).xmlnsAttribute(this.namespace).rightAngleBracket();
        for (String name : getNames()) {
            buf.element(name, getValue(name));
        }
        buf.closeElement(this.elementName);
        return buf;
    }

    private synchronized Collection<String> getNames() {
        Collection<String> emptySet;
        if (this.map == null) {
            emptySet = Collections.emptySet();
        } else {
            emptySet = Collections.unmodifiableSet(new HashMap(this.map).keySet());
        }
        return emptySet;
    }

    private synchronized String getValue(String name) {
        String str;
        if (this.map == null) {
            str = null;
        } else {
            str = (String) this.map.get(name);
        }
        return str;
    }

    public final synchronized void setValue(String name, String value) {
        if (this.map == null) {
            this.map = new HashMap();
        }
        this.map.put(name, value);
    }
}
