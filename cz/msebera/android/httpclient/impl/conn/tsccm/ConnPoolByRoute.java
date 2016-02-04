package cz.msebera.android.httpclient.impl.conn.tsccm;

import cz.msebera.android.httpclient.conn.ClientConnectionOperator;
import cz.msebera.android.httpclient.conn.ConnectionPoolTimeoutException;
import cz.msebera.android.httpclient.conn.OperatedClientConnection;
import cz.msebera.android.httpclient.conn.params.ConnManagerParams;
import cz.msebera.android.httpclient.conn.params.ConnPerRoute;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.Args;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Deprecated
public final class ConnPoolByRoute extends AbstractConnPool {
    protected final ConnPerRoute connPerRoute;
    private final long connTTL;
    private final TimeUnit connTTLTimeUnit;
    protected final Queue<BasicPoolEntry> freeConnections;
    protected final Set<BasicPoolEntry> leasedConnections;
    public HttpClientAndroidLog log;
    protected volatile int maxTotalConnections;
    protected volatile int numConnections;
    protected final ClientConnectionOperator operator;
    private final Lock poolLock;
    protected final Map<HttpRoute, RouteSpecificPool> routeToPool;
    protected volatile boolean shutdown;
    protected final Queue<WaitingThread> waitingThreads;

    /* renamed from: cz.msebera.android.httpclient.impl.conn.tsccm.ConnPoolByRoute.1 */
    class C12491 implements PoolEntryRequest {
        final /* synthetic */ WaitingThreadAborter val$aborter;
        final /* synthetic */ HttpRoute val$route;
        final /* synthetic */ Object val$state;

        C12491(WaitingThreadAborter waitingThreadAborter, HttpRoute httpRoute, Object obj) {
            this.val$aborter = waitingThreadAborter;
            this.val$route = httpRoute;
            this.val$state = obj;
        }

        public final BasicPoolEntry getPoolEntry(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
            return ConnPoolByRoute.this.getEntryBlocking(this.val$route, this.val$state, timeout, tunit, this.val$aborter);
        }
    }

