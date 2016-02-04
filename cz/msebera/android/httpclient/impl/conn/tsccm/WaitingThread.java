package cz.msebera.android.httpclient.impl.conn.tsccm;

import cz.msebera.android.httpclient.util.Args;
import java.util.Date;
import java.util.concurrent.locks.Condition;

@Deprecated
public final class WaitingThread {
    boolean aborted;
    final Condition cond;
    private final RouteSpecificPool pool;
    private Thread waiter;

    public WaitingThread(Condition cond, RouteSpecificPool pool) {
        Args.notNull(cond, "Condition");
        this.cond = cond;
        this.pool = pool;
    }

    public final boolean await(Date deadline) throws InterruptedException {
        if (this.waiter != null) {
            throw new IllegalStateException("A thread is already waiting on this object.\ncaller: " + Thread.currentThread() + "\nwaiter: " + this.waiter);
        } else if (this.aborted) {
            throw new InterruptedException("Operation interrupted");
        } else {
            boolean success;
            this.waiter = Thread.currentThread();
            if (deadline != null) {
                try {
                    success = this.cond.awaitUntil(deadline);
                } catch (Throwable th) {
                    this.waiter = null;
                }
            } else {
                this.cond.await();
                success = true;
            }
            if (this.aborted) {
                throw new InterruptedException("Operation interrupted");
            }
            this.waiter = null;
            return success;
        }
    }

    public final void wakeup() {
        if (this.waiter == null) {
            throw new IllegalStateException("Nobody waiting on this object.");
        }
        this.cond.signalAll();
    }
}
