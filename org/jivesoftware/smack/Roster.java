package org.jivesoftware.smack;

import android.support.v7.appcompat.BuildConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.rosterstore.RosterStore;
import org.jxmpp.util.XmppStringUtils;

public class Roster {
    private static final Logger LOGGER;
    private static final PacketFilter PRESENCE_PACKET_FILTER;
    private static final PacketFilter ROSTER_PUSH_FILTER;
    private static int defaultSubscriptionMode$68076adb;
    public final XMPPConnection connection;
    public final Map<String, RosterEntry> entries;
    private final Map<String, RosterGroup> groups;
    private final Map<String, Map<String, Presence>> presenceMap;
    private final PresencePacketListener presencePacketListener;
    boolean rosterInitialized;
    public final List<RosterListener> rosterListeners;
    private final RosterStore rosterStore;
    public int subscriptionMode$68076adb;
    public final List<RosterEntry> unfiledEntries;

    /* renamed from: org.jivesoftware.smack.Roster.1 */
    class C12801 extends AbstractConnectionListener {
        C12801() {
        }

        public final void authenticated(XMPPConnection connection) {
            if (!connection.isAnonymous() && connection.isRosterLoadedAtLogin()) {
                try {
                    Roster.this.reload();
                } catch (SmackException e) {
                    Roster.LOGGER.log(Level.SEVERE, "Could not reload Roster", e);
                }
            }
        }

        public final void connectionClosed() {
            try {
                Roster.access$300(Roster.this);
            } catch (NotConnectedException e) {
                Roster.LOGGER.log(Level.SEVERE, "Not connected exception", e);
            }
        }

        public final void connectionClosedOnError(Exception e) {
            try {
                Roster.access$300(Roster.this);
            } catch (NotConnectedException e2) {
                Roster.LOGGER.log(Level.SEVERE, "Not connected exception", e);
            }
        }
    }

    /* renamed from: org.jivesoftware.smack.Roster.2 */
    class C12812 implements ExceptionCallback {
        C12812() {
        }

        public final void processException(Exception exception) {
            Roster.LOGGER.log(Level.SEVERE, "Exception reloading roster", exception);
        }
    }

