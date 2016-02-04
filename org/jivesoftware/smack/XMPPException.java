package org.jivesoftware.smack;

import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.StreamError;
import org.jivesoftware.smack.packet.XMPPError;

public abstract class XMPPException extends Exception {

    public static class StreamErrorException extends XMPPException {
        final StreamError streamError;

        public StreamErrorException(StreamError streamError) {
            this.streamError = streamError;
        }

        public final String getMessage() {
            return this.streamError.toString();
        }

        public final String toString() {
            return getMessage();
        }
    }

    public static class XMPPErrorException extends XMPPException {
        public final XMPPError error;

        public XMPPErrorException(XMPPError error) {
            this.error = error;
        }

        public XMPPErrorException(String message, XMPPError error) {
            super(message);
            this.error = error;
        }

        public final String getMessage() {
            String superMessage = super.getMessage();
            return superMessage != null ? superMessage : this.error.toString();
        }

        public final String toString() {
            return getMessage();
        }

        public static void ifHasErrorThenThrow(Packet packet) throws XMPPErrorException {
            XMPPError xmppError = packet.error;
            if (xmppError != null) {
                throw new XMPPErrorException(xmppError);
            }
        }
    }

    protected XMPPException() {
    }

    public XMPPException(String message) {
        super(message);
    }
}
