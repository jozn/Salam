package org.jivesoftware.smackx.filetransfer;

import java.util.Map;
import java.util.WeakHashMap;
import org.jivesoftware.smack.Manager;
import org.jivesoftware.smack.XMPPConnection;

public class FileTransferManager extends Manager {
    private static final Map<XMPPConnection, FileTransferManager> INSTANCES;

    static {
        INSTANCES = new WeakHashMap();
    }
}
