package org.jivesoftware.smackx.bytestreams.ibb;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.bytestreams.BytestreamListener;

public class InBandBytestreamManager {
    private static final Map<XMPPConnection, InBandBytestreamManager> managers;
    private static final Random randomGenerator;
    final List<BytestreamListener> allRequestListeners;
    private final CloseListener closeListener;
    final XMPPConnection connection;
    private final DataListener dataListener;
    private int defaultBlockSize;
    List<String> ignoredBytestreamRequests;
    private final InitiationListener initiationListener;
    int maximumBlockSize;
    final Map<String, InBandBytestreamSession> sessions;
    private StanzaType stanza;
    final Map<String, BytestreamListener> userListeners;

    /* renamed from: org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager.1 */
    static class C12981 implements ConnectionCreationListener {

        /* renamed from: org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager.1.1 */
        class C12971 extends AbstractConnectionListener {
            final /* synthetic */ XMPPConnection val$connection;

            C12971(XMPPConnection xMPPConnection) {
                this.val$connection = xMPPConnection;
            }

            public final void connectionClosed() {
                InBandBytestreamManager.access$000(InBandBytestreamManager.getByteStreamManager(this.val$connection));
            }

            public final void connectionClosedOnError(Exception e) {
                InBandBytestreamManager.access$000(InBandBytestreamManager.getByteStreamManager(this.val$connection));
            }

            public final void reconnectionSuccessful() {
                InBandBytestreamManager.getByteStreamManager(this.val$connection);
            }
        }

        C12981() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            InBandBytestreamManager.getByteStreamManager(connection);
            connection.addConnectionListener(new C12971(connection));
        }
    }

    public enum StanzaType {
        IQ,
        MESSAGE
    }

    static /* synthetic */ void access$000(InBandBytestreamManager x0) {
        managers.remove(x0.connection);
        x0.connection.removePacketListener(x0.initiationListener);
        x0.connection.removePacketListener(x0.dataListener);
        x0.connection.removePacketListener(x0.closeListener);
        x0.initiationListener.initiationListenerExecutor.shutdownNow();
        x0.userListeners.clear();
        x0.allRequestListeners.clear();
        x0.sessions.clear();
        x0.ignoredBytestreamRequests.clear();
    }

    static {
        XMPPConnectionRegistry.addConnectionCreationListener(new C12981());
        randomGenerator = new Random();
        managers = new HashMap();
    }

    public static synchronized InBandBytestreamManager getByteStreamManager(XMPPConnection connection) {
        InBandBytestreamManager inBandBytestreamManager;
        synchronized (InBandBytestreamManager.class) {
            if (connection == null) {
                inBandBytestreamManager = null;
            } else {
                inBandBytestreamManager = (InBandBytestreamManager) managers.get(connection);
                if (inBandBytestreamManager == null) {
                    inBandBytestreamManager = new InBandBytestreamManager(connection);
                    managers.put(connection, inBandBytestreamManager);
                }
            }
        }
        return inBandBytestreamManager;
    }

    private InBandBytestreamManager(XMPPConnection connection) {
        this.userListeners = new ConcurrentHashMap();
        this.allRequestListeners = Collections.synchronizedList(new LinkedList());
        this.sessions = new ConcurrentHashMap();
        this.defaultBlockSize = AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD;
        this.maximumBlockSize = SupportMenu.USER_MASK;
        this.stanza = StanzaType.IQ;
        this.ignoredBytestreamRequests = Collections.synchronizedList(new LinkedList());
        this.connection = connection;
        this.initiationListener = new InitiationListener(this);
        this.connection.addPacketListener(this.initiationListener, this.initiationListener.initFilter);
        this.dataListener = new DataListener(this);
        this.connection.addPacketListener(this.dataListener, this.dataListener.dataFilter);
        this.closeListener = new CloseListener(this);
        this.connection.addPacketListener(this.closeListener, this.closeListener.closeFilter);
    }

    protected final void replyItemNotFoundPacket(IQ request) throws NotConnectedException {
        this.connection.sendPacket(IQ.createErrorResponse(request, new XMPPError(Condition.item_not_found)));
    }
}
