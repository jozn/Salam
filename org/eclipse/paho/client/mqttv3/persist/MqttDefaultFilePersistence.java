package org.eclipse.paho.client.mqttv3.persist;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Enumeration;
import java.util.Vector;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttPersistable;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.internal.FileLock;
import org.eclipse.paho.client.mqttv3.internal.MqttPersistentData;

public class MqttDefaultFilePersistence implements MqttClientPersistence {
    private static final FilenameFilter FILE_FILTER;
    private static final String LOCK_FILENAME = ".lck";
    private static final String MESSAGE_BACKUP_FILE_EXTENSION = ".bup";
    private static final String MESSAGE_FILE_EXTENSION = ".msg";
    private File clientDir;
    private File dataDir;
    private FileLock fileLock;

    /* renamed from: org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence.1 */
    class C12731 implements FilenameFilter {
        C12731() {
        }

        public boolean accept(File dir, String name) {
            return name.endsWith(MqttDefaultFilePersistence.MESSAGE_FILE_EXTENSION);
        }
    }

    /* renamed from: org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence.2 */
    class C12742 implements FileFilter {
        final MqttDefaultFilePersistence this$0;

        C12742(MqttDefaultFilePersistence mqttDefaultFilePersistence) {
            this.this$0 = mqttDefaultFilePersistence;
        }

        public boolean accept(File f) {
            return f.getName().endsWith(MqttDefaultFilePersistence.MESSAGE_BACKUP_FILE_EXTENSION);
        }
    }

    static {
        FILE_FILTER = new C12731();
    }

    public MqttDefaultFilePersistence() {
        this(System.getProperty("user.dir"));
    }

    public MqttDefaultFilePersistence(String directory) {
        this.clientDir = null;
        this.fileLock = null;
        this.dataDir = new File(directory);
    }

    public void open(String clientId, String theConnection) throws MqttPersistenceException {
        if (this.dataDir.exists() && !this.dataDir.isDirectory()) {
            throw new MqttPersistenceException();
        } else if (!this.dataDir.exists() && !this.dataDir.mkdirs()) {
            throw new MqttPersistenceException();
        } else if (this.dataDir.canWrite()) {
            int i;
            char c;
            StringBuffer keyBuffer = new StringBuffer();
            for (i = 0; i < clientId.length(); i++) {
                c = clientId.charAt(i);
                if (isSafeChar(c)) {
                    keyBuffer.append(c);
                }
            }
            keyBuffer.append("-");
            for (i = 0; i < theConnection.length(); i++) {
                c = theConnection.charAt(i);
                if (isSafeChar(c)) {
                    keyBuffer.append(c);
                }
            }
            synchronized (this) {
                if (this.clientDir == null) {
                    this.clientDir = new File(this.dataDir, keyBuffer.toString());
                    if (!this.clientDir.exists()) {
                        this.clientDir.mkdir();
                    }
                }
                try {
                    this.fileLock = new FileLock(this.clientDir, LOCK_FILENAME);
                    restoreBackups(this.clientDir);
                } catch (Exception e) {
                    throw new MqttPersistenceException(32200);
                }
            }
        } else {
            throw new MqttPersistenceException();
        }
    }

    private void checkIsOpen() throws MqttPersistenceException {
        if (this.clientDir == null) {
            throw new MqttPersistenceException();
        }
    }

    public void close() throws MqttPersistenceException {
        synchronized (this) {
            if (this.fileLock != null) {
                this.fileLock.release();
            }
            if (getFiles().length == 0) {
                this.clientDir.delete();
            }
            this.clientDir = null;
        }
    }

    public void put(String key, MqttPersistable message) throws MqttPersistenceException {
        checkIsOpen();
        File file = new File(this.clientDir, new StringBuffer(String.valueOf(key)).append(MESSAGE_FILE_EXTENSION).toString());
        File backupFile = new File(this.clientDir, new StringBuffer(String.valueOf(key)).append(".msg.bup").toString());
        if (file.exists() && !file.renameTo(backupFile)) {
            backupFile.delete();
            file.renameTo(backupFile);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(message.getHeaderBytes(), message.getHeaderOffset(), message.getHeaderLength());
            if (message.getPayloadBytes() != null) {
                fos.write(message.getPayloadBytes(), message.getPayloadOffset(), message.getPayloadLength());
            }
            fos.getFD().sync();
            fos.close();
            if (backupFile.exists()) {
                backupFile.delete();
            }
            if (backupFile.exists() && !backupFile.renameTo(file)) {
                file.delete();
                backupFile.renameTo(file);
            }
        } catch (Throwable ex) {
            throw new MqttPersistenceException(ex);
        } catch (Throwable th) {
            if (backupFile.exists() && !backupFile.renameTo(file)) {
                file.delete();
                backupFile.renameTo(file);
            }
        }
    }

    public MqttPersistable get(String key) throws MqttPersistenceException {
        checkIsOpen();
        try {
            FileInputStream fis = new FileInputStream(new File(this.clientDir, new StringBuffer(String.valueOf(key)).append(MESSAGE_FILE_EXTENSION).toString()));
            int size = fis.available();
            byte[] data = new byte[size];
            for (int read = 0; read < size; read += fis.read(data, read, size - read)) {
            }
            fis.close();
            return new MqttPersistentData(key, data, 0, size, null, 0, 0);
        } catch (Throwable ex) {
            throw new MqttPersistenceException(ex);
        }
    }

    public void remove(String key) throws MqttPersistenceException {
        checkIsOpen();
        File file = new File(this.clientDir, new StringBuffer(String.valueOf(key)).append(MESSAGE_FILE_EXTENSION).toString());
        if (file.exists()) {
            file.delete();
        }
    }

    public Enumeration keys() throws MqttPersistenceException {
        checkIsOpen();
        File[] files = getFiles();
        Vector result = new Vector(files.length);
        for (File name : files) {
            String filename = name.getName();
            result.addElement(filename.substring(0, filename.length() - 4));
        }
        return result.elements();
    }

    private File[] getFiles() throws MqttPersistenceException {
        checkIsOpen();
        File[] files = this.clientDir.listFiles(FILE_FILTER);
        if (files != null) {
            return files;
        }
        throw new MqttPersistenceException();
    }

    private boolean isSafeChar(char c) {
        return Character.isJavaIdentifierPart(c) || c == '-';
    }

    private void restoreBackups(File dir) throws MqttPersistenceException {
        File[] files = dir.listFiles(new C12742(this));
        if (files == null) {
            throw new MqttPersistenceException();
        }
        for (int i = 0; i < files.length; i++) {
            File originalFile = new File(dir, files[i].getName().substring(0, files[i].getName().length() - 4));
            if (!files[i].renameTo(originalFile)) {
                originalFile.delete();
                files[i].renameTo(originalFile);
            }
        }
    }

    public boolean containsKey(String key) throws MqttPersistenceException {
        checkIsOpen();
        return new File(this.clientDir, new StringBuffer(String.valueOf(key)).append(MESSAGE_FILE_EXTENSION).toString()).exists();
    }

    public void clear() throws MqttPersistenceException {
        checkIsOpen();
        File[] files = getFiles();
        for (File delete : files) {
            delete.delete();
        }
    }
}
