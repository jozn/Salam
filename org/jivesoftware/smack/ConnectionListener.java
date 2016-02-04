package org.jivesoftware.smack;

public interface ConnectionListener {
    void authenticated(XMPPConnection xMPPConnection);

    void connected(XMPPConnection xMPPConnection);

    void connectionClosed();

    void connectionClosedOnError(Exception exception);

    void reconnectionSuccessful();
}
