package org.jivesoftware.smack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class RosterGroup {
    public XMPPConnection connection;
    public final Set<RosterEntry> entries;
    public String name;

    RosterGroup(String name, XMPPConnection connection) {
        this.name = name;
        this.connection = connection;
        this.entries = new LinkedHashSet();
    }

    public final int getEntryCount() {
        int size;
        synchronized (this.entries) {
            size = this.entries.size();
        }
        return size;
    }

    public final Collection<RosterEntry> getEntries() {
        Collection unmodifiableList;
        synchronized (this.entries) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(this.entries));
        }
        return unmodifiableList;
    }

    public final boolean contains(RosterEntry entry) {
        boolean contains;
        synchronized (this.entries) {
            contains = this.entries.contains(entry);
        }
        return contains;
    }

    final void removeEntryLocal(RosterEntry entry) {
        synchronized (this.entries) {
            if (this.entries.contains(entry)) {
                this.entries.remove(entry);
            }
        }
    }
}
