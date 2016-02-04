package com.nostra13.universalimageloader.core.assist.deque;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedBlockingDeque<E> extends AbstractQueue<E> implements BlockingDeque<E>, Serializable {
    private final int capacity;
    private transient int count;
    transient Node<E> first;
    transient Node<E> last;
    final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    private abstract class AbstractItr implements Iterator<E> {
        private Node<E> lastRet;
        Node<E> next;
        E nextItem;

        abstract Node<E> firstNode();

        abstract Node<E> nextNode(Node<E> node);

        AbstractItr() {
            ReentrantLock lock = LinkedBlockingDeque.this.lock;
            lock.lock();
            try {
                this.next = firstNode();
                this.nextItem = this.next == null ? null : this.next.item;
            } finally {
                lock.unlock();
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void advance() {
            /*
            r5 = this;
            r1 = 0;
            r2 = com.nostra13.universalimageloader.core.assist.deque.LinkedBlockingDeque.this;
            r0 = r2.lock;
            r0.lock();
            r2 = r5.next;	 Catch:{ all -> 0x0030 }
            r3 = r2;
        L_0x000b:
            r2 = r5.nextNode(r3);	 Catch:{ all -> 0x0030 }
            if (r2 != 0) goto L_0x001e;
        L_0x0011:
            r2 = r1;
        L_0x0012:
            r5.next = r2;	 Catch:{ all -> 0x0030 }
            r2 = r5.next;	 Catch:{ all -> 0x0030 }
            if (r2 != 0) goto L_0x002b;
        L_0x0018:
            r5.nextItem = r1;	 Catch:{ all -> 0x0030 }
            r0.unlock();
            return;
        L_0x001e:
            r4 = r2.item;	 Catch:{ all -> 0x0030 }
            if (r4 != 0) goto L_0x0012;
        L_0x0022:
            if (r2 != r3) goto L_0x0029;
        L_0x0024:
            r2 = r5.firstNode();	 Catch:{ all -> 0x0030 }
            goto L_0x0012;
        L_0x0029:
            r3 = r2;
            goto L_0x000b;
        L_0x002b:
            r1 = r5.next;	 Catch:{ all -> 0x0030 }
            r1 = r1.item;	 Catch:{ all -> 0x0030 }
            goto L_0x0018;
        L_0x0030:
            r1 = move-exception;
            r0.unlock();
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.core.assist.deque.LinkedBlockingDeque.AbstractItr.advance():void");
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public E next() {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            this.lastRet = this.next;
            E x = this.nextItem;
            advance();
            return x;
        }

        public void remove() {
            Node<E> n = this.lastRet;
            if (n == null) {
                throw new IllegalStateException();
            }
            this.lastRet = null;
            ReentrantLock lock = LinkedBlockingDeque.this.lock;
            lock.lock();
            try {
                if (n.item != null) {
                    LinkedBlockingDeque.this.unlink(n);
                }
                lock.unlock();
            } catch (Throwable th) {
                lock.unlock();
            }
        }
    }

    private class Itr extends AbstractItr {
        private Itr() {
            super();
        }

        final Node<E> firstNode() {
            return LinkedBlockingDeque.this.first;
        }

        final Node<E> nextNode(Node<E> n) {
            return n.next;
        }
    }

    static final class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(E x) {
            this.item = x;
        }
    }

    public LinkedBlockingDeque() {
        this((byte) 0);
    }

    private LinkedBlockingDeque(byte b) {
        this.lock = new ReentrantLock();
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
        this.capacity = ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
    }

    private boolean linkLast(Node<E> node) {
        if (this.count >= this.capacity) {
            return false;
        }
        Node<E> l = this.last;
        node.prev = l;
        this.last = node;
        if (this.first == null) {
            this.first = node;
        } else {
            l.next = node;
        }
        this.count++;
        this.notEmpty.signal();
        return true;
    }

    private E unlinkFirst() {
        Node<E> f = this.first;
        if (f == null) {
            return null;
        }
        Node<E> n = f.next;
        E item = f.item;
        f.item = null;
        f.next = f;
        this.first = n;
        if (n == null) {
            this.last = null;
        } else {
            n.prev = null;
        }
        this.count--;
        this.notFull.signal();
        return item;
    }

    final void unlink(Node<E> x) {
        Node<E> p = x.prev;
        Node<E> n = x.next;
        if (p == null) {
            unlinkFirst();
        } else if (n == null) {
            Node node = this.last;
            if (node != null) {
                Node node2 = node.prev;
                node.item = null;
                node.prev = node;
                this.last = node2;
                if (node2 == null) {
                    this.first = null;
                } else {
                    node2.next = null;
                }
                this.count--;
                this.notFull.signal();
            }
        } else {
            p.next = n;
            n.prev = p;
            x.item = null;
            this.count--;
            this.notFull.signal();
        }
    }

    public final boolean offerFirst(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node(e);
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            boolean z;
            if (this.count >= this.capacity) {
                z = false;
            } else {
                Node node2 = this.first;
                node.next = node2;
                this.first = node;
                if (this.last == null) {
                    this.last = node;
                } else {
                    node2.prev = node;
                }
                this.count++;
                this.notEmpty.signal();
                z = true;
            }
            lock.unlock();
            return z;
        } catch (Throwable th) {
            lock.unlock();
        }
    }

    private boolean offerLast(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node(e);
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            boolean linkLast = linkLast(node);
            return linkLast;
        } finally {
            lock.unlock();
        }
    }

    private boolean offerLast(E e, long timeout, TimeUnit unit) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node(e);
        long nanos = unit.toNanos(timeout);
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        while (!linkLast(node)) {
            try {
                if (nanos <= 0) {
                    return false;
                }
                nanos = this.notFull.awaitNanos(nanos);
            } finally {
                lock.unlock();
            }
        }
        lock.unlock();
        return true;
    }

    public final E removeFirst() {
        E x = pollFirst();
        if (x != null) {
            return x;
        }
        throw new NoSuchElementException();
    }

    private E pollFirst() {
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            E unlinkFirst = unlinkFirst();
            return unlinkFirst;
        } finally {
            lock.unlock();
        }
    }

    private E takeFirst() throws InterruptedException {
        E x;
        ReentrantLock lock = this.lock;
        lock.lock();
        while (true) {
            try {
                x = unlinkFirst();
                if (x != null) {
                    break;
                }
                this.notEmpty.await();
            } finally {
                lock.unlock();
            }
        }
        return x;
    }

    private E pollFirst(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        while (true) {
            try {
                E x = unlinkFirst();
                if (x != null) {
                    lock.unlock();
                    return x;
                } else if (nanos <= 0) {
                    break;
                } else {
                    nanos = this.notEmpty.awaitNanos(nanos);
                }
            } finally {
                lock.unlock();
            }
        }
        return null;
    }

    private E peekFirst() {
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            E e = this.first == null ? null : this.first.item;
            lock.unlock();
            return e;
        } catch (Throwable th) {
            lock.unlock();
        }
    }

    private boolean removeFirstOccurrence(Object o) {
        if (o == null) {
            return false;
        }
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            for (Node<E> p = this.first; p != null; p = p.next) {
                if (o.equals(p.item)) {
                    unlink(p);
                    return true;
                }
            }
            lock.unlock();
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean add(E e) {
        if (offerLast(e)) {
            return true;
        }
        throw new IllegalStateException("Deque full");
    }

    public boolean offer(E e) {
        return offerLast(e);
    }

    public void put(E e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        Node node = new Node(e);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        while (!linkLast(node)) {
            try {
                this.notFull.await();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return offerLast(e, timeout, unit);
    }

    public E remove() {
        return removeFirst();
    }

    public E poll() {
        return pollFirst();
    }

    public E take() throws InterruptedException {
        return takeFirst();
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return pollFirst(timeout, unit);
    }

    public E element() {
        E peekFirst = peekFirst();
        if (peekFirst != null) {
            return peekFirst;
        }
        throw new NoSuchElementException();
    }

    public E peek() {
        return peekFirst();
    }

    public int remainingCapacity() {
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int i = this.capacity - this.count;
            return i;
        } finally {
            lock.unlock();
        }
    }

    public int drainTo(Collection<? super E> c) {
        return drainTo(c, ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null) {
            throw new NullPointerException();
        } else if (c == this) {
            throw new IllegalArgumentException();
        } else {
            ReentrantLock lock = this.lock;
            lock.lock();
            try {
                int n = Math.min(maxElements, this.count);
                for (int i = 0; i < n; i++) {
                    c.add(this.first.item);
                    unlinkFirst();
                }
                return n;
            } finally {
                lock.unlock();
            }
        }
    }

    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    public int size() {
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int i = this.count;
            return i;
        } finally {
            lock.unlock();
        }
    }

    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            for (Node<E> p = this.first; p != null; p = p.next) {
                if (o.equals(p.item)) {
                    return true;
                }
            }
            lock.unlock();
            return false;
        } finally {
            lock.unlock();
        }
    }

    public Object[] toArray() {
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] a = new Object[this.count];
            Node<E> p = this.first;
            int k = 0;
            while (p != null) {
                int k2 = k + 1;
                a[k] = p.item;
                p = p.next;
                k = k2;
            }
            return a;
        } finally {
            lock.unlock();
        }
    }

    public <T> T[] toArray(T[] a) {
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            if (a.length < this.count) {
                a = (Object[]) Array.newInstance(a.getClass().getComponentType(), this.count);
            }
            Node<E> p = this.first;
            int k = 0;
            while (p != null) {
                int k2 = k + 1;
                a[k] = p.item;
                p = p.next;
                k = k2;
            }
            if (a.length > k) {
                a[k] = null;
            }
            lock.unlock();
            return a;
        } catch (Throwable th) {
            lock.unlock();
        }
    }

    public String toString() {
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            String str;
            Node<E> p = this.first;
            if (p == null) {
                str = "[]";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append('[');
                while (true) {
                    E e = p.item;
                    if (e == this) {
                        e = "(this Collection)";
                    }
                    sb.append(e);
                    p = p.next;
                    if (p == null) {
                        break;
                    }
                    sb.append(',').append(' ');
                }
                str = sb.append(']').toString();
                lock.unlock();
            }
            return str;
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Node<E> f = this.first;
            while (f != null) {
                f.item = null;
                Node<E> n = f.next;
                f.prev = null;
                f.next = null;
                f = n;
            }
            this.last = null;
            this.first = null;
            this.count = 0;
            this.notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Iterator<E> iterator() {
        return new Itr();
    }
}
