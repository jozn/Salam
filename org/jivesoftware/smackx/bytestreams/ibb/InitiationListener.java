package org.jivesoftware.smackx.bytestreams.ibb;

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
import org.jivesoftware.smackx.bytestreams.ibb.packet.Open;

class InitiationListener implements PacketListener {
    private static final Logger LOGGER;
    final PacketFilter initFilter;
    final ExecutorService initiationListenerExecutor;
    private final InBandBytestreamManager manager;

    /* renamed from: org.jivesoftware.smackx.bytestreams.ibb.InitiationListener.1 */
    class C12991 implements Runnable {
        final /* synthetic */ Packet val$packet;

        C12991(Packet packet) {
            this.val$packet = packet;
        }

        public final void run() {
            try {
                InitiationListener.access$000(InitiationListener.this, this.val$packet);
            } catch (NotConnectedException e) {
                InitiationListener.LOGGER.log(Level.WARNING, "proccessRequest", e);
            }
        }
    }

    static /* synthetic */ void access$000(InitiationListener x0, Packet x1) throws NotConnectedException {
        Open open = (Open) x1;
        InBandBytestreamManager inBandBytestreamManager;
        if (open.blockSize > x0.manager.maximumBlockSize) {
            inBandBytestreamManager = x0.manager;
            inBandBytestreamManager.connection.sendPacket(IQ.createErrorResponse(open, new XMPPError(Condition.resource_constraint)));
        } else if (!x0.manager.ignoredBytestreamRequests.remove(open.sessionID)) {
            InBandBytestreamRequest inBandBytestreamRequest = new InBandBytestreamRequest(x0.manager, open);
            inBandBytestreamManager = x0.manager;
            if (((BytestreamListener) inBandBytestreamManager.userListeners.get(open.from)) != null) {
                return;
            }
            if (x0.manager.allRequestListeners.isEmpty()) {
                inBandBytestreamManager = x0.manager;
                inBandBytestreamManager.connection.sendPacket(IQ.createErrorResponse(open, new XMPPError(Condition.not_acceptable)));
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

    protected InitiationListener(InBandBytestreamManager manager) {
        this.initFilter = new AndFilter(new PacketTypeFilter(Open.class), IQTypeFilter.SET);
        this.manager = manager;
        this.initiationListenerExecutor = Executors.newCachedThreadPool();
    }

    public final void processPacket(Packet packet) {
        this.initiationListenerExecutor.execute(new C12991(packet));
    }
}
