package org.jivesoftware.smackx.iqprivate.packet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class DefaultPrivateData implements PrivateData {
    private String elementName;
    private Map<String, String> map;
    private String namespace;

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(this.elementName).append(" xmlns=\"").append(this.namespace).append("\">");
        for (String str : getNames()) {
            String value = getValue(str);
            stringBuilder.append("<").append(str).append(">");
            stringBuilder.append(value);
            stringBuilder.append("</").append(str).append(">");
        }
        stringBuilder.append("</").append(this.elementName).append(">");
        return stringBuilder.toString();
    }

    public DefaultPrivateData(String elementName, String namespace) {
        this.elementName = elementName;
        this.namespace = namespace;
    }

    private synchronized Set<String> getNames() {
        Set<String> emptySet;
        if (this.map == null) {
            emptySet = Collections.emptySet();
        } else {
            emptySet = Collections.unmodifiableSet(this.map.keySet());
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
