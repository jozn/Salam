package org.jivesoftware.smackx.muc.packet;

import com.squareup.okhttp.internal.http.StatusLine;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.XmlStringBuilder;

public final class MUCUser implements PacketExtension {
    public Decline decline;
    public Destroy destroy;
    public Invite invite;
    public MUCItem item;
    public String password;
    public final Set<Status> statusCodes;

    public static class Decline implements NamedElement {
        public String from;
        public String reason;
        public String to;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            CharSequence xmlStringBuilder = new XmlStringBuilder((NamedElement) this);
            xmlStringBuilder.optAttribute("to", this.to);
            xmlStringBuilder.optAttribute("from", this.from);
            xmlStringBuilder.rightAngleBracket();
            xmlStringBuilder.optElement("reason", this.reason);
            xmlStringBuilder.closeElement((NamedElement) this);
            return xmlStringBuilder;
        }

        public final String getElementName() {
            return "decline";
        }
    }

    public static class Invite implements NamedElement {
        public String from;
        public String reason;
        public String to;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            CharSequence xmlStringBuilder = new XmlStringBuilder((NamedElement) this);
            xmlStringBuilder.optAttribute("to", this.to);
            xmlStringBuilder.optAttribute("from", this.from);
            xmlStringBuilder.rightAngleBracket();
            xmlStringBuilder.optElement("reason", this.reason);
            xmlStringBuilder.closeElement((NamedElement) this);
            return xmlStringBuilder;
        }

        public final String getElementName() {
            return "invite";
        }
    }

    public static class Status implements NamedElement {
        public static final Status BANNED_301;
        public static final Status KICKED_307;
        public static final Status NEW_NICKNAME_303;
        public static final Status REMOVED_AFFIL_CHANGE_321;
        public static final Status ROOM_CREATED_201;
        private static final Map<Integer, Status> statusMap;
        private final Integer code;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            CharSequence xmlStringBuilder = new XmlStringBuilder((NamedElement) this);
            xmlStringBuilder.attribute("code", String.valueOf(this.code.intValue()));
            xmlStringBuilder.closeEmptyElement();
            return xmlStringBuilder;
        }

        static {
            statusMap = new HashMap(8);
            ROOM_CREATED_201 = create(Integer.valueOf(201));
            BANNED_301 = create(Integer.valueOf(301));
            NEW_NICKNAME_303 = create(Integer.valueOf(303));
            KICKED_307 = create(Integer.valueOf(StatusLine.HTTP_TEMP_REDIRECT));
            REMOVED_AFFIL_CHANGE_321 = create(Integer.valueOf(321));
        }

        public static Status create(String string) {
            return create(Integer.valueOf(string));
        }

        private static Status create(Integer i) {
            Status status = (Status) statusMap.get(i);
            if (status != null) {
                return status;
            }
            status = new Status(i.intValue());
            statusMap.put(i, status);
            return status;
        }

        private Status(int code) {
            this.code = Integer.valueOf(code);
        }

        public final boolean equals(Object other) {
            if (other == null || !(other instanceof Status)) {
                return false;
            }
            return this.code.equals(Integer.valueOf(((Status) other).code.intValue()));
        }

        public final int hashCode() {
            return this.code.intValue();
        }

        public final String getElementName() {
            return NotificationCompatApi21.CATEGORY_STATUS;
        }
    }

    public MUCUser() {
        this.statusCodes = new HashSet(4);
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        CharSequence xmlStringBuilder = new XmlStringBuilder((PacketExtension) this);
        xmlStringBuilder.rightAngleBracket();
        xmlStringBuilder.optElement(this.invite);
        xmlStringBuilder.optElement(this.decline);
        xmlStringBuilder.optElement(this.item);
        xmlStringBuilder.optElement("password", this.password);
        for (Status status : this.statusCodes) {
            if (XmlStringBuilder.$assertionsDisabled || status != null) {
                xmlStringBuilder.append(status.toXML());
            } else {
                throw new AssertionError();
            }
        }
        xmlStringBuilder.optElement(this.destroy);
        xmlStringBuilder.closeElement((NamedElement) this);
        return xmlStringBuilder;
    }

    public final String getElementName() {
        return "x";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/muc#user";
    }

    @Deprecated
    public static MUCUser getFrom(Packet packet) {
        return (MUCUser) packet.getExtension("x", "http://jabber.org/protocol/muc#user");
    }
}
