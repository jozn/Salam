package org.jivesoftware.smack.parsing;

public final class ExceptionThrowingCallback extends ParsingExceptionCallback {
    public final void handleUnparsablePacket(UnparsablePacket packetData) throws Exception {
        throw packetData.f39e;
    }
}
