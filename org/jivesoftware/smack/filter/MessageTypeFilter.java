package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;

public final class MessageTypeFilter extends FlexiblePacketTypeFilter<Message> {
    public static final PacketFilter CHAT;
    public static final PacketFilter ERROR;
    public static final PacketFilter GROUPCHAT;
    public static final PacketFilter HEADLINE;
    public static final PacketFilter NORMAL;
    private final Type type;

    protected final /* bridge */ /* synthetic */ boolean acceptSpecific(Packet x0) {
        return ((Message) x0).type.equals(this.type);
    }

    static {
        NORMAL = new MessageTypeFilter(Type.normal);
        CHAT = new MessageTypeFilter(Type.chat);
        GROUPCHAT = new MessageTypeFilter(Type.groupchat);
        HEADLINE = new MessageTypeFilter(Type.headline);
        ERROR = new MessageTypeFilter(Type.error);
    }

    private MessageTypeFilter(Type type) {
        super(Message.class);
        this.type = type;
    }
}
