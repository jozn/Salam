package org.jivesoftware.smack.filter;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;

public final class IQTypeFilter extends FlexiblePacketTypeFilter<IQ> {
    public static final PacketFilter ERROR;
    public static final PacketFilter GET;
    public static final PacketFilter RESULT;
    public static final PacketFilter SET;
    private final Type type;

    protected final /* bridge */ /* synthetic */ boolean acceptSpecific(Packet x0) {
        return ((IQ) x0).type.equals(this.type);
    }

    static {
        GET = new IQTypeFilter(Type.get);
        SET = new IQTypeFilter(Type.set);
        RESULT = new IQTypeFilter(Type.result);
        ERROR = new IQTypeFilter(Type.error);
    }

    private IQTypeFilter(Type type) {
        super(IQ.class);
        this.type = type;
    }
}
