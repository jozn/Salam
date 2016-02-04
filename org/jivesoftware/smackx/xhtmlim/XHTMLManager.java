package org.jivesoftware.smackx.xhtmlim;

import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPConnectionRegistry;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;

public class XHTMLManager {

    /* renamed from: org.jivesoftware.smackx.xhtmlim.XHTMLManager.1 */
    static class C13551 implements ConnectionCreationListener {
        C13551() {
        }

        public final void connectionCreated(XMPPConnection connection) {
            XHTMLManager.setServiceEnabled$43c3b476(connection);
        }
    }

    static {
        XMPPConnectionRegistry.addConnectionCreationListener(new C13551());
    }

    public static synchronized void setServiceEnabled$43c3b476(XMPPConnection connection) {
        synchronized (XHTMLManager.class) {
            if (!ServiceDiscoveryManager.getInstanceFor(connection).includesFeature("http://jabber.org/protocol/xhtml-im")) {
                ServiceDiscoveryManager.getInstanceFor(connection).addFeature("http://jabber.org/protocol/xhtml-im");
            }
        }
    }
}
