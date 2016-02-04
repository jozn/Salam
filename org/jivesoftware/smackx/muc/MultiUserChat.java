package org.jivesoftware.smackx.muc;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.disco.NodeInformationProvider;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.disco.packet.DiscoverItems.Item;
import org.jivesoftware.smackx.muc.packet.MUCInitialPresence;
import org.jivesoftware.smackx.muc.packet.MUCUser;
import org.jivesoftware.smackx.muc.packet.MUCUser.Status;

public class MultiUserChat {
    private static final Logger LOGGER;
    private static Map<XMPPConnection, List<String>> joinedRooms;
    public XMPPConnection connection;
    private List<PacketListener> connectionListeners;
    private final List<InvitationRejectionListener> invitationRejectionListeners;
    private boolean joined;
    private ConnectionDetachedPacketCollector messageCollector;
    private PacketFilter messageFilter;
    private String nickname;
    private Map<String, Presence> occupantsMap;
    private final List<ParticipantStatusListener> participantStatusListeners;
    private PacketFilter presenceFilter;
    private List<PacketInterceptor> presenceInterceptors;
    public String room;
    private RoomListenerMultiplexor roomListenerMultiplexor;
    private String subject;
    private final List<SubjectUpdatedListener> subjectUpdatedListeners;
    private final List<UserStatusListener> userStatusListeners;

    /* renamed from: org.jivesoftware.smackx.muc.MultiUserChat.1 */
    static class C13311 implements ConnectionCreationListener {

        /* renamed from: org.jivesoftware.smackx.muc.MultiUserChat.1.1 */
        class C13301 implements NodeInformationProvider {
            final /* synthetic */ WeakReference val$weakRefConnection;

            C13301(WeakReference weakReference) {
                this.val$weakRefConnection = weakReference;
            }

            public final List<Item> getNodeItems() {
                XMPPConnection connection = (XMPPConnection) this.val$weakRefConnection.get();
                if (connection == null) {
                    return new LinkedList();
                }
                List<Item> answer = new ArrayList();
                for (String room : MultiUserChat.access$000(connection)) {
                    answer.add(new Item(room));
                }
                return answer;
            }

            public final List<String> getNodeFeatures() {
                return null;
            }

            public final List<Identity> getNodeIdentities() {
                return null;
            }

            public final List<PacketExtension> getNodePacketExtensions() {
                return null;
            }
        }

