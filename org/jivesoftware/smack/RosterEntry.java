package org.jivesoftware.smack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.packet.RosterPacket.ItemStatus;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;

public final class RosterEntry {
    public final XMPPConnection connection;
    public String name;
    private final Roster roster;
    ItemStatus status;
    ItemType type;
    public String user;

    RosterEntry(String user, String name, ItemType type, ItemStatus status, Roster roster, XMPPConnection connection) {
        this.user = user;
        this.name = name;
        this.type = type;
        this.status = status;
        this.roster = roster;
        this.connection = connection;
    }

    public final Collection<RosterGroup> getGroups() {
        List<RosterGroup> results = new ArrayList();
        for (RosterGroup group : this.roster.getGroups()) {
            if (group.contains(this)) {
                results.add(group);
            }
        }
        return Collections.unmodifiableCollection(results);
    }

    public final String toString() {
        StringBuilder buf = new StringBuilder();
        if (this.name != null) {
            buf.append(this.name).append(": ");
        }
        buf.append(this.user);
        Collection<RosterGroup> groups = getGroups();
        if (!groups.isEmpty()) {
            buf.append(" [");
            Iterator<RosterGroup> iter = groups.iterator();
            buf.append(((RosterGroup) iter.next()).name);
            while (iter.hasNext()) {
                buf.append(", ");
                buf.append(((RosterGroup) iter.next()).name);
            }
            buf.append("]");
        }
        return buf.toString();
    }

    public final int hashCode() {
        return this.user == null ? 0 : this.user.hashCode();
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || !(object instanceof RosterEntry)) {
            return false;
        }
        return this.user.equals(((RosterEntry) object).user);
    }

    public static Item toRosterItem(RosterEntry entry) {
        Item item = new Item(entry.user, entry.name);
        item.itemType = entry.type;
        item.itemStatus = entry.status;
        for (RosterGroup group : entry.getGroups()) {
            item.addGroupName(group.name);
        }
        return item;
    }
}
