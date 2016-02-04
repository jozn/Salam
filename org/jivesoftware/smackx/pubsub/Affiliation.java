package org.jivesoftware.smackx.pubsub;

import org.jivesoftware.smack.packet.PacketExtension;

public final class Affiliation implements PacketExtension {
    protected String node;
    protected Type type;

    public enum Type {
        member,
        none,
        outcast,
        owner,
        publisher
    }

    public Affiliation(String nodeId, Type affiliation) {
        this.node = nodeId;
        this.type = affiliation;
    }

    public final String getElementName() {
        return "subscription";
    }

    public final String getNamespace() {
        return null;
    }

    public final String toXML() {
        StringBuilder builder = new StringBuilder("<");
        builder.append("subscription");
        appendAttribute(builder, "node", this.node);
        appendAttribute(builder, "affiliation", this.type.toString());
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
