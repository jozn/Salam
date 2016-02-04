package org.jivesoftware.smackx.pep.packet;

import org.jivesoftware.smack.packet.PacketExtension;

public abstract class PEPItem implements PacketExtension {
    String id;

    abstract String getItemDetailsXML();

    abstract String getNode();
}
