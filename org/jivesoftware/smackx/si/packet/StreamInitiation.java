package org.jivesoftware.smackx.si.packet;

import java.util.Date;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jxmpp.util.XmppDateTime;

public class StreamInitiation extends IQ {
    public Feature featureNegotiation;
    public File file;
    public String id;
    public String mimeType;

    public class Feature implements PacketExtension {
        private final DataForm data;

        public Feature(DataForm data) {
            this.data = data;
        }

        public final String getNamespace() {
            return "http://jabber.org/protocol/feature-neg";
        }

        public final String getElementName() {
            return "feature";
        }

        public final String toXML() {
            StringBuilder buf = new StringBuilder();
            buf.append("<feature xmlns=\"http://jabber.org/protocol/feature-neg\">");
            buf.append(this.data.toXML());
            buf.append("</feature>");
            return buf.toString();
        }
    }

    public static class File implements PacketExtension {
        public Date date;
        public String desc;
        public String hash;
        public boolean isRanged;
        private final String name;
        private final long size;

        public File(String name, long size) {
            if (name == null) {
                throw new NullPointerException("name cannot be null");
            }
            this.name = name;
            this.size = size;
        }

        public final String getElementName() {
            return "file";
        }

        public final String getNamespace() {
            return "http://jabber.org/protocol/si/profile/file-transfer";
        }

        public final String toXML() {
            StringBuilder buffer = new StringBuilder();
            buffer.append("<file xmlns=\"http://jabber.org/protocol/si/profile/file-transfer\" ");
            if (this.name != null) {
                buffer.append("name=\"").append(StringUtils.escapeForXML(this.name)).append("\" ");
            }
            if (this.size > 0) {
                buffer.append("size=\"").append(this.size).append("\" ");
            }
            if (this.date != null) {
                buffer.append("date=\"").append(XmppDateTime.formatXEP0082Date(this.date)).append("\" ");
            }
            if (this.hash != null) {
                buffer.append("hash=\"").append(this.hash).append("\" ");
            }
            if ((this.desc == null || this.desc.length() <= 0) && !this.isRanged) {
                buffer.append("/>");
            } else {
                buffer.append(">");
                if (this.desc != null && this.desc.length() > 0) {
                    buffer.append("<desc>").append(StringUtils.escapeForXML(this.desc)).append("</desc>");
                }
                if (this.isRanged) {
                    buffer.append("<range/>");
                }
                buffer.append("</file>");
            }
            return buffer.toString();
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.type.equals(Type.set)) {
            stringBuilder.append("<si xmlns=\"http://jabber.org/protocol/si\" ");
            if (this.id != null) {
                stringBuilder.append("id=\"").append(this.id).append("\" ");
            }
            if (this.mimeType != null) {
                stringBuilder.append("mime-type=\"").append(this.mimeType).append("\" ");
            }
            stringBuilder.append("profile=\"http://jabber.org/protocol/si/profile/file-transfer\">");
            String toXML = this.file.toXML();
            if (toXML != null) {
                stringBuilder.append(toXML);
            }
        } else if (this.type.equals(Type.result)) {
            stringBuilder.append("<si xmlns=\"http://jabber.org/protocol/si\">");
        } else {
            throw new IllegalArgumentException("IQ Type not understood");
        }
        if (this.featureNegotiation != null) {
            stringBuilder.append(this.featureNegotiation.toXML());
        }
        stringBuilder.append("</si>");
        return stringBuilder.toString();
    }
}
