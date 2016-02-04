package org.jivesoftware.smack;

import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;
import org.jivesoftware.smack.XMPPException.StreamErrorException;

public class ReconnectionManager extends AbstractConnectionListener {
    private static final Logger LOGGER;
    private final AbstractXMPPConnection connection;
    boolean done;
    private int randomBase;
    private Thread reconnectionThread;

    /* renamed from: org.jivesoftware.smack.ReconnectionManager.1 */
    static class C12781 implements ConnectionCreationListener {
        C12781() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            if (connection instanceof AbstractXMPPConnection) {
                connection.addConnectionListener(new ReconnectionManager((byte) 0));
            }
        }
    }

    /* renamed from: org.jivesoftware.smack.ReconnectionManager.2 */
    class C12792 extends Thread {
        private int attempts;

        C12792() {
            this.attempts = 0;
        }

        public final void run() {
            while (ReconnectionManager.this.isReconnectionAllowed()) {
                this.attempts++;
                int remainingSeconds = this.attempts > 13 ? (ReconnectionManager.this.randomBase * 6) * 5 : this.attempts > 7 ? ReconnectionManager.this.randomBase * 6 : ReconnectionManager.this.randomBase;
                while (ReconnectionManager.this.isReconnectionAllowed() && remainingSeconds > 0) {
                    try {
                        Thread.sleep(1000);
                        remainingSeconds--;
                        ReconnectionManager.this.notifyAttemptToReconnectIn$13462e();
                    } catch (InterruptedException e) {
                        ReconnectionManager.LOGGER.warning("Sleeping thread interrupted");
                        ReconnectionManager.this.notifyReconnectionFailed$698b7e31();
                    }
                }
                try {
                    if (ReconnectionManager.this.isReconnectionAllowed()) {
                        ReconnectionManager.this.connection.connect();
                    }
                } catch (Exception e2) {
                    ReconnectionManager.this.notifyReconnectionFailed$698b7e31();
                }
            }
        }
    }

    static {
        LOGGER = Logger.getLogger(ReconnectionManager.class.getName());
        XMPPConnectionRegistry.addConnectionCreationListener(new C12781());
    }

    private ReconnectionManager(AbstractXMPPConnection connection) {
        this.randomBase = new Random().nextInt(11) + 5;
        this.done = false;
        this.connection = connection;
    }

    private boolean isReconnectionAllowed() {
        return (this.done || this.connection.isConnected() || !this.connection.config.reconnectionAllowed) ? false : true;
    }

    private synchronized void reconnect() {
        if (isReconnectionAllowed() && (this.reconnectionThread == null || !this.reconnectionThread.isAlive())) {
            this.reconnectionThread = new C12792();
            this.reconnectionThread.setName("Smack Reconnection Manager");
            this.reconnectionThread.setDaemon(true);
            this.reconnectionThread.start();
        }
    }

    protected final void notifyReconnectionFailed$698b7e31() {
        if (isReconnectionAllowed()) {
            Iterator i$ = this.connection.connectionListeners.iterator();
            while (i$.hasNext()) {
                i$.next();
            }
        }
    }

    protected final void notifyAttemptToReconnectIn$13462e() {
        if (isReconnectionAllowed()) {
            Iterator i$ = this.connection.connectionListeners.iterator();
            while (i$.hasNext()) {
                i$.next();
            }
        }
    }

    public final void connectionClosed() {
        this.done = true;
    }

    public final void connectionClosedOnError(Exception e) {
        this.done = false;
        if (e instanceof StreamErrorException) {
            if ("conflict".equals(((StreamErrorException) e).streamError.code)) {
                return;
            }
        }
        if (isReconnectionAllowed()) {
            reconnect();
        }
    }
}