    /* renamed from: org.jivesoftware.smack.Roster.3 */
    static /* synthetic */ class C12823 {
        static final /* synthetic */ int[] $SwitchMap$org$jivesoftware$smack$Roster$SubscriptionMode;

        static {
            $SwitchMap$org$jivesoftware$smack$Roster$SubscriptionMode = new int[SubscriptionMode.values$50356b61().length];
            try {
                $SwitchMap$org$jivesoftware$smack$Roster$SubscriptionMode[SubscriptionMode.accept_all$68076adb - 1] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$Roster$SubscriptionMode[SubscriptionMode.reject_all$68076adb - 1] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$Roster$SubscriptionMode[SubscriptionMode.manual$68076adb - 1] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private class PresencePacketListener implements PacketListener {
        private PresencePacketListener() {
        }

        public final void processPacket(Packet packet) throws NotConnectedException {
            Presence presence = (Presence) packet;
            String from = presence.from;
            String key = Roster.this.getPresenceMapKey(from);
            Map<String, Presence> userPresences;
            if (presence.type == Type.available) {
                if (Roster.this.presenceMap.get(key) == null) {
                    userPresences = new ConcurrentHashMap();
                    Roster.this.presenceMap.put(key, userPresences);
                } else {
                    userPresences = (Map) Roster.this.presenceMap.get(key);
                }
                userPresences.remove(BuildConfig.VERSION_NAME);
                userPresences.put(XmppStringUtils.parseResource(from), presence);
                if (((RosterEntry) Roster.this.entries.get(key)) != null) {
                    Roster.access$800(Roster.this, presence);
                }
            } else if (presence.type == Type.unavailable) {
                if (BuildConfig.VERSION_NAME.equals(XmppStringUtils.parseResource(from))) {
                    if (Roster.this.presenceMap.get(key) == null) {
                        userPresences = new ConcurrentHashMap();
                        Roster.this.presenceMap.put(key, userPresences);
                    } else {
                        userPresences = (Map) Roster.this.presenceMap.get(key);
                    }
                    userPresences.put(BuildConfig.VERSION_NAME, presence);
                } else if (Roster.this.presenceMap.get(key) != null) {
                    ((Map) Roster.this.presenceMap.get(key)).put(XmppStringUtils.parseResource(from), presence);
                }
                if (((RosterEntry) Roster.this.entries.get(key)) != null) {
                    Roster.access$800(Roster.this, presence);
                }
            } else if (presence.type == Type.subscribe) {
                response = null;
                switch (C12823.$SwitchMap$org$jivesoftware$smack$Roster$SubscriptionMode[Roster.this.subscriptionMode$68076adb - 1]) {
                    case org.eclipse.paho.client.mqttv3.logging.Logger.SEVERE /*1*/:
                        response = new Presence(Type.subscribed);
                        break;
                    case org.eclipse.paho.client.mqttv3.logging.Logger.WARNING /*2*/:
                        response = new Presence(Type.unsubscribed);
                        break;
                }
                if (response != null) {
                    response.to = presence.from;
                    Roster.this.connection.sendPacket(response);
                }
            } else if (presence.type == Type.unsubscribe) {
                if (Roster.this.subscriptionMode$68076adb != SubscriptionMode.manual$68076adb) {
                    response = new Presence(Type.unsubscribed);
                    response.to = presence.from;
                    Roster.this.connection.sendPacket(response);
                }
            } else if (presence.type == Type.error && BuildConfig.VERSION_NAME.equals(XmppStringUtils.parseResource(from))) {
                if (Roster.this.presenceMap.containsKey(key)) {
                    userPresences = (Map) Roster.this.presenceMap.get(key);
                    userPresences.clear();
                } else {
                    userPresences = new ConcurrentHashMap();
                    Roster.this.presenceMap.put(key, userPresences);
                }
                userPresences.put(BuildConfig.VERSION_NAME, presence);
                if (((RosterEntry) Roster.this.entries.get(key)) != null) {
                    Roster.access$800(Roster.this, presence);
                }
            }
        }
    }

    private class RosterPushListener implements PacketListener {
        private RosterPushListener() {
        }

        public final void processPacket(Packet packet) throws NotConnectedException {
            RosterPacket rosterPacket = (RosterPacket) packet;
            String str = rosterPacket.rosterVersion;
            String jid = XmppStringUtils.parseBareAddress(Roster.this.connection.getUser());
            if (rosterPacket.from == null || rosterPacket.from.equals(jid)) {
                Collection<Item> items = rosterPacket.getRosterItems();
                if (items.size() != 1) {
                    Roster.LOGGER.warning("Ignoring roster push with not exaclty one entry. size=" + items.size());
                    return;
                }
                Collection<String> addedEntries = new ArrayList();
                Collection<String> updatedEntries = new ArrayList();
                Collection<String> deletedEntries = new ArrayList();
                Collection<String> unchangedEntries = new ArrayList();
                Item item = (Item) items.iterator().next();
                RosterEntry entry = new RosterEntry(item.user, item.name, item.itemType, item.itemStatus, Roster.this, Roster.this.connection);
                if (item.itemType.equals(ItemType.remove)) {
                    Roster.access$1300(Roster.this, deletedEntries, entry);
                    if (Roster.this.rosterStore != null) {
                        Roster.this.rosterStore;
                    }
                } else if (Roster.access$1100(item)) {
                    Roster.access$1200(Roster.this, addedEntries, updatedEntries, unchangedEntries, item, entry);
                    if (Roster.this.rosterStore != null) {
                        Roster.this.rosterStore;
                    }
                }
                Roster.this.connection.sendPacket(IQ.createResultIQ(rosterPacket));
                Roster.access$1500(Roster.this);
                Roster.access$1600(Roster.this, addedEntries, updatedEntries, deletedEntries);
                return;
            }
            Roster.LOGGER.warning("Ignoring roster push with a non matching 'from' ourJid=" + jid + " from=" + rosterPacket.from);
        }
    }

    private class RosterResultListener implements PacketListener {
        private RosterResultListener() {
        }

        public final void processPacket(Packet packet) {
            Roster.LOGGER.fine("RosterResultListener received stanza");
            Collection<String> addedEntries = new ArrayList();
            Collection<String> updatedEntries = new ArrayList();
            Collection<String> deletedEntries = new ArrayList();
            Collection<String> unchangedEntries = new ArrayList();
            Iterator i$;
            Item item;
            if (packet instanceof RosterPacket) {
                RosterPacket rosterPacket = (RosterPacket) packet;
                String str = rosterPacket.rosterVersion;
                ArrayList<Item> validItems = new ArrayList();
                for (Item item2 : rosterPacket.getRosterItems()) {
                    if (Roster.access$1100(item2)) {
                        validItems.add(item2);
                    }
                }
                i$ = validItems.iterator();
                while (i$.hasNext()) {
                    item2 = (Item) i$.next();
                    Roster.access$1200(Roster.this, addedEntries, updatedEntries, unchangedEntries, item2, new RosterEntry(item2.user, item2.name, item2.itemType, item2.itemStatus, Roster.this, Roster.this.connection));
                }
                Set<String> toDelete = new HashSet();
                for (RosterEntry entry : Roster.this.entries.values()) {
                    toDelete.add(entry.user);
                }
                toDelete.removeAll(addedEntries);
                toDelete.removeAll(updatedEntries);
                toDelete.removeAll(unchangedEntries);
                for (String user : toDelete) {
                    Roster.access$1300(Roster.this, deletedEntries, (RosterEntry) Roster.this.entries.get(user));
                }
                if (Roster.this.rosterStore != null) {
                    Roster.this.rosterStore;
                }
                Roster.access$1500(Roster.this);
            } else {
                for (Item item22 : Roster.this.rosterStore.getEntries()) {
                    Roster.access$1200(Roster.this, addedEntries, updatedEntries, unchangedEntries, item22, new RosterEntry(item22.user, item22.name, item22.itemType, item22.itemStatus, Roster.this, Roster.this.connection));
                }
            }
            Roster.this.rosterInitialized = true;
            synchronized (Roster.this) {
                Roster.this.notifyAll();
            }
            Roster.access$1600(Roster.this, addedEntries, updatedEntries, deletedEntries);
        }
    }

    public enum SubscriptionMode {
        ;

        public static int[] values$50356b61() {
            return (int[]) $VALUES$6de25380.clone();
        }

        static {
            accept_all$68076adb = 1;
            reject_all$68076adb = 2;
            manual$68076adb = 3;
            $VALUES$6de25380 = new int[]{accept_all$68076adb, reject_all$68076adb, manual$68076adb};
        }
    }

    static /* synthetic */ boolean access$1100(Item x0) {
        return x0.itemType.equals(ItemType.none) || x0.itemType.equals(ItemType.from) || x0.itemType.equals(ItemType.to) || x0.itemType.equals(ItemType.both);
    }

    static /* synthetic */ void access$1200(Roster x0, Collection x1, Collection x2, Collection x3, Item x4, RosterEntry x5) {
        RosterEntry rosterEntry = (RosterEntry) x0.entries.put(x4.user, x5);
        if (rosterEntry == null) {
            x1.add(x4.user);
        } else {
            Object obj;
            Item toRosterItem = RosterEntry.toRosterItem(rosterEntry);
            if (rosterEntry != x5) {
                if (rosterEntry.getClass() != x5.getClass()) {
                    obj = null;
                } else {
                    RosterEntry rosterEntry2 = x5;
                    if (rosterEntry.name == null) {
                        if (rosterEntry2.name != null) {
                            obj = null;
                        }
                    } else if (!rosterEntry.name.equals(rosterEntry2.name)) {
                        obj = null;
                    }
                    if (rosterEntry.status == null) {
                        if (rosterEntry2.status != null) {
                            obj = null;
                        }
                    } else if (!rosterEntry.status.equals(rosterEntry2.status)) {
                        obj = null;
                    }
                    if (rosterEntry.type == null) {
                        if (rosterEntry2.type != null) {
                            obj = null;
                        }
                    } else if (!rosterEntry.type.equals(rosterEntry2.type)) {
                        obj = null;
                    }
                    if (rosterEntry.user == null) {
                        if (rosterEntry2.user != null) {
                            obj = null;
                        }
                    } else if (!rosterEntry.user.equals(rosterEntry2.user)) {
                        obj = null;
                    }
                }
                if (obj == null && Collections.unmodifiableSet(x4.groupNames).equals(Collections.unmodifiableSet(toRosterItem.groupNames))) {
                    x3.add(x4.user);
                } else {
                    x2.add(x4.user);
                }
            }
            obj = 1;
            if (obj == null) {
            }
            x2.add(x4.user);
        }
        if (Collections.unmodifiableSet(x4.groupNames).isEmpty()) {
            x0.unfiledEntries.remove(x5);
            x0.unfiledEntries.add(x5);
        } else {
            x0.unfiledEntries.remove(x5);
        }
        Collection arrayList = new ArrayList();
        for (String str : Collections.unmodifiableSet(x4.groupNames)) {
            arrayList.add(str);
            RosterGroup group = x0.getGroup(str);
            if (group == null) {
                group = x0.createGroup(str);
                x0.groups.put(str, group);
            }
            RosterGroup rosterGroup = group;
            synchronized (rosterGroup.entries) {
                rosterGroup.entries.remove(x5);
                rosterGroup.entries.add(x5);
            }
        }
        List<String> arrayList2 = new ArrayList();
        for (RosterGroup rosterGroup2 : x0.getGroups()) {
            arrayList2.add(rosterGroup2.name);
        }
        arrayList2.removeAll(arrayList);
        for (String str2 : arrayList2) {
            RosterGroup group2 = x0.getGroup(str2);
            group2.removeEntryLocal(x5);
            if (group2.getEntryCount() == 0) {
                x0.groups.remove(str2);
            }
        }
    }

    static /* synthetic */ void access$1300(Roster x0, Collection x1, RosterEntry x2) {
        String str = x2.user;
        x0.entries.remove(str);
        x0.unfiledEntries.remove(x2);
        x0.presenceMap.remove(XmppStringUtils.parseBareAddress(str));
        x1.add(str);
        for (Entry entry : x0.groups.entrySet()) {
            RosterGroup rosterGroup = (RosterGroup) entry.getValue();
            rosterGroup.removeEntryLocal(x2);
            if (rosterGroup.getEntryCount() == 0) {
                x0.groups.remove(entry.getKey());
            }
        }
    }

    static /* synthetic */ void access$1500(Roster x0) {
        for (RosterGroup rosterGroup : x0.getGroups()) {
            if (rosterGroup.getEntryCount() == 0) {
                x0.groups.remove(rosterGroup.name);
            }
        }
    }

    static /* synthetic */ void access$1600(Roster x0, Collection x1, Collection x2, Collection x3) {
        for (RosterListener rosterListener : x0.rosterListeners) {
            if (!x1.isEmpty()) {
                rosterListener.entriesAdded(x1);
            }
            if (!x2.isEmpty()) {
                rosterListener.entriesUpdated(x2);
            }
            if (!x3.isEmpty()) {
                rosterListener.entriesDeleted(x3);
            }
        }
    }

    static /* synthetic */ void access$300(Roster x0) throws NotConnectedException {
        for (String str : x0.presenceMap.keySet()) {
            Map map = (Map) x0.presenceMap.get(str);
            if (map != null) {
                for (String str2 : map.keySet()) {
                    Packet presence = new Presence(Type.unavailable);
                    presence.from = str + MqttTopic.TOPIC_LEVEL_SEPARATOR + str2;
                    x0.presencePacketListener.processPacket(presence);
                }
            }
        }
    }

    static /* synthetic */ void access$800(Roster x0, Presence x1) {
        for (RosterListener presenceChanged : x0.rosterListeners) {
            presenceChanged.presenceChanged(x1);
        }
    }

    static {
        LOGGER = Logger.getLogger(Roster.class.getName());
        ROSTER_PUSH_FILTER = new AndFilter(new PacketTypeFilter(RosterPacket.class), IQTypeFilter.SET);
        PRESENCE_PACKET_FILTER = new PacketTypeFilter(Presence.class);
        defaultSubscriptionMode$68076adb = SubscriptionMode.accept_all$68076adb;
    }

    Roster(XMPPConnection connection) {
        this.groups = new ConcurrentHashMap();
        this.entries = new ConcurrentHashMap();
        this.unfiledEntries = new CopyOnWriteArrayList();
        this.rosterListeners = new CopyOnWriteArrayList();
        this.presenceMap = new ConcurrentHashMap();
        this.rosterInitialized = false;
        this.presencePacketListener = new PresencePacketListener();
        this.subscriptionMode$68076adb = defaultSubscriptionMode$68076adb;
        this.connection = connection;
        this.rosterStore = connection.getRosterStore();
        connection.addPacketListener(new RosterPushListener(), ROSTER_PUSH_FILTER);
        connection.addPacketListener(this.presencePacketListener, PRESENCE_PACKET_FILTER);
        connection.addConnectionListener(new C12801());
        if (connection.isAuthenticated()) {
            try {
                reload();
            } catch (SmackException e) {
                LOGGER.log(Level.SEVERE, "Could not reload Roster", e);
            }
        }
    }

    public final void reload() throws NotLoggedInException, NotConnectedException {
        if (!this.connection.isAuthenticated()) {
            throw new NotLoggedInException();
        } else if (this.connection.isAnonymous()) {
            throw new IllegalStateException("Anonymous users can't have a roster.");
        } else {
            RosterPacket packet = new RosterPacket();
            if (this.rosterStore != null && this.connection.hasFeature("ver", "urn:xmpp:features:rosterver")) {
                packet.rosterVersion = this.rosterStore.getRosterVersion();
            }
            this.connection.sendIqWithResponseCallback(packet, new RosterResultListener(), new C12812());
        }
    }

    public final void removeRosterListener(RosterListener rosterListener) {
        this.rosterListeners.remove(rosterListener);
    }

    public final RosterGroup createGroup(String name) {
        if (this.connection.isAnonymous()) {
            throw new IllegalStateException("Anonymous users can't have a roster.");
        } else if (this.groups.containsKey(name)) {
            return (RosterGroup) this.groups.get(name);
        } else {
            RosterGroup group = new RosterGroup(name, this.connection);
            this.groups.put(name, group);
            return group;
        }
    }

    public final RosterEntry getEntry(String user) {
        if (user == null) {
            return null;
        }
        return (RosterEntry) this.entries.get(user.toLowerCase(Locale.US));
    }

    public final RosterGroup getGroup(String name) {
        return (RosterGroup) this.groups.get(name);
    }

    public final Collection<RosterGroup> getGroups() {
        return Collections.unmodifiableCollection(this.groups.values());
    }

    public final Presence getPresence(String user) {
        Map<String, Presence> userPresences = (Map) this.presenceMap.get(getPresenceMapKey(XmppStringUtils.parseBareAddress(user)));
        if (userPresences == null) {
            Presence presence = new Presence(Type.unavailable);
            presence.from = user;
            return presence;
        }
        presence = null;
        for (String resource : userPresences.keySet()) {
            Presence p = (Presence) userPresences.get(resource);
            if ((p.type == Type.available ? 1 : null) != null) {
                if (presence == null || p.priority > presence.priority) {
                    presence = p;
                } else if (p.priority == presence.priority) {
                    Mode pMode = p.mode;
                    if (pMode == null) {
                        pMode = Mode.available;
                    }
                    Mode presenceMode = presence.mode;
                    if (presenceMode == null) {
                        presenceMode = Mode.available;
                    }
                    if (pMode.compareTo(presenceMode) < 0) {
                        presence = p;
                    }
                }
            }
        }
        if (presence != null) {
            return presence;
        }
        presence = new Presence(Type.unavailable);
        presence.from = user;
        return presence;
    }

    private String getPresenceMapKey(String user) {
        if (user == null) {
            return null;
        }
        String key = user;
        if ((getEntry(user) != null ? 1 : null) == null) {
            key = XmppStringUtils.parseBareAddress(user);
        }
        return key.toLowerCase(Locale.US);
    }
}
