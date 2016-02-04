package org.jivesoftware.smack.parsing;

public final class UnparsablePacket {
    private final CharSequence content;
    final Exception f39e;

    public UnparsablePacket(CharSequence content, Exception e) {
        this.content = content;
        this.f39e = e;
    }
}
