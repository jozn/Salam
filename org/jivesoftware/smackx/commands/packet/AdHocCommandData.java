package org.jivesoftware.smackx.commands.packet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.commands.AdHocCommand.Action;
import org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition;
import org.jivesoftware.smackx.commands.AdHocCommand.Status;
import org.jivesoftware.smackx.commands.AdHocCommandNote;
import org.jivesoftware.smackx.xdata.packet.DataForm;

public class AdHocCommandData extends IQ {
    public Action action;
    public ArrayList<Action> actions;
    public Action executeAction;
    public DataForm form;
    public String id;
    public String name;
    public String node;
    public List<AdHocCommandNote> notes;
    public String sessionID;
    public Status status;

    public static class SpecificError implements PacketExtension {
        public SpecificErrorCondition condition;

        public final /* bridge */ /* synthetic */ CharSequence toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<").append(this.condition.toString());
            stringBuilder.append(" xmlns=\"http://jabber.org/protocol/commands\"/>");
            return stringBuilder.toString();
        }

        public SpecificError(SpecificErrorCondition condition) {
            this.condition = condition;
        }

        public final String getElementName() {
            return this.condition.toString();
        }

        public final String getNamespace() {
            return "http://jabber.org/protocol/commands";
        }
    }

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        Iterator it;
        CharSequence xmlStringBuilder = new XmlStringBuilder();
        xmlStringBuilder.halfOpenElement("command").xmlnsAttribute("http://jabber.org/protocol/commands");
        xmlStringBuilder.attribute("node", this.node);
        xmlStringBuilder.optAttribute("sessionid", this.sessionID);
        xmlStringBuilder.optAttribute(NotificationCompatApi21.CATEGORY_STATUS, this.status);
        xmlStringBuilder.optAttribute("action", this.action);
        xmlStringBuilder.rightAngleBracket();
        if (this.type == Type.result) {
            xmlStringBuilder.halfOpenElement("actions");
            xmlStringBuilder.optAttribute("execute", this.executeAction);
            if (this.actions.size() == 0) {
                xmlStringBuilder.closeEmptyElement();
            } else {
                xmlStringBuilder.rightAngleBracket();
                it = this.actions.iterator();
                while (it.hasNext()) {
                    xmlStringBuilder.emptyElement(((Action) it.next()).name());
                }
                xmlStringBuilder.closeElement("actions");
            }
        }
        if (this.form != null) {
            xmlStringBuilder.append(this.form.toXML());
        }
        for (AdHocCommandNote adHocCommandNote : this.notes) {
            xmlStringBuilder.halfOpenElement("note").attribute("type", adHocCommandNote.type.toString()).rightAngleBracket();
            xmlStringBuilder.append(adHocCommandNote.value);
            xmlStringBuilder.closeElement("note");
        }
        xmlStringBuilder.closeElement("command");
        return xmlStringBuilder;
    }

    public AdHocCommandData() {
        this.notes = new ArrayList();
        this.actions = new ArrayList();
    }

    public final void addAction(Action action) {
        this.actions.add(action);
    }

    public final void setSessionID(String sessionID) {
        if (StringUtils.isNullOrEmpty(sessionID)) {
            throw new IllegalArgumentException("session id must not be null or empty");
        }
        this.sessionID = sessionID;
    }
}
