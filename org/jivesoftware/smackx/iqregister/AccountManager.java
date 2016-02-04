package org.jivesoftware.smackx.iqregister;

import java.util.Map;
import java.util.WeakHashMap;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.iqregister.packet.Registration;

public final class AccountManager extends Manager {
    private static final Map<XMPPConnection, AccountManager> INSTANCES;
    private boolean accountCreationSupported;
    private Registration info;

    static {
        INSTANCES = new WeakHashMap();
    }

    public static synchronized AccountManager getInstance(XMPPConnection connection) {
        AccountManager accountManager;
        synchronized (AccountManager.class) {
            accountManager = (AccountManager) INSTANCES.get(connection);
            if (accountManager == null) {
                accountManager = new AccountManager(connection);
                INSTANCES.put(connection, accountManager);
            }
        }
        return accountManager;
    }

    private AccountManager(XMPPConnection connection) {
        super(connection);
        this.info = null;
        this.accountCreationSupported = false;
    }
}
