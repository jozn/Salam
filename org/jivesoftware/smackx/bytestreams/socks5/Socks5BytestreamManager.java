package org.jivesoftware.smackx.bytestreams.socks5;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smackx.bytestreams.BytestreamListener;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;

public final class Socks5BytestreamManager {
    private static final Map<XMPPConnection, Socks5BytestreamManager> managers;
    private static final Random randomGenerator;
    final List<BytestreamListener> allRequestListeners;
    final XMPPConnection connection;
    List<String> ignoredBytestreamRequests;
    private final InitiationListener initiationListener;
    private String lastWorkingProxy;
    private final List<String> proxyBlacklist;
    private int proxyConnectionTimeout;
    private boolean proxyPrioritizationEnabled;
    private int targetResponseTimeout;
    final Map<String, BytestreamListener> userListeners;

    /* renamed from: org.jivesoftware.smackx.bytestreams.socks5.Socks5BytestreamManager.1 */
    static class C13021 implements ConnectionCreationListener {

        /* renamed from: org.jivesoftware.smackx.bytestreams.socks5.Socks5BytestreamManager.1.1 */
        class C13011 extends AbstractConnectionListener {
            final /* synthetic */ XMPPConnection val$connection;

            C13011(XMPPConnection xMPPConnection) {
                this.val$connection = xMPPConnection;
            }

            public final void connectionClosed() {
                Socks5BytestreamManager.getBytestreamManager(this.val$connection).disableService();
            }

            public final void connectionClosedOnError(Exception e) {
                Socks5BytestreamManager.getBytestreamManager(this.val$connection).disableService();
            }

            public final void reconnectionSuccessful() {
                Socks5BytestreamManager.getBytestreamManager(this.val$connection);
            }
        }

        C13021() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            Socks5BytestreamManager.getBytestreamManager(connection);
            connection.addConnectionListener(new C13011(connection));
        }
    }

    static {
        XMPPConnectionRegistry.addConnectionCreationListener(new C13021());
        randomGenerator = new Random();
        managers = new HashMap();
    }

    public static synchronized Socks5BytestreamManager getBytestreamManager(XMPPConnection connection) {
        Socks5BytestreamManager socks5BytestreamManager;
        synchronized (Socks5BytestreamManager.class) {
            if (connection == null) {
                socks5BytestreamManager = null;
            } else {
                socks5BytestreamManager = (Socks5BytestreamManager) managers.get(connection);
                if (socks5BytestreamManager == null) {
                    socks5BytestreamManager = new Socks5BytestreamManager(connection);
                    managers.put(connection, socks5BytestreamManager);
                    socks5BytestreamManager.connection.addPacketListener(socks5BytestreamManager.initiationListener, socks5BytestreamManager.initiationListener.initFilter);
                    ServiceDiscoveryManager.getInstanceFor(socks5BytestreamManager.connection).addFeature("http://jabber.org/protocol/bytestreams");
                }
            }
        }
        return socks5BytestreamManager;
    }

    private Socks5BytestreamManager(XMPPConnection connection) {
        this.userListeners = new ConcurrentHashMap();
        this.allRequestListeners = Collections.synchronizedList(new LinkedList());
        this.targetResponseTimeout = 10000;
        this.proxyConnectionTimeout = 10000;
        this.proxyBlacklist = Collections.synchronizedList(new LinkedList());
        this.lastWorkingProxy = null;
        this.proxyPrioritizationEnabled = true;
        this.ignoredBytestreamRequests = Collections.synchronizedList(new LinkedList());
        this.connection = connection;
        this.initiationListener = new InitiationListener(this);
    }

    public final synchronized void disableService() {
        this.connection.removePacketListener(this.initiationListener);
        this.initiationListener.initiationListenerExecutor.shutdownNow();
        this.allRequestListeners.clear();
        this.userListeners.clear();
        this.lastWorkingProxy = null;
        this.proxyBlacklist.clear();
        this.ignoredBytestreamRequests.clear();
        managers.remove(this.connection);
        if (managers.size() == 0) {
            Socks5Proxy.getSocks5Proxy().stop();
        }
        ServiceDiscoveryManager serviceDiscoveryManager = ServiceDiscoveryManager.getInstanceFor(this.connection);
        if (serviceDiscoveryManager != null) {
            String str = "http://jabber.org/protocol/bytestreams";
            synchronized (serviceDiscoveryManager.features) {
                serviceDiscoveryManager.features.remove(str);
                serviceDiscoveryManager.renewEntityCapsVersion();
            }
        }
    }
}
