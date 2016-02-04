package org.jivesoftware.smackx.caps.cache;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.util.stringencoder.Base32;
import org.jivesoftware.smack.util.stringencoder.StringEncoder;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;

public class SimpleDirectoryPersistentCache implements EntityCapsPersistentCache {
    private static final Logger LOGGER;
    private File cacheDir;
    private StringEncoder filenameEncoder;

    static {
        LOGGER = Logger.getLogger(SimpleDirectoryPersistentCache.class.getName());
    }

    public SimpleDirectoryPersistentCache(File cacheDir) {
        this(cacheDir, Base32.getStringEncoder());
    }

    private SimpleDirectoryPersistentCache(File cacheDir, StringEncoder filenameEncoder) {
        if (!cacheDir.exists()) {
            throw new IllegalStateException("Cache directory \"" + cacheDir + "\" does not exist");
        } else if (cacheDir.isDirectory()) {
            this.cacheDir = cacheDir;
            this.filenameEncoder = filenameEncoder;
        } else {
            throw new IllegalStateException("Cache directory \"" + cacheDir + "\" is not a directory");
        }
    }

    public final void addDiscoverInfoByNodePersistent(String nodeVer, DiscoverInfo info) {
        File nodeFile = new File(this.cacheDir, this.filenameEncoder.encode(nodeVer));
        DataOutputStream dataOutputStream;
        try {
            if (nodeFile.createNewFile()) {
                dataOutputStream = new DataOutputStream(new FileOutputStream(nodeFile));
                dataOutputStream.writeUTF(info.toXML().toString());
                dataOutputStream.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write disco info to file", e);
        } catch (Throwable th) {
            dataOutputStream.close();
        }
    }
}
