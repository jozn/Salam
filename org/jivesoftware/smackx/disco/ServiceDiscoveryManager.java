package org.jivesoftware.smackx.disco;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.caps.EntityCapsManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.disco.packet.DiscoverItems.Item;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jxmpp.util.cache.Cache;
import org.jxmpp.util.cache.ExpirationCache;

public class ServiceDiscoveryManager extends Manager {
    private static final PacketFilter GET_DISCOVER_INFO;
    private static final PacketFilter GET_DISCOVER_ITEMS;
    private static final Logger LOGGER;
    private static Identity defaultIdentity;
    private static Map<XMPPConnection, ServiceDiscoveryManager> instances;
    public EntityCapsManager capsManager;
    public DataForm extendedInfo;
    public final Set<String> features;
    private Set<Identity> identities;
    private Identity identity;
    public Map<String, NodeInformationProvider> nodeInformationProviders;
    private Cache<String, List<String>> services;

    /* renamed from: org.jivesoftware.smackx.disco.ServiceDiscoveryManager.1 */
    static class C13181 implements ConnectionCreationListener {
        C13181() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            ServiceDiscoveryManager.getInstanceFor(connection);
        }
    }

    /* renamed from: org.jivesoftware.smackx.disco.ServiceDiscoveryManager.2 */
    class C13192 implements PacketListener {
        C13192() {
        }

        public final void processPacket(Packet packet) throws NotConnectedException {
            DiscoverItems discoverItems = (DiscoverItems) packet;
            DiscoverItems response = new DiscoverItems();
            response.setType(Type.result);
            response.to = discoverItems.from;
            response.packetID = discoverItems.packetID;
            response.node = discoverItems.node;
            NodeInformationProvider nodeInformationProvider = ServiceDiscoveryManager.access$000(ServiceDiscoveryManager.this, discoverItems.node);
            if (nodeInformationProvider != null) {
                Collection<Item> nodeItems = nodeInformationProvider.getNodeItems();
                if (nodeItems != null) {
                    for (Item addItem : nodeItems) {
                        response.addItem(addItem);
                    }
                }
                response.addExtensions(nodeInformationProvider.getNodePacketExtensions());
            } else if (discoverItems.node != null) {
                response.setType(Type.error);
                response.error = new XMPPError(Condition.item_not_found);
            }
            ServiceDiscoveryManager.this.connection().sendPacket(response);
        }
    }

    /* renamed from: org.jivesoftware.smackx.disco.ServiceDiscoveryManager.3 */
    class C13203 implements PacketListener {
        C13203() {
        }

        public final void processPacket(Packet packet) throws NotConnectedException {
            DiscoverInfo discoverInfo = (DiscoverInfo) packet;
            DiscoverInfo response = new DiscoverInfo();
            response.setType(Type.result);
            response.to = discoverInfo.from;
            response.packetID = discoverInfo.packetID;
            response.node = discoverInfo.node;
            if (discoverInfo.node == null) {
                ServiceDiscoveryManager.this.addDiscoverInfoTo(response);
            } else {
                NodeInformationProvider nodeInformationProvider = ServiceDiscoveryManager.access$000(ServiceDiscoveryManager.this, discoverInfo.node);
                if (nodeInformationProvider != null) {
                    Collection<String> nodeFeatures = nodeInformationProvider.getNodeFeatures();
                    if (nodeFeatures != null) {
                        for (String addFeature : nodeFeatures) {
                            response.addFeature(addFeature);
                        }
                    }
                    response.addIdentities(nodeInformationProvider.getNodeIdentities());
                    response.addExtensions(nodeInformationProvider.getNodePacketExtensions());
                } else {
                    response.setType(Type.error);
                    response.error = new XMPPError(Condition.item_not_found);
                }
            }
            ServiceDiscoveryManager.this.connection().sendPacket(response);
        }
    }

    static /* synthetic */ NodeInformationProvider access$000(ServiceDiscoveryManager x0, String x1) {
        return x1 == null ? null : (NodeInformationProvider) x0.nodeInformationProviders.get(x1);
    }

    static {
        LOGGER = Logger.getLogger(ServiceDiscoveryManager.class.getName());
        GET_DISCOVER_ITEMS = new AndFilter(IQTypeFilter.GET, new PacketTypeFilter(DiscoverItems.class));
        GET_DISCOVER_INFO = new AndFilter(IQTypeFilter.GET, new PacketTypeFilter(DiscoverInfo.class));
        defaultIdentity = new Identity("client", "Smack", "pc");
        instances = Collections.synchronizedMap(new WeakHashMap());
        XMPPConnectionRegistry.addConnectionCreationListener(new C13181());
    }

    public static void setDefaultIdentity(Identity identity) {
        defaultIdentity = identity;
    }

    private ServiceDiscoveryManager(XMPPConnection connection) {
        super(connection);
        this.identities = new HashSet();
        this.identity = defaultIdentity;
        this.features = new HashSet();
        this.extendedInfo = null;
        this.nodeInformationProviders = new ConcurrentHashMap();
        this.services = new ExpirationCache(25, 86400000);
        addFeature("http://jabber.org/protocol/disco#info");
        addFeature("http://jabber.org/protocol/disco#items");
        connection.addPacketListener(new C13192(), GET_DISCOVER_ITEMS);
        connection.addPacketListener(new C13203(), GET_DISCOVER_INFO);
    }

    public final Set<Identity> getIdentities() {
        Set<Identity> res = new HashSet(this.identities);
        res.add(defaultIdentity);
        return Collections.unmodifiableSet(res);
    }

    public static synchronized ServiceDiscoveryManager getInstanceFor(XMPPConnection connection) {
        ServiceDiscoveryManager sdm;
        synchronized (ServiceDiscoveryManager.class) {
            sdm = (ServiceDiscoveryManager) instances.get(connection);
            if (sdm == null) {
                sdm = new ServiceDiscoveryManager(connection);
                instances.put(connection, sdm);
            }
        }
        return sdm;
    }

    public final void addDiscoverInfoTo(DiscoverInfo response) {
        response.addIdentities(getIdentities());
        synchronized (this.features) {
            for (String feature : getFeatures()) {
                response.addFeature(feature);
            }
            response.addExtension(this.extendedInfo);
        }
    }

    public final void setNodeInformationProvider(String node, NodeInformationProvider listener) {
        this.nodeInformationProviders.put(node, listener);
    }

    private List<String> getFeatures() {
        List<String> unmodifiableList;
        synchronized (this.features) {
            unmodifiableList = Collections.unmodifiableList(new ArrayList(this.features));
        }
        return unmodifiableList;
    }

    public final List<String> getFeaturesList() {
        List linkedList;
        synchronized (this.features) {
            linkedList = new LinkedList(this.features);
        }
        return linkedList;
    }

    public final void addFeature(String feature) {
        synchronized (this.features) {
            if (!this.features.contains(feature)) {
                this.features.add(feature);
                renewEntityCapsVersion();
            }
        }
    }

    public final boolean includesFeature(String feature) {
        boolean contains;
        synchronized (this.features) {
            contains = this.features.contains(feature);
        }
        return contains;
    }

    public final void renewEntityCapsVersion() {
        if (this.capsManager != null && this.capsManager.entityCapsEnabled) {
            this.capsManager.updateLocalEntityCaps();
        }
    }
}
