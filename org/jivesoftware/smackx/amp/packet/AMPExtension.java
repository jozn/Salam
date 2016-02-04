package org.jivesoftware.smackx.amp.packet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jivesoftware.smack.packet.PacketExtension;

public final class AMPExtension implements PacketExtension {
    private final String from;
    private boolean perHop;
    public CopyOnWriteArrayList<Rule> rules;
    private final Status status;
    private final String to;

    public interface Condition {
        String getName();

        String getValue();
    }

    public enum Action {
        alert,
        drop,
        error,
        notify
    }

    public static class Rule {
        final Action action;
        final Condition condition;

        public Rule(Action action, Condition condition) {
            if (action == null) {
                throw new NullPointerException("Can't create Rule with null action");
            } else if (condition == null) {
                throw new NullPointerException("Can't create Rule with null condition");
            } else {
                this.action = action;
                this.condition = condition;
            }
        }
    }

    public enum Status {
        alert,
        error,
        notify
    }

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<amp xmlns=\"http://jabber.org/protocol/amp\"");
        if (this.status != null) {
            stringBuilder.append(" status=\"").append(this.status.toString()).append("\"");
        }
        if (this.to != null) {
            stringBuilder.append(" to=\"").append(this.to).append("\"");
        }
        if (this.from != null) {
            stringBuilder.append(" from=\"").append(this.from).append("\"");
        }
        if (this.perHop) {
            stringBuilder.append(" per-hop=\"true\"");
        }
        stringBuilder.append(">");
        for (Rule rule : Collections.unmodifiableList(new ArrayList(this.rules))) {
            stringBuilder.append("<rule action=\"" + rule.action.toString() + "\" condition=\"" + rule.condition.getName() + "\" value=\"" + rule.condition.getValue() + "\"/>");
        }
        stringBuilder.append("</amp>");
        return stringBuilder.toString();
    }

    public AMPExtension(String from, String to, Status status) {
        this.rules = new CopyOnWriteArrayList();
        this.perHop = false;
        this.from = from;
        this.to = to;
        this.status = status;
    }

    public AMPExtension() {
        this.rules = new CopyOnWriteArrayList();
        this.perHop = false;
        this.from = null;
        this.to = null;
        this.status = null;
    }

    public final synchronized void setPerHop(boolean enabled) {
        this.perHop = enabled;
    }

    public final String getElementName() {
        return "amp";
    }

    public final String getNamespace() {
        return "http://jabber.org/protocol/amp";
    }
}
