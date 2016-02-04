package org.jivesoftware.smack.debugger;

import java.io.Reader;
import java.io.Writer;
import org.jivesoftware.smack.PacketListener;

public interface SmackDebugger {
    PacketListener getReaderListener();

    PacketListener getWriterListener();

    Reader newConnectionReader$32745501();

    Writer newConnectionWriter$1bc0ff();
}