        C13311() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            ServiceDiscoveryManager.getInstanceFor(connection).addFeature("http://jabber.org/protocol/muc");
            ServiceDiscoveryManager.getInstanceFor(connection).setNodeInformationProvider("http://jabber.org/protocol/muc#rooms", new C13301(new WeakReference(connection)));
        }
    }

    /* renamed from: org.jivesoftware.smackx.muc.MultiUserChat.3 */
    class C13323 implements PacketListener {
        C13323() {
        }

        public final void processPacket(Packet packet) {
            Message msg = (Message) packet;
            MultiUserChat.this.subject = msg.getSubject(null);
            MultiUserChat multiUserChat = MultiUserChat.this;
            msg.getSubject(null);
            String str = msg.from;
            MultiUserChat.access$200$42885ab1(multiUserChat);
        }
    }

    /* renamed from: org.jivesoftware.smackx.muc.MultiUserChat.4 */
    class C13334 implements PacketListener {
        C13334() {
        }

        public final void processPacket(Packet packet) {
            Presence presence = (Presence) packet;
            String from = presence.from;
            String myRoomJID = MultiUserChat.this.room + MqttTopic.TOPIC_LEVEL_SEPARATOR + MultiUserChat.this.nickname;
            boolean isUserStatusModification = presence.from.equals(myRoomJID);
            List<String> params;
            if (presence.type == Type.available) {
                if (((Presence) MultiUserChat.this.occupantsMap.put(from, presence)) != null) {
                    MUCUser mucExtension = MUCUser.getFrom(packet);
                    MUCAffiliation oldAffiliation = mucExtension.item.affiliation;
                    MUCRole oldRole = mucExtension.item.role;
                    mucExtension = MUCUser.getFrom(packet);
                    MUCAffiliation newAffiliation = mucExtension.item.affiliation;
                    MultiUserChat.access$600(MultiUserChat.this, oldRole, mucExtension.item.role, isUserStatusModification, from);
                    MultiUserChat.access$700(MultiUserChat.this, oldAffiliation, newAffiliation, isUserStatusModification, from);
                } else if (!isUserStatusModification) {
                    params = new ArrayList();
                    params.add(from);
                    MultiUserChat.this.fireParticipantStatusListeners("joined", params);
                }
            } else if (presence.type == Type.unavailable) {
                MultiUserChat.this.occupantsMap.remove(from);
                MUCUser mucUser = MUCUser.getFrom(packet);
                if (mucUser != null && mucUser.statusCodes != null) {
                    MultiUserChat.access$900(MultiUserChat.this, mucUser.statusCodes, presence.from.equals(myRoomJID), mucUser, from);
                } else if (!isUserStatusModification) {
                    params = new ArrayList();
                    params.add(from);
                    MultiUserChat.this.fireParticipantStatusListeners("left", params);
                }
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.muc.MultiUserChat.5 */
    class C13345 implements PacketListener {
        C13345() {
        }

        public final void processPacket(Packet packet) {
            if (MUCUser.getFrom(packet).decline != null && ((Message) packet).type != Message.Type.error) {
                MultiUserChat.access$1000$42885ab1(MultiUserChat.this);
            }
        }
    }

    private static class InvitationsMonitor extends AbstractConnectionListener {
        private static final Map<XMPPConnection, WeakReference<InvitationsMonitor>> monitors;
        XMPPConnection connection;
        PacketFilter invitationFilter;
        PacketListener invitationPacketListener;
        final List<InvitationListener> invitationsListeners;

        /* renamed from: org.jivesoftware.smackx.muc.MultiUserChat.InvitationsMonitor.1 */
        class C13351 implements PacketListener {
            C13351() {
            }

            public final void processPacket(Packet packet) {
                MUCUser mucUser = MUCUser.getFrom(packet);
                if (mucUser.invite != null && ((Message) packet).type != Message.Type.error) {
                    InvitationsMonitor.access$1100$5ea32d19(InvitationsMonitor.this, packet.from, mucUser.invite.reason);
                }
            }
        }

        static /* synthetic */ void access$1100$5ea32d19(InvitationsMonitor x0, String x1, String x3) {
            synchronized (x0.invitationsListeners) {
                InvitationListener[] invitationListenerArr = new InvitationListener[x0.invitationsListeners.size()];
                x0.invitationsListeners.toArray(invitationListenerArr);
            }
            for (InvitationListener invitationReceived$fdc76a0 : invitationListenerArr) {
                invitationReceived$fdc76a0.invitationReceived$fdc76a0(x1, x3);
            }
        }

        static {
            monitors = new WeakHashMap();
        }

        public static InvitationsMonitor getInvitationsMonitor(XMPPConnection conn) {
            synchronized (monitors) {
                if (!monitors.containsKey(conn) || ((WeakReference) monitors.get(conn)).get() == null) {
                    InvitationsMonitor ivm = new InvitationsMonitor(conn);
                    monitors.put(conn, new WeakReference(ivm));
                    return ivm;
                }
                InvitationsMonitor invitationsMonitor = (InvitationsMonitor) ((WeakReference) monitors.get(conn)).get();
                return invitationsMonitor;
            }
        }

        private InvitationsMonitor(XMPPConnection connection) {
            this.invitationsListeners = new ArrayList();
            this.connection = connection;
        }

        public final void connectionClosed() {
            this.connection.removePacketListener(this.invitationPacketListener);
            this.connection.removeConnectionListener(this);
        }
    }

    static /* synthetic */ List access$000(XMPPConnection x0) {
        List list = (List) joinedRooms.get(x0);
        return list != null ? list : Collections.emptyList();
    }

    static /* synthetic */ void access$1000$42885ab1(MultiUserChat x0) {
        synchronized (x0.invitationRejectionListeners) {
            x0.invitationRejectionListeners.toArray(new InvitationRejectionListener[x0.invitationRejectionListeners.size()]);
        }
    }

    static /* synthetic */ void access$200$42885ab1(MultiUserChat x0) {
        synchronized (x0.subjectUpdatedListeners) {
            x0.subjectUpdatedListeners.toArray(new SubjectUpdatedListener[x0.subjectUpdatedListeners.size()]);
        }
    }

    static /* synthetic */ void access$600(MultiUserChat x0, MUCRole x1, MUCRole x2, boolean x3, String x4) {
        List arrayList;
        if (("visitor".equals(x1) || "none".equals(x1)) && "participant".equals(x2)) {
            if (x3) {
                x0.fireUserStatusListeners("voiceGranted", new Object[0]);
            } else {
                arrayList = new ArrayList();
                arrayList.add(x4);
                x0.fireParticipantStatusListeners("voiceGranted", arrayList);
            }
        } else if ("participant".equals(x1) && ("visitor".equals(x2) || "none".equals(x2))) {
            if (x3) {
                x0.fireUserStatusListeners("voiceRevoked", new Object[0]);
            } else {
                arrayList = new ArrayList();
                arrayList.add(x4);
                x0.fireParticipantStatusListeners("voiceRevoked", arrayList);
            }
        }
        if (!"moderator".equals(x1) && "moderator".equals(x2)) {
            if ("visitor".equals(x1) || "none".equals(x1)) {
                if (x3) {
                    x0.fireUserStatusListeners("voiceGranted", new Object[0]);
                } else {
                    arrayList = new ArrayList();
                    arrayList.add(x4);
                    x0.fireParticipantStatusListeners("voiceGranted", arrayList);
                }
            }
            if (x3) {
                x0.fireUserStatusListeners("moderatorGranted", new Object[0]);
                return;
            }
            arrayList = new ArrayList();
            arrayList.add(x4);
            x0.fireParticipantStatusListeners("moderatorGranted", arrayList);
        } else if ("moderator".equals(x1) && !"moderator".equals(x2)) {
            if ("visitor".equals(x2) || "none".equals(x2)) {
                if (x3) {
                    x0.fireUserStatusListeners("voiceRevoked", new Object[0]);
                } else {
                    arrayList = new ArrayList();
                    arrayList.add(x4);
                    x0.fireParticipantStatusListeners("voiceRevoked", arrayList);
                }
            }
            if (x3) {
                x0.fireUserStatusListeners("moderatorRevoked", new Object[0]);
                return;
            }
            arrayList = new ArrayList();
            arrayList.add(x4);
            x0.fireParticipantStatusListeners("moderatorRevoked", arrayList);
        }
    }

    static /* synthetic */ void access$700(MultiUserChat x0, MUCAffiliation x1, MUCAffiliation x2, boolean x3, String x4) {
        List arrayList;
        if (!"owner".equals(x1) || "owner".equals(x2)) {
            if (!"admin".equals(x1) || "admin".equals(x2)) {
                if ("member".equals(x1) && !"member".equals(x2)) {
                    if (x3) {
                        x0.fireUserStatusListeners("membershipRevoked", new Object[0]);
                    } else {
                        arrayList = new ArrayList();
                        arrayList.add(x4);
                        x0.fireParticipantStatusListeners("membershipRevoked", arrayList);
                    }
                }
            } else if (x3) {
                x0.fireUserStatusListeners("adminRevoked", new Object[0]);
            } else {
                arrayList = new ArrayList();
                arrayList.add(x4);
                x0.fireParticipantStatusListeners("adminRevoked", arrayList);
            }
        } else if (x3) {
            x0.fireUserStatusListeners("ownershipRevoked", new Object[0]);
        } else {
            arrayList = new ArrayList();
            arrayList.add(x4);
            x0.fireParticipantStatusListeners("ownershipRevoked", arrayList);
        }
        if ("owner".equals(x1) || !"owner".equals(x2)) {
            if ("admin".equals(x1) || !"admin".equals(x2)) {
                if (!"member".equals(x1) && "member".equals(x2)) {
                    if (x3) {
                        x0.fireUserStatusListeners("membershipGranted", new Object[0]);
                        return;
                    }
                    arrayList = new ArrayList();
                    arrayList.add(x4);
                    x0.fireParticipantStatusListeners("membershipGranted", arrayList);
                }
            } else if (x3) {
                x0.fireUserStatusListeners("adminGranted", new Object[0]);
            } else {
                arrayList = new ArrayList();
                arrayList.add(x4);
                x0.fireParticipantStatusListeners("adminGranted", arrayList);
            }
        } else if (x3) {
            x0.fireUserStatusListeners("ownershipGranted", new Object[0]);
        } else {
            arrayList = new ArrayList();
            arrayList.add(x4);
            x0.fireParticipantStatusListeners("ownershipGranted", arrayList);
        }
    }

    static /* synthetic */ void access$900(MultiUserChat x0, Set x1, boolean x2, MUCUser x3, String x4) {
        if (x1.contains(Status.KICKED_307)) {
            if (x2) {
                x0.joined = false;
                x0.fireUserStatusListeners("kicked", new Object[]{x3.item.actor, x3.item.reason});
                x0.occupantsMap.clear();
                x0.nickname = null;
                x0.userHasLeft();
            } else {
                List arrayList = new ArrayList();
                arrayList.add(x4);
                arrayList.add(x3.item.actor);
                arrayList.add(x3.item.reason);
                x0.fireParticipantStatusListeners("kicked", arrayList);
            }
        }
        if (x1.contains(Status.BANNED_301)) {
            if (x2) {
                x0.joined = false;
                x0.fireUserStatusListeners("banned", new Object[]{x3.item.actor, x3.item.reason});
                x0.occupantsMap.clear();
                x0.nickname = null;
                x0.userHasLeft();
            } else {
                arrayList = new ArrayList();
                arrayList.add(x4);
                arrayList.add(x3.item.actor);
                arrayList.add(x3.item.reason);
                x0.fireParticipantStatusListeners("banned", arrayList);
            }
        }
        if (x1.contains(Status.REMOVED_AFFIL_CHANGE_321) && x2) {
            x0.joined = false;
            x0.fireUserStatusListeners("membershipRevoked", new Object[0]);
            x0.occupantsMap.clear();
            x0.nickname = null;
            x0.userHasLeft();
        }
        if (x1.contains(Status.NEW_NICKNAME_303)) {
            arrayList = new ArrayList();
            arrayList.add(x4);
            arrayList.add(x3.item.nick);
            x0.fireParticipantStatusListeners("nicknameChanged", arrayList);
        }
    }

    static {
        LOGGER = Logger.getLogger(MultiUserChat.class.getName());
        joinedRooms = new WeakHashMap();
        XMPPConnectionRegistry.addConnectionCreationListener(new C13311());
    }

    public MultiUserChat(XMPPConnection connection, String room) {
        this.nickname = null;
        this.joined = false;
        this.occupantsMap = new ConcurrentHashMap();
        this.invitationRejectionListeners = new ArrayList();
        this.subjectUpdatedListeners = new ArrayList();
        this.userStatusListeners = new ArrayList();
        this.participantStatusListeners = new ArrayList();
        this.presenceInterceptors = new ArrayList();
        this.connectionListeners = new ArrayList();
        this.connection = connection;
        this.room = room.toLowerCase(Locale.US);
        this.messageFilter = new AndFilter(FromMatchesFilter.create(this.room), MessageTypeFilter.GROUPCHAT);
        this.presenceFilter = new AndFilter(FromMatchesFilter.create(this.room), PacketTypeFilter.PRESENCE);
        this.messageCollector = new ConnectionDetachedPacketCollector();
        PacketListener c13323 = new C13323();
        PacketMultiplexListener packetMultiplexListener = new PacketMultiplexListener(this.messageCollector, new C13334(), c13323, new C13345());
        this.roomListenerMultiplexor = RoomListenerMultiplexor.getRoomMultiplexor(this.connection);
        RoomListenerMultiplexor roomListenerMultiplexor = this.roomListenerMultiplexor;
        String str = this.room;
        RoomMultiplexFilter roomMultiplexFilter = roomListenerMultiplexor.filter;
        if (str != null) {
            roomMultiplexFilter.roomAddressTable.put(str.toLowerCase(Locale.US), str);
        }
        RoomMultiplexListener roomMultiplexListener = roomListenerMultiplexor.listener;
        if (str != null) {
            roomMultiplexListener.roomListenersByAddress.put(str.toLowerCase(Locale.US), packetMultiplexListener);
        }
    }

    private Presence enter$324fb5b(String nickname, long timeout) throws NotConnectedException, NoResponseException, XMPPErrorException {
        if (StringUtils.isNullOrEmpty(nickname)) {
            throw new IllegalArgumentException("Nickname must not be null or blank.");
        }
        Presence joinPresence = new Presence(Type.available);
        joinPresence.to = this.room + MqttTopic.TOPIC_LEVEL_SEPARATOR + nickname;
        joinPresence.addExtension(new MUCInitialPresence());
        for (PacketInterceptor interceptPacket : this.presenceInterceptors) {
            interceptPacket.interceptPacket(joinPresence);
        }
        PacketCollector response = this.connection.createPacketCollector(new AndFilter(FromMatchesFilter.createFull(this.room + MqttTopic.TOPIC_LEVEL_SEPARATOR + nickname), new PacketTypeFilter(Presence.class)));
        this.connection.sendPacket(joinPresence);
        Presence presence = (Presence) response.nextResultOrThrow(timeout);
        this.nickname = nickname;
        this.joined = true;
        List<String> rooms = (List) joinedRooms.get(this.connection);
        if (rooms == null) {
            rooms = new ArrayList();
            joinedRooms.put(this.connection, rooms);
        }
        rooms.add(this.room);
        return presence;
    }

    public final synchronized void create(String nickname) throws NoResponseException, XMPPErrorException, SmackException {
        if (this.joined) {
            throw new IllegalStateException("Creation failed - User already joined the room.");
        } else if (!createOrJoin(nickname)) {
            leave();
            throw new SmackException("Creation failed - Missing acknowledge of room creation.");
        }
    }

    private synchronized boolean createOrJoin(String nickname) throws NoResponseException, XMPPErrorException, SmackException {
        boolean z;
        if (this.joined) {
            throw new IllegalStateException("Creation failed - User already joined the room.");
        }
        MUCUser mucUser = MUCUser.getFrom(enter$324fb5b(nickname, this.connection.getPacketReplyTimeout()));
        if (mucUser == null || !mucUser.statusCodes.contains(Status.ROOM_CREATED_201)) {
            z = false;
        } else {
            z = true;
        }
        return z;
    }

    private synchronized void leave() throws NotConnectedException {
        if (this.joined) {
            Presence leavePresence = new Presence(Type.unavailable);
            leavePresence.to = this.room + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.nickname;
            for (PacketInterceptor interceptPacket : this.presenceInterceptors) {
                interceptPacket.interceptPacket(leavePresence);
            }
            this.connection.sendPacket(leavePresence);
            this.occupantsMap.clear();
            this.nickname = null;
            this.joined = false;
            userHasLeft();
        }
    }

    public static void addInvitationListener(XMPPConnection conn, InvitationListener listener) {
        InvitationsMonitor invitationsMonitor = InvitationsMonitor.getInvitationsMonitor(conn);
        synchronized (invitationsMonitor.invitationsListeners) {
            if (invitationsMonitor.invitationsListeners.size() == 0) {
                invitationsMonitor.invitationFilter = new PacketExtensionFilter("x", "http://jabber.org/protocol/muc#user");
                invitationsMonitor.invitationPacketListener = new C13351();
                invitationsMonitor.connection.addPacketListener(invitationsMonitor.invitationPacketListener, invitationsMonitor.invitationFilter);
                invitationsMonitor.connection.addConnectionListener(invitationsMonitor);
            }
            if (!invitationsMonitor.invitationsListeners.contains(listener)) {
                invitationsMonitor.invitationsListeners.add(listener);
            }
        }
    }

    private synchronized void userHasLeft() {
        List<String> rooms = (List) joinedRooms.get(this.connection);
        if (rooms != null) {
            rooms.remove(this.room);
            cleanup();
        }
    }

    private void fireUserStatusListeners(String methodName, Object[] params) {
        synchronized (this.userStatusListeners) {
            UserStatusListener[] listeners = new UserStatusListener[this.userStatusListeners.size()];
            this.userStatusListeners.toArray(listeners);
        }
        Class<?>[] paramClasses = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            paramClasses[i] = params[i].getClass();
        }
        try {
            Method method = UserStatusListener.class.getDeclaredMethod(methodName, paramClasses);
            UserStatusListener[] arr$ = listeners;
            int len$ = listeners.length;
            for (int i$ = 0; i$ < len$; i$++) {
                method.invoke(arr$[i$], params);
            }
        } catch (NoSuchMethodException e) {
            LOGGER.log(Level.SEVERE, "Failed to invoke method on UserStatusListener", e);
        } catch (InvocationTargetException e2) {
            LOGGER.log(Level.SEVERE, "Failed to invoke method on UserStatusListener", e2);
        } catch (IllegalAccessException e3) {
            LOGGER.log(Level.SEVERE, "Failed to invoke method on UserStatusListener", e3);
        }
    }

    private void fireParticipantStatusListeners(String methodName, List<String> params) {
        synchronized (this.participantStatusListeners) {
            ParticipantStatusListener[] listeners = new ParticipantStatusListener[this.participantStatusListeners.size()];
            this.participantStatusListeners.toArray(listeners);
        }
        try {
            Class<?>[] classes = new Class[params.size()];
            for (int i = 0; i < params.size(); i++) {
                classes[i] = String.class;
            }
            Method method = ParticipantStatusListener.class.getDeclaredMethod(methodName, classes);
            ParticipantStatusListener[] arr$ = listeners;
            int len$ = listeners.length;
            for (int i$ = 0; i$ < len$; i$++) {
                method.invoke(arr$[i$], params.toArray());
            }
        } catch (NoSuchMethodException e) {
            LOGGER.log(Level.SEVERE, "Failed to invoke method on ParticipantStatusListener", e);
        } catch (InvocationTargetException e2) {
            LOGGER.log(Level.SEVERE, "Failed to invoke method on ParticipantStatusListener", e2);
        } catch (IllegalAccessException e3) {
            LOGGER.log(Level.SEVERE, "Failed to invoke method on ParticipantStatusListener", e3);
        }
    }

    private void cleanup() {
        try {
            if (this.connection != null) {
                RoomListenerMultiplexor roomListenerMultiplexor = this.roomListenerMultiplexor;
                String str = this.room;
                RoomMultiplexFilter roomMultiplexFilter = roomListenerMultiplexor.filter;
                if (str != null) {
                    roomMultiplexFilter.roomAddressTable.remove(str.toLowerCase(Locale.US));
                }
                RoomMultiplexListener roomMultiplexListener = roomListenerMultiplexor.listener;
                if (str != null) {
                    roomMultiplexListener.roomListenersByAddress.remove(str.toLowerCase(Locale.US));
                }
                for (PacketListener connectionListener : this.connectionListeners) {
                    this.connection.removePacketListener(connectionListener);
                }
            }
        } catch (Exception e) {
        }
    }

    protected void finalize() throws Throwable {
        cleanup();
        super.finalize();
    }
}
