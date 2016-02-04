package org.jivesoftware.smackx.disco.packet;

import android.support.v7.appcompat.BuildConfig;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class DiscoverInfo extends IQ implements Cloneable {
    public final List<Feature> features;
    public final List<Identity> identities;
    public String node;

    public static class Feature implements Cloneable {
        public final String variable;

        private Feature(Feature feature) {
            this.variable = feature.variable;
        }

        public Feature(String variable) {
            if (variable == null) {
                throw new IllegalArgumentException("variable cannot be null");
            }
            this.variable = variable;
        }

        public final boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            return this.variable.equals(((Feature) obj).variable);
        }

        public final int hashCode() {
            return this.variable.hashCode() * 37;
        }

        public final Feature clone() {
            return new Feature(this);
        }
    }

    public static class Identity implements Cloneable, Comparable<Identity> {
        public final String category;
        public String lang;
        public String name;
        public final String type;

        public final /* bridge */ /* synthetic */ int compareTo(Object x0) {
            Identity identity = (Identity) x0;
            String str = identity.lang == null ? BuildConfig.VERSION_NAME : identity.lang;
            String str2 = this.lang == null ? BuildConfig.VERSION_NAME : this.lang;
            String str3 = identity.type == null ? BuildConfig.VERSION_NAME : identity.type;
            String str4 = this.type == null ? BuildConfig.VERSION_NAME : this.type;
            if (!this.category.equals(identity.category)) {
                return this.category.compareTo(identity.category);
            }
            if (str4.equals(str3)) {
                return str2.equals(str) ? 0 : str2.compareTo(str);
            } else {
                return str4.compareTo(str3);
            }
        }

        private Identity(Identity identity) {
            this(identity.category, identity.name, identity.type);
            this.lang = identity.lang;
        }

        public Identity(String category, String name, String type) {
            if (category == null || type == null) {
                throw new IllegalArgumentException("category and type cannot be null");
            }
            this.category = category;
            this.name = name;
            this.type = type;
        }

        public final boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            Identity other = (Identity) obj;
            if (!this.category.equals(other.category)) {
                return false;
            }
            if (!(other.lang == null ? BuildConfig.VERSION_NAME : other.lang).equals(this.lang == null ? BuildConfig.VERSION_NAME : this.lang)) {
                return false;
            }
            if (!(other.type == null ? BuildConfig.VERSION_NAME : other.type).equals(this.type == null ? BuildConfig.VERSION_NAME : this.type)) {
                return false;
            }
            if ((this.name == null ? BuildConfig.VERSION_NAME : other.name).equals(other.name == null ? BuildConfig.VERSION_NAME : other.name)) {
                return true;
            }
            return false;
        }

        public final int hashCode() {
            int i = 0;
            int hashCode = (((((this.category.hashCode() + 37) * 37) + (this.lang == null ? 0 : this.lang.hashCode())) * 37) + (this.type == null ? 0 : this.type.hashCode())) * 37;
            if (this.name != null) {
                i = this.name.hashCode();
            }
            return hashCode + i;
        }

        public final Identity clone() {
            return new Identity(this);
        }
    }

    public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
        return new DiscoverInfo(this);
    }

    public DiscoverInfo() {
        this.features = new LinkedList();
        this.identities = new LinkedList();
    }

    private DiscoverInfo(DiscoverInfo d) {
        super(d);
        this.features = new LinkedList();
        this.identities = new LinkedList();
        this.node = d.node;
        for (Feature f : d.features) {
            addFeature(f.clone());
        }
        for (Identity i : d.identities) {
            addIdentity(i.clone());
        }
    }

    public final void addFeature(String feature) {
        addFeature(new Feature(feature));
    }

    private void addFeature(Feature feature) {
        this.features.add(feature);
    }

    public final void addIdentity(Identity identity) {
        this.identities.add(identity);
    }

    public final void addIdentities(Collection<Identity> identitiesToAdd) {
        if (identitiesToAdd != null) {
            this.identities.addAll(identitiesToAdd);
        }
    }

    public final CharSequence getChildElementXML() {
        XmlStringBuilder xml = new XmlStringBuilder();
        xml.halfOpenElement("query");
        xml.xmlnsAttribute("http://jabber.org/protocol/disco#info");
        xml.optAttribute("node", this.node);
        xml.rightAngleBracket();
        for (Identity identity : this.identities) {
            XmlStringBuilder xmlStringBuilder = new XmlStringBuilder();
            xmlStringBuilder.halfOpenElement("identity");
            xmlStringBuilder.xmllangAttribute(identity.lang);
            xmlStringBuilder.attribute("category", identity.category);
            xmlStringBuilder.optAttribute("name", identity.name);
            xmlStringBuilder.optAttribute("type", identity.type);
            xmlStringBuilder.closeEmptyElement();
            xml.append(xmlStringBuilder);
        }
        for (Feature feature : this.features) {
            xmlStringBuilder = new XmlStringBuilder();
            xmlStringBuilder.halfOpenElement("feature");
            xmlStringBuilder.attribute("var", feature.variable);
            xmlStringBuilder.closeEmptyElement();
            xml.append(xmlStringBuilder);
        }
        xml.append(getExtensionsXML());
        xml.closeElement("query");
        return xml;
    }
}