    protected final cz.msebera.android.httpclient.impl.conn.tsccm.BasicPoolEntry getEntryBlocking(cz.msebera.android.httpclient.conn.routing.HttpRoute r16, java.lang.Object r17, long r18, java.util.concurrent.TimeUnit r20, cz.msebera.android.httpclient.impl.conn.tsccm.WaitingThreadAborter r21) throws cz.msebera.android.httpclient.conn.ConnectionPoolTimeoutException, java.lang.InterruptedException {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:87:0x0178 in {2, 9, 12, 17, 20, 25, 26, 36, 41, 47, 49, 52, 55, 60, 64, 68, 79, 82, 84, 85, 86, 88, 89, 91, 92, 93, 94, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107} preds:[]
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:129)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.rerun(BlockProcessor.java:44)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:57)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r15 = this;
        r4 = 0;
        r10 = 0;
        r10 = (r18 > r10 ? 1 : (r18 == r10 ? 0 : -1));
        if (r10 <= 0) goto L_0x0019;
    L_0x0007:
        r4 = new java.util.Date;
        r10 = java.lang.System.currentTimeMillis();
        r0 = r20;
        r1 = r18;
        r12 = r0.toMillis(r1);
        r10 = r10 + r12;
        r4.<init>(r10);
    L_0x0019:
        r5 = 0;
        r10 = r15.poolLock;
        r10.lock();
        r7 = r15.getRoutePool$1a2e780(r16);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r9 = 0;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x0024:
        if (r5 != 0) goto L_0x0217;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x0026:
        r10 = r15.shutdown;	 Catch:{ all -> 0x020d, all -> 0x017f }
        if (r10 != 0) goto L_0x00ed;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x002a:
        r10 = 1;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x002b:
        r11 = "Connection pool shut down";	 Catch:{ all -> 0x020d, all -> 0x017f }
        cz.msebera.android.httpclient.util.Asserts.check(r10, r11);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = r15.log;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = r10.debugEnabled;	 Catch:{ all -> 0x020d, all -> 0x017f }
        if (r10 == 0) goto L_0x0084;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x0036:
        r10 = r15.log;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = new java.lang.StringBuilder;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = "[";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11.<init>(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r0 = r16;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r0);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = "] total kept alive: ";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = r15.freeConnections;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = r12.size();	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = ", total issued: ";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = r15.leasedConnections;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = r12.size();	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = ", total allocated: ";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = r15.numConnections;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = " out of ";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = r15.maxTotalConnections;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.toString();	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10.debug(r11);	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x0084:
        r0 = r17;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r5 = r15.getFreeEntry(r7, r0);	 Catch:{ all -> 0x020d, all -> 0x017f }
        if (r5 != 0) goto L_0x0217;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x008c:
        r10 = r7.getCapacity();	 Catch:{ all -> 0x020d, all -> 0x017f }
        if (r10 <= 0) goto L_0x00f0;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x0092:
        r6 = 1;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x0093:
        r10 = r15.log;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = r10.debugEnabled;	 Catch:{ all -> 0x020d, all -> 0x017f }
        if (r10 == 0) goto L_0x00dd;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x0099:
        r10 = r15.log;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = new java.lang.StringBuilder;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = "Available capacity: ";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11.<init>(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = r7.getCapacity();	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = " out of ";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = r7.getMaxEntries();	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = " [";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r0 = r16;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r0);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = "][";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r0 = r17;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r0);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = "]";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.toString();	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10.debug(r11);	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x00dd:
        if (r6 == 0) goto L_0x00f2;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x00df:
        r10 = r15.numConnections;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r15.maxTotalConnections;	 Catch:{ all -> 0x020d, all -> 0x017f }
        if (r10 >= r11) goto L_0x00f2;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x00e5:
        r10 = r15.operator;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r5 = r15.createEntry(r7, r10);	 Catch:{ all -> 0x020d, all -> 0x017f }
        goto L_0x0024;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x00ed:
        r10 = 0;	 Catch:{ all -> 0x020d, all -> 0x017f }
        goto L_0x002b;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x00f0:
        r6 = 0;	 Catch:{ all -> 0x020d, all -> 0x017f }
        goto L_0x0093;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x00f2:
        if (r6 == 0) goto L_0x0194;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x00f4:
        r10 = r15.freeConnections;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = r10.isEmpty();	 Catch:{ all -> 0x020d, all -> 0x017f }
        if (r10 != 0) goto L_0x0194;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x00fc:
        r10 = r15.poolLock;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10.lock();	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = r15.freeConnections;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r10 = r10.remove();	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r10 = (cz.msebera.android.httpclient.impl.conn.tsccm.BasicPoolEntry) r10;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        if (r10 == 0) goto L_0x0186;	 Catch:{ all -> 0x0171, all -> 0x0178 }
    L_0x010b:
        r11 = r10.route;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r12 = r15.log;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r12 = r12.debugEnabled;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        if (r12 == 0) goto L_0x013b;	 Catch:{ all -> 0x0171, all -> 0x0178 }
    L_0x0113:
        r12 = r15.log;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r13 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r14 = "Deleting connection [";	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r13.<init>(r14);	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r13 = r13.append(r11);	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r14 = "][";	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r13 = r13.append(r14);	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r14 = r10.getState();	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r13 = r13.append(r14);	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r14 = "]";	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r13 = r13.append(r14);	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r13 = r13.toString();	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r12.debug(r13);	 Catch:{ all -> 0x0171, all -> 0x0178 }
    L_0x013b:
        r12 = r15.poolLock;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r12.lock();	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r15.closeConnection(r10);	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r12 = r15.getRoutePool$1a2e780(r11);	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r12.deleteEntry(r10);	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r10 = r15.numConnections;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r10 = r10 + -1;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r15.numConnections = r10;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r10 = r12.isUnused();	 Catch:{ all -> 0x0171, all -> 0x0178 }
        if (r10 == 0) goto L_0x015b;	 Catch:{ all -> 0x0171, all -> 0x0178 }
    L_0x0156:
        r10 = r15.routeToPool;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r10.remove(r11);	 Catch:{ all -> 0x0171, all -> 0x0178 }
    L_0x015b:
        r10 = r15.poolLock;
        r10.unlock();
    L_0x0160:
        r10 = r15.poolLock;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10.unlock();	 Catch:{ all -> 0x020d, all -> 0x017f }
        r7 = r15.getRoutePool$1a2e780(r16);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = r15.operator;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r5 = r15.createEntry(r7, r10);	 Catch:{ all -> 0x020d, all -> 0x017f }
        goto L_0x0024;
    L_0x0171:
        r10 = move-exception;
        r11 = r15.poolLock;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r11.unlock();	 Catch:{ all -> 0x0171, all -> 0x0178 }
        throw r10;	 Catch:{ all -> 0x0171, all -> 0x0178 }
    L_0x0178:
        r10 = move-exception;
        r11 = r15.poolLock;
        r11.unlock();
        throw r10;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x017f:
        r10 = move-exception;
        r11 = r15.poolLock;
        r11.unlock();
        throw r10;
    L_0x0186:
        r10 = r15.log;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r10 = r10.debugEnabled;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        if (r10 == 0) goto L_0x0160;	 Catch:{ all -> 0x0171, all -> 0x0178 }
    L_0x018c:
        r10 = r15.log;	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r11 = "No free connection to delete";	 Catch:{ all -> 0x0171, all -> 0x0178 }
        r10.debug(r11);	 Catch:{ all -> 0x0171, all -> 0x0178 }
        goto L_0x0160;
    L_0x0194:
        r10 = r15.log;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = r10.debugEnabled;	 Catch:{ all -> 0x020d, all -> 0x017f }
        if (r10 == 0) goto L_0x01c2;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x019a:
        r10 = r15.log;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = new java.lang.StringBuilder;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = "Need to wait for connection [";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11.<init>(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r0 = r16;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r0);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = "][";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r0 = r17;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r0);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = "]";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.append(r12);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r11.toString();	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10.debug(r11);	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x01c2:
        if (r9 != 0) goto L_0x01e1;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x01c4:
        r10 = r15.poolLock;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = r10.newCondition();	 Catch:{ all -> 0x020d, all -> 0x017f }
        r9 = new cz.msebera.android.httpclient.impl.conn.tsccm.WaitingThread;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r9.<init>(r10, r7);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r0 = r21;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r0.waitingThread = r9;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r0 = r21;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = r0.aborted;	 Catch:{ all -> 0x020d, all -> 0x017f }
        if (r10 == 0) goto L_0x01e1;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x01d9:
        r10 = 1;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r9.aborted = r10;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = r9.cond;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10.signalAll();	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x01e1:
        r7.queueThread(r9);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = r15.waitingThreads;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10.add(r9);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r8 = r9.await(r4);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r7.removeThread(r9);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = r15.waitingThreads;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10.remove(r9);	 Catch:{ all -> 0x020d, all -> 0x017f }
        if (r8 != 0) goto L_0x0024;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x01f7:
        if (r4 == 0) goto L_0x0024;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x01f9:
        r10 = r4.getTime();	 Catch:{ all -> 0x020d, all -> 0x017f }
        r12 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));	 Catch:{ all -> 0x020d, all -> 0x017f }
        if (r10 > 0) goto L_0x0024;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x0205:
        r10 = new cz.msebera.android.httpclient.conn.ConnectionPoolTimeoutException;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = "Timeout waiting for connection from pool";	 Catch:{ all -> 0x020d, all -> 0x017f }
        r10.<init>(r11);	 Catch:{ all -> 0x020d, all -> 0x017f }
        throw r10;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x020d:
        r10 = move-exception;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r7.removeThread(r9);	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11 = r15.waitingThreads;	 Catch:{ all -> 0x020d, all -> 0x017f }
        r11.remove(r9);	 Catch:{ all -> 0x020d, all -> 0x017f }
        throw r10;	 Catch:{ all -> 0x020d, all -> 0x017f }
    L_0x0217:
        r10 = r15.poolLock;
        r10.unlock();
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: cz.msebera.android.httpclient.impl.conn.tsccm.ConnPoolByRoute.getEntryBlocking(cz.msebera.android.httpclient.conn.routing.HttpRoute, java.lang.Object, long, java.util.concurrent.TimeUnit, cz.msebera.android.httpclient.impl.conn.tsccm.WaitingThreadAborter):cz.msebera.android.httpclient.impl.conn.tsccm.BasicPoolEntry");
    }

    private ConnPoolByRoute(ClientConnectionOperator operator, ConnPerRoute connPerRoute, int maxTotalConnections) {
        this(operator, connPerRoute, maxTotalConnections, TimeUnit.MILLISECONDS);
    }

    public ConnPoolByRoute(ClientConnectionOperator operator, ConnPerRoute connPerRoute, int maxTotalConnections, TimeUnit connTTLTimeUnit) {
        this.log = new HttpClientAndroidLog(getClass());
        Args.notNull(operator, "Connection operator");
        Args.notNull(connPerRoute, "Connections per route");
        this.poolLock = this.poolLock;
        this.leasedConnections = this.leasedConnections;
        this.operator = operator;
        this.connPerRoute = connPerRoute;
        this.maxTotalConnections = maxTotalConnections;
        this.freeConnections = new LinkedList();
        this.waitingThreads = new LinkedList();
        this.routeToPool = new HashMap();
        this.connTTL = -1;
        this.connTTLTimeUnit = connTTLTimeUnit;
    }

    @Deprecated
    public ConnPoolByRoute(ClientConnectionOperator operator, HttpParams params) {
        this(operator, ConnManagerParams.getMaxConnectionsPerRoute(params), ConnManagerParams.getMaxTotalConnections(params));
    }

    private void closeConnection(BasicPoolEntry entry) {
        OperatedClientConnection conn = entry.connection;
        if (conn != null) {
            try {
                conn.close();
            } catch (IOException ex) {
                this.log.debug("I/O error closing connection", ex);
            }
        }
    }

    private RouteSpecificPool getRoutePool$1a2e780(HttpRoute route) {
        this.poolLock.lock();
        try {
            RouteSpecificPool rospl = (RouteSpecificPool) this.routeToPool.get(route);
            if (rospl == null) {
                rospl = new RouteSpecificPool(route, this.connPerRoute);
                this.routeToPool.put(route, rospl);
            }
            this.poolLock.unlock();
            return rospl;
        } catch (Throwable th) {
            this.poolLock.unlock();
        }
    }

    public final void freeEntry(BasicPoolEntry entry, boolean reusable, long validDuration, TimeUnit timeUnit) {
        HttpRoute route = entry.route;
        if (this.log.debugEnabled) {
            this.log.debug("Releasing connection [" + route + "][" + entry.getState() + "]");
        }
        this.poolLock.lock();
        try {
            if (this.shutdown) {
                closeConnection(entry);
                return;
            }
            this.leasedConnections.remove(entry);
            RouteSpecificPool rospl = getRoutePool$1a2e780(route);
            if (!reusable || rospl.getCapacity() < 0) {
                closeConnection(entry);
                rospl.dropEntry();
                this.numConnections--;
            } else {
                if (this.log.debugEnabled) {
                    String s;
                    if (validDuration > 0) {
                        s = "for " + validDuration + " " + timeUnit;
                    } else {
                        s = "indefinitely";
                    }
                    this.log.debug("Pooling connection [" + route + "][" + entry.getState() + "]; keep alive " + s);
                }
                rospl.freeEntry(entry);
                entry.updated = System.currentTimeMillis();
                entry.expiry = Math.min(entry.validUntil, validDuration > 0 ? entry.updated + timeUnit.toMillis(validDuration) : Long.MAX_VALUE);
                this.freeConnections.add(entry);
            }
            WaitingThread waitingThread = null;
            this.poolLock.lock();
            if (rospl != null) {
                if (rospl.hasThread()) {
                    if (this.log.debugEnabled) {
                        this.log.debug("Notifying thread waiting on pool [" + rospl.getRoute() + "]");
                    }
                    waitingThread = rospl.nextThread();
                    if (waitingThread != null) {
                        waitingThread.wakeup();
                    }
                    this.poolLock.unlock();
                    this.poolLock.unlock();
                }
            }
            if (!this.waitingThreads.isEmpty()) {
                if (this.log.debugEnabled) {
                    this.log.debug("Notifying thread waiting on any pool");
                }
                waitingThread = (WaitingThread) this.waitingThreads.remove();
            } else if (this.log.debugEnabled) {
                this.log.debug("Notifying no-one, there are no waiting threads");
            }
            if (waitingThread != null) {
                waitingThread.wakeup();
            }
            this.poolLock.unlock();
            this.poolLock.unlock();
        } catch (Throwable th) {
        } finally {
            this.poolLock.unlock();
        }
    }

    private BasicPoolEntry getFreeEntry(RouteSpecificPool rospl, Object state) {
        BasicPoolEntry entry = null;
        this.poolLock.lock();
        boolean done = false;
        while (!done) {
            entry = rospl.allocEntry(state);
            if (entry != null) {
                if (this.log.debugEnabled) {
                    this.log.debug("Getting free connection [" + rospl.getRoute() + "][" + state + "]");
                }
                this.freeConnections.remove(entry);
                if ((System.currentTimeMillis() >= entry.expiry ? 1 : null) != null) {
                    if (this.log.debugEnabled) {
                        this.log.debug("Closing expired free connection [" + rospl.getRoute() + "][" + state + "]");
                    }
                    closeConnection(entry);
                    rospl.dropEntry();
                    this.numConnections--;
                } else {
                    try {
                        this.leasedConnections.add(entry);
                        done = true;
                    } catch (Throwable th) {
                        this.poolLock.unlock();
                    }
                }
            } else {
                done = true;
                if (this.log.debugEnabled) {
                    this.log.debug("No free connections [" + rospl.getRoute() + "][" + state + "]");
                }
            }
        }
        this.poolLock.unlock();
        return entry;
    }

    private BasicPoolEntry createEntry(RouteSpecificPool rospl, ClientConnectionOperator op) {
        if (this.log.debugEnabled) {
            this.log.debug("Creating new connection [" + rospl.getRoute() + "]");
        }
        BasicPoolEntry entry = new BasicPoolEntry(op, rospl.getRoute(), this.connTTL, this.connTTLTimeUnit);
        this.poolLock.lock();
        try {
            rospl.createdEntry(entry);
            this.numConnections++;
            this.leasedConnections.add(entry);
            return entry;
        } finally {
            this.poolLock.unlock();
        }
    }

    public final void shutdown() {
        this.poolLock.lock();
        try {
            if (!this.shutdown) {
                BasicPoolEntry entry;
                this.shutdown = true;
                Iterator<BasicPoolEntry> iter1 = this.leasedConnections.iterator();
                while (iter1.hasNext()) {
                    entry = (BasicPoolEntry) iter1.next();
                    iter1.remove();
                    closeConnection(entry);
                }
                Iterator<BasicPoolEntry> iter2 = this.freeConnections.iterator();
                while (iter2.hasNext()) {
                    entry = (BasicPoolEntry) iter2.next();
                    iter2.remove();
                    if (this.log.debugEnabled) {
                        this.log.debug("Closing connection [" + entry.route + "][" + entry.getState() + "]");
                    }
                    closeConnection(entry);
                }
                Iterator<WaitingThread> iwth = this.waitingThreads.iterator();
                while (iwth.hasNext()) {
                    WaitingThread waiter = (WaitingThread) iwth.next();
                    iwth.remove();
                    waiter.wakeup();
                }
                this.routeToPool.clear();
                this.poolLock.unlock();
            }
        } finally {
            this.poolLock.unlock();
        }
    }
}
