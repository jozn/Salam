package org.jivesoftware.smackx.muc.packet;

import org.jivesoftware.smack.packet.NamedElement;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.muc.MUCAffiliation;
import org.jivesoftware.smackx.muc.MUCRole;

public final class MUCItem implements NamedElement {
    public final String actor;
    public final MUCAffiliation affiliation;
    private final String jid;
    public final String nick;
    public final String reason;
    public final MUCRole role;

    public MUCItem(MUCAffiliation affiliation, MUCRole role, String actor, String reason, String jid, String nick) {
        this.affiliation = affiliation;
        this.role = role;
        this.actor = actor;
        this.reason = reason;
        this.jid = jid;
        this.nick = nick;
    }

    public final XmlStringBuilder toXML() {
        XmlStringBuilder xml = new XmlStringBuilder((NamedElement) this);
        xml.optAttribute("affiliation", this.affiliation);
        xml.optAttribute("jid", this.jid);
        xml.optAttribute("nick", this.nick);
        if (this.role != MUCRole.none) {
            xml.attribute("role", this.role);
        }
        xml.rightAngleBracket();
        xml.optElement("reason", this.reason);
        if (this.actor != null) {
            xml.halfOpenElement("actor").attribute("jid", this.actor).closeEmptyElement();
        }
        xml.closeElement("item");
        return xml;
    }

    public final String getElementName() {
        return "item";
    }
}
