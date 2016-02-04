package org.jivesoftware.smackx.privacy.packet;

public final class PrivacyItem {
    final boolean allow;
    public boolean filterIQ;
    public boolean filterMessage;
    public boolean filterPresenceIn;
    public boolean filterPresenceOut;
    final int order;
    final Type type;
    final String value;

    public enum Type {
        group,
        jid,
        subscription
    }

    public PrivacyItem(boolean allow, int order) {
        this(null, null, allow, order);
    }

    public PrivacyItem(Type type, String value, boolean allow, int order) {
        this.filterIQ = false;
        this.filterMessage = false;
        this.filterPresenceIn = false;
        this.filterPresenceOut = false;
        this.type = type;
        this.value = value;
        this.allow = allow;
        this.order = order;
    }
}
