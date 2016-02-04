package com.squareup.okhttp;

import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ConnectionPool {
    private static final long DEFAULT_KEEP_ALIVE_DURATION_MS = 300000;
    private static final ConnectionPool systemDefault;
    private final LinkedList<Connection> connections;
    private final Runnable connectionsCleanupRunnable;
    private Executor executor;
    private final long keepAliveDurationNs;
    private final int maxIdleConnections;

    /* renamed from: com.squareup.okhttp.ConnectionPool.1 */
    class C11881 implements Runnable {
        C11881() {
        }

        public void run() {
            ConnectionPool.this.runCleanupUntilPoolIsEmpty();
        }
    }

    static {
        String keepAlive = System.getProperty("http.keepAlive");
        String keepAliveDuration = System.getProperty("http.keepAliveDuration");
        String maxIdleConnections = System.getProperty("http.maxConnections");
        long keepAliveDurationMs = keepAliveDuration != null ? Long.parseLong(keepAliveDuration) : DEFAULT_KEEP_ALIVE_DURATION_MS;
        if (keepAlive != null && !Boolean.parseBoolean(keepAlive)) {
            systemDefault = new ConnectionPool(0, keepAliveDurationMs);
        } else if (maxIdleConnections != null) {
            systemDefault = new ConnectionPool(Integer.parseInt(maxIdleConnections), keepAliveDurationMs);
        } else {
            systemDefault = new ConnectionPool(5, keepAliveDurationMs);
        }
    }

    public ConnectionPool(int maxIdleConnections, long keepAliveDurationMs) {
        this.connections = new LinkedList();
        this.executor = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory("OkHttp ConnectionPool", true));
        this.connectionsCleanupRunnable = new C11881();
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDurationNs = (keepAliveDurationMs * 1000) * 1000;
    }

    public static ConnectionPool getDefault() {
        return systemDefault;
    }

    public final synchronized int getConnectionCount() {
        return this.connections.size();
    }

    @Deprecated
    public final synchronized int getSpdyConnectionCount() {
        return getMultiplexedConnectionCount();
    }

    public final synchronized int getMultiplexedConnectionCount() {
        int total;
        total = 0;
        Iterator it = this.connections.iterator();
        while (it.hasNext()) {
            if (((Connection) it.next()).isSpdy()) {
                total++;
            }
        }
        return total;
    }

    public final synchronized int getHttpConnectionCount() {
        return this.connections.size() - getMultiplexedConnectionCount();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized com.squareup.okhttp.Connection get(com.squareup.okhttp.Address r9) {
        /*
        r8 = this;
        monitor-enter(r8);
        r2 = 0;
        r4 = r8.connections;	 Catch:{ all -> 0x0080 }
        r5 = r8.connections;	 Catch:{ all -> 0x0080 }
        r5 = r5.size();	 Catch:{ all -> 0x0080 }
        r3 = r4.listIterator(r5);	 Catch:{ all -> 0x0080 }
    L_0x000e:
        r4 = r3.hasPrevious();	 Catch:{ all -> 0x0080 }
        if (r4 == 0) goto L_0x0052;
    L_0x0014:
        r0 = r3.previous();	 Catch:{ all -> 0x0080 }
        r0 = (com.squareup.okhttp.Connection) r0;	 Catch:{ all -> 0x0080 }
        r4 = r0.getRoute();	 Catch:{ all -> 0x0080 }
        r4 = r4.getAddress();	 Catch:{ all -> 0x0080 }
        r4 = r4.equals(r9);	 Catch:{ all -> 0x0080 }
        if (r4 == 0) goto L_0x000e;
    L_0x0028:
        r4 = r0.isAlive();	 Catch:{ all -> 0x0080 }
        if (r4 == 0) goto L_0x000e;
    L_0x002e:
        r4 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0080 }
        r6 = r0.getIdleStartTimeNs();	 Catch:{ all -> 0x0080 }
        r4 = r4 - r6;
        r6 = r8.keepAliveDurationNs;	 Catch:{ all -> 0x0080 }
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 >= 0) goto L_0x000e;
    L_0x003d:
        r3.remove();	 Catch:{ all -> 0x0080 }
        r4 = r0.isSpdy();	 Catch:{ all -> 0x0080 }
        if (r4 != 0) goto L_0x0051;
    L_0x0046:
        r4 = com.squareup.okhttp.internal.Platform.get();	 Catch:{ SocketException -> 0x0061 }
        r5 = r0.getSocket();	 Catch:{ SocketException -> 0x0061 }
        r4.tagSocket(r5);	 Catch:{ SocketException -> 0x0061 }
    L_0x0051:
        r2 = r0;
    L_0x0052:
        if (r2 == 0) goto L_0x005f;
    L_0x0054:
        r4 = r2.isSpdy();	 Catch:{ all -> 0x0080 }
        if (r4 == 0) goto L_0x005f;
    L_0x005a:
        r4 = r8.connections;	 Catch:{ all -> 0x0080 }
        r4.addFirst(r2);	 Catch:{ all -> 0x0080 }
    L_0x005f:
        monitor-exit(r8);
        return r2;
    L_0x0061:
        r1 = move-exception;
        r4 = r0.getSocket();	 Catch:{ all -> 0x0080 }
        com.squareup.okhttp.internal.Util.closeQuietly(r4);	 Catch:{ all -> 0x0080 }
        r4 = com.squareup.okhttp.internal.Platform.get();	 Catch:{ all -> 0x0080 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0080 }
        r6 = "Unable to tagSocket(): ";
        r5.<init>(r6);	 Catch:{ all -> 0x0080 }
        r5 = r5.append(r1);	 Catch:{ all -> 0x0080 }
        r5 = r5.toString();	 Catch:{ all -> 0x0080 }
        r4.logW(r5);	 Catch:{ all -> 0x0080 }
        goto L_0x000e;
    L_0x0080:
        r4 = move-exception;
        monitor-exit(r8);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.ConnectionPool.get(com.squareup.okhttp.Address):com.squareup.okhttp.Connection");
    }

    final void recycle(Connection connection) {
        if (connection.isSpdy() || !connection.clearOwner()) {
            return;
        }
        if (connection.isAlive()) {
            try {
                Platform.get().untagSocket(connection.getSocket());
                synchronized (this) {
                    addConnection(connection);
                    connection.incrementRecycleCount();
                    connection.resetIdleStartTime();
                }
                return;
            } catch (SocketException e) {
                Platform.get().logW("Unable to untagSocket(): " + e);
                Util.closeQuietly(connection.getSocket());
                return;
            }
        }
        Util.closeQuietly(connection.getSocket());
    }

    private void addConnection(Connection connection) {
        boolean empty = this.connections.isEmpty();
        this.connections.addFirst(connection);
        if (empty) {
            this.executor.execute(this.connectionsCleanupRunnable);
        } else {
            notifyAll();
        }
    }

    final void share(Connection connection) {
        if (!connection.isSpdy()) {
            throw new IllegalArgumentException();
        } else if (connection.isAlive()) {
            synchronized (this) {
                addConnection(connection);
            }
        }
    }

    public final void evictAll() {
        List<Connection> toEvict;
        synchronized (this) {
            toEvict = new ArrayList(this.connections);
            this.connections.clear();
            notifyAll();
        }
        int size = toEvict.size();
        for (int i = 0; i < size; i++) {
            Util.closeQuietly(((Connection) toEvict.get(i)).getSocket());
        }
    }

    private void runCleanupUntilPoolIsEmpty() {
        do {
        } while (performCleanup());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final boolean performCleanup() {
        /*
        r22 = this;
        monitor-enter(r22);
        r0 = r22;
        r0 = r0.connections;	 Catch:{ all -> 0x0060 }
        r18 = r0;
        r18 = r18.isEmpty();	 Catch:{ all -> 0x0060 }
        if (r18 == 0) goto L_0x0011;
    L_0x000d:
        r18 = 0;
        monitor-exit(r22);	 Catch:{ all -> 0x0060 }
    L_0x0010:
        return r18;
    L_0x0011:
        r3 = new java.util.ArrayList;	 Catch:{ all -> 0x0060 }
        r3.<init>();	 Catch:{ all -> 0x0060 }
        r6 = 0;
        r14 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0060 }
        r0 = r22;
        r12 = r0.keepAliveDurationNs;	 Catch:{ all -> 0x0060 }
        r0 = r22;
        r0 = r0.connections;	 Catch:{ all -> 0x0060 }
        r18 = r0;
        r0 = r22;
        r0 = r0.connections;	 Catch:{ all -> 0x0060 }
        r19 = r0;
        r19 = r19.size();	 Catch:{ all -> 0x0060 }
        r5 = r18.listIterator(r19);	 Catch:{ all -> 0x0060 }
    L_0x0033:
        r18 = r5.hasPrevious();	 Catch:{ all -> 0x0060 }
        if (r18 == 0) goto L_0x0070;
    L_0x0039:
        r2 = r5.previous();	 Catch:{ all -> 0x0060 }
        r2 = (com.squareup.okhttp.Connection) r2;	 Catch:{ all -> 0x0060 }
        r18 = r2.getIdleStartTimeNs();	 Catch:{ all -> 0x0060 }
        r0 = r22;
        r0 = r0.keepAliveDurationNs;	 Catch:{ all -> 0x0060 }
        r20 = r0;
        r18 = r18 + r20;
        r10 = r18 - r14;
        r18 = 0;
        r18 = (r10 > r18 ? 1 : (r10 == r18 ? 0 : -1));
        if (r18 <= 0) goto L_0x0059;
    L_0x0053:
        r18 = r2.isAlive();	 Catch:{ all -> 0x0060 }
        if (r18 != 0) goto L_0x0063;
    L_0x0059:
        r5.remove();	 Catch:{ all -> 0x0060 }
        r3.add(r2);	 Catch:{ all -> 0x0060 }
        goto L_0x0033;
    L_0x0060:
        r18 = move-exception;
        monitor-exit(r22);	 Catch:{ all -> 0x0060 }
        throw r18;
    L_0x0063:
        r18 = r2.isIdle();	 Catch:{ all -> 0x0060 }
        if (r18 == 0) goto L_0x0033;
    L_0x0069:
        r6 = r6 + 1;
        r12 = java.lang.Math.min(r12, r10);	 Catch:{ all -> 0x0060 }
        goto L_0x0033;
    L_0x0070:
        r0 = r22;
        r0 = r0.connections;	 Catch:{ all -> 0x0060 }
        r18 = r0;
        r0 = r22;
        r0 = r0.connections;	 Catch:{ all -> 0x0060 }
        r19 = r0;
        r19 = r19.size();	 Catch:{ all -> 0x0060 }
        r5 = r18.listIterator(r19);	 Catch:{ all -> 0x0060 }
    L_0x0084:
        r18 = r5.hasPrevious();	 Catch:{ all -> 0x0060 }
        if (r18 == 0) goto L_0x00a9;
    L_0x008a:
        r0 = r22;
        r0 = r0.maxIdleConnections;	 Catch:{ all -> 0x0060 }
        r18 = r0;
        r0 = r18;
        if (r6 <= r0) goto L_0x00a9;
    L_0x0094:
        r2 = r5.previous();	 Catch:{ all -> 0x0060 }
        r2 = (com.squareup.okhttp.Connection) r2;	 Catch:{ all -> 0x0060 }
        r18 = r2.isIdle();	 Catch:{ all -> 0x0060 }
        if (r18 == 0) goto L_0x0084;
    L_0x00a0:
        r3.add(r2);	 Catch:{ all -> 0x0060 }
        r5.remove();	 Catch:{ all -> 0x0060 }
        r6 = r6 + -1;
        goto L_0x0084;
    L_0x00a9:
        r18 = r3.isEmpty();	 Catch:{ all -> 0x0060 }
        if (r18 == 0) goto L_0x00cd;
    L_0x00af:
        r18 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r8 = r12 / r18;
        r18 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r18 = r18 * r8;
        r16 = r12 - r18;
        r0 = r16;
        r0 = (int) r0;	 Catch:{ InterruptedException -> 0x00cc }
        r18 = r0;
        r0 = r22;
        r1 = r18;
        r0.wait(r8, r1);	 Catch:{ InterruptedException -> 0x00cc }
        r18 = 1;
        monitor-exit(r22);	 Catch:{ all -> 0x0060 }
        goto L_0x0010;
    L_0x00cc:
        r18 = move-exception;
    L_0x00cd:
        monitor-exit(r22);	 Catch:{ all -> 0x0060 }
        r4 = 0;
        r7 = r3.size();
    L_0x00d3:
        if (r4 >= r7) goto L_0x00e5;
    L_0x00d5:
        r18 = r3.get(r4);
        r18 = (com.squareup.okhttp.Connection) r18;
        r18 = r18.getSocket();
        com.squareup.okhttp.internal.Util.closeQuietly(r18);
        r4 = r4 + 1;
        goto L_0x00d3;
    L_0x00e5:
        r18 = 1;
        goto L_0x0010;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.ConnectionPool.performCleanup():boolean");
    }

    final void replaceCleanupExecutorForTests(Executor cleanupExecutor) {
        this.executor = cleanupExecutor;
    }

    final synchronized List<Connection> getConnections() {
        return new ArrayList(this.connections);
    }
}
