package cz.msebera.android.httpclient.pool;

import cz.msebera.android.httpclient.util.Args;
import java.util.concurrent.TimeUnit;

public abstract class PoolEntry<T, C> {
    public final C conn;
    private final long created;
    private long expiry;
    private final String id;
    public final T route;
    public volatile Object state;
    private long updated;
    private final long validityDeadline;

    public PoolEntry(String id, T route, C conn, TimeUnit tunit) {
        Args.notNull(route, "Route");
        Args.notNull(conn, "Connection");
        Args.notNull(tunit, "Time unit");
        this.id = id;
        this.route = route;
        this.conn = conn;
        this.created = System.currentTimeMillis();
        if (0 > 0) {
            this.validityDeadline = this.created + tunit.toMillis(0);
        } else {
            this.validityDeadline = Long.MAX_VALUE;
        }
        this.expiry = this.validityDeadline;
    }

    public final synchronized long getExpiry() {
        return this.expiry;
    }

    public final synchronized void updateExpiry(long time, TimeUnit tunit) {
        long newExpiry;
        Args.notNull(tunit, "Time unit");
        this.updated = System.currentTimeMillis();
        if (time > 0) {
            newExpiry = this.updated + tunit.toMillis(time);
        } else {
            newExpiry = Long.MAX_VALUE;
        }
        this.expiry = Math.min(newExpiry, this.validityDeadline);
    }

    public synchronized boolean isExpired(long now) {
        return now >= this.expiry;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[id:");
        buffer.append(this.id);
        buffer.append("][route:");
        buffer.append(this.route);
        buffer.append("][state:");
        buffer.append(this.state);
        buffer.append("]");
        return buffer.toString();
    }
}
