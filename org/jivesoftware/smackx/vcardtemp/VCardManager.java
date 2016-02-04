package org.jivesoftware.smackx.vcardtemp;

import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;

public class VCardManager {

    /* renamed from: org.jivesoftware.smackx.vcardtemp.VCardManager.1 */
    static class C13461 implements ConnectionCreationListener {
        C13461() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            ServiceDiscoveryManager.getInstanceFor(connection).addFeature("vcard-temp");
        }
    }

    static {
        XMPPConnectionRegistry.addConnectionCreationListener(new C13461());
    }
}
