package org.jivesoftware.smack.sasl;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.sasl.packet.SaslStreamElements.AuthMechanism;
import org.jivesoftware.smack.sasl.packet.SaslStreamElements.Response;
import org.jivesoftware.smack.util.stringencoder.Base64;

public abstract class SASLMechanism implements Comparable<SASLMechanism> {
    public String authenticationId;
    protected XMPPConnection connection;
    protected String host;
    public String password;
    public String serviceName;

    public abstract void authenticateInternal$4e2ceb8() throws SmackException;

    public abstract byte[] getAuthenticationText() throws SmackException;

    public abstract String getName();

    public abstract int getPriority();

    public abstract SASLMechanism newInstance();

    public /* bridge */ /* synthetic */ int compareTo(Object x0) {
        return getPriority() - ((SASLMechanism) x0).getPriority();
    }

    public final void authenticate(String username, String host, String serviceName, String password) throws SmackException, NotConnectedException {
        this.authenticationId = username;
        this.host = host;
        this.serviceName = serviceName;
        this.password = password;
        authenticate();
    }

    public final void authenticate$79f1ca4c(String host, String serviceName) throws SmackException, NotConnectedException {
        this.host = host;
        this.serviceName = serviceName;
        authenticateInternal$4e2ceb8();
        authenticate();
    }

    private final void authenticate() throws SmackException, NotConnectedException {
        String authenticationText;
        byte[] authenticationBytes = getAuthenticationText();
        if (authenticationBytes != null) {
            authenticationText = Base64.encodeToString(authenticationBytes);
        } else {
            authenticationText = "=";
        }
        this.connection.send(new AuthMechanism(getName(), authenticationText));
    }

    public final void challengeReceived(String challengeString, boolean finalChallenge) throws SmackException, NotConnectedException {
        byte[] response = evaluateChallenge(Base64.decode(challengeString));
        if (!finalChallenge) {
            Response responseStanza;
            if (response == null) {
                responseStanza = new Response();
            } else {
                responseStanza = new Response(Base64.encodeToString(response));
            }
            this.connection.send(responseStanza);
        }
    }

    public byte[] evaluateChallenge(byte[] challenge) throws SmackException {
        return null;
    }

    public final SASLMechanism instanceForAuthentication(XMPPConnection connection) {
        SASLMechanism saslMechansim = newInstance();
        saslMechansim.connection = connection;
        return saslMechansim;
    }
}
