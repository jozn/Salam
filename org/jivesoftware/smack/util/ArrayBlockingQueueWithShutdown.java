package org.jivesoftware.smack.util;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public final class ArrayBlockingQueueWithShutdown<E> extends AbstractQueue<E> implements BlockingQueue<E> {
    private int count;
    public volatile boolean isShutdown;
    private final E[] items;
    public final ReentrantLock lock;
    public final Condition notEmpty;
    public final Condition notFull;
    private int putIndex;
    private int takeIndex;

    private class Itr implements Iterator<E> {
        private int lastRet;
        private int nextIndex;
        private E nextItem;

        Itr() {
            this.lastRet = -1;
            if (ArrayBlockingQueueWithShutdown.this.count == 0) {
                this.nextIndex = -1;
                return;
            }
            this.nextIndex = ArrayBlockingQueueWithShutdown.this.takeIndex;
            this.nextItem = ArrayBlockingQueueWithShutdown.this.items[ArrayBlockingQueueWithShutdown.this.takeIndex];
        }

        public final boolean hasNext() {
            return this.nextIndex >= 0;
        }

        private void checkNext() {
            if (this.nextIndex == ArrayBlockingQueueWithShutdown.this.putIndex) {
                this.nextIndex = -1;
                this.nextItem = null;
                return;
            }
            this.nextItem = ArrayBlockingQueueWithShutdown.this.items[this.nextIndex];
            if (this.nextItem == null) {
                this.nextIndex = -1;
            }
        }

        public final E next() {
            ArrayBlockingQueueWithShutdown.this.lock.lock();
            try {
                if (this.nextIndex < 0) {
                    throw new NoSuchElementException();
                }
                this.lastRet = this.nextIndex;
                E e = this.nextItem;
                this.nextIndex = ArrayBlockingQueueWithShutdown.this.inc(this.nextIndex);
                checkNext();
                return e;
            } finally {
                ArrayBlockingQueueWithShutdown.this.lock.unlock();
            }
        }

        public final void remove() {
            ArrayBlockingQueueWithShutdown.this.lock.lock();
            try {
                int i = this.lastRet;
                if (i < 0) {
                    throw new IllegalStateException();
                }
                this.lastRet = -1;
                int ti = ArrayBlockingQueueWithShutdown.this.takeIndex;
                ArrayBlockingQueueWithShutdown.access$600(ArrayBlockingQueueWithShutdown.this, i);
                if (i == ti) {
                    i = ArrayBlockingQueueWithShutdown.this.takeIndex;
                }
                this.nextIndex = i;
                checkNext();
            } finally {
                ArrayBlockingQueueWithShutdown.this.lock.unlock();
            }
        }
    }

    static /* synthetic */ void access$600(ArrayBlockingQueueWithShutdown x0, int x1) {
        if (x1 == x0.takeIndex) {
            x0.items[x0.takeIndex] = null;
            x0.takeIndex = x0.inc(x0.takeIndex);
        } else {
            while (true) {
                int inc = x0.inc(x1);
                if (inc == x0.putIndex) {
                    break;
                }
                x0.items[x1] = x0.items[inc];
                x1 = inc;
            }
            x0.items[x1] = null;
            x0.putIndex = x1;
        }
        x0.count--;
        x0.notFull.signal();
    }

    private final int inc(int i) {
        i++;
        return i == this.items.length ? 0 : i;
    }

    private final void insert(E e) {
        this.items[this.putIndex] = e;
        this.putIndex = inc(this.putIndex);
        this.count++;
        this.notEmpty.signal();
    }

    private final E extract() {
        E e = this.items[this.takeIndex];
        this.items[this.takeIndex] = null;
        this.takeIndex = inc(this.takeIndex);
        this.count--;
        this.notFull.signal();
        return e;
    }

    private static final void checkNotNull(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
    }

    private final void checkNotShutdown() throws InterruptedException {
        if (this.isShutdown) {
            throw new InterruptedException();
        }
    }

    private final boolean hasNoElements() {
        return this.count == 0;
    }

    private final boolean isFull() {
        return this.count == this.items.length;
    }

    public ArrayBlockingQueueWithShutdown() {
        this.isShutdown = false;
        this.items = new Object[500];
        this.lock = new ReentrantLock(true);
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
    }

    public final E poll() {
        this.lock.lock();
        try {
            if (hasNoElements()) {
                return null;
            }
            E extract = extract();
            this.lock.unlock();
            return extract;
        } finally {
            this.lock.unlock();
        }
    }

    public final E peek() {
        this.lock.lock();
        try {
            E e = hasNoElements() ? null : this.items[this.takeIndex];
            this.lock.unlock();
            return e;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public final boolean offer(E e) {
        checkNotNull(e);
        this.lock.lock();
        try {
            if (isFull() || this.isShutdown) {
                this.lock.unlock();
                return false;
            }
            insert(e);
            this.lock.unlock();
            return true;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public final void put(E e) throws InterruptedException {
        checkNotNull(e);
        this.lock.lockInterruptibly();
        while (isFull()) {
            try {
                this.notFull.await();
                checkNotShutdown();
            } catch (InterruptedException ie) {
                this.notFull.signal();
                throw ie;
            } catch (Throwable th) {
                this.lock.unlock();
            }
        }
        insert(e);
        this.lock.unlock();
    }

    public final boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        checkNotNull(e);
        long nanos = unit.toNanos(timeout);
        this.lock.lockInterruptibly();
        while (true) {
            try {
                if (!isFull()) {
                    insert(e);
                    this.lock.unlock();
                    return true;
                } else if (nanos <= 0) {
                    this.lock.unlock();
                    return false;
                } else {
                    nanos = this.notFull.awaitNanos(nanos);
                    checkNotShutdown();
                }
            } catch (InterruptedException ie) {
                this.notFull.signal();
                throw ie;
            } catch (Throwable th) {
                this.lock.unlock();
            }
        }
    }

    public final E take() throws InterruptedException {
        this.lock.lockInterruptibly();
        try {
            checkNotShutdown();
            while (hasNoElements()) {
                this.notEmpty.await();
                checkNotShutdown();
            }
            E extract = extract();
            this.lock.unlock();
            return extract;
        } catch (InterruptedException ie) {
            this.notEmpty.signal();
            throw ie;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public final E poll(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        this.lock.lockInterruptibly();
        try {
            checkNotShutdown();
            while (true) {
                if ((!hasNoElements() ? 1 : null) != null) {
                    E extract = extract();
                    this.lock.unlock();
                    return extract;
                } else if (nanos <= 0) {
                    this.lock.unlock();
                    return null;
                } else {
                    nanos = this.notEmpty.awaitNanos(nanos);
                    checkNotShutdown();
                }
            }
        } catch (InterruptedException ie) {
            this.notEmpty.signal();
            throw ie;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public final int remainingCapacity() {
        this.lock.lock();
        try {
            int length = this.items.length - this.count;
            return length;
        } finally {
            this.lock.unlock();
        }
    }

    public final int drainTo(Collection<? super E> c) {
        checkNotNull(c);
        if (c == this) {
            throw new IllegalArgumentException();
        }
        this.lock.lock();
        try {
            int i = this.takeIndex;
            int n = 0;
            while (n < this.count) {
                c.add(this.items[i]);
                this.items[i] = null;
                i = inc(i);
                n++;
            }
            if (n > 0) {
                this.count = 0;
                this.putIndex = 0;
                this.takeIndex = 0;
                this.notFull.signalAll();
            }
            this.lock.unlock();
            return n;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }

    public final int drainTo(Collection<? super E> c, int maxElements) {
        checkNotNull(c);
        if (c == this) {
            throw new IllegalArgumentException();
        } else if (maxElements <= 0) {
            return 0;
        } else {
            this.lock.lock();
            try {
                int i = this.takeIndex;
                int n = 0;
                int max = maxElements < this.count ? maxElements : this.count;
                while (n < max) {
                    c.add(this.items[i]);
                    this.items[i] = null;
                    i = inc(i);
                    n++;
                }
                if (n > 0) {
                    this.count -= n;
                    this.takeIndex = i;
                    this.notFull.signalAll();
                }
                this.lock.unlock();
                return n;
            } catch (Throwable th) {
                this.lock.unlock();
            }
        }
    }

    public final int size() {
        this.lock.lock();
        try {
            int i = this.count;
            return i;
        } finally {
            this.lock.unlock();
        }
    }

    public final Iterator<E> iterator() {
        this.lock.lock();
        try {
            Iterator<E> itr = new Itr();
            return itr;
        } finally {
            this.lock.unlock();
        }
    }
}
