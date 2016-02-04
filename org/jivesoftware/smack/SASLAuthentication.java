package org.jivesoftware.smack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jivesoftware.smack.packet.Mechanisms;
import org.jivesoftware.smack.sasl.SASLErrorException;
import org.jivesoftware.smack.sasl.SASLMechanism;

public final class SASLAuthentication {
    private static final Set<String> BLACKLISTED_MECHANISMS;
    private static final List<SASLMechanism> REGISTERED_MECHANISMS;
    public boolean authenticationSuccessful;
    public final AbstractXMPPConnection connection;
    public SASLMechanism currentMechanism;
    private Exception saslException;

    static {
        REGISTERED_MECHANISMS = new ArrayList();
        BLACKLISTED_MECHANISMS = new HashSet();
    }

    public static void registerSASLMechanism(SASLMechanism mechanism) {
        synchronized (REGISTERED_MECHANISMS) {
            REGISTERED_MECHANISMS.add(mechanism);
            Collections.sort(REGISTERED_MECHANISMS);
        }
    }

    SASLAuthentication(AbstractXMPPConnection connection) {
        this.currentMechanism = null;
        this.connection = connection;
        init();
    }

    public final boolean hasAnonymousAuthentication() {
        return serverMechanisms().contains("ANONYMOUS");
    }

    public final void maybeThrowException() throws SmackException, SASLErrorException {
        if (this.saslException == null) {
            return;
        }
        if (this.saslException instanceof SmackException) {
            throw ((SmackException) this.saslException);
        } else if (this.saslException instanceof SASLErrorException) {
            throw ((SASLErrorException) this.saslException);
        } else {
            throw new IllegalStateException("Unexpected exception type", this.saslException);
        }
    }

    public final void challengeReceived(String challenge, boolean finalChallenge) throws SmackException {
        try {
            this.currentMechanism.challengeReceived(challenge, finalChallenge);
        } catch (SmackException e) {
            authenticationFailed(e);
            throw e;
        }
    }

    public final void authenticationFailed(Exception exception) {
        this.saslException = exception;
        synchronized (this) {
            notify();
        }
    }

    protected final void init() {
        this.authenticationSuccessful = false;
        this.saslException = null;
    }

    public final SASLMechanism selectMechanism() {
        for (SASLMechanism mechanism : REGISTERED_MECHANISMS) {
            String mechanismName = mechanism.getName();
            synchronized (BLACKLISTED_MECHANISMS) {
                if (BLACKLISTED_MECHANISMS.contains(mechanismName)) {
                } else {
                    if (serverMechanisms().contains(mechanismName)) {
                        return mechanism.instanceForAuthentication(this.connection);
                    }
                }
            }
        }
        return null;
    }

    public final List<String> serverMechanisms() {
        return Collections.unmodifiableList(((Mechanisms) this.connection.getFeature("mechanisms", "urn:ietf:params:xml:ns:xmpp-sasl")).mechanisms);
    }
}
