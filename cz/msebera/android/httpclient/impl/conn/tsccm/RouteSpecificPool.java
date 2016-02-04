package cz.msebera.android.httpclient.impl.conn.tsccm;

import cz.msebera.android.httpclient.conn.params.ConnPerRoute;
import cz.msebera.android.httpclient.conn.routing.HttpRoute;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.util.Args;
import cz.msebera.android.httpclient.util.Asserts;
import cz.msebera.android.httpclient.util.LangUtils;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

@Deprecated
public final class RouteSpecificPool {
    protected final ConnPerRoute connPerRoute;
    protected final LinkedList<BasicPoolEntry> freeEntries;
    public HttpClientAndroidLog log;
    protected final int maxEntries;
    protected int numEntries;
    protected final HttpRoute route;
    protected final Queue<WaitingThread> waitingThreads;

    public RouteSpecificPool(HttpRoute route, ConnPerRoute connPerRoute) {
        this.log = new HttpClientAndroidLog(getClass());
        this.route = route;
        this.connPerRoute = connPerRoute;
        this.maxEntries = connPerRoute.getMaxForRoute(route);
        this.freeEntries = new LinkedList();
        this.waitingThreads = new LinkedList();
        this.numEntries = 0;
    }

    public final HttpRoute getRoute() {
        return this.route;
    }

    public final int getMaxEntries() {
        return this.maxEntries;
    }

    public final boolean isUnused() {
        return this.numEntries <= 0 && this.waitingThreads.isEmpty();
    }

    public final int getCapacity() {
        return this.connPerRoute.getMaxForRoute(this.route) - this.numEntries;
    }

    public final BasicPoolEntry allocEntry(Object state) {
        BasicPoolEntry entry;
        if (!this.freeEntries.isEmpty()) {
            ListIterator<BasicPoolEntry> it = this.freeEntries.listIterator(this.freeEntries.size());
            while (it.hasPrevious()) {
                entry = (BasicPoolEntry) it.previous();
                if (entry.getState() != null) {
                    if (LangUtils.equals(state, entry.getState())) {
                    }
                }
                it.remove();
                return entry;
            }
        }
        if (getCapacity() != 0 || this.freeEntries.isEmpty()) {
            return null;
        }
        entry = (BasicPoolEntry) this.freeEntries.remove();
        entry.shutdownEntry();
        try {
            entry.connection.close();
            return entry;
        } catch (IOException ex) {
            this.log.debug("I/O error closing connection", ex);
            return entry;
        }
    }

    public final void freeEntry(BasicPoolEntry entry) {
        if (this.numEntries <= 0) {
            throw new IllegalStateException("No entry created for this pool. " + this.route);
        } else if (this.numEntries <= this.freeEntries.size()) {
            throw new IllegalStateException("No entry allocated from this pool. " + this.route);
        } else {
            this.freeEntries.add(entry);
        }
    }

    public final void createdEntry(BasicPoolEntry entry) {
        Args.check(this.route.equals(entry.route), "Entry not planned for this pool");
        this.numEntries++;
    }

    public final boolean deleteEntry(BasicPoolEntry entry) {
        boolean found = this.freeEntries.remove(entry);
        if (found) {
            this.numEntries--;
        }
        return found;
    }

    public final void dropEntry() {
        Asserts.check(this.numEntries > 0, "There is no entry that could be dropped");
        this.numEntries--;
    }

    public final void queueThread(WaitingThread wt) {
        Args.notNull(wt, "Waiting thread");
        this.waitingThreads.add(wt);
    }

    public final boolean hasThread() {
        return !this.waitingThreads.isEmpty();
    }

    public final WaitingThread nextThread() {
        return (WaitingThread) this.waitingThreads.peek();
    }

    public final void removeThread(WaitingThread wt) {
        if (wt != null) {
            this.waitingThreads.remove(wt);
        }
    }
}
