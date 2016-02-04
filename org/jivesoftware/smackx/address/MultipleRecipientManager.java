package org.jivesoftware.smackx.address;

import org.jivesoftware.smack.packet.Packet;

public final class MultipleRecipientManager {

    private static class PacketCopy extends Packet {
        private CharSequence text;

        public final CharSequence toXML() {
            return this.text;
        }
    }
}
