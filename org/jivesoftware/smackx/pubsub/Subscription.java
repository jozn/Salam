package org.jivesoftware.smackx.pubsub;

public final class Subscription extends NodeExtension {
    protected boolean configRequired;
    protected String id;
    protected String jid;
    protected State state;

    public enum State {
        subscribed,
        unconfigured,
        pending,
        none
    }

    public Subscription(String jid, String nodeId, String subscriptionId, State state, boolean configRequired) {
        super(PubSubElementType.SUBSCRIPTION, nodeId);
        this.configRequired = false;
        this.jid = jid;
        this.id = subscriptionId;
        this.state = state;
        this.configRequired = configRequired;
    }

    public final String toXML() {
        StringBuilder builder = new StringBuilder("<subscription");
        appendAttribute(builder, "jid", this.jid);
        if (this.node != null) {
            appendAttribute(builder, "node", this.node);
        }
        if (this.id != null) {
            appendAttribute(builder, "subid", this.id);
        }
        if (this.state != null) {
            appendAttribute(builder, "subscription", this.state.toString());
        }
        builder.append("/>");
        return builder.toString();
    }

    private static void appendAttribute(StringBuilder builder, String att, String value) {
        builder.append(" ");
        builder.append(att);
        builder.append("='");
        builder.append(value);
        builder.append("'");
    }
}
