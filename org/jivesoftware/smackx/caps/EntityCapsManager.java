package org.jivesoftware.smackx.caps;

import android.support.v7.appcompat.BuildConfig;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.NotFilter;
import org.jivesoftware.smack.filter.PacketExtensionFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.stringencoder.Base64;
import org.jivesoftware.smackx.caps.cache.EntityCapsPersistentCache;
import org.jivesoftware.smackx.caps.packet.CapsExtension;
import org.jivesoftware.smackx.disco.NodeInformationProvider;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo.Feature;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.disco.packet.DiscoverItems.Item;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jxmpp.util.cache.LruCache;

public class EntityCapsManager extends Manager {
    private static final LruCache<String, DiscoverInfo> CAPS_CACHE;
    private static String DEFAULT_ENTITY_NODE;
    private static final LruCache<String, NodeVerHash> JID_TO_NODEVER_CACHE;
    private static final Logger LOGGER;
    private static final PacketFilter PRESENCES;
    private static final PacketFilter PRESENCES_WITHOUT_CAPS;
    private static final PacketFilter PRESENCES_WITH_CAPS;
    private static final Map<String, MessageDigest> SUPPORTED_HASHES;
    private static boolean autoEnableEntityCaps;
    private static Map<XMPPConnection, EntityCapsManager> instances;
    protected static EntityCapsPersistentCache persistentCache;
    String currentCapsVersion;
    public boolean entityCapsEnabled;
    private String entityNode;
    private final Queue<String> lastLocalCapsVersions;
    private boolean presenceSend;
    private final ServiceDiscoveryManager sdm;

