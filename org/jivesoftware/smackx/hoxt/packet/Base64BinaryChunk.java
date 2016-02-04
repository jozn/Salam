package org.jivesoftware.smackx.hoxt.packet;

import org.jivesoftware.smack.packet.PacketExtension;

public final class Base64BinaryChunk implements PacketExtension {
    private final boolean last;
    private final int nr;
    private final String streamId;
    private final String text;

    public final /* bridge */ /* synthetic */ CharSequence toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<chunk xmlns='urn:xmpp:http' streamId='");
        stringBuilder.append(this.streamId);
        stringBuilder.append("' nr='");
        stringBuilder.append(this.nr);
        stringBuilder.append("' last='");
        stringBuilder.append(Boolean.toString(this.last));
        stringBuilder.append("'>");
        stringBuilder.append(this.text);
        stringBuilder.append("</chunk>");
        return stringBuilder.toString();
    }

    public Base64BinaryChunk(String text, String streamId, int nr, boolean last) {
        this.text = text;
        this.streamId = streamId;
        this.nr = nr;
        this.last = last;
    }

    public final String getElementName() {
        return "chunk";
    }

    public final String getNamespace() {
        return "urn:xmpp:http";
    }
}
