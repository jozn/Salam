package org.jivesoftware.smackx.bytestreams.socks5;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.bytestreams.BytestreamListener;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream;

final class InitiationListener implements PacketListener {
    private static final Logger LOGGER;
    final PacketFilter initFilter;
    final ExecutorService initiationListenerExecutor;
    private final Socks5BytestreamManager manager;

    /* renamed from: org.jivesoftware.smackx.bytestreams.socks5.InitiationListener.1 */
    class C13001 implements Runnable {
        final /* synthetic */ Packet val$packet;

        C13001(Packet packet) {
            this.val$packet = packet;
        }

        public final void run() {
            try {
                InitiationListener.access$000(InitiationListener.this, this.val$packet);
            } catch (NotConnectedException e) {
                InitiationListener.LOGGER.log(Level.WARNING, "process request", e);
            }
        }
    }

    static /* synthetic */ void access$000(InitiationListener x0, Packet x1) throws NotConnectedException {
        Bytestream bytestream = (Bytestream) x1;
        if (!x0.manager.ignoredBytestreamRequests.remove(bytestream.sessionID)) {
            Socks5BytestreamRequest socks5BytestreamRequest = new Socks5BytestreamRequest(x0.manager, bytestream);
            Socks5BytestreamManager socks5BytestreamManager = x0.manager;
            if (((BytestreamListener) socks5BytestreamManager.userListeners.get(bytestream.from)) != null) {
                return;
            }
            if (x0.manager.allRequestListeners.isEmpty()) {
                socks5BytestreamManager = x0.manager;
                socks5BytestreamManager.connection.sendPacket(IQ.createErrorResponse(bytestream, new XMPPError(Condition.not_acceptable)));
                return;
            }
            Iterator it = x0.manager.allRequestListeners.iterator();
            while (it.hasNext()) {
                it.next();
            }
        }
    }

    static {
        LOGGER = Logger.getLogger(InitiationListener.class.getName());
    }

    protected InitiationListener(Socks5BytestreamManager manager) {
        this.initFilter = new AndFilter(new PacketTypeFilter(Bytestream.class), IQTypeFilter.SET);
        this.manager = manager;
        this.initiationListenerExecutor = Executors.newCachedThreadPool();
    }

    public final void processPacket(Packet packet) {
        this.initiationListenerExecutor.execute(new C13001(packet));
    }
}
