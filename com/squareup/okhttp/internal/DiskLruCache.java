package com.squareup.okhttp.internal;

import android.support.v7.appcompat.BuildConfig;
import com.squareup.okhttp.internal.io.FileSystem;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class DiskLruCache implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled;
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN;
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final Sink NULL_SINK;
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Runnable cleanupRunnable;
    private boolean closed;
    private final File directory;
    private final Executor executor;
    private final FileSystem fileSystem;
    private boolean hasJournalErrors;
    private boolean initialized;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private BufferedSink journalWriter;
    private final LinkedHashMap<String, Entry> lruEntries;
    private long maxSize;
    private long nextSequenceNumber;
    private int redundantOpCount;
    private long size;
    private final int valueCount;

    /* renamed from: com.squareup.okhttp.internal.DiskLruCache.1 */
    class C11971 implements Runnable {
        C11971() {
        }

        public void run() {
            int i = 0;
            synchronized (DiskLruCache.this) {
                if (!DiskLruCache.this.initialized) {
                    i = 1;
                }
                if ((i | DiskLruCache.this.closed) != 0) {
                    return;
                }
                try {
                    DiskLruCache.this.trimToSize();
                    if (DiskLruCache.this.journalRebuildRequired()) {
                        DiskLruCache.this.rebuildJournal();
                        DiskLruCache.this.redundantOpCount = 0;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /* renamed from: com.squareup.okhttp.internal.DiskLruCache.2 */
    class C11982 extends FaultHidingSink {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !DiskLruCache.class.desiredAssertionStatus() ? true : DiskLruCache.$assertionsDisabled;
        }

        C11982(Sink delegate) {
            super(delegate);
        }

        protected void onException(IOException e) {
            if ($assertionsDisabled || Thread.holdsLock(DiskLruCache.this)) {
                DiskLruCache.this.hasJournalErrors = true;
                return;
            }
            throw new AssertionError();
        }
    }

    /* renamed from: com.squareup.okhttp.internal.DiskLruCache.3 */
    class C11993 implements Iterator<Snapshot> {
        final Iterator<Entry> delegate;
        Snapshot nextSnapshot;
        Snapshot removeSnapshot;

        C11993() {
            this.delegate = new ArrayList(DiskLruCache.this.lruEntries.values()).iterator();
        }

        public boolean hasNext() {
            if (this.nextSnapshot != null) {
                return true;
            }
            synchronized (DiskLruCache.this) {
                if (DiskLruCache.this.closed) {
                    return DiskLruCache.$assertionsDisabled;
                }
                while (this.delegate.hasNext()) {
                    Snapshot snapshot = ((Entry) this.delegate.next()).snapshot();
                    if (snapshot != null) {
                        this.nextSnapshot = snapshot;
                        return true;
                    }
                }
                return DiskLruCache.$assertionsDisabled;
            }
        }

        public Snapshot next() {
            if (hasNext()) {
                this.removeSnapshot = this.nextSnapshot;
                this.nextSnapshot = null;
                return this.removeSnapshot;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            if (this.removeSnapshot == null) {
                throw new IllegalStateException("remove() before next()");
            }
            try {
                DiskLruCache.this.remove(this.removeSnapshot.key);
            } catch (IOException e) {
            } finally {
                this.removeSnapshot = null;
            }
        }
    }

    /* renamed from: com.squareup.okhttp.internal.DiskLruCache.4 */
    static class C12004 implements Sink {
        C12004() {
        }

        public final void write(Buffer source, long byteCount) throws IOException {
            source.skip(byteCount);
        }

        public final void flush() throws IOException {
        }

        public final Timeout timeout() {
            return Timeout.NONE;
        }

        public final void close() throws IOException {
        }
    }

    public final class Editor {
        private boolean committed;
        private final Entry entry;
        private boolean hasErrors;
        private final boolean[] written;

        /* renamed from: com.squareup.okhttp.internal.DiskLruCache.Editor.1 */
        class C12011 extends FaultHidingSink {
            C12011(Sink delegate) {
                super(delegate);
            }

            protected void onException(IOException e) {
                synchronized (DiskLruCache.this) {
                    Editor.this.hasErrors = true;
                }
            }
        }

        private Editor(Entry entry) {
            this.entry = entry;
            this.written = entry.readable ? null : new boolean[DiskLruCache.this.valueCount];
        }

        public final Source newSource(int index) throws IOException {
            Source source = null;
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                } else if (this.entry.readable) {
                    try {
                        source = DiskLruCache.this.fileSystem.source(this.entry.cleanFiles[index]);
                    } catch (FileNotFoundException e) {
                    }
                }
            }
            return source;
        }

        public final Sink newSink(int index) throws IOException {
            Sink c12011;
            synchronized (DiskLruCache.this) {
                if (this.entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!this.entry.readable) {
                    this.written[index] = true;
                }
                try {
                    c12011 = new C12011(DiskLruCache.this.fileSystem.sink(this.entry.dirtyFiles[index]));
                } catch (FileNotFoundException e) {
                    c12011 = DiskLruCache.NULL_SINK;
                }
            }
            return c12011;
        }

        public final void commit() throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.hasErrors) {
                    DiskLruCache.this.completeEdit(this, DiskLruCache.$assertionsDisabled);
                    DiskLruCache.this.removeEntry(this.entry);
                } else {
                    DiskLruCache.this.completeEdit(this, true);
                }
                this.committed = true;
            }
        }

        public final void abort() throws IOException {
            synchronized (DiskLruCache.this) {
                DiskLruCache.this.completeEdit(this, DiskLruCache.$assertionsDisabled);
            }
        }

        public final void abortUnlessCommitted() {
            synchronized (DiskLruCache.this) {
                if (!this.committed) {
                    try {
                        DiskLruCache.this.completeEdit(this, DiskLruCache.$assertionsDisabled);
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    private final class Entry {
        private final File[] cleanFiles;
        private Editor currentEditor;
        private final File[] dirtyFiles;
        private final String key;
        private final long[] lengths;
        private boolean readable;
        private long sequenceNumber;

        private Entry(String key) {
            this.key = key;
            this.lengths = new long[DiskLruCache.this.valueCount];
            this.cleanFiles = new File[DiskLruCache.this.valueCount];
            this.dirtyFiles = new File[DiskLruCache.this.valueCount];
            StringBuilder fileBuilder = new StringBuilder(key).append('.');
            int truncateTo = fileBuilder.length();
            for (int i = 0; i < DiskLruCache.this.valueCount; i++) {
                fileBuilder.append(i);
                this.cleanFiles[i] = new File(DiskLruCache.this.directory, fileBuilder.toString());
                fileBuilder.append(".tmp");
                this.dirtyFiles[i] = new File(DiskLruCache.this.directory, fileBuilder.toString());
                fileBuilder.setLength(truncateTo);
            }
        }

        private void setLengths(String[] strings) throws IOException {
            if (strings.length != DiskLruCache.this.valueCount) {
                throw invalidLengths(strings);
            }
            int i = 0;
            while (i < strings.length) {
                try {
                    this.lengths[i] = Long.parseLong(strings[i]);
                    i++;
                } catch (NumberFormatException e) {
                    throw invalidLengths(strings);
                }
            }
        }

        final void writeLengths(BufferedSink writer) throws IOException {
            for (long length : this.lengths) {
                writer.writeByte(32).writeDecimalLong(length);
            }
        }

        private IOException invalidLengths(String[] strings) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strings));
        }

        final Snapshot snapshot() {
            if (Thread.holdsLock(DiskLruCache.this)) {
                Source[] sources = new Source[DiskLruCache.this.valueCount];
                long[] lengths = (long[]) this.lengths.clone();
                int i = 0;
                while (i < DiskLruCache.this.valueCount) {
                    try {
                        sources[i] = DiskLruCache.this.fileSystem.source(this.cleanFiles[i]);
                        i++;
                    } catch (FileNotFoundException e) {
                        i = 0;
                        while (i < DiskLruCache.this.valueCount && sources[i] != null) {
                            Util.closeQuietly(sources[i]);
                            i++;
                        }
                        return null;
                    }
                }
                return new Snapshot(this.key, this.sequenceNumber, sources, lengths, null);
            }
            throw new AssertionError();
        }
    }

    public final class Snapshot implements Closeable {
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        private final Source[] sources;

        private Snapshot(String key, long sequenceNumber, Source[] sources, long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.sources = sources;
            this.lengths = lengths;
        }

        public final String key() {
            return this.key;
        }

        public final Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public final Source getSource(int index) {
            return this.sources[index];
        }

        public final long getLength(int index) {
            return this.lengths[index];
        }

        public final void close() {
            for (Closeable closeQuietly : this.sources) {
                Util.closeQuietly(closeQuietly);
            }
        }
    }

    static {
        $assertionsDisabled = !DiskLruCache.class.desiredAssertionStatus() ? true : $assertionsDisabled;
        LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
        NULL_SINK = new C12004();
    }

    DiskLruCache(FileSystem fileSystem, File directory, int appVersion, int valueCount, long maxSize, Executor executor) {
        this.size = 0;
        this.lruEntries = new LinkedHashMap(0, 0.75f, true);
        this.nextSequenceNumber = 0;
        this.cleanupRunnable = new C11971();
        this.fileSystem = fileSystem;
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, JOURNAL_FILE);
        this.journalFileTmp = new File(directory, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(directory, JOURNAL_FILE_BACKUP);
        this.valueCount = valueCount;
        this.maxSize = maxSize;
        this.executor = executor;
    }

    final void initialize() throws IOException {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        } else if (!this.initialized) {
            if (this.fileSystem.exists(this.journalFileBackup)) {
                if (this.fileSystem.exists(this.journalFile)) {
                    this.fileSystem.delete(this.journalFileBackup);
                } else {
                    this.fileSystem.rename(this.journalFileBackup, this.journalFile);
                }
            }
            if (this.fileSystem.exists(this.journalFile)) {
                try {
                    readJournal();
                    processJournal();
                    this.initialized = true;
                    return;
                } catch (IOException journalIsCorrupt) {
                    Platform.get().logW("DiskLruCache " + this.directory + " is corrupt: " + journalIsCorrupt.getMessage() + ", removing");
                    delete();
                    this.closed = $assertionsDisabled;
                }
            }
            rebuildJournal();
            this.initialized = true;
        }
    }

    public static DiskLruCache create(FileSystem fileSystem, File directory, int appVersion, int valueCount, long maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        } else if (valueCount <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        } else {
            return new DiskLruCache(fileSystem, directory, appVersion, valueCount, maxSize, new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory("OkHttp DiskLruCache", true)));
        }
    }

    private void readJournal() throws IOException {
        Closeable source = Okio.buffer(this.fileSystem.source(this.journalFile));
        int lineCount;
        try {
            String magic = source.readUtf8LineStrict();
            String version = source.readUtf8LineStrict();
            String appVersionString = source.readUtf8LineStrict();
            String valueCountString = source.readUtf8LineStrict();
            String blank = source.readUtf8LineStrict();
            if (MAGIC.equals(magic) && VERSION_1.equals(version) && Integer.toString(this.appVersion).equals(appVersionString) && Integer.toString(this.valueCount).equals(valueCountString) && BuildConfig.VERSION_NAME.equals(blank)) {
                lineCount = 0;
                while (true) {
                    readJournalLine(source.readUtf8LineStrict());
                    lineCount++;
                }
            } else {
                throw new IOException("unexpected journal header: [" + magic + ", " + version + ", " + valueCountString + ", " + blank + "]");
            }
        } catch (EOFException e) {
            this.redundantOpCount = lineCount - this.lruEntries.size();
            if (source.exhausted()) {
                this.journalWriter = newJournalWriter();
            } else {
                rebuildJournal();
            }
            Util.closeQuietly(source);
        } catch (Throwable th) {
            Util.closeQuietly(source);
        }
    }

    private BufferedSink newJournalWriter() throws FileNotFoundException {
        return Okio.buffer(new C11982(this.fileSystem.appendingSink(this.journalFile)));
    }

    private void readJournalLine(String line) throws IOException {
        int firstSpace = line.indexOf(32);
        if (firstSpace == -1) {
            throw new IOException("unexpected journal line: " + line);
        }
        String key;
        int keyBegin = firstSpace + 1;
        int secondSpace = line.indexOf(32, keyBegin);
        if (secondSpace == -1) {
            key = line.substring(keyBegin);
            if (firstSpace == 6 && line.startsWith(REMOVE)) {
                this.lruEntries.remove(key);
                return;
            }
        }
        key = line.substring(keyBegin, secondSpace);
        Entry entry = (Entry) this.lruEntries.get(key);
        if (entry == null) {
            entry = new Entry(key, null);
            this.lruEntries.put(key, entry);
        }
        if (secondSpace != -1 && firstSpace == 5 && line.startsWith(CLEAN)) {
            String[] parts = line.substring(secondSpace + 1).split(" ");
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths(parts);
        } else if (secondSpace == -1 && firstSpace == 5 && line.startsWith(DIRTY)) {
            entry.currentEditor = new Editor(entry, null);
        } else if (secondSpace != -1 || firstSpace != 4 || !line.startsWith(READ)) {
            throw new IOException("unexpected journal line: " + line);
        }
    }

    private void processJournal() throws IOException {
        this.fileSystem.delete(this.journalFileTmp);
        Iterator<Entry> i = this.lruEntries.values().iterator();
        while (i.hasNext()) {
            Entry entry = (Entry) i.next();
            int t;
            if (entry.currentEditor == null) {
                for (t = 0; t < this.valueCount; t++) {
                    this.size += entry.lengths[t];
                }
            } else {
                entry.currentEditor = null;
                for (t = 0; t < this.valueCount; t++) {
                    this.fileSystem.delete(entry.cleanFiles[t]);
                    this.fileSystem.delete(entry.dirtyFiles[t]);
                }
                i.remove();
            }
        }
    }

    private synchronized void rebuildJournal() throws IOException {
        if (this.journalWriter != null) {
            this.journalWriter.close();
        }
        BufferedSink writer = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
        try {
            writer.writeUtf8(MAGIC).writeByte(10);
            writer.writeUtf8(VERSION_1).writeByte(10);
            writer.writeDecimalLong((long) this.appVersion).writeByte(10);
            writer.writeDecimalLong((long) this.valueCount).writeByte(10);
            writer.writeByte(10);
            for (Entry entry : this.lruEntries.values()) {
                if (entry.currentEditor != null) {
                    writer.writeUtf8(DIRTY).writeByte(32);
                    writer.writeUtf8(entry.key);
                    writer.writeByte(10);
                } else {
                    writer.writeUtf8(CLEAN).writeByte(32);
                    writer.writeUtf8(entry.key);
                    entry.writeLengths(writer);
                    writer.writeByte(10);
                }
            }
            writer.close();
            if (this.fileSystem.exists(this.journalFile)) {
                this.fileSystem.rename(this.journalFile, this.journalFileBackup);
            }
            this.fileSystem.rename(this.journalFileTmp, this.journalFile);
            this.fileSystem.delete(this.journalFileBackup);
            this.journalWriter = newJournalWriter();
            this.hasJournalErrors = $assertionsDisabled;
        } catch (Throwable th) {
            writer.close();
        }
    }

    public final synchronized Snapshot get(String key) throws IOException {
        Snapshot snapshot;
        initialize();
        checkNotClosed();
        validateKey(key);
        Entry entry = (Entry) this.lruEntries.get(key);
        if (entry == null || !entry.readable) {
            snapshot = null;
        } else {
            snapshot = entry.snapshot();
            if (snapshot == null) {
                snapshot = null;
            } else {
                this.redundantOpCount++;
                this.journalWriter.writeUtf8(READ).writeByte(32).writeUtf8(key).writeByte(10);
                if (journalRebuildRequired()) {
                    this.executor.execute(this.cleanupRunnable);
                }
            }
        }
        return snapshot;
    }

    public final Editor edit(String key) throws IOException {
        return edit(key, ANY_SEQUENCE_NUMBER);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized com.squareup.okhttp.internal.DiskLruCache.Editor edit(java.lang.String r5, long r6) throws java.io.IOException {
        /*
        r4 = this;
        r0 = 0;
        monitor-enter(r4);
        r4.initialize();	 Catch:{ all -> 0x0064 }
        r4.checkNotClosed();	 Catch:{ all -> 0x0064 }
        r4.validateKey(r5);	 Catch:{ all -> 0x0064 }
        r2 = r4.lruEntries;	 Catch:{ all -> 0x0064 }
        r1 = r2.get(r5);	 Catch:{ all -> 0x0064 }
        r1 = (com.squareup.okhttp.internal.DiskLruCache.Entry) r1;	 Catch:{ all -> 0x0064 }
        r2 = -1;
        r2 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x0025;
    L_0x0019:
        if (r1 == 0) goto L_0x0023;
    L_0x001b:
        r2 = r1.sequenceNumber;	 Catch:{ all -> 0x0064 }
        r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));
        if (r2 == 0) goto L_0x0025;
    L_0x0023:
        monitor-exit(r4);
        return r0;
    L_0x0025:
        if (r1 == 0) goto L_0x002d;
    L_0x0027:
        r2 = r1.currentEditor;	 Catch:{ all -> 0x0064 }
        if (r2 != 0) goto L_0x0023;
    L_0x002d:
        r2 = r4.journalWriter;	 Catch:{ all -> 0x0064 }
        r3 = "DIRTY";
        r2 = r2.writeUtf8(r3);	 Catch:{ all -> 0x0064 }
        r3 = 32;
        r2 = r2.writeByte(r3);	 Catch:{ all -> 0x0064 }
        r2 = r2.writeUtf8(r5);	 Catch:{ all -> 0x0064 }
        r3 = 10;
        r2.writeByte(r3);	 Catch:{ all -> 0x0064 }
        r2 = r4.journalWriter;	 Catch:{ all -> 0x0064 }
        r2.flush();	 Catch:{ all -> 0x0064 }
        r2 = r4.hasJournalErrors;	 Catch:{ all -> 0x0064 }
        if (r2 != 0) goto L_0x0023;
    L_0x004d:
        if (r1 != 0) goto L_0x005a;
    L_0x004f:
        r1 = new com.squareup.okhttp.internal.DiskLruCache$Entry;	 Catch:{ all -> 0x0064 }
        r2 = 0;
        r1.<init>(r5, r2);	 Catch:{ all -> 0x0064 }
        r2 = r4.lruEntries;	 Catch:{ all -> 0x0064 }
        r2.put(r5, r1);	 Catch:{ all -> 0x0064 }
    L_0x005a:
        r0 = new com.squareup.okhttp.internal.DiskLruCache$Editor;	 Catch:{ all -> 0x0064 }
        r2 = 0;
        r0.<init>(r1, r2);	 Catch:{ all -> 0x0064 }
        r1.currentEditor = r0;	 Catch:{ all -> 0x0064 }
        goto L_0x0023;
    L_0x0064:
        r2 = move-exception;
        monitor-exit(r4);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.DiskLruCache.edit(java.lang.String, long):com.squareup.okhttp.internal.DiskLruCache$Editor");
    }

    public final File getDirectory() {
        return this.directory;
    }

    public final synchronized long getMaxSize() {
        return this.maxSize;
    }

    public final synchronized void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
        if (this.initialized) {
            this.executor.execute(this.cleanupRunnable);
        }
    }

    public final synchronized long size() throws IOException {
        initialize();
        return this.size;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void completeEdit(com.squareup.okhttp.internal.DiskLruCache.Editor r13, boolean r14) throws java.io.IOException {
        /*
        r12 = this;
        monitor-enter(r12);
        r2 = r13.entry;	 Catch:{ all -> 0x0011 }
        r8 = r2.currentEditor;	 Catch:{ all -> 0x0011 }
        if (r8 == r13) goto L_0x0014;
    L_0x000b:
        r8 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0011 }
        r8.<init>();	 Catch:{ all -> 0x0011 }
        throw r8;	 Catch:{ all -> 0x0011 }
    L_0x0011:
        r8 = move-exception;
        monitor-exit(r12);
        throw r8;
    L_0x0014:
        if (r14 == 0) goto L_0x0057;
    L_0x0016:
        r8 = r2.readable;	 Catch:{ all -> 0x0011 }
        if (r8 != 0) goto L_0x0057;
    L_0x001c:
        r3 = 0;
    L_0x001d:
        r8 = r12.valueCount;	 Catch:{ all -> 0x0011 }
        if (r3 >= r8) goto L_0x0057;
    L_0x0021:
        r8 = r13.written;	 Catch:{ all -> 0x0011 }
        r8 = r8[r3];	 Catch:{ all -> 0x0011 }
        if (r8 != 0) goto L_0x0041;
    L_0x0029:
        r13.abort();	 Catch:{ all -> 0x0011 }
        r8 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0011 }
        r9 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0011 }
        r10 = "Newly created entry didn't create value for index ";
        r9.<init>(r10);	 Catch:{ all -> 0x0011 }
        r9 = r9.append(r3);	 Catch:{ all -> 0x0011 }
        r9 = r9.toString();	 Catch:{ all -> 0x0011 }
        r8.<init>(r9);	 Catch:{ all -> 0x0011 }
        throw r8;	 Catch:{ all -> 0x0011 }
    L_0x0041:
        r8 = r12.fileSystem;	 Catch:{ all -> 0x0011 }
        r9 = r2.dirtyFiles;	 Catch:{ all -> 0x0011 }
        r9 = r9[r3];	 Catch:{ all -> 0x0011 }
        r8 = r8.exists(r9);	 Catch:{ all -> 0x0011 }
        if (r8 != 0) goto L_0x0054;
    L_0x004f:
        r13.abort();	 Catch:{ all -> 0x0011 }
    L_0x0052:
        monitor-exit(r12);
        return;
    L_0x0054:
        r3 = r3 + 1;
        goto L_0x001d;
    L_0x0057:
        r3 = 0;
    L_0x0058:
        r8 = r12.valueCount;	 Catch:{ all -> 0x0011 }
        if (r3 >= r8) goto L_0x0098;
    L_0x005c:
        r8 = r2.dirtyFiles;	 Catch:{ all -> 0x0011 }
        r1 = r8[r3];	 Catch:{ all -> 0x0011 }
        if (r14 == 0) goto L_0x0092;
    L_0x0064:
        r8 = r12.fileSystem;	 Catch:{ all -> 0x0011 }
        r8 = r8.exists(r1);	 Catch:{ all -> 0x0011 }
        if (r8 == 0) goto L_0x008f;
    L_0x006c:
        r8 = r2.cleanFiles;	 Catch:{ all -> 0x0011 }
        r0 = r8[r3];	 Catch:{ all -> 0x0011 }
        r8 = r12.fileSystem;	 Catch:{ all -> 0x0011 }
        r8.rename(r1, r0);	 Catch:{ all -> 0x0011 }
        r8 = r2.lengths;	 Catch:{ all -> 0x0011 }
        r6 = r8[r3];	 Catch:{ all -> 0x0011 }
        r8 = r12.fileSystem;	 Catch:{ all -> 0x0011 }
        r4 = r8.size(r0);	 Catch:{ all -> 0x0011 }
        r8 = r2.lengths;	 Catch:{ all -> 0x0011 }
        r8[r3] = r4;	 Catch:{ all -> 0x0011 }
        r8 = r12.size;	 Catch:{ all -> 0x0011 }
        r8 = r8 - r6;
        r8 = r8 + r4;
        r12.size = r8;	 Catch:{ all -> 0x0011 }
    L_0x008f:
        r3 = r3 + 1;
        goto L_0x0058;
    L_0x0092:
        r8 = r12.fileSystem;	 Catch:{ all -> 0x0011 }
        r8.delete(r1);	 Catch:{ all -> 0x0011 }
        goto L_0x008f;
    L_0x0098:
        r8 = r12.redundantOpCount;	 Catch:{ all -> 0x0011 }
        r8 = r8 + 1;
        r12.redundantOpCount = r8;	 Catch:{ all -> 0x0011 }
        r8 = 0;
        r2.currentEditor = r8;	 Catch:{ all -> 0x0011 }
        r8 = r2.readable;	 Catch:{ all -> 0x0011 }
        r8 = r8 | r14;
        if (r8 == 0) goto L_0x00f7;
    L_0x00a9:
        r8 = 1;
        r2.readable = r8;	 Catch:{ all -> 0x0011 }
        r8 = r12.journalWriter;	 Catch:{ all -> 0x0011 }
        r9 = "CLEAN";
        r8 = r8.writeUtf8(r9);	 Catch:{ all -> 0x0011 }
        r9 = 32;
        r8.writeByte(r9);	 Catch:{ all -> 0x0011 }
        r8 = r12.journalWriter;	 Catch:{ all -> 0x0011 }
        r9 = r2.key;	 Catch:{ all -> 0x0011 }
        r8.writeUtf8(r9);	 Catch:{ all -> 0x0011 }
        r8 = r12.journalWriter;	 Catch:{ all -> 0x0011 }
        r2.writeLengths(r8);	 Catch:{ all -> 0x0011 }
        r8 = r12.journalWriter;	 Catch:{ all -> 0x0011 }
        r9 = 10;
        r8.writeByte(r9);	 Catch:{ all -> 0x0011 }
        if (r14 == 0) goto L_0x00db;
    L_0x00d1:
        r8 = r12.nextSequenceNumber;	 Catch:{ all -> 0x0011 }
        r10 = 1;
        r10 = r10 + r8;
        r12.nextSequenceNumber = r10;	 Catch:{ all -> 0x0011 }
        r2.sequenceNumber = r8;	 Catch:{ all -> 0x0011 }
    L_0x00db:
        r8 = r12.journalWriter;	 Catch:{ all -> 0x0011 }
        r8.flush();	 Catch:{ all -> 0x0011 }
        r8 = r12.size;	 Catch:{ all -> 0x0011 }
        r10 = r12.maxSize;	 Catch:{ all -> 0x0011 }
        r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r8 > 0) goto L_0x00ee;
    L_0x00e8:
        r8 = r12.journalRebuildRequired();	 Catch:{ all -> 0x0011 }
        if (r8 == 0) goto L_0x0052;
    L_0x00ee:
        r8 = r12.executor;	 Catch:{ all -> 0x0011 }
        r9 = r12.cleanupRunnable;	 Catch:{ all -> 0x0011 }
        r8.execute(r9);	 Catch:{ all -> 0x0011 }
        goto L_0x0052;
    L_0x00f7:
        r8 = r12.lruEntries;	 Catch:{ all -> 0x0011 }
        r9 = r2.key;	 Catch:{ all -> 0x0011 }
        r8.remove(r9);	 Catch:{ all -> 0x0011 }
        r8 = r12.journalWriter;	 Catch:{ all -> 0x0011 }
        r9 = "REMOVE";
        r8 = r8.writeUtf8(r9);	 Catch:{ all -> 0x0011 }
        r9 = 32;
        r8.writeByte(r9);	 Catch:{ all -> 0x0011 }
        r8 = r12.journalWriter;	 Catch:{ all -> 0x0011 }
        r9 = r2.key;	 Catch:{ all -> 0x0011 }
        r8.writeUtf8(r9);	 Catch:{ all -> 0x0011 }
        r8 = r12.journalWriter;	 Catch:{ all -> 0x0011 }
        r9 = 10;
        r8.writeByte(r9);	 Catch:{ all -> 0x0011 }
        goto L_0x00db;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.DiskLruCache.completeEdit(com.squareup.okhttp.internal.DiskLruCache$Editor, boolean):void");
    }

    private boolean journalRebuildRequired() {
        return (this.redundantOpCount < 2000 || this.redundantOpCount < this.lruEntries.size()) ? $assertionsDisabled : true;
    }

    public final synchronized boolean remove(String key) throws IOException {
        boolean z;
        initialize();
        checkNotClosed();
        validateKey(key);
        Entry entry = (Entry) this.lruEntries.get(key);
        if (entry == null) {
            z = $assertionsDisabled;
        } else {
            z = removeEntry(entry);
        }
        return z;
    }

    private boolean removeEntry(Entry entry) throws IOException {
        if (entry.currentEditor != null) {
            entry.currentEditor.hasErrors = true;
        }
        for (int i = 0; i < this.valueCount; i++) {
            this.fileSystem.delete(entry.cleanFiles[i]);
            this.size -= entry.lengths[i];
            entry.lengths[i] = 0;
        }
        this.redundantOpCount++;
        this.journalWriter.writeUtf8(REMOVE).writeByte(32).writeUtf8(entry.key).writeByte(10);
        this.lruEntries.remove(entry.key);
        if (journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
        return true;
    }

    public final synchronized boolean isClosed() {
        return this.closed;
    }

    private synchronized void checkNotClosed() {
        if (isClosed()) {
            throw new IllegalStateException("cache is closed");
        }
    }

    public final synchronized void flush() throws IOException {
        if (this.initialized) {
            checkNotClosed();
            trimToSize();
            this.journalWriter.flush();
        }
    }

    public final synchronized void close() throws IOException {
        if (!this.initialized || this.closed) {
            this.closed = true;
        } else {
            for (Entry entry : (Entry[]) this.lruEntries.values().toArray(new Entry[this.lruEntries.size()])) {
                if (entry.currentEditor != null) {
                    entry.currentEditor.abort();
                }
            }
            trimToSize();
            this.journalWriter.close();
            this.journalWriter = null;
            this.closed = true;
        }
    }

    private void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            removeEntry((Entry) this.lruEntries.values().iterator().next());
        }
    }

    public final void delete() throws IOException {
        close();
        this.fileSystem.deleteContents(this.directory);
    }

    public final synchronized void evictAll() throws IOException {
        initialize();
        for (Entry entry : (Entry[]) this.lruEntries.values().toArray(new Entry[this.lruEntries.size()])) {
            removeEntry(entry);
        }
    }

    private void validateKey(String key) {
        if (!LEGAL_KEY_PATTERN.matcher(key).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + key + "\"");
        }
    }

    public final synchronized Iterator<Snapshot> snapshots() throws IOException {
        initialize();
        return new C11993();
    }
}
