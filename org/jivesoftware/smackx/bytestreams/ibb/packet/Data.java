package org.jivesoftware.smackx.bytestreams.ibb.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;

public class Data extends IQ {
    public final DataPacketExtension dataPacketExtension;

    public final /* bridge */ /* synthetic */ CharSequence getChildElementXML() {
        return this.dataPacketExtension.toXML();
    }

    public Data(DataPacketExtension data) {
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null");
        }
        this.dataPacketExtension = data;
        addExtension(data);
        setType(Type.set);
    }
}