    /* renamed from: org.jivesoftware.smackx.caps.EntityCapsManager.1 */
    static class C13041 implements ConnectionCreationListener {
        C13041() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            EntityCapsManager.getInstanceFor(connection);
        }
    }

    /* renamed from: org.jivesoftware.smackx.caps.EntityCapsManager.2 */
    class C13052 extends AbstractConnectionListener {
        C13052() {
        }

        public final void connected(XMPPConnection connection) {
            C13052.processCapsStreamFeatureIfAvailable(connection);
        }

        public final void authenticated(XMPPConnection connection) {
            C13052.processCapsStreamFeatureIfAvailable(connection);
        }

        public final void connectionClosed() {
            EntityCapsManager.this.presenceSend = false;
        }

        public final void connectionClosedOnError(Exception e) {
            EntityCapsManager.this.presenceSend = false;
        }

        private static void processCapsStreamFeatureIfAvailable(XMPPConnection connection) {
            CapsExtension capsExtension = (CapsExtension) connection.getFeature("c", "http://jabber.org/protocol/caps");
            if (capsExtension != null) {
                EntityCapsManager.access$200(connection.getServiceName(), capsExtension);
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.caps.EntityCapsManager.3 */
    class C13063 implements PacketListener {
        C13063() {
        }

        public final void processPacket(Packet packet) {
            if (EntityCapsManager.this.entityCapsEnabled) {
                EntityCapsManager.access$200(packet.from, CapsExtension.from(packet));
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.caps.EntityCapsManager.4 */
    class C13074 implements PacketListener {
        C13074() {
        }

        public final void processPacket(Packet packet) {
            EntityCapsManager.JID_TO_NODEVER_CACHE.remove(packet.from);
        }
    }

    /* renamed from: org.jivesoftware.smackx.caps.EntityCapsManager.5 */
    class C13085 implements PacketListener {
        C13085() {
        }

        public final void processPacket(Packet packet) {
            EntityCapsManager.this.presenceSend = true;
        }
    }

    /* renamed from: org.jivesoftware.smackx.caps.EntityCapsManager.6 */
    class C13096 implements PacketInterceptor {
        C13096() {
        }

        public final void interceptPacket(Packet packet) {
            if (EntityCapsManager.this.entityCapsEnabled) {
                packet.addExtension(new CapsExtension(EntityCapsManager.this.entityNode, EntityCapsManager.this.currentCapsVersion, "sha-1"));
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.caps.EntityCapsManager.7 */
    class C13107 implements NodeInformationProvider {
        List<String> features;
        List<PacketExtension> packetExtensions;
        final /* synthetic */ List val$identities;

        C13107(List list) {
            this.val$identities = list;
            this.features = EntityCapsManager.this.sdm.getFeaturesList();
            ServiceDiscoveryManager access$600 = EntityCapsManager.this.sdm;
            List list2 = null;
            if (access$600.extendedInfo != null) {
                list2 = new ArrayList(1);
                list2.add(access$600.extendedInfo);
            }
            this.packetExtensions = list2;
        }

        public final List<Item> getNodeItems() {
            return null;
        }

        public final List<String> getNodeFeatures() {
            return this.features;
        }

        public final List<Identity> getNodeIdentities() {
            return this.val$identities;
        }

        public final List<PacketExtension> getNodePacketExtensions() {
            return this.packetExtensions;
        }
    }

    /* renamed from: org.jivesoftware.smackx.caps.EntityCapsManager.8 */
    static class C13118 implements Comparator<FormField> {
        C13118() {
        }

        public final /* bridge */ /* synthetic */ int compare(Object x0, Object x1) {
            return ((FormField) x0).variable.compareTo(((FormField) x1).variable);
        }
    }

    public static class NodeVerHash {
        private String hash;
        private String node;
        private String nodeVer;
        private String ver;

        NodeVerHash(String node, String ver, String hash) {
            this.node = node;
            this.ver = ver;
            this.hash = hash;
            this.nodeVer = node + MqttTopic.MULTI_LEVEL_WILDCARD + ver;
        }
    }

    static /* synthetic */ void access$200(String x0, CapsExtension x1) {
        String toLowerCase = x1.hash.toLowerCase(Locale.US);
        if (SUPPORTED_HASHES.containsKey(toLowerCase)) {
            JID_TO_NODEVER_CACHE.put(x0, new NodeVerHash(x1.node, x1.ver, toLowerCase));
        }
    }

    static {
        LOGGER = Logger.getLogger(EntityCapsManager.class.getName());
        SUPPORTED_HASHES = new HashMap();
        DEFAULT_ENTITY_NODE = "http://www.igniterealtime.org/projects/smack";
        autoEnableEntityCaps = true;
        instances = Collections.synchronizedMap(new WeakHashMap());
        PRESENCES_WITH_CAPS = new AndFilter(new PacketTypeFilter(Presence.class), new PacketExtensionFilter("c", "http://jabber.org/protocol/caps"));
        PRESENCES_WITHOUT_CAPS = new AndFilter(new PacketTypeFilter(Presence.class), new NotFilter(new PacketExtensionFilter("c", "http://jabber.org/protocol/caps")));
        PRESENCES = new PacketTypeFilter(Presence.class);
        CAPS_CACHE = new LruCache(1000);
        JID_TO_NODEVER_CACHE = new LruCache(10000);
        XMPPConnectionRegistry.addConnectionCreationListener(new C13041());
        try {
            SUPPORTED_HASHES.put("sha-1", MessageDigest.getInstance("SHA-1"));
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public static void setPersistentCache(EntityCapsPersistentCache cache) {
        persistentCache = cache;
    }

    private EntityCapsManager(XMPPConnection connection) {
        super(connection);
        this.lastLocalCapsVersions = new ConcurrentLinkedQueue();
        this.presenceSend = false;
        this.entityNode = DEFAULT_ENTITY_NODE;
        this.sdm = ServiceDiscoveryManager.getInstanceFor(connection);
        instances.put(connection, this);
        connection.addConnectionListener(new C13052());
        updateLocalEntityCaps();
        if (autoEnableEntityCaps) {
            enableEntityCaps();
        }
        connection.addPacketListener(new C13063(), PRESENCES_WITH_CAPS);
        connection.addPacketListener(new C13074(), PRESENCES_WITHOUT_CAPS);
        connection.addPacketSendingListener(new C13085(), PRESENCES);
        connection.addPacketInterceptor(new C13096(), PRESENCES);
        this.sdm.capsManager = this;
    }

    public static synchronized EntityCapsManager getInstanceFor(XMPPConnection connection) {
        EntityCapsManager entityCapsManager;
        synchronized (EntityCapsManager.class) {
            if (SUPPORTED_HASHES.size() <= 0) {
                throw new IllegalStateException("No supported hashes for EntityCapsManager");
            }
            entityCapsManager = (EntityCapsManager) instances.get(connection);
            if (entityCapsManager == null) {
                entityCapsManager = new EntityCapsManager(connection);
            }
        }
        return entityCapsManager;
    }

    private synchronized void enableEntityCaps() {
        this.sdm.addFeature("http://jabber.org/protocol/caps");
        updateLocalEntityCaps();
        this.entityCapsEnabled = true;
    }

    public final void updateLocalEntityCaps() {
        XMPPConnection connection = connection();
        DiscoverInfo discoverInfo = new DiscoverInfo();
        discoverInfo.setType(Type.result);
        discoverInfo.node = this.entityNode + '#' + this.currentCapsVersion;
        if (connection != null) {
            discoverInfo.from = connection.getUser();
        }
        this.sdm.addDiscoverInfoTo(discoverInfo);
        this.currentCapsVersion = generateVerificationString(discoverInfo, "sha-1");
        String str = this.entityNode + '#' + this.currentCapsVersion;
        CAPS_CACHE.put(str, discoverInfo);
        if (persistentCache != null) {
            persistentCache.addDiscoverInfoByNodePersistent(str, discoverInfo);
        }
        if (this.lastLocalCapsVersions.size() > 10) {
            String oldCapsVersion = (String) this.lastLocalCapsVersions.poll();
            ServiceDiscoveryManager serviceDiscoveryManager = this.sdm;
            serviceDiscoveryManager.nodeInformationProviders.remove(this.entityNode + '#' + oldCapsVersion);
        }
        this.lastLocalCapsVersions.add(this.currentCapsVersion);
        CAPS_CACHE.put(this.currentCapsVersion, discoverInfo);
        if (connection != null) {
            JID_TO_NODEVER_CACHE.put(connection.getUser(), new NodeVerHash(this.entityNode, this.currentCapsVersion, "sha-1"));
        }
        this.sdm.setNodeInformationProvider(this.entityNode + '#' + this.currentCapsVersion, new C13107(new LinkedList(ServiceDiscoveryManager.getInstanceFor(connection).getIdentities())));
        if (connection != null && connection.isAuthenticated() && this.presenceSend) {
            try {
                connection.sendPacket(new Presence(Presence.Type.available));
            } catch (NotConnectedException e) {
                LOGGER.log(Level.WARNING, "Could could not update presence with caps info", e);
            }
        }
    }

    private static String generateVerificationString(DiscoverInfo discoverInfo, String hash) {
        MessageDigest md = (MessageDigest) SUPPORTED_HASHES.get(hash.toLowerCase(Locale.US));
        if (md == null) {
            return null;
        }
        DataForm extendedInfo = DataForm.from(discoverInfo);
        StringBuilder sb = new StringBuilder();
        SortedSet<Identity> sortedIdentities = new TreeSet();
        for (Identity i : Collections.unmodifiableList(discoverInfo.identities)) {
            sortedIdentities.add(i);
        }
        for (Identity identity : sortedIdentities) {
            sb.append(identity.category);
            sb.append(MqttTopic.TOPIC_LEVEL_SEPARATOR);
            sb.append(identity.type);
            sb.append(MqttTopic.TOPIC_LEVEL_SEPARATOR);
            sb.append(identity.lang == null ? BuildConfig.VERSION_NAME : identity.lang);
            sb.append(MqttTopic.TOPIC_LEVEL_SEPARATOR);
            sb.append(identity.name == null ? BuildConfig.VERSION_NAME : identity.name);
            sb.append("<");
        }
        SortedSet<String> features = new TreeSet();
        for (Feature f : Collections.unmodifiableList(discoverInfo.features)) {
            features.add(f.variable);
        }
        for (String f2 : features) {
            sb.append(f2);
            sb.append("<");
        }
        if (extendedInfo != null && extendedInfo.hasHiddenFormTypeField()) {
            synchronized (extendedInfo) {
                SortedSet<FormField> fs = new TreeSet(new C13118());
                FormField ft = null;
                for (FormField f3 : extendedInfo.getFields()) {
                    if (f3.variable.equals("FORM_TYPE")) {
                        ft = f3;
                    } else {
                        fs.add(f3);
                    }
                }
                if (ft != null) {
                    formFieldValuesToCaps(ft.getValues(), sb);
                }
                for (FormField f32 : fs) {
                    sb.append(f32.variable);
                    sb.append("<");
                    formFieldValuesToCaps(f32.getValues(), sb);
                }
            }
        }
        return Base64.encodeToString(md.digest(sb.toString().getBytes()));
    }

    private static void formFieldValuesToCaps(List<String> i, StringBuilder sb) {
        SortedSet<String> fvs = new TreeSet();
        for (String s : i) {
            fvs.add(s);
        }
        for (String fv : fvs) {
            sb.append(fv);
            sb.append("<");
        }
    }
}
