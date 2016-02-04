package cz.msebera.android.httpclient.impl.conn.tsccm;

import cz.msebera.android.httpclient.conn.OperatedClientConnection;
import cz.msebera.android.httpclient.extras.HttpClientAndroidLog;
import cz.msebera.android.httpclient.impl.conn.IdleConnectionHandler;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Deprecated
public abstract class AbstractConnPool {
    protected IdleConnectionHandler idleConnHandler;
    protected volatile boolean isShutDown;
    protected Set<BasicPoolEntry> leasedConnections;
    public HttpClientAndroidLog log;
    protected final Lock poolLock;

    protected AbstractConnPool() {
        this.log = new HttpClientAndroidLog(getClass());
        this.leasedConnections = new HashSet();
        this.idleConnHandler = new IdleConnectionHandler();
        this.poolLock = new ReentrantLock();
    }

    public void shutdown() {
        this.poolLock.lock();
        try {
            if (this.isShutDown) {
                this.poolLock.unlock();
                return;
            }
            Iterator<BasicPoolEntry> iter = this.leasedConnections.iterator();
            while (iter.hasNext()) {
                BasicPoolEntry entry = (BasicPoolEntry) iter.next();
                iter.remove();
                OperatedClientConnection operatedClientConnection = entry.connection;
                if (operatedClientConnection != null) {
                    operatedClientConnection.close();
                }
            }
            this.idleConnHandler.connectionToTimes.clear();
            this.isShutDown = true;
            this.poolLock.unlock();
        } catch (Throwable e) {
            this.log.debug("I/O error closing connection", e);
        } catch (Throwable th) {
            this.poolLock.unlock();
        }
    }
}
