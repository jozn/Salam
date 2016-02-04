package org.jivesoftware.smackx.ping;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.ping.packet.Ping;

public class PingManager extends Manager {
    private static final Map<XMPPConnection, PingManager> INSTANCES;
    private static final Logger LOGGER;
    private static final PacketFilter PING_PACKET_FILTER;
    private static int defaultPingInterval;
    private final ScheduledExecutorService executorService;
    private ScheduledFuture<?> nextAutomaticPing;
    private final Set<Object> pingFailedListeners;
    private int pingInterval;
    private final Runnable pingServerRunnable;

    /* renamed from: org.jivesoftware.smackx.ping.PingManager.1 */
    static class C13371 implements ConnectionCreationListener {
        C13371() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            PingManager.getInstanceFor(connection);
        }
    }

    /* renamed from: org.jivesoftware.smackx.ping.PingManager.2 */
    class C13382 implements PacketListener {
        C13382() {
        }

        public final void processPacket(Packet packet) throws NotConnectedException {
            PingManager.this.connection().sendPacket(IQ.createResultIQ((Ping) packet));
        }
    }

    /* renamed from: org.jivesoftware.smackx.ping.PingManager.3 */
    class C13393 extends AbstractConnectionListener {
        C13393() {
        }

        public final void authenticated(XMPPConnection connection) {
            PingManager.this.maybeSchedulePingServerTask(0);
        }

        public final void connectionClosed() {
            PingManager.this.maybeStopPingServerTask();
        }

        public final void connectionClosedOnError(Exception arg0) {
            PingManager.this.maybeStopPingServerTask();
        }
    }

    /* renamed from: org.jivesoftware.smackx.ping.PingManager.4 */
    class C13404 implements Runnable {
        C13404() {
        }

        public final void run() {
            PingManager.LOGGER.fine("ServerPingTask run()");
            PingManager.this.pingServerIfNecessary();
        }
    }

    private static class PingExecutorThreadFactory implements ThreadFactory {
        private final int connectionCounterValue;

        public PingExecutorThreadFactory(int connectionCounterValue) {
            this.connectionCounterValue = connectionCounterValue;
        }

        public final Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "Smack Scheduled Ping Executor Service (" + this.connectionCounterValue + ")");
            thread.setDaemon(true);
            return thread;
        }
    }

    static {
        LOGGER = Logger.getLogger(PingManager.class.getName());
        INSTANCES = Collections.synchronizedMap(new WeakHashMap());
        PING_PACKET_FILTER = new AndFilter(new PacketTypeFilter(Ping.class), IQTypeFilter.GET);
        XMPPConnectionRegistry.addConnectionCreationListener(new C13371());
        defaultPingInterval = 1800;
    }

    public static synchronized PingManager getInstanceFor(XMPPConnection connection) {
        PingManager pingManager;
        synchronized (PingManager.class) {
            pingManager = (PingManager) INSTANCES.get(connection);
            if (pingManager == null) {
                pingManager = new PingManager(connection);
            }
        }
        return pingManager;
    }

    private PingManager(XMPPConnection connection) {
        super(connection);
        this.pingFailedListeners = Collections.synchronizedSet(new HashSet());
        this.pingInterval = defaultPingInterval;
        this.pingServerRunnable = new C13404();
        this.executorService = new ScheduledThreadPoolExecutor(1, new PingExecutorThreadFactory(connection.getConnectionCounter()));
        ServiceDiscoveryManager.getInstanceFor(connection).addFeature("urn:xmpp:ping");
        INSTANCES.put(connection, this);
        connection.addPacketListener(new C13382(), PING_PACKET_FILTER);
        connection.addConnectionListener(new C13393());
        maybeSchedulePingServerTask(0);
    }

    private boolean ping(String jid, long pingTimeout) throws NotConnectedException, NoResponseException {
        XMPPConnection connection = connection();
        if (connection.isAuthenticated()) {
            try {
                connection.createPacketCollectorAndSend(new Ping(jid)).nextResultOrThrow(pingTimeout);
                return true;
            } catch (XMPPException e) {
                return jid.equals(connection().getServiceName());
            }
        }
        throw new NotConnectedException();
    }

    private boolean pingMyServer$25db0a9(long pingTimeout) throws NotConnectedException {
        try {
            return ping(connection().getServiceName(), pingTimeout);
        } catch (NoResponseException e) {
            return false;
        }
    }

    private synchronized void maybeSchedulePingServerTask(int delta) {
        maybeStopPingServerTask();
        if (this.pingInterval > 0) {
            int nextPingIn = this.pingInterval - delta;
            LOGGER.fine("Scheduling ServerPingTask in " + nextPingIn + " seconds (pingInterval=" + this.pingInterval + ", delta=" + delta + ")");
            this.nextAutomaticPing = this.executorService.schedule(this.pingServerRunnable, (long) nextPingIn, TimeUnit.SECONDS);
        }
    }

    private void maybeStopPingServerTask() {
        if (this.nextAutomaticPing != null) {
            this.nextAutomaticPing.cancel(true);
            this.nextAutomaticPing = null;
        }
    }

    public final synchronized void pingServerIfNecessary() {
        XMPPConnection connection = connection();
        if (connection != null) {
            if (this.pingInterval > 0) {
                long lastStanzaReceived = connection.getLastStanzaReceived();
                if (lastStanzaReceived > 0) {
                    int deltaInSeconds = (int) ((System.currentTimeMillis() - lastStanzaReceived) / 1000);
                    if (deltaInSeconds < this.pingInterval) {
                        maybeSchedulePingServerTask(deltaInSeconds);
                    }
                }
                if (connection.isAuthenticated()) {
                    boolean res = false;
                    for (int i = 0; i < 3; i++) {
                        if (i != 0) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                            }
                        }
                        try {
                            res = pingMyServer$25db0a9(connection().getPacketReplyTimeout());
                        } catch (SmackException e2) {
                            LOGGER.log(Level.WARNING, "SmackError while pinging server", e2);
                            res = false;
                        }
                        if (res) {
                            break;
                        }
                    }
                    if (res) {
                        maybeSchedulePingServerTask(0);
                    } else {
                        Iterator i$ = this.pingFailedListeners.iterator();
                        while (i$.hasNext()) {
                            i$.next();
                        }
                    }
                } else {
                    LOGGER.warning("XMPPConnection was not authenticated");
                }
            }
        }
    }

    protected void finalize() throws Throwable {
        try {
            this.executorService.shutdown();
        } finally {
            super.finalize();
        }
    }
}
