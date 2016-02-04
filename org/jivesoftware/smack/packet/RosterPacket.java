package org.jivesoftware.smack.packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.util.XmlStringBuilder;

public class RosterPacket extends IQ {
    private final List<Item> rosterItems;
    public String rosterVersion;

    public static class Item {
        public final Set<String> groupNames;
        public ItemStatus itemStatus;
        public ItemType itemType;
        public String name;
        public String user;

        public Item(String user, String name) {
            this.user = user.toLowerCase(Locale.US);
            this.name = name;
            this.itemType = null;
            this.itemStatus = null;
            this.groupNames = new CopyOnWriteArraySet();
        }

        public final void addGroupName(String groupName) {
            this.groupNames.add(groupName);
        }

        public final void removeGroupName(String groupName) {
            this.groupNames.remove(groupName);
        }

        public final int hashCode() {
            int i = 0;
            int hashCode = ((((((((this.groupNames == null ? 0 : this.groupNames.hashCode()) + 31) * 31) + (this.itemStatus == null ? 0 : this.itemStatus.hashCode())) * 31) + (this.itemType == null ? 0 : this.itemType.hashCode())) * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31;
            if (this.user != null) {
                i = this.user.hashCode();
            }
            return hashCode + i;
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Item other = (Item) obj;
            if (this.groupNames == null) {
                if (other.groupNames != null) {
                    return false;
                }
            } else if (!this.groupNames.equals(other.groupNames)) {
                return false;
            }
            if (this.itemStatus != other.itemStatus) {
                return false;
            }
            if (this.itemType != other.itemType) {
                return false;
            }
            if (this.name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!this.name.equals(other.name)) {
                return false;
            }
            if (this.user == null) {
                if (other.user != null) {
                    return false;
                }
                return true;
            } else if (this.user.equals(other.user)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public enum ItemStatus {
        subscribe,
        unsubscribe;
        
        public static final ItemStatus SUBSCRIPTION_PENDING;
        public static final ItemStatus UNSUBSCRIPTION_PENDING;

        static {
            SUBSCRIPTION_PENDING = subscribe;
            UNSUBSCRIPTION_PENDING = unsubscribe;
        }

        public static ItemStatus fromString(String s) {
            ItemStatus itemStatus = null;
            if (s != null) {
                try {
                    itemStatus = valueOf(s);
                } catch (IllegalArgumentException e) {
                }
            }
            return itemStatus;
        }
    }

    public enum ItemType {
        none,
        to,
        from,
        both,
        remove
    }

    public RosterPacket() {
        this.rosterItems = new ArrayList();
    }

    public final void addRosterItem(Item item) {
        synchronized (this.rosterItems) {
            this.rosterItems.add(item);
        }
    }

    public final Collection<Item> getRosterItems() {
        Collection unmodifiableList;
        synchronized (this.rosterItems) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(this.rosterItems));
        }
        return unmodifiableList;
    }

    private XmlStringBuilder getChildElementXML() {
        XmlStringBuilder buf = new XmlStringBuilder();
        buf.halfOpenElement("query");
        buf.xmlnsAttribute("jabber:iq:roster");
        buf.optAttribute("ver", this.rosterVersion);
        buf.rightAngleBracket();
        synchronized (this.rosterItems) {
            for (Item entry : this.rosterItems) {
                XmlStringBuilder xmlStringBuilder = new XmlStringBuilder();
                xmlStringBuilder.halfOpenElement("item").attribute("jid", entry.user);
                xmlStringBuilder.optAttribute("name", entry.name);
                xmlStringBuilder.optAttribute("subscription", entry.itemType);
                xmlStringBuilder.optAttribute("ask", entry.itemStatus);
                xmlStringBuilder.rightAngleBracket();
                for (String escape : entry.groupNames) {
                    xmlStringBuilder.openElement("group").escape(escape).closeElement("group");
                }
                xmlStringBuilder.closeElement("item");
                buf.append(xmlStringBuilder);
            }
        }
        buf.closeElement("query");
        return buf;
    }
}
