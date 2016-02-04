package org.jivesoftware.smack.rosterstore;

import java.util.Collection;
import org.jivesoftware.smack.packet.RosterPacket.Item;

public interface RosterStore {
    Collection<Item> getEntries();

    String getRosterVersion();
}
