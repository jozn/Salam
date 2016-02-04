package org.jivesoftware.smack;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.packet.TopLevelStreamElement;

public class SynchronizationPoint<E extends Exception> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final Logger LOGGER;
    private final Condition condition;
    private final AbstractXMPPConnection connection;
    private final Lock connectionLock;
    private E failureException;
    private int state$455837db;

    /* renamed from: org.jivesoftware.smack.SynchronizationPoint.1 */
    static /* synthetic */ class C12831 {
        static final /* synthetic */ int[] $SwitchMap$org$jivesoftware$smack$SynchronizationPoint$State;

        static {
            $SwitchMap$org$jivesoftware$smack$SynchronizationPoint$State = new int[State.values$44094e21().length];
            try {
                $SwitchMap$org$jivesoftware$smack$SynchronizationPoint$State[State.Failure$455837db - 1] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$SynchronizationPoint$State[State.Initial$455837db - 1] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$SynchronizationPoint$State[State.NoResponse$455837db - 1] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$jivesoftware$smack$SynchronizationPoint$State[State.RequestSent$455837db - 1] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private enum State {
        ;

        public static int[] values$44094e21() {
            return (int[]) $VALUES$6ea8e860.clone();
        }

        static {
            Initial$455837db = 1;
            RequestSent$455837db = 2;
            NoResponse$455837db = 3;
            Success$455837db = 4;
            Failure$455837db = 5;
            $VALUES$6ea8e860 = new int[]{Initial$455837db, RequestSent$455837db, NoResponse$455837db, Success$455837db, Failure$455837db};
        }
    }

    static {
        $assertionsDisabled = !SynchronizationPoint.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(SynchronizationPoint.class.getName());
    }

    public SynchronizationPoint(AbstractXMPPConnection connection) {
        this.connection = connection;
        this.connectionLock = connection.getConnectionLock();
        this.condition = connection.getConnectionLock().newCondition();
        init();
    }

    public final void init() {
        this.state$455837db = State.Initial$455837db;
        this.failureException = null;
    }

    public final void sendAndWaitForResponse(TopLevelStreamElement request) throws NoResponseException, NotConnectedException {
        if ($assertionsDisabled || this.state$455837db == State.Initial$455837db) {
            this.connectionLock.lock();
            if (request != null) {
                try {
                    if (request instanceof Packet) {
                        this.connection.sendPacket((Packet) request);
                    } else if (request instanceof PlainStreamElement) {
                        this.connection.send((PlainStreamElement) request);
                    } else {
                        throw new IllegalStateException("Unsupported element type");
                    }
                    this.state$455837db = State.RequestSent$455837db;
                } catch (Throwable th) {
                    this.connectionLock.unlock();
                }
            }
            waitForConditionOrTimeout();
            this.connectionLock.unlock();
            checkForResponse();
            return;
        }
        throw new AssertionError();
    }

    public final void sendAndWaitForResponseOrThrow(PlainStreamElement request) throws Exception, NoResponseException, NotConnectedException {
        sendAndWaitForResponse(request);
        switch (C12831.$SwitchMap$org$jivesoftware$smack$SynchronizationPoint$State[this.state$455837db - 1]) {
            case org.eclipse.paho.client.mqttv3.logging.Logger.SEVERE /*1*/:
                if (this.failureException != null) {
                    throw this.failureException;
                }
            default:
        }
    }

    public final void checkIfSuccessOrWaitOrThrow() throws NoResponseException, Exception {
        checkIfSuccessOrWait();
        if (this.state$455837db == State.Failure$455837db) {
            throw this.failureException;
        }
    }

    public final void checkIfSuccessOrWait() throws NoResponseException {
        this.connectionLock.lock();
        try {
            if (this.state$455837db != State.Success$455837db) {
                waitForConditionOrTimeout();
                this.connectionLock.unlock();
                checkForResponse();
            }
        } finally {
            this.connectionLock.unlock();
        }
    }

    public final void reportSuccess() {
        this.connectionLock.lock();
        try {
            this.state$455837db = State.Success$455837db;
            this.condition.signal();
        } finally {
            this.connectionLock.unlock();
        }
    }

    public final void reportFailure(E failureException) {
        this.connectionLock.lock();
        try {
            this.state$455837db = State.Failure$455837db;
            this.failureException = failureException;
            this.condition.signal();
        } finally {
            this.connectionLock.unlock();
        }
    }

    public final boolean wasSuccessful() {
        return this.state$455837db == State.Success$455837db;
    }

    public final boolean requestSent() {
        return this.state$455837db == State.RequestSent$455837db;
    }

    private void waitForConditionOrTimeout() {
        long remainingWait = TimeUnit.MILLISECONDS.toNanos(this.connection.packetReplyTimeout);
        while (true) {
            if (this.state$455837db == State.RequestSent$455837db || this.state$455837db == State.Initial$455837db) {
                try {
                    remainingWait = this.condition.awaitNanos(remainingWait);
                    if (remainingWait <= 0) {
                        this.state$455837db = State.NoResponse$455837db;
                        return;
                    }
                } catch (InterruptedException e) {
                    LOGGER.log(Level.FINE, "was interrupted while waiting, this should not happen", e);
                }
            } else {
                return;
            }
        }
    }

    private void checkForResponse() throws NoResponseException {
        switch (C12831.$SwitchMap$org$jivesoftware$smack$SynchronizationPoint$State[this.state$455837db - 1]) {
            case org.eclipse.paho.client.mqttv3.logging.Logger.WARNING /*2*/:
            case org.eclipse.paho.client.mqttv3.logging.Logger.INFO /*3*/:
            case org.eclipse.paho.client.mqttv3.logging.Logger.CONFIG /*4*/:
                throw new NoResponseException();
            default:
        }
    }
}
