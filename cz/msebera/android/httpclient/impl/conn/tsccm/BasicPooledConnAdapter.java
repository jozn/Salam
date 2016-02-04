package cz.msebera.android.httpclient.impl.conn.tsccm;

import cz.msebera.android.httpclient.conn.ClientConnectionManager;
import cz.msebera.android.httpclient.impl.conn.AbstractPoolEntry;
import cz.msebera.android.httpclient.impl.conn.AbstractPooledConnAdapter;

@Deprecated
public final class BasicPooledConnAdapter extends AbstractPooledConnAdapter {
    protected BasicPooledConnAdapter(ThreadSafeClientConnManager tsccm, AbstractPoolEntry entry) {
        super(tsccm, entry);
        this.markedReusable = true;
    }

    protected final ClientConnectionManager getManager() {
        return super.getManager();
    }

    protected final AbstractPoolEntry getPoolEntry() {
        return super.getPoolEntry();
    }

    protected final void detach() {
        super.detach();
    }
}
