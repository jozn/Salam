package org.jivesoftware.smack.packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.XmlStringBuilder;

public abstract class Packet extends TopLevelStreamElement {
    protected static final String DEFAULT_LANGUAGE;
    private static AtomicLong id;
    private static String prefix;
    public XMPPError error;
    public String from;
    protected String language;
    private final List<PacketExtension> packetExtensions;
    public String packetID;
    public String to;

    static {
        DEFAULT_LANGUAGE = Locale.getDefault().getLanguage().toLowerCase(Locale.US);
        prefix = StringUtils.randomString(5) + "-";
        id = new AtomicLong();
    }

    public Packet() {
        this(prefix + Long.toString(id.incrementAndGet()));
    }

    private Packet(String packetID) {
        this.packetID = null;
        this.to = null;
        this.from = null;
        this.packetExtensions = new CopyOnWriteArrayList();
        this.error = null;
        this.packetID = packetID;
    }

    public Packet(Packet p) {
        this.packetID = null;
        this.to = null;
        this.from = null;
        this.packetExtensions = new CopyOnWriteArrayList();
        this.error = null;
        this.packetID = p.packetID;
        this.to = p.to;
        this.from = p.from;
        this.error = p.error;
        for (PacketExtension pe : p.getExtensions()) {
            addExtension(pe);
        }
    }

    public final void setLanguage(String language) {
        this.language = language;
    }

    public final synchronized Collection<PacketExtension> getExtensions() {
        Collection<PacketExtension> emptyList;
        if (this.packetExtensions == null) {
            emptyList = Collections.emptyList();
        } else {
            emptyList = Collections.unmodifiableList(new ArrayList(this.packetExtensions));
        }
        return emptyList;
    }

    public final <PE extends PacketExtension> PE getExtension(String elementName, String namespace) {
        if (namespace == null) {
            return null;
        }
        for (PacketExtension packetExtension : this.packetExtensions) {
            if ((elementName == null || packetExtension.getElementName().equals(elementName)) && packetExtension.getNamespace().equals(namespace)) {
                return packetExtension;
            }
        }
        return null;
    }

    public final void addExtension(PacketExtension extension) {
        if (extension != null) {
            this.packetExtensions.add(extension);
        }
    }

    public final void addExtensions(Collection<PacketExtension> extensions) {
        if (extensions != null) {
            this.packetExtensions.addAll(extensions);
        }
    }

    public final synchronized CharSequence getExtensionsXML() {
        XmlStringBuilder xml;
        xml = new XmlStringBuilder();
        for (PacketExtension extension : getExtensions()) {
            xml.append(extension.toXML());
        }
        return xml;
    }

    public static String getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Packet packet = (Packet) o;
        if (this.error == null ? packet.error != null : !this.error.equals(packet.error)) {
            return false;
        }
        if (this.from == null ? packet.from != null : !this.from.equals(packet.from)) {
            return false;
        }
        if (!this.packetExtensions.equals(packet.packetExtensions)) {
            return false;
        }
        if (this.packetID == null ? packet.packetID != null : !this.packetID.equals(packet.packetID)) {
            return false;
        }
        if (this.to != null) {
            if (this.to.equals(packet.to)) {
                return true;
            }
        } else if (packet.to == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        if (this.packetID != null) {
            hashCode = this.packetID.hashCode();
        } else {
            hashCode = 0;
        }
        int i2 = (hashCode + 31) * 31;
        if (this.to != null) {
            hashCode = this.to.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.from != null) {
            hashCode = this.from.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (((i2 + hashCode) * 31) + this.packetExtensions.hashCode()) * 31;
        if (this.error != null) {
            i = this.error.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        return toXML().toString();
    }

    protected final void addCommonAttributes(XmlStringBuilder xml) {
        xml.optAttribute("id", this.packetID);
        xml.optAttribute("to", this.to);
        xml.optAttribute("from", this.from);
        xml.xmllangAttribute(this.language);
    }
}
