package org.jivesoftware.smack;

import java.lang.ref.WeakReference;

public abstract class Manager {
    final WeakReference<XMPPConnection> weakConnection;

    public Manager(XMPPConnection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("XMPPConnection must not be null");
        }
        this.weakConnection = new WeakReference(connection);
    }

    public final XMPPConnection connection() {
        return (XMPPConnection) this.weakConnection.get();
    }
}
