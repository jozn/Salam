package org.jivesoftware.smack.compression;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class XMPPInputOutputStream {
    protected static int flushMethod$2c003870;
    protected String compressionMethod;

    public enum FlushMethod {
        ;

        static {
            FULL_FLUSH$2c003870 = 1;
            SYNC_FLUSH$2c003870 = 2;
            $VALUES$13770f75 = new int[]{FULL_FLUSH$2c003870, SYNC_FLUSH$2c003870};
        }
    }

    public abstract InputStream getInputStream(InputStream inputStream) throws Exception;

    public abstract OutputStream getOutputStream(OutputStream outputStream) throws Exception;

    public abstract boolean isSupported();

    public final String getCompressionMethod() {
        return this.compressionMethod;
    }
}
