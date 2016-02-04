package org.eclipse.paho.client.mqttv3.internal;

import android.support.v7.appcompat.BuildConfig;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileLock {
    private RandomAccessFile file;
    private Object fileLock;
    private File lockFile;

    public FileLock(File clientDir, String lockFilename) throws Exception {
        this.lockFile = new File(clientDir, lockFilename);
        if (ExceptionHelper.isClassAvailable("java.nio.channels.FileLock")) {
            try {
                this.file = new RandomAccessFile(this.lockFile, "rw");
                Object channel = this.file.getClass().getMethod("getChannel", new Class[0]).invoke(this.file, new Object[0]);
                this.fileLock = channel.getClass().getMethod("tryLock", new Class[0]).invoke(channel, new Object[0]);
            } catch (NoSuchMethodException e) {
                this.fileLock = null;
            } catch (IllegalArgumentException e2) {
                this.fileLock = null;
            } catch (IllegalAccessException e3) {
                this.fileLock = null;
            }
            if (this.fileLock == null) {
                release();
                throw new Exception("Problem obtaining file lock");
            }
        }
    }

    public void release() {
        try {
            if (this.fileLock != null) {
                this.fileLock.getClass().getMethod(BuildConfig.BUILD_TYPE, new Class[0]).invoke(this.fileLock, new Object[0]);
                this.fileLock = null;
            }
        } catch (Exception e) {
        }
        if (this.file != null) {
            try {
                this.file.close();
            } catch (IOException e2) {
            }
            this.file = null;
        }
        if (this.lockFile != null && this.lockFile.exists()) {
            this.lockFile.delete();
        }
        this.lockFile = null;
    }
}
