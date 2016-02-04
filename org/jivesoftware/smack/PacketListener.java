package org.jivesoftware.smack;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Packet;

public interface PacketListener {
    void processPacket(Packet packet) throws NotConnectedException;
}
